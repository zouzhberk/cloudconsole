package com.oc.os.support.jaxrs;

import jersey.repackaged.com.google.common.collect.Maps;
import org.glassfish.jersey.examples.helloworld.fileproxy.*;

import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.container.ResourceInfo;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by berk (zouzhberk@163.com)) on 4/25/16.
 */
public class FileProxyContext implements ResourceContext
{
    private String path;
    private String user;
    private String reserved;
    private OperationType operationType;

    public static FileProxyContext from(ElementalProxy proxy)
    {

        FileProxyContext context = new FileProxyContext();
        context.path = proxy.path;
        context.user = proxy.user;

        if (proxy instanceof RSWriteProxy)
        {
            context.operationType = OperationType.WRITE;
        }
        if (proxy instanceof RSReadProxy)
        {
            context.operationType = OperationType.READ;
        }
        if (proxy instanceof RSDirectoryProxy)
        {
            context.operationType = OperationType.LIST;
        }
        return context;
    }

    public ResourceInfo getResourceInfo()
    {
        ResourceInfo info = new ResourceInfo()
        {
            @Override
            public Method getResourceMethod()
            {
                return null;
            }

            @Override
            public Class<?> getResourceClass()
            {
                return null;
            }
        };

        return info;
    }

    public Map<String, String> getPathParams(List<String> groupNames)
    {
        Binding binding = getClass().getAnnotation(Binding.class);
        String regex = binding.value();
        Map<String, String> groupValues = Maps.newLinkedHashMap();
        PatternUtil.match(regex, path, groupNames, groupValues);
        return groupValues;
    }

    @Override
    public <T> T getResource(Class<T> resourceClass)
    {
        return null;
    }

    @Override
    public <T> T initResource(T resource)
    {
        return null;
    }

    public static enum OperationType
    {
        READ,
        WRITE,
        LIST,
        INFO

    }
}
