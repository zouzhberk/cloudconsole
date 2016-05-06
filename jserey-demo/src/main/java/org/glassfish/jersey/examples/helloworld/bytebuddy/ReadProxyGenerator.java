package org.glassfish.jersey.examples.helloworld.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import com.oc.os.support.jaxrs.fileproxy.Binding;
import com.oc.os.support.jaxrs.fileproxy.ElementalProxy;
import com.oc.os.support.jaxrs.fileproxy.RSReadProxy;

import java.util.Arrays;
import java.util.UUID;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 4/25/16.
 */
public class ReadProxyGenerator
{

    public Class<?> generate() throws Exception
    {
        String bindingpath = "/virtualmachine/info";
        String generatedClassPath = "com.berk.VMReadProxy";

        DynamicType.Unloaded<RSReadProxy> buddy = new ByteBuddy().subclass
                (RSReadProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding
                        .class)
                        .define("value", bindingpath)
                        .build())
                .name(generatedClassPath)
                .method(named("read"))
                .intercept(MethodDelegation.to(DefaultReadProxyInterceptor
                        .class))
                .make();

        Class<? extends RSReadProxy> object = buddy.load(ByteBuddyDemo.class
                .getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        System.out.println(Arrays.asList(object.getAnnotations()));

        System.out.println(object.getAnnotation(Binding.class).value());
        System.out.println(object.newInstance().read());

        return object;
    }

    public interface ReadProxyInterceptor
    {
        @RuntimeType
        String intercept() throws Exception;

    }

    public static class DefaultReadProxyInterceptor
    {
        @RuntimeType
        public static String intercept(@This ElementalProxy t) throws Exception
        {
            System.out.println("hello," + "world" + ".");
            return "hellworld" + UUID.randomUUID();

        }
    }

}
