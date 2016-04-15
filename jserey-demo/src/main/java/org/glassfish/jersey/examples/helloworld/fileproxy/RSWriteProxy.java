package org.glassfish.jersey.examples.helloworld.fileproxy;

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
    public void write()
    {
        post();
    }

    protected abstract void post();

}