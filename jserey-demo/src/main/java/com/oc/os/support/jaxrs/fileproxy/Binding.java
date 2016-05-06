package com.oc.os.support.jaxrs.fileproxy;

import java.lang.annotation.*;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Binding
{
    String value() default "";
}
