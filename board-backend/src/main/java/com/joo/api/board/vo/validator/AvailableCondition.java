package com.joo.api.board.vo.validator;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)     //컴파일 이후에도 참조 가능
@Documented
@Constraint(validatedBy = SearchVoValidator.class)
public @interface AvailableCondition {
    String message() default "잘못된 입력값";
    Class<?>[] groups() default {};                           //특정 그룹만 validate
    Class<? extends Payload>[] payload() default {};        //심각도 정의
}
