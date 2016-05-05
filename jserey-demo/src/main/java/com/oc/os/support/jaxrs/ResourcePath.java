package com.oc.os.support.jaxrs;

/**
 * Created by berk (zouzhberk@163.com)) on 4/29/16.
 */
public class ResourcePath
{
    public String resourcePath;

    public static void main(String[] args)
    {
        ResourcePath path = new ResourcePath();
        path.resourcePath = "/cloudos/{name1}/virtuamchine/{name2}";
        System.out.println(path.getRegexPath());
    }

    public String getRegexPath()
    {
        return resourcePath.replaceAll("\\{[^\\{]+\\}", "([^/]+)");
    }
}
