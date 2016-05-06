package org.glassfish.jersey.examples.helloworld.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import com.oc.os.support.jaxrs.fileproxy.Binding;
import com.oc.os.support.jaxrs.fileproxy.RSWriteProxy;

import java.util.Arrays;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 4/25/16.
 */
public class WriteProxyGenerator
{
    public Class<?> generate() throws Exception
    {

        DynamicType.Unloaded<RSWriteProxy> buddy = new ByteBuddy().subclass
                (RSWriteProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding
                        .class)
                        .define("value", "/virtualmachine")
                        .build())
                .name("com.berk.VMWriteProxy").method(named("post"))
                        //.intercept(StubMethod.INSTANCE)
                .intercept(MethodDelegation.to(MyServiceInterceptor.class))
                .make();

        Class<? extends RSWriteProxy> object = buddy.load(ByteBuddyDemo.class
                .getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        System.out.println(Arrays.asList(object.getAnnotations()));

        System.out.println(object.getAnnotation(Binding.class).value());
        object.newInstance().write("fileproxy");
        return object;
    }

    public static class MyServiceInterceptor
    {
        @RuntimeType
        public static void intercept(String content) throws Exception
        {
            System.out.println("hello," + content + ".");

        }
    }
}