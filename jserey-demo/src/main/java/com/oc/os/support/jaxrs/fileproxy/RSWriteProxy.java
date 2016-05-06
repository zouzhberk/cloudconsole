package com.oc.os.support.jaxrs.fileproxy;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
public abstract class RSWriteProxy extends FileProxy
{
    @Override
    public Object read()
    {
        return null;
    }

    @Override
    public void write(String content)
    {
        post(content);
    }

    protected abstract void post(String content);

}