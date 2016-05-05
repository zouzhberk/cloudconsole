package com.oc.os.support.jaxrs;

/**
 * Created by berk (zouzhberk@163.com)) on 4/29/16.
 */
public interface FileProxyResourceInfo<T>
{
    String getBindingPath();

    FileProxyContext.OperationType getOperationType();

    T getMethodSupplier();

}
