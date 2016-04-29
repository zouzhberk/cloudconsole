package com.oc.os.support.jaxrs;

import com.oc.os.support.jaxrs.utils.PathUtils;

/**
 * Created by berk (zouzhberk@163.com)) on 4/27/16.
 */
public class DirectoryResourceInfo implements
        FileProxyResourceInfo<DirectorySupplier>

{
    //    FileProxyContext.OperationType type;
    String resourcePath;
    DirectorySupplier supplier;

    public static DirectoryResourceInfo from(String curPath, String fullPath)
    {
        DirectoryResourceInfo resourceInfo = new DirectoryResourceInfo();
        resourceInfo.resourcePath = curPath;
        resourceInfo.supplier = PathUtils.nextSegment(curPath, fullPath)
                .map(DirectorySupplier::as)
                .orElseGet(DirectorySupplier::empty);
        return resourceInfo;
    }

    public DirectorySupplier getMethodSupplier()
    {

        return supplier;

    }

    public String getBindingPath()
    {
        return null;
    }

    @Override
    public FileProxyContext.OperationType getOperationType()
    {
        return FileProxyContext.OperationType.LIST;
    }
}
