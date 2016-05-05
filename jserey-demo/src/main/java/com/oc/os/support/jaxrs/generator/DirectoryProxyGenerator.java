package com.oc.os.support.jaxrs.generator;

import com.oc.os.support.jaxrs.DirectoryResourceInfo;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.glassfish.jersey.examples.helloworld.fileproxy.Binding;
import org.glassfish.jersey.examples.helloworld.fileproxy.ElementalProxy;
import org.glassfish.jersey.examples.helloworld.fileproxy.RSDirectoryProxy;

import javax.ws.rs.PathParam;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 5/4/16.
 */
public class DirectoryProxyGenerator
{
    private DirectoryResourceInfo info;

    public DirectoryProxyGenerator(DirectoryResourceInfo info)
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
    public static void processParameter(Parameter parameter, String
            bindingPath, String realPath)
    {
        PathParam pathParam = parameter.getAnnotation(PathParam.class);

        if (pathParam == null)
        {
            return;
        }

        String pathName = pathParam.value();

        //  PatternUtil.match(bindingPath, realPath,"");
    }


    public Class<? extends RSDirectoryProxy> generate()
    {
        String bindingpath = info.getBindingPath();
        String generatedClassPath = info.getProxyClassName();

        DynamicType.Unloaded<? extends RSDirectoryProxy> buddy = new ByteBuddy()
                .subclass(RSDirectoryProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding
                        .class)
                        .define("value", bindingpath)
                        .build())
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
        Class<? extends RSDirectoryProxy> object = buddy.load(getClass()
                .getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        return object;
    }

    public static Optional<String> findPathParamValue(String resourcePath, String
            realPath, String pathParam)
    {
        String str = "{" + pathParam + "}";

        if (!resourcePath.contains(str))
        {
            return Optional.empty();
        }
        String bindingPath = resourcePath.replace(str, "[^/]+");

        Pattern.compile(bindingPath)
                .splitAsStream(realPath)
                .forEach(System.out::println);
        return Optional.empty();
    }

    @RuntimeType
    public List<String> intercept(@This ElementalProxy t) throws Exception
    {
        String realPath = t.path;

        Method m = info.getResourceMethod();

        Class<?> resourceClass = m.getDeclaringClass();
        Object object = getResourceObject(resourceClass);

        Parameter[] parameters = m.getParameters();

        Stream.of(parameters).map(parameter -> {

            PathParam pathParam = parameter.getAnnotation(PathParam.class);

            if (pathParam == null)
            {
                return null;
            }

            String pathName = pathParam.value();


            return null;
        });

        return null;
    }

}
