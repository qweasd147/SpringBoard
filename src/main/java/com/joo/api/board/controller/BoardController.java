package com.joo.api.board.controller;

import com.joo.api.board.service.BoardServce;
import com.joo.api.board.service.FileService;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import com.joo.api.board.vo.FileVo;
import com.joo.api.common.BaseController;
import com.joo.api.exception.BusinessException;
import com.joo.api.exception.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "*")
public class BoardController extends BaseController {

    @Autowired
    @Qualifier("boardServiceImpl")
    private BoardServce boardServce;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public ResponseEntity selectBoardList(@ModelAttribute @Valid BoardSearchVo searchVo, BindingResult br){

        if(br.hasErrors()){
            throw new ValidateException(br, searchVo);
        }

        Map<String, ?> listData = boardServce.selectBoardList(searchVo);

        return successResult(listData);
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.GET)
    public ResponseEntity selectBoardOne(@ModelAttribute BoardSearchVo searchVo,
                                         @PathVariable int boardId){

        searchVo.setBoardIdx(boardId);
        BoardVo boardOne = boardServce.selectBoardOne(searchVo);

        return successResult(boardOne);
    }

    @RequestMapping(value = "/board", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public ResponseEntity insertBoard(
            @Valid @ModelAttribute BoardVo boardVo
            , BindingResult br
            , @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles){

        if(br.hasErrors()){
            throw new ValidateException(br, boardVo);
        }

        boardServce.insertBoard(boardVo, uploadFiles);

        return createResult();
    }

    @RequestMapping(value = "/board/{boardId}", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public ResponseEntity updateBoard(@ModelAttribute @Valid BoardVo boardVo, BindingResult br
            , @PathVariable int boardId
            , @RequestParam(value="deleteFile[]", required=false) List<Integer> deleteFiles
            , @RequestParam(value="uploadFile[]", required=false) MultipartFile[] uploadFiles
            ){

        if(br.hasErrors()){
            throw new ValidateException(br, boardVo);
        }

        boardVo.setIdx(boardId);
        boardServce.updateBoard(boardVo, uploadFiles, deleteFiles);

        return successResult();
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBoard(@PathVariable int boardId){

        boardServce.deleteBoardById(boardId);

        return successResult();
    }

    @RequestMapping(value = "/board/download/{boardId}/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable int boardId, @PathVariable int fileId){

        FileVo fileVo = fileService.selectFileVo(boardId, fileId);

        if(fileVo == null)  throw new BusinessException("HTTP_404", "잘못된 파일정보 다운로드 요청."+fileVo.toString());

        Resource fileResource = fileService.getFileResouce(fileVo);

        return setFileDownload(fileVo.getOriginFileName(), fileResource);
    }
}
