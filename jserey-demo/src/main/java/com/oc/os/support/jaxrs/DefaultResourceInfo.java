package com.oc.os.support.jaxrs;

import java.lang.reflect.Method;

/**
 * Created by berk (zouzhberk@163.com)) on 4/21/16.
 */
public class DefaultResourceInfo implements javax.ws.rs.container.ResourceInfo
{
    Object resourceObject;
    Method resourceMethod;

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
}
