package com.oc.os.support.jaxrs;


import com.google.common.collect.Maps;
import com.oc.os.support.jaxrs.utils.ReflectUtil;
import com.oc.os.support.jaxrs.utils.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    public static void main(String[] args)
    {
        System.out.println(parentPaths("/", "/cloudos/virtualmachine"));

        System.out.println(parentPaths("/cloudos/virtualmachine", "{vmname}"));
        System.out.println(parentPaths("/cloudos/virtualmachine",
                "{vmname}/clouddisk"));
        System.out.println(parentPaths("/cloudos/virtualmachine",
                "{vmname}///clouddisk/{diskname}"));

    }

    public static Map<String, String> parentPaths(String rootPath, String
            resPath)
    {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isEmpty(resPath))
        {
            return map;
        }

        String[] segments = Stream.of(resPath.split("/+"))
                .filter(StringUtils::nonEmpty)
                .toArray(String[]::new);

        if (segments.length == 0)
        {
            return map;
        }

        map.put(rootPath, segments[0]);
        if (segments.length == 1)
        {
            return map;
        }

        Function<Integer, String> keymapper = i -> {
            StringBuilder sb = new StringBuilder(rootPath);

            if (!rootPath.endsWith("/"))
            {
                sb.append("/");
            }

            for (int j = 0; j < i + 1; j++)
            {
                sb.append(segments[j]);
                if (j < i)
                {
                    sb.append("/");
                }
            }

            return sb.toString();
        };

        map.putAll(IntStream.range(0, segments.length - 1)
                .mapToObj(Integer::new)
                .collect(Collectors.toMap(keymapper, i -> segments[i + 1])));

        return map;
    }

    public static Function<Object, List<String>> getDirectoryResponseMapper()
    {
        Function<Object, List<String>> a = response -> {
            if (response == null)
            {
                return Collections.emptyList();
            }

            if (!(response instanceof Response))
            {
                throw new RuntimeException("No support such type" +
                        response.getClass());
            }
            Object entity = ((Response) response).getEntity();
            if (entity instanceof Collection)
            {
                return ((Collection<?>) entity).stream().map(x -> {
                    return x + "";
                }).filter(StringUtils::nonEmpty).collect(Collectors.toList());
            }

            if (entity.getClass().isArray())
            {
                return Stream.of((Object[]) entity)
                        .map(x -> x + "")
                        .filter(StringUtils::nonEmpty)
                        .collect(Collectors.toList());

            }
            return Collections.emptyList();
        };
        return a;
    }

    public void parse()
    {
        this.resourcePath.split("/");
        // 1. 获取class路径, 所有Directory方法。
        Stream<DirectoryResourceInfo> parentInfos = parentPaths("/",
                resourcePath)
                .entrySet()
                .stream()
                .map(x -> DirectoryResourceInfo.from(x.getKey(), x.getValue()));

//        Observable.from(resourceClass.getDeclaredMethods())
//                .filter(Method::isAccessible)
//                .map(m -> {
//                    Observable.from(m.getDeclaredAnnotations());
//                    return m;
//                });
        Function<String, Stream<DirectoryResourceInfo>> mapper = p -> {
            return parentPaths(resourcePath, p).entrySet()
                    .stream()
                    .map(x -> DirectoryResourceInfo.from(x.getKey(), x
                            .getValue()));
        };

        // 2. 获取method path, 所有Directory方法。
        Stream<DirectoryResourceInfo> info1 = Stream.of(resourceClass
                .getDeclaredMethods())
                .filter(Method::isAccessible)
                .filter(x -> ReflectUtil.anyDeclaredAnnotation(x, LIST.class,
                        GET.class, POST.class))
                .flatMap(m -> ReflectUtil.getDeclaredAnnotation(m, Path.class)
                        .map(Path::value)
                        .map(Stream::of)
                        .orElseGet(Stream::empty)
                        .flatMap(mapper));

        // 3. LIST method
        Stream.of(resourceClass.getDeclaredMethods())
                .filter(Method::isAccessible)
                .filter(x -> ReflectUtil.anyDeclaredAnnotation(x, LIST.class))
                .map(m -> invoke(m))
                .map(getDirectoryResponseMapper());

    }

    public Function<Method, Object> invokeMapper()
    {

        return null;
    }

    public Object invoke(Method m, Object... args)
    {
        try
        {
            return m.invoke(getResourceObject(), args);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }

    }

    public Object getResourceObject()
    {
        try
        {
            return resourceClass.newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }


//    public List<String> getPaths()
//    {
//        Paths.get(resourcePath)
//                .iterator()
//                .forEachRemaining(System.out::println);
//        return null;
//    }

}