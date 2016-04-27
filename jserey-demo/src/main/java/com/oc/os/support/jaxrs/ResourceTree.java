package com.oc.os.support.jaxrs;

import java.util.List;

/**
 * Created by berk (zouzhberk@163.com)) on 4/21/16.
 */
public class ResourceTree
{

    String path;
    DefaultResourceInfo classInfo;
    List<ResourceTree> subtrees;

    public ResourceTree(String path, DefaultResourceInfo classInfo,
                        List<ResourceTree> subtrees)
    {
        this.path = path;
        this.classInfo = classInfo;
        this.subtrees = subtrees;
    }

    public boolean isRoot()
    {
        return this.path.equals("/");
    }

    public boolean hasSubResources()
    {
        return subtrees == null || subtrees.isEmpty();
    }

    public static class Root extends ResourceTree
    {
        public Root(DefaultResourceInfo classInfo, List<ResourceTree> subtrees)
        {
            super("/", classInfo, subtrees);
        }
    }
}
