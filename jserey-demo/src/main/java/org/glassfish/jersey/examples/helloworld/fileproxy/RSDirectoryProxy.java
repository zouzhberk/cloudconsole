package org.glassfish.jersey.examples.helloworld.fileproxy;

import java.util.List;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
public abstract class RSDirectoryProxy
{
    public abstract List<String> list();

    public String help()
    {
        return "";
    }

}