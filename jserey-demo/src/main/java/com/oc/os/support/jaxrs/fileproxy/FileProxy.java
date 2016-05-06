package com.oc.os.support.jaxrs.fileproxy;

import jersey.repackaged.com.google.common.collect.Maps;
import org.glassfish.jersey.examples.helloworld.rest.ResourceFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
public abstract class FileProxy extends ElementalProxy
{

    public static <T> T getInstance(Class<T> clazz)
    {
        return ResourceFactory.getInstance(clazz);
    }

    /**
     * @param rsPath like "/xxx/{name}"
     * @return
     */
    public static List<String> getPathNames(String rsPath)
    {
        return Stream.of(rsPath.split("\\{|\\}"))
                .filter(x -> !x.contains("/"))
                .collect(Collectors.toList());

    }

    public static void main(String[] args)
    {
        String url = "/cloudos/virtualmachine/{name}/clouddisk/{diskname}";
        System.out.println(getPathNames(url));
    }

    public abstract Object read();

    public void write(String content)
    {

    }

    public String help()
    {
        return "";
    }

    public long getSize()
    {
        return 1;
    }

    public String getPathParam(String tag)
    {
        Matcher matcher = Pattern.compile("").matcher(path);
        return "";
    }

    public Map<String, String> getPathParams(List<String> groupNames)
    {
        Binding binding = getClass().getAnnotation(Binding.class);
        String regex = binding.value();
        Map<String, String> groupValues = Maps.newLinkedHashMap();
        PatternUtil.match(regex, path, groupNames, groupValues);
        return groupValues;
    }
}
