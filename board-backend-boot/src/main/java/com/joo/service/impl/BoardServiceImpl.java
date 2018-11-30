package com.joo.service.impl;

import com.joo.model.BoardSearchVo;
import com.joo.model.entity.Board;
import com.joo.repository.BoardRepository;
import com.joo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    BoardRepository boardRepository;

    @Override
    public Map selectBoardList(BoardSearchVo searchVo) {

        List<Board> boards = boardRepository.findAll();

        return null;
    }

    @Override
    public int selectBoardListTotCount(BoardSearchVo searchVo) {
        return (int) boardRepository.count();
    }

    @Override
    public Board selectBoardOne(BoardSearchVo searchVo) {
        return boardRepository.findById((long) searchVo.getBoardIdx()).orElseThrow(()->new RuntimeException("못찾음"));
    }

    @Override
    public Board insertBoard(Board boardVo, MultipartFile[] uploadFile) {

        Board result = boardRepository.save(boardVo);

        
        return result;
    }

    @Override
    public Board updateBoard(Board boardVo, MultipartFile[] uploadFile, List<Integer> detachFileList) {

        boardRepository.save(boardVo);

        return null;
    }

    @Override
    public int deleteBoardById(int boardId) {
        boardRepository.deleteById((long) boardId);
        return 0;
    }
}
