package com.joo.board;

import com.joo.common.state.CommonState;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.repository.BoardRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;


    //TODO : 현재 테스트 코드 샘플 형식으로 작성. 추후 정확한 값이 나오도록 셋팅

    @Test
    @DisplayName("정상적으로 사용가능한 게시물 검색 테스트")
    public void findEnableBoardTest(){
        long boardIndex = 15;

        BoardEntity boardEntity = boardRepository.findEnableBoardByBoardIdx(boardIndex)
                .orElse(BoardEntity.builder().build());

        assertThat(boardEntity.getIdx()).isEqualTo(boardIndex);
        assertThat(boardEntity.getState()).isEqualTo(CommonState.ENABLE);

        List<FileEntity> fileList = boardEntity.getFileList();

        //검색된 파일이 모두 사용 가능한것만 검색 된건지 테스트
        boolean isDisableFileExist = fileList.stream()
                .filter(file -> file.getState() != CommonState.ENABLE)
                .findAny().isPresent();

        assertTrue(isDisableFileExist);
    }
}
