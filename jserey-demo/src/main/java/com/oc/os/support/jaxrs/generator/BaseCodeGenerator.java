package com.oc.os.support.jaxrs.generator;

import javax.ws.rs.PathParam;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by berk (zouzhberk@163.com)) on 5/6/16.
 */
public abstract class BaseCodeGenerator implements RuntimeGenerator
{
    public static Object getResourceObject(Class<?> resourceClass)
    {
        if (resourceClass.isInterface())
        {
            throw new RuntimeException("Unsupport interface.");
        }
        try
        {
            return resourceClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * @param parameter
     */
    public static void processParameter(Parameter parameter, String bindingPath, String realPath)
    {
        PathParam pathParam = parameter.getAnnotation(PathParam.class);

        if (pathParam == null)
        {
            return;
        }

        String pathName = pathParam.value();

        //  PatternUtil.match(bindingPath, realPath,"");
    }

    public static Optional<String> findPathParamValue(String resourcePath, String realPath, String pathParam)
    {
        String str = "{" + pathParam + "}";

        if (!resourcePath.contains(str))
        {
            return Optional.empty();
        }
        String bindingPath = resourcePath.replace(str, "([^/]+)/?");
        Matcher matcher = Pattern.compile(bindingPath).matcher(realPath);

        return Optional.of(matcher).filter(Matcher::matches).map(x -> x.group(1));
    }
}
