package com.joo.web.controller;

import com.joo.exception.BusinessException;
import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.FileDto;
import com.joo.model.dto.limited.LimitedBoardDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.service.BoardService;
import com.joo.service.FileService;
import com.joo.utils.FileUtils;
import com.joo.web.controller.common.BaseController;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class BoardController implements BaseController{

    private BoardService boardService;
    private FileService fileService;
    private FileUtils fileUtils;

    //private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/board")
    public ResponseEntity selectBoardList(@Valid BoardSearchDto searchDto, Pageable pageable){

        Map<String, ?> listData = boardService.selectBoardList(searchDto, pageable);

        return successRespResult(listData);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity selectBoardOne(@ModelAttribute BoardSearchDto searchDto,
                                         @PathVariable Long boardId){

        BoardEntity boardEntity = boardService.selectBoardOne(boardId);

        //전체 정보가 아닌 제한된 정보만 넘겨준다.
        //LimitedBoardDto limitedBoardDto = modelMapper.map(boardEntity, LimitedBoardDto.class);
        LimitedBoardDto limitedBoardDto = LimitedBoardDto.of(boardEntity);

        return successRespResult(limitedBoardDto);
    }

    //@PostMapping(value = "/board")
    @PostMapping(value = "/board", consumes = "*/*")
    public ResponseEntity insertBoard(
            @Valid @ModelAttribute BoardDto boardDto
            , @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles
            //, @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){

        //boardEntity.setWriter(customUserDetails.getNickName());
        boardService.insertBoard(boardDto, uploadFiles);

        return createRespResult();
    }

    @PostMapping(value = "/board/{boardId}", headers = "content-type=multipart/*")
    public ResponseEntity updateBoard(@ModelAttribute @Valid BoardDto boardDto
            , @PathVariable Long boardId
            , @RequestParam(value="deleteFile[]", required=false) List<Long> deleteFiles
            , @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles){

        boardDto.setIdx(boardId);
        boardService.updateBoard(boardDto, uploadFiles, deleteFiles);

        return successRespResult();
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId){

        boardService.deleteBoardById(boardId);

        return successRespResult();
    }

    @GetMapping("/board/download/{boardId}/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long boardId, @PathVariable Long fileId){

        FileEntity fileEntity = fileService.selectFileOne(boardId, fileId);

        if(fileEntity == null)  throw new BusinessException("HTTP_404", "잘못된 파일정보 다운로드 요청."+fileEntity.toString());

        Resource fileResource = fileUtils.getFileResouce(FileDto.of(fileEntity));

        return getRespResource(fileEntity.getOriginFileName(), fileResource);
    }
}
