package com.joo.api.board.service;

import com.joo.api.board.mapper.BoardMapper;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import com.joo.api.board.vo.FileVo;
import com.joo.api.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "boardServiceImpl")
public class BoardServceImpl implements BoardServce{

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    FileService fileService;

    @Override
    public Map<String, ?> selectBoardList(BoardSearchVo searchVo) {

        Map<String, Object> listData = new HashMap<>();

        List<BoardVo> boardList = boardMapper.selectBoardList(searchVo);
        int listCount = boardMapper.selectBoardListTotCount(searchVo);

        //TODO : map 말고 다른 객체 만들긴 해야할꺼 같음....
        listData.put("boardList", boardList);
        listData.put("count", listCount);
        listData.put("page", searchVo.getPageIdx());

        return listData;
    }

    @Override
    public int selectBoardListTotCount(BoardSearchVo searchVo) {
        return boardMapper.selectBoardListTotCount(searchVo);
    }

    @Override
    public BoardVo selectBoardOne(BoardSearchVo searchVo) {
        boardMapper.updateBoardHits(searchVo.getBoardIdx());

        BoardVo boardOne = boardMapper.selectBoardOne(searchVo);

        if(boardOne == null)    throw new BusinessException("99","데이터가 존재하지 않습니다.", null);

        List<FileVo> fileList = fileService.selectBasicFileList(boardOne.getIdx());
        boardOne.setFileList(fileList);
        
        return boardOne;
    }

    @Override
    public BoardVo insertBoard(BoardVo boardVo, MultipartFile[] uploadFile) {

        boardMapper.insertBoard(boardVo);
        List<FileVo> fileVoList = fileService.uploadFiles(uploadFile);
        fileService.insertFileList(fileVoList);

        boardVo.setFileList(fileVoList);
        fileService.insertFileMapping(boardVo.getIdx(), fileVoList);

        return boardVo;
    }

    @Override
    public BoardVo updateBoard(BoardVo boardVo, MultipartFile[] uploadFile, List<Integer> detachFileIdList) {

        boardMapper.updateBoard(boardVo);
        List<FileVo> fileVoList = fileService.uploadFiles(uploadFile);
        fileService.insertFileList(fileVoList);

        boardVo.setFileList(fileVoList);
        fileService.insertFileMapping(boardVo.getIdx(), fileVoList);
        fileService.deleteFileMappingByFileID(detachFileIdList);

        return boardVo;
    }

    @Override
    public int deleteBoardById(int boardId) {
        boardMapper.deleteBoardById(boardId);
        fileService.deleteFileMappingByBoardID(boardId);
        return SUCCESS_STATE;
    }
}
