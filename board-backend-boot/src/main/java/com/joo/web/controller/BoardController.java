package com.joo.web.controller;

import com.joo.exception.BusinessException;
import com.joo.exception.ValidateException;
import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.FileDto;
import com.joo.service.BoardService;
import com.joo.service.FileService;
import com.joo.utils.FileUtils;
import com.joo.web.controller.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(origins = "*")
public class BoardController implements BaseController{

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileUtils fileUtils;

    @GetMapping("/board")
    public ResponseEntity selectBoardList(@Valid BoardSearchDto searchDto, Pageable pageable, BindingResult br){

        if(br.hasErrors()){
            throw new ValidateException(br, searchDto);
        }

        Map<String, ?> listData = boardService.selectBoardList(searchDto, pageable);

        return successRespResult(listData);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity selectBoardOne(@ModelAttribute BoardSearchDto searchDto,
                                         @PathVariable Long boardId){

        BoardDto boardDto = boardService.selectBoardOne(boardId);

        return successRespResult(boardDto);
    }

    //@PostMapping(value = "/board")
    @PostMapping(value = "/board", consumes = "*/*")
    public ResponseEntity insertBoard(
            @Valid @ModelAttribute BoardDto boardDto
            , BindingResult br
            , @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles
            //, @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){

        if(br.hasErrors()){
            throw new ValidateException(br, boardDto);
        }

        //boardEntity.setWriter(customUserDetails.getNickName());
        boardService.insertBoard(boardDto, uploadFiles);

        return createRespResult();
    }

    @PostMapping(value = "/board/{boardId}", headers = "content-type=multipart/*")
    public ResponseEntity updateBoard(@ModelAttribute @Valid BoardDto boardDto, BindingResult br
            , @PathVariable Long boardId
            , @RequestParam(value="deleteFile[]", required=false) List<Long> deleteFiles
            , @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles){

        if(br.hasErrors()){
            throw new ValidateException(br, boardDto);
        }

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

        FileDto fileDto = fileService.selectFileOne(boardId, fileId);

        if(fileDto == null)  throw new BusinessException("HTTP_404", "잘못된 파일정보 다운로드 요청."+fileDto.toString());

        Resource fileResource = fileUtils.getFileResouce(fileDto);

        return getRespResource(fileDto.getOriginFileName(), fileResource);
    }
}
