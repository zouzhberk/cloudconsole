package com.oc.os.support.jaxrs.generator;

import com.oc.os.support.jaxrs.DirectoryResourceInfo;
import com.oc.os.support.jaxrs.FileProxyContext;
import com.oc.os.support.jaxrs.utils.ReflectUtil;
import com.oc.os.support.jaxrs.utils.StringUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import com.oc.os.support.jaxrs.fileproxy.Binding;
import com.oc.os.support.jaxrs.fileproxy.ElementalProxy;
import com.oc.os.support.jaxrs.fileproxy.RSDirectoryProxy;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 5/4/16.
 */
@Deprecated
public class DeprecatedDirectoryProxyGenerator
{
    private DirectoryResourceInfo info;

    public DeprecatedDirectoryProxyGenerator(DirectoryResourceInfo info)
    {
        this.info = info;
    }

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

    public static Function<Object, List<String>> getDirectoryResponseMapper()
    {
        Function<Object, List<String>> a = response -> {
            if (response == null)
            {
                return Collections.emptyList();
            }

            if (!(response instanceof Response))
            {
                throw new RuntimeException("No support such type" + response.getClass());
            }
            Object entity = ((Response) response).getEntity();
            if (entity instanceof Collection)
            {
                return ((Collection<?>) entity).stream().map(x -> {
                    return x + "";
                }).filter(StringUtils::nonEmpty).collect(Collectors.toList());
            }

            if (entity.getClass().isArray())
            {
                return Stream.of((Object[]) entity)
                        .map(x -> x + "")
                        .filter(StringUtils::nonEmpty)
                        .collect(Collectors.toList());

            }
            return Collections.emptyList();
        };
        return a;
    }


    public Class<? extends RSDirectoryProxy> generate()
    {
        String bindingpath = info.getBindingPath();
        String generatedClassPath = info.getProxyClassName();

        DynamicType.Unloaded<? extends RSDirectoryProxy> buddy = new ByteBuddy().subclass(RSDirectoryProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding.class).define("value", bindingpath).build())
                .name(generatedClassPath)
                .method(named("list"))
                .intercept(MethodDelegation.to(this))
                .make();

        try
        {
            buddy.saveIn(new File("testjar"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Class<? extends RSDirectoryProxy> object = buddy.load(getClass().getClassLoader(), ClassLoadingStrategy
                .Default.WRAPPER)
                .getLoaded();

        return object;
    }

    @RuntimeType
    public List<String> intercept(@This ElementalProxy t) throws Exception
    {
        String realPath = t.path;

        Method m = info.getResourceMethod();

        Class<?> resourceClass = m.getDeclaringClass();
        Object object = getResourceObject(resourceClass);

        Object[] args = Stream.of(m.getParameters()).map(parameter -> {


            System.out.println(parameter.isNamePresent());
            PathParam pathParam = parameter.getAnnotation(PathParam.class);
            if (pathParam != null)
            {
                if (!String.class.isAssignableFrom(parameter.getType()))
                {
                    throw new RuntimeException("The PathParam annotation only" + " support String type.");
                }

                String pathName = pathParam.value();


                if (StringUtils.isEmpty(pathName))
                {
                    throw new RuntimeException("PathParam must not be empty");
                }


                return findPathParamValue(info.getResourcePath(), realPath, pathName).orElse(null);

            }

            if (parameter.isAnnotationPresent(Context.class))
            {
                if (!FileProxyContext.class.isAssignableFrom(parameter.getType()))
                {
                    throw new RuntimeException("The context annnotation only supports " +
                            FileProxyContext.class +
                            " class.");
                }

                return FileProxyContext.from(t);

            }
            return null;
        }).toArray();

        Object response = ReflectUtil.invoke(object, m, args);
        List<String> result = info.getMethodSupplier().get();
        result.addAll(Optional.ofNullable(response)
                .map(getDirectoryResponseMapper())
                .orElseGet(Collections::emptyList));
        return result;
    }
}
