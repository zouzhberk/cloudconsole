package com.oc.os.support.jaxrs;


import com.oc.os.support.jaxrs.utils.ReflectUtil;

import javax.ws.rs.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/21/16.
 */
public class ResourceConfig
{

    private Set<Class<?>> resourceClasses;

    public ResourceConfig()
    {
        this.resourceClasses = new HashSet<>();
    }

    public static void main(String[] args)
    {


    }

    public Set<Class<?>> getResourceClasses()
    {
        return resourceClasses;
    }

    public void registerClass(Class<?>... classes)
    {
        Stream.of(classes)
                .filter(Objects::nonNull)
                .filter(clazz -> ReflectUtil.hasAnnotation(clazz, Path.class))
                .forEach(resourceClasses::add);
    }


}
