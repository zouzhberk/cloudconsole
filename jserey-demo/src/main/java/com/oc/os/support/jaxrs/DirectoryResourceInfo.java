package com.oc.os.support.jaxrs;

import java.lang.reflect.Method;

/**
 * Created by berk (zouzhberk@163.com)) on 4/27/16.
 */
public class DirectoryResourceInfo implements
        FileProxyResourceInfo<DirectorySupplier>

{
    String resourcePath;
    DirectorySupplier supplier;

    private Method resourceMethod;


    public static DirectoryResourceInfo from(String curPath, String... values)
    {
        DirectoryResourceInfo resourceInfo = new DirectoryResourceInfo();
        resourceInfo.resourcePath = curPath;
        resourceInfo.supplier = DirectorySupplier.as(values);
        return resourceInfo;
    }

    @Deprecated
    public DirectorySupplier getMethodSupplier()
    {
        return supplier;
    }

    public String getBindingPath()
    {
        return resourcePath;
    }

    @Override
    public FileProxyContext.OperationType getOperationType()
    {
        return FileProxyContext.OperationType.LIST;
    }

    public Method getResourceMethod()
    {
        return resourceMethod;
    }


    public String getResourcePath()
    {
        return resourcePath;
    }
//    public static String getFirstChars(String path)
//    {
//        return null;
//    }

    public String getProxyClassName()
    {
        return "com.berk.proxy." +
                "DirectoryProxy" + resourcePath.hashCode();
    }
}
