package com.oc.os.support.jaxrs.generator;

import com.oc.os.support.jaxrs.FileProxyContext;
import com.oc.os.support.jaxrs.ResourceTreeNode;
import com.oc.os.support.jaxrs.fileproxy.Binding;
import com.oc.os.support.jaxrs.fileproxy.ElementalProxy;
import com.oc.os.support.jaxrs.fileproxy.RSReadProxy;
import com.oc.os.support.jaxrs.utils.ReflectUtil;
import com.oc.os.support.jaxrs.utils.StringUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.glassfish.jersey.examples.helloworld.bytebuddy.ByteBuddyDemo;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 5/6/16.
 */
public class GetProxyGenerator extends BaseCodeGenerator
{
    private ResourceTreeNode info;

    public GetProxyGenerator(ResourceTreeNode info)
    {
        this.info = info;
    }

    @RuntimeType
    public String intercept(@This ElementalProxy t) throws Exception
    {
        String realPath = t.path;

        Method m = info.getResourceMethod();

        Class<?> resourceClass = m.getDeclaringClass();
        Object object = getResourceObject(resourceClass);

        Object[] args = Stream.of(m.getParameters()).map(parameter -> {
            PathParam pathParam = parameter.getAnnotation(PathParam.class);
            if (pathParam != null)
            {
                if (!String.class.isAssignableFrom(parameter.getType()))
                {
                    throw new RuntimeException("The PathParam annotation only" + " support String type.");
                }

                String pathName = pathParam.value();
                System.out.println(info.hasPathParam(pathName));
                if (StringUtils.isEmpty(pathName))
                {
                    throw new RuntimeException("PathParam must not be empty");
                }


                return findPathParamValue(info.getResourcePath(), realPath, info.getName()).orElse(null);

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

        if (response instanceof Response)
        {
            return ((Response) response).getEntity() + "";
        }
        return response + "";

    }

    @Override
    public Class<? extends ElementalProxy> generate() throws Exception
    {
        String bindingpath = info.getBindingPath();
        String generatedClassPath = info.getProxyClassName();

        DynamicType.Unloaded<RSReadProxy> buddy = new ByteBuddy().subclass(RSReadProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding.class).define("value", bindingpath).build())
                .name(generatedClassPath)
                .method(named("read"))
                .intercept(MethodDelegation.to(this))
                .make();

        try
        {
            buddy.saveIn(new File("testjar1"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        Class<? extends RSReadProxy> object = buddy.load(ByteBuddyDemo.class.getClassLoader(), ClassLoadingStrategy
                .Default.WRAPPER)
                .getLoaded();

        return object;
    }

}
