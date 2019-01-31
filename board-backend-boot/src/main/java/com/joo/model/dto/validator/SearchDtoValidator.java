package com.joo.model.dto.validator;

import com.joo.model.dto.BoardSearchDto;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SearchDtoValidator implements ConstraintValidator<AvailableCondition, String> {

    @Override
    public void initialize(AvailableCondition constraintAnnotation) {}

    @Override
    public boolean isValid(String searchCondition, ConstraintValidatorContext context) {

        if(StringUtils.isEmpty(searchCondition))    return true;
        return BoardSearchDto.SearchKeyWord.contains(searchCondition);
    }
}
