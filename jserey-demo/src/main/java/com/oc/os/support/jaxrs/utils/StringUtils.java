package com.oc.os.support.jaxrs.utils;

/**
 * Created by berk (zouzhberk@163.com)) on 4/26/16.
 */
public class StringUtils
{
    /**
     * Check whether the given {@code String} is empty.
     * <p>This method accepts any Object as an argument, comparing it to
     * {@code null} and the empty String. As a consequence, this method
     * will never return {@code true} for a non-null non-String object.
     * <p>The Object signature is useful for general attribute handling code
     * that commonly deals with Strings but generally has to iterate over
     * Objects since attributes may e.g. be primitive value objects as well.
     *
     * @param str the candidate String
     */
    public static boolean isEmpty(Object str)
    {
        return (str == null || "".equals(str));
    }

    public static boolean nonEmpty(String str)
    {
        return (str != null && str.trim().length() != 0);
    }
}
