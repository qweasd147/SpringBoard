package com.joo.api.board.controller;

import com.joo.api.board.service.BoardServce;
import com.joo.api.board.service.FileService;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import com.joo.api.board.vo.FileVo;
import com.joo.api.common.BaseController;
import com.joo.api.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity selectBoardList(@ModelAttribute BoardSearchVo searchVo){

        Map<String, ?> listData = boardServce.selectBoardList(searchVo);

        return successResult(listData);
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.GET)
    public ResponseEntity selectBoardOne(@ModelAttribute BoardSearchVo searchVo,
                                         @PathVariable int boardId){

        searchVo.setBoardIdx(boardId);

        BoardVo boardOne = boardServce.selectBoardOne(searchVo);

        if(boardOne == null){
            throw new BusinessException("99","데이터가 존재하지 않습니다.", null);
        }

        return successResult(boardOne);
    }

    @RequestMapping(value = "/board", headers = "content-type=multipart/*", method = RequestMethod.POST)
    public ResponseEntity insertBoard(@ModelAttribute BoardVo boardVo, @RequestParam(value="uploadFile", required=false) MultipartFile[] uploadFiles){

        boardServce.insertBoard(boardVo, uploadFiles);

        return createResult();
    }

    @RequestMapping(value = "/board/{boardId}", method = RequestMethod.POST)
    public ResponseEntity updateBoard(@ModelAttribute BoardVo boardVo
            , @PathVariable int boardId, @RequestParam(value = "detachFiles") List<Integer> detachFiles
            , @RequestParam("file") MultipartFile[] uploadFiles){

        boardVo.setIdx(boardId);

        boardServce.updateBoard(boardVo, uploadFiles, detachFiles);

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
        Resource fileResource = fileService.getFileResouce(fileVo);

        return setFileDownload(fileVo.getOriginFileName(), fileResource);
    }
}
