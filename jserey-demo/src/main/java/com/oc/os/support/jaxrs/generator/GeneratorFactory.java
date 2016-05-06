package com.oc.os.support.jaxrs.generator;

import com.oc.os.support.jaxrs.ResourceTreeNode;

/**
 * Created by berk (zouzhberk@163.com)) on 5/6/16.
 */
public class GeneratorFactory
{
    public static RuntimeGenerator get(ResourceTreeNode node)
    {
        switch (node.getType())
        {
            case LIST:
                return new DirectoryProxyGenerator(node);
            case GET:
                break;
            case POST:
                break;
        }
        return null;
    }
}
