package com.joo.repository.dsl;

import com.joo.model.dto.BoardDto;
import com.joo.model.dto.BoardSearchDto;
import com.joo.model.dto.FileDto;
import com.joo.model.entity.BoardEntity;
import com.joo.model.state.BoardState;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


import static com.joo.model.entity.QBoardEntity.boardEntity;
import static com.joo.model.entity.QFileEntity.fileEntity;

public class BoardRepositoryImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom{

    private static final Logger logger = LoggerFactory.getLogger(BoardRepositoryImpl.class);

    @Autowired
    private JPAQueryFactory queryFactory;

    public BoardRepositoryImpl() {
        super(BoardEntity.class);
    }

    @Override
    public Page<BoardEntity> findAllDynamic(BoardSearchDto boardSearchDto, BoardState boardState, Pageable pageable) {

        /*
        JPAQuery<Tuple> query = queryFactory
            .select(
                    boardEntity.idx
                    , boardEntity.subject
                    , boardEntity.contents
                    , boardEntity.createdBy
                    , boardEntity.lastModifiedBy
                    , boardEntity.lastModifiedDate
                    , fileEntity.filePath
            ).where(boardEntity.state.eq(0));
        */

        //기본 검색 조건
        BooleanExpression dynamicCondition = boardEntity.state.eq(boardState.getState());

        //검색 조건에 따른 동적 검색 조건
        if(StringUtils.isNotEmpty(boardSearchDto.getSearchCondition())){

            try {
                //검색 할 컬럼을 동적으로 가져온다. String이어야 하고, validate는 상위에서 검사함
                StringExpression searchFiled = (StringExpression) boardEntity.getClass().getDeclaredField(boardSearchDto.getSearchCondition()).get(boardEntity);
                dynamicCondition = dynamicCondition.and(searchFiled.like("%" + boardSearchDto.getSearchKeyWord() + "%"));
            } catch (NoSuchFieldException ignore) {
                logger.warn("찾을 수 없는 필드 : " + boardSearchDto.getSearchCondition());
            } catch (IllegalAccessException e) {
                logger.error("필드를 가져올 수 없음 : " + boardSearchDto.getSearchCondition());
                logger.error(e.getMessage());
            }
        }


        QueryResults<BoardDto> queryResult =
                queryFactory
                .select(Projections.fields(BoardDto.class
                        , boardEntity.idx, boardEntity.subject, boardEntity.contents, boardEntity.hits
                        , boardEntity.createdBy, boardEntity.createdDate, boardEntity.lastModifiedBy, boardEntity.lastModifiedDate
                        //, fileEntity.idx, fileEntity.contentType, fileEntity.fileSize)
                ))
                .from(boardEntity)
                //.leftJoin(boardEntity.fileList, fileEntity)
                .where(dynamicCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardEntity.idx.desc())
                .fetchResults();

        return new PageImpl(queryResult.getResults(), pageable, queryResult.getTotal());
    }
}
