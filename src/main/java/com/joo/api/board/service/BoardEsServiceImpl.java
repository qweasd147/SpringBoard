package com.joo.api.board.service;

import com.joo.api.board.mapper.repository.BoardRepository;
import com.joo.api.board.vo.BoardEsVo;
import com.joo.api.board.vo.BoardSearchVo;
import com.joo.api.board.vo.BoardVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@Service(value = "boardEsServiceImpl")
public class BoardEsServiceImpl implements BoardServce{

    //@Resource(name = "boardServiceImpl")
    private BoardServce boardServce;

    private BoardRepository boardRepository;

    //@Autowired
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
    public BoardVo insertBoard(BoardVo boardVo, MultipartFile[] uploadFile) {

        BoardVo insertBoardVo = boardServce.insertBoard(boardVo, uploadFile);

        insertBoardVo.setHits(0);

        boardRepository.save(new BoardEsVo(insertBoardVo));

        return boardVo;
    }

    @Override
    public BoardVo updateBoard(BoardVo boardVo, MultipartFile[] uploadFile, List<Integer> detachFileIds) {

        boardServce.updateBoard(boardVo, uploadFile, detachFileIds);

        BoardSearchVo searchVo = new BoardSearchVo();
        searchVo.setPageIdx(boardVo.getIdx());

        //es에 조회수 정보도 입력하기 위해 조회
        BoardVo updatedBoardVo = boardServce.selectBoardOne(searchVo);
        boardRepository.save(new BoardEsVo(updatedBoardVo));

        return updatedBoardVo;
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