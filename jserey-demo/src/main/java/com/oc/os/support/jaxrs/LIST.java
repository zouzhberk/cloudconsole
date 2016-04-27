package com.oc.os.support.jaxrs;

import java.lang.annotation.*;

/**
 * Created by berk (zouzhberk@163.com)) on 4/26/16.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LIST
{
    String value() default "LIST";
}
