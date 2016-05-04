package com.oc.os.support.jaxrs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/27/16.
 */
public interface DirectorySupplier extends Supplier<List<String>>
{
    static DirectorySupplier empty()
    {
        return ArrayList::new;
    }

    static DirectorySupplier as(String... items)
    {
        if (items == null)
        {
            return empty();
        }
        return () -> Arrays.asList(items);
    }

    static DirectorySupplier as(List<String> items)
    {
        if (items == null)
        {
            return empty();
        }
        return () -> items;
    }

    default DirectorySupplier merge(DirectorySupplier otherSupplier)
    {
        List<String> list = get();
        list.addAll(otherSupplier.get());
        return as(list);
    }

    default DirectorySupplier append(String... elements)
    {
        return () -> {
            List<String> list = this.get();
            Stream.of(elements).forEach(list::add);
            return list;
        };
    }
}
