package com.joo.board;

import com.google.common.collect.ImmutableList;
import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardWriteRequestDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.repository.BoardRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    private BoardEntity saveNormalBoard;

    @After
    public void deleteAll(){
        //TODO : 차후 테스트 한 데이터만 삭제 or use in memory DB
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("정상적인 게시물 입력 테스트")
    @WithUserDetails("customUsername")
    public void a_registBoard(){
        BoardWriteRequestDto normalBoardWriteRequestDto = BoardWriteRequestDto.builder()
                .subject("Board Repository test subject")
                .contents("Board Repository test Contents")
                .build();


        //temp file
        ImmutableList<FileEntity> tempFileList = ImmutableList.of(
                FileEntity.builder()
                        .state(CommonState.ENABLE)
                        .build()
                , FileEntity.builder()
                        .state(CommonState.DELETE)
                        .build()
                , FileEntity.builder()
                        .state(CommonState.ENABLE)
                        .build()
                , FileEntity.builder()
                        .state(CommonState.ENABLE)
                        .build()
                , FileEntity.builder()
                        .state(CommonState.DELETE)
                        .build()
        );



        BoardEntity boardEntity = normalBoardWriteRequestDto.toEntity();

        boardEntity.addFiles(tempFileList);

        saveNormalBoard = boardRepository.save(boardEntity);
    }
    

    @Test
    @DisplayName("정상적으로 사용가능한 게시물 검색 테스트")
    @WithUserDetails("customUsername")
    public void b_findEnableBoardTest(){

        a_registBoard();

        BoardEntity searchBoardEntity = boardRepository.findEnableBoardByBoardIdx(saveNormalBoard.getIdx())
                .orElse(BoardEntity.builder().build());

        assertThat(searchBoardEntity.getIdx()).isEqualTo(saveNormalBoard.getIdx());
        assertThat(searchBoardEntity.getState()).isEqualTo(CommonState.ENABLE);

        assertThat(searchBoardEntity.getSubject()).isEqualTo(saveNormalBoard.getSubject());
        assertThat(searchBoardEntity.getContents()).isEqualTo(saveNormalBoard.getContents());


        List<FileEntity> fileList = searchBoardEntity.getFileList();

        //검색된 파일이 모두 사용 가능한것만 검색 된건지 테스트
        boolean isDisableFileExist = fileList.stream()
                .map(FileEntity::getState)
                .filter(fileState -> fileState != CommonState.ENABLE)
                .findAny().isPresent();

        assertFalse(isDisableFileExist);
    }
}
