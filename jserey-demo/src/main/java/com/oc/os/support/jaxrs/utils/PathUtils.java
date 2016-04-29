package com.oc.os.support.jaxrs.utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/27/16.
 */
public class PathUtils
{
    public static Optional<String> nextSegment(String curPath, String fullPath)
    {
        return Optional.ofNullable(curPath)
                .filter(fullPath::startsWith)
                .map(c -> fullPath.replaceFirst(c, ""))
                .map(x -> x.split("/"))
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .filter(StringUtils::nonEmpty)
                .findFirst();

    }

    public static Optional<String> subPath(String originPath, String parentPath)
    {
        return Optional.ofNullable(originPath)
                .filter(x -> x.startsWith(parentPath))
                .map(x -> x.substring(parentPath.length()))
                .map(x -> Stream.of(x.split("/")))
                .orElseGet(Stream::empty)
                .filter(StringUtils::nonEmpty)
                .findFirst();

    }

    public static Map<String, String> listParentPaths(String resourcePath)
    {

        return parentPaths(resourcePath).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Function.identity(), (x) -> subPath
                        (resourcePath, x)
                        .orElse("")));


    }

    public static Stream<String> segments(String resourcePath)
    {
        return Stream.of(resourcePath.split("/")).filter(StringUtils::nonEmpty);
    }


    public static Set<String> parentPaths(String resPath, String parentPath)
    {
        if (StringUtils.isEmpty(resPath))
        {
            return Collections.emptySet();
        }

        Set<String> ret = new LinkedHashSet<>();
        int fIndex = resPath.indexOf("/", 0);


        while (true)
        {
            if (fIndex < 0)
            {
                ret.add(parentPath);
                break;
            }
            else
            {
                ret.add(concatPath(parentPath, resPath.substring(0, fIndex)));
            }

            fIndex = resPath.indexOf("/", fIndex + 1);
        }

        return ret;
    }

    public static Set<String> parentPaths(String resourcePath)
    {
        return parentPaths(resourcePath, "/");
    }

    public static void main(String[] argms)
    {
        System.out.println(concatPath("/cloudos/", "/virtualmchine/",
                "diskname/"));
        String path = "/cloudos/virtualmachine/{vmname}/clouddisk/{diskname}/";
        System.out.println(parentPaths("asdfas"));
        System.out.println(parentPaths(path, "/"));
        System.out.println(parentPaths(path, "/cloudos"));
        System.out.println(parentPaths(path, path + "/"));
        System.out.println(listParentPaths(path));
    }

    public static String concatPath(String first, String... more)
    {
        return Stream.concat(Stream.of(first), Stream.of(more))
                .flatMap(x -> Stream.of(x.split("/")))
                .filter(StringUtils::nonEmpty)
                .collect(Collectors.joining("/", "/", ""));
    }
}
