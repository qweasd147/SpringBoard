package com.joo.api.board.service;

import com.joo.api.board.mapper.BoardMapper;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardServceImpl implements BoardServce{

    @Autowired
    BoardMapper boardMapper;

    @Override
    public List<BoardVo> selectBoardList(BoardSearchVo searchVo) {
        return boardMapper.selectBoardList(searchVo);
    }

    @Override
    public int selectBoardListTotCount(BoardSearchVo searchVo) {
        return boardMapper.selectBoardListTotCount(searchVo);
    }

    @Override
    public BoardVo selectBoard(BoardSearchVo searchVo) {
        return boardMapper.selectBoard(searchVo);
    }

    @Override
    public int insertBoard(BoardVo boardVo) {
        return boardMapper.insertBoard(boardVo);
    }

    @Override
    public int updateBoard(BoardVo boardVo) {
        return boardMapper.updateBoard(boardVo);
    }

    @Override
    public int deleteBoardById(int boardId) {
        return boardMapper.deleteBoardById(boardId);
    }
}
