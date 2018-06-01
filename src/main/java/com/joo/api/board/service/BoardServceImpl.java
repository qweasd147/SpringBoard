package com.joo.api.board.service;

import com.joo.api.board.mapper.BoardMapper;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "boardServiceImpl")
public class BoardServceImpl implements BoardServce{

    @Autowired
    BoardMapper boardMapper;

    @Override
    public Map<String, ?> selectBoardList(BoardSearchVo searchVo) {


        Map<String, Object> listData = new HashMap<>();

        List<BoardVo> boardList = boardMapper.selectBoardList(searchVo);
        int listCount = boardMapper.selectBoardListTotCount(searchVo);

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
        return boardMapper.selectBoardOne(searchVo);
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
