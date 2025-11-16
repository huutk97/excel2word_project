package com.handler.excel2word.core.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ExcelColumn {
    String name() default "";

    int index() default 0;

    int width() default 20;

    boolean skip() default false;

    String textMethod() default "";

    boolean isJavaBean() default false;

    boolean haveColor() default false;
}
