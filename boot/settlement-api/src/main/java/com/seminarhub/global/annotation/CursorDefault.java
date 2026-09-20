package com.seminarhub.global.annotation;

import com.seminarhub.global.dto.Direction;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CursorDefault {
    int size() default 10;
    Direction direction() default Direction.DESC;
}
