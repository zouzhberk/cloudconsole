package com.oc.os.support.jaxrs;


import com.google.common.collect.Maps;
import com.oc.os.support.jaxrs.utils.ReflectUtil;
import com.oc.os.support.jaxrs.utils.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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


    public static Map<String, String> parentPaths(String rootPath, String resPath)
    {
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isEmpty(resPath))
        {
            return map;
        }

        String[] segments = Stream.of(resPath.split("/+")).filter(StringUtils::nonEmpty).toArray(String[]::new);

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


    public ResourceTreeNode parse1(ResourceTreeNode rootNode)
    {
        final ResourceTreeNode node = rootNode.addChild(resourcePath);

        Stream.of(resourceClass.getDeclaredMethods()).forEach(m -> {
            ResourceTreeNode tmpnode = ReflectUtil.getDeclaredAnnotation(m, Path.class)
                    .map(Path::value)
                    .filter(StringUtils::nonEmpty)
                    .map(node::addChild)
                    .orElse(node);
            if (ReflectUtil.hasAnnotation(m, LIST.class))
            {
                tmpnode.setResourceMethod(m);
            }

            if (ReflectUtil.hasAnnotation(m, GET.class))
            {
                tmpnode.addChild(new ResourceTreeNode("info", tmpnode, m, RequestType.GET));
            }

            if (ReflectUtil.hasAnnotation(m, POST.class))
            {
                tmpnode.addChild(new ResourceTreeNode("post", null, m, RequestType.POST));
            }

        });

        return node;
    }

    public void parse()
    {
        this.resourcePath.split("/");
        // 1. 获取class路径, 所有Directory方法。
        Stream<DirectoryResourceInfo> parentInfos = parentPaths("/", resourcePath).entrySet()
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
                    .map(x -> DirectoryResourceInfo.from(x.getKey(), x.getValue()));
        };

        // 2. 获取method path, 所有Directory方法。
        Stream<DirectoryResourceInfo> info1 = Stream.of(resourceClass.getDeclaredMethods())
                .filter(Method::isAccessible)
                .filter(x -> ReflectUtil.anyDeclaredAnnotation(x, LIST.class, GET.class, POST.class))
                .flatMap(m -> ReflectUtil.getDeclaredAnnotation(m, Path.class)
                        .map(Path::value)
                        .map(Stream::of)
                        .orElseGet(Stream::empty)
                        .flatMap(mapper));

        // 3. LIST method
        Stream.of(resourceClass.getDeclaredMethods())
                .filter(Method::isAccessible)
                .filter(x -> ReflectUtil.anyDeclaredAnnotation(x, LIST.class))
                .map(m -> m);


    }

    public Function<Method, Object> invokeMapper()
    {
        return method -> {
            Parameter[] parameters = method.getParameters();

            //Stream.of(parameters)

            return null;
        };
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