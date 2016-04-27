package com.oc.os.support.jaxrs;


import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/26/16.
 */
public class ResourceNode
{
    private final Class<?> resourceClass;
    private final String resourcePath;

    public ResourceNode(String resourcePath, Class<?> resourceClass)
    {
        this.resourceClass = resourceClass;
        this.resourcePath = resourcePath;
    }


    public void parse()
    {
        this.resourcePath.split("/");


        Stream.of(resourceClass.getMethods())
                .filter(x -> x.getAnnotation(LIST.class) != null);

    }
}