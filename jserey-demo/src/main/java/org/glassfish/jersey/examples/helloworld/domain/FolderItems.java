package org.glassfish.jersey.examples.helloworld.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Created by berk (zouzhberk@163.com)) on 4/13/16.
 */
public class FolderItems
{
    List<String> items;


    private FolderItems(String... c)
    {
        items = Arrays.asList(c);
    }

    public static FolderItems asList(String... items)
    {
        return new FolderItems(items);
    }

    public List<String> getItems()
    {
        return items;
    }

    public void setItems(List<String> items)
    {
        this.items = items;
    }
}
