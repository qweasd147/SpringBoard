package com.joo.api.board.vo.validator;

import com.joo.api.board.vo.BoardSearchVo.SearchKeyWord;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SearchVoValidator implements ConstraintValidator<AvailableCondition, String> {

    @Override
    public void initialize(AvailableCondition constraintAnnotation) {}

    @Override
    public boolean isValid(String searchCondition, ConstraintValidatorContext context) {

        if(StringUtils.isEmpty(searchCondition))    return true;
        return SearchKeyWord.hasCondition(searchCondition);
    }
}
