package com.oc.os.support.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/21/16.
 */
public class ReflectUtil
{

    public static boolean hasAnnotation(Class<?> clazz, Class<? extends
            Annotation> annotationClass)
    {
        return clazz.getAnnotation(annotationClass) != null;
    }


    public static Stream<Method> getMethodByAnnotation(Class<?> clazz,
                                                       Annotation...
                                                               annotations)
    {

        //Stream.of(clazz.getMethods()).filter(x-> x.get)
        return null;
    }
}
