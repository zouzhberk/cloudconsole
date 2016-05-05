package org.glassfish.jersey.examples.helloworld.bytebuddy;

/**
 * Created by berk (zouzhberk@163.com)) on 4/25/16.
 */
public interface RuntimeGenerator
{
    Class<?> generate() throws Exception;
}
