package org.glassfish.jersey.examples.helloworld.rest;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
public class ResourceFactory
{
    public static <T> T getInstance(Class<T> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
