package com.joo.board;

import com.google.common.collect.ImmutableList;
import com.joo.common.state.CommonState;
import com.joo.model.dto.BoardWriteRequestDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.entity.FileEntity;
import com.joo.repository.BoardRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @TestConfiguration
    static class TestConfig {

        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(entityManager);
        }

        @Bean
        public AuditorAware auditorAware(){
            return () -> Optional.of("jpaTestAccount");
        }
    }



    @Test
    @DisplayName("정상적인 게시물 입력 테스트")
    //@WithUserDetails("customUsername")
    public void a_registBoard(){
        BoardWriteRequestDto normalBoardWriteRequestDto = BoardWriteRequestDto.builder()
                .subject("Board Repository test subject")
                .contents("Board Repository test Contents")
                .build();

        //temp file
        ImmutableList<FileEntity> tempFileList = ImmutableList.of(
                FileEntity.builder().build()
                , FileEntity.builder().build()
                , FileEntity.builder().build()
                , FileEntity.builder().build()
                , FileEntity.builder().build()
        );

        BoardEntity boardEntity = normalBoardWriteRequestDto.toEntity();

        boardEntity.addFiles(tempFileList);

        List<FileEntity> saveFileList = boardEntity.getFileList();

        IntStream.range(0, tempFileList.size())
                .filter(idx -> idx % 2 == 0)
                .mapToObj(idx -> saveFileList.get(idx))
                .forEach(fileEntity -> fileEntity.delete());

        BoardEntity saveNormalBoard = boardRepository.save(boardEntity);

        BoardEntity saveBoardEntity = boardRepository.findById(saveNormalBoard.getIdx()).get();

        assertNotNull(saveBoardEntity);

    }


    /**
     * entity manager에서 수정된 사항을 바로 적용 하기 위하여 transaction을 해제한다.
     */
    @Test
    @DisplayName("정상적으로 사용가능한 게시물 검색 테스트")
    //@WithUserDetails("customUsername")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void b_findEnableBoardTest(){

        BoardWriteRequestDto normalBoardWriteRequestDto = BoardWriteRequestDto.builder()
                .subject("Board Repository test subject")
                .contents("Board Repository test Contents")
                .build();

        BoardEntity boardEntity = normalBoardWriteRequestDto.toEntity();

        //temp file
        ImmutableList<FileEntity> tempFileList = ImmutableList.of(
                FileEntity.builder().build()
                , FileEntity.builder().build()
                , FileEntity.builder().build()
                , FileEntity.builder().build()
                , FileEntity.builder().build()
        );

        boardEntity.addFiles(tempFileList);

        List<FileEntity> saveFileList = boardEntity.getFileList();
        IntStream.range(0, tempFileList.size())
                .filter(idx -> idx % 2 == 0)
                .mapToObj(idx -> saveFileList.get(idx))
                .forEach(fileEntity -> fileEntity.delete());

        BoardEntity boardEntityFromMemory = boardRepository.save(boardEntity);

        BoardEntity searchBoardEntity = boardRepository.findEnableBoardByBoardIdx(boardEntityFromMemory.getIdx())
                .orElse(null);

        assertNotNull(searchBoardEntity);

        List<FileEntity> fileList = searchBoardEntity.getFileList();

        //검색된 파일이 모두 사용 가능한것만 검색 된건지 테스트
        boolean isDisableFileExist = fileList.stream()
                .map(FileEntity::getState)
                .filter(fileState -> fileState != CommonState.ENABLE)
                .findAny().isPresent();


        assertFalse(isDisableFileExist);
        assertThat(fileList.size(), is(2));
    }
}
