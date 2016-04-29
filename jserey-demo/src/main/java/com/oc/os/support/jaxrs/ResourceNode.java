package com.oc.os.support.jaxrs;


import com.oc.os.support.jaxrs.utils.PathUtils;
import com.oc.os.support.jaxrs.utils.ReflectUtil;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.List;
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

        Stream<DirectoryResourceInfo> parentInfos = PathUtils.parentPaths
                (resourcePath)
                .stream()
                .map(p -> DirectoryResourceInfo.from(p, resourcePath));

//        Observable.from(resourceClass.getDeclaredMethods())
//                .filter(Method::isAccessible)
//                .map(m -> {
//                    Observable.from(m.getDeclaredAnnotations());
//                    return m;
//                });

        Stream.of(resourceClass.getDeclaredMethods())
                .filter(Method::isAccessible)
                .map(m -> {

                    ReflectUtil.getDeclaredAnnotation(m, LIST.class)
                            .ifPresent(a -> {
                                DirectoryResourceInfo info = new
                                        DirectoryResourceInfo();

                                ReflectUtil.getDeclaredAnnotation(m, Path.class)
                                        .map(Path::value)
                                        .map(p -> PathUtils.parentPaths(p,
                                                resourcePath));
                                info.resourcePath = "";
                            });

                    return null;
                });

    }

    public List<String> getPaths()
    {
        Paths.get(resourcePath)
                .iterator()
                .forEachRemaining(System.out::println);
        return null;
    }

}