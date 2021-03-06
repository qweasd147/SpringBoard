package com.joo.web.controller;

import com.joo.exception.BusinessException;
import com.joo.model.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
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

        Page<LimitedBoardDto> boardPage = boardService.selectBoardList(searchDto, pageable)
                .map(LimitedBoardDto::of);

        Map<String, Object> listData = new HashMap<>();

        listData.put("boardList", boardPage.getContent());
        listData.put("count", boardPage.getTotalElements());    //전체 데이터 수
        listData.put("page", boardPage.getNumber()+1);          //현재 페이지

        return successRespResult(listData);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity selectBoardOne(@ModelAttribute BoardSearchDto searchDto,
                                         @PathVariable Long boardId){

        BoardEntity boardEntity = boardService.selectBoardOne(boardId);

        //전체 정보가 아닌 제한된 정보만 넘겨준다.
        //LimitedBoardDto limitedBoardDto = modelMapper.map(boardEntity, LimitedBoardDto.class);
        return successRespResult(LimitedBoardDto.of(boardEntity));
    }

    //@PostMapping(value = "/board")
    @PostMapping(value = "/board", consumes = "*/*")
    public ResponseEntity insertBoard(
            //@Valid @ModelAttribute BoardDto boardDto
            //, @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles
            @Valid @ModelAttribute BoardWriteRequestDto boardWriteRequestDto
            //, @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){

        //boardEntity.setWriter(customUserDetails.getNickName());
        BoardEntity boardEntity = boardService.insertBoard(boardWriteRequestDto);

        return createRespResult(LimitedBoardDto.of(boardEntity));
    }

    @PostMapping(value = "/board/{idx}", headers = "content-type=multipart/*")
    public ResponseEntity updateBoard(@PathVariable Long idx
            //, @RequestParam(value="deleteFile[]", required=false) List<Long> deleteFiles
            //, @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles
            //, @ModelAttribute @Valid BoardDto boardDto
            , @Valid @ModelAttribute BoardUpdateRequestDto boardUpdateRequestDto){

        boardService.updateBoard(idx, boardUpdateRequestDto);

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
