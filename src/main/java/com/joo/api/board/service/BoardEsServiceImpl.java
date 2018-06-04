package com.joo.api.board.service;

import com.joo.api.board.mapper.BoardRepository;
import com.joo.api.board.vo.BoardEsVo;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "boardEsServiceImpl")
public class BoardEsServiceImpl implements BoardServce{

    @Resource(name = "boardServiceImpl")
    private BoardServce boardServce;

    private BoardRepository boardRepository;

    @Autowired
    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Map selectBoardList(BoardSearchVo searchVo) {

        Page<BoardEsVo> boardEsVos = getBoardPage(searchVo);
        Map<String, Object> listData = new HashMap<>();

        List<BoardEsVo> boardEsVoList = boardEsVos.getContent();
        List<BoardVo> boardVoList = boardEsVoList.stream().map(BoardVo::new).collect(Collectors.toList());

        listData.put("boardList", boardVoList);
        listData.put("count", boardEsVos.getTotalPages());
        listData.put("page", searchVo.getPageIdx());

        return listData;
    }

    @Override
    public int selectBoardListTotCount(BoardSearchVo searchVo) {
        Page<BoardEsVo> boardEsVos = getBoardPage(searchVo);
        return boardEsVos.getTotalPages();
    }

    @Override
    public BoardVo selectBoardOne(BoardSearchVo searchVo) {
        BoardEsVo boardEsVo = boardRepository.findBoardEsVoByIdx(searchVo.getPageIdx());
        return new BoardVo(boardEsVo);
    }

    @Override
    public int insertBoard(BoardVo boardVo) {

        int insertResult = boardServce.insertBoard(boardVo);
        boardVo.setHits(0);

        boardRepository.save(new BoardEsVo(boardVo));

        return insertResult;
    }

    @Override
    public int updateBoard(BoardVo boardVo) {

        int updateResult = boardServce.updateBoard(boardVo);

        BoardSearchVo searchVo = new BoardSearchVo();
        searchVo.setPageIdx(boardVo.getIdx());

        boardVo = boardServce.selectBoardOne(searchVo);
        boardRepository.save(new BoardEsVo(boardVo));

        return updateResult;
    }

    @Override
    public int deleteBoardById(int boardId) {

        BoardSearchVo searchVo = new BoardSearchVo();
        searchVo.setPageIdx(boardId);

        BoardVo boardVo = boardServce.selectBoardOne(searchVo);
        int deleteResult = boardServce.deleteBoardById(boardId);

        boardVo.setState(0);
        boardRepository.save(new BoardEsVo(boardVo));

        return deleteResult;
    }

    private Page<BoardEsVo> getBoardPage(BoardSearchVo searchVo){

        String subject = "";
        String contents = "";

        if(BoardSearchVo.searchKeyWord.subject.toString().equals(searchVo.getSearchCondition())){
            if(StringUtils.isNotEmpty(searchVo.getSearchKeyWord())){
                subject = searchVo.getSearchKeyWord();
            }
        }else if(BoardSearchVo.searchKeyWord.contents.toString().equals(searchVo.getSearchCondition())){
            if(StringUtils.isNotEmpty(searchVo.getSearchKeyWord())){
                contents = searchVo.getSearchKeyWord();
            }
        }

        PageRequest pageRequest = new PageRequest(searchVo.getPageIdx(), 10, null);

        return boardRepository.findBoardEsVosBySubjectAndContentsAndState(subject, contents, 1, pageRequest);
    }
}