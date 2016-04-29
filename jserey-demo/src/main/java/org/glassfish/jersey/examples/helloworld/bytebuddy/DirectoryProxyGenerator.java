package org.glassfish.jersey.examples.helloworld.bytebuddy;

import com.oc.os.support.jaxrs.FileProxyContext;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.This;
import org.glassfish.jersey.examples.helloworld.fileproxy.Binding;
import org.glassfish.jersey.examples.helloworld.fileproxy.ElementalProxy;
import org.glassfish.jersey.examples.helloworld.fileproxy.RSDirectoryProxy;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 4/25/16.
 */
public class DirectoryProxyGenerator implements RuntimeGenerator
{

    @Override
    public Class<?> generate() throws Exception
    {
        String bindingpath = "/virtualmachine/info";
        String generatedClassPath = "com.berk.VMReadProxy";

        DynamicType.Unloaded<RSDirectoryProxy> buddy = new ByteBuddy()
                .subclass(RSDirectoryProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding
                        .class)
                        .define("value", bindingpath)
                        .build())
                .name(generatedClassPath)
                .method(named("list"))
                .intercept(MethodDelegation.to
                        (DefaultDirectoryProxyInterceptor.class))
                .make();

        buddy.saveIn(new File("testjar"));
        Class<? extends RSDirectoryProxy> object = buddy.load(ByteBuddyDemo
                .class
                .getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        System.out.println(Arrays.asList(object.getAnnotations()));

        System.out.println(object.getAnnotation(Binding.class).value());
        RSDirectoryProxy proxy = object.newInstance();
        proxy.user = "berk";
        System.out.println(proxy.list());

        return object;
    }


    public static class DefaultDirectoryProxyInterceptor
    {
        //@RuntimeType
        public static List<String> intercept(@This ElementalProxy t) throws
                Exception
        {
            FileProxyContext.from(t);
            //ResourceInfo resourceInfo = new DefaultResourceInfo();


            System.out.println("hello," + "world" + ".");
            return Arrays.asList("hello", t.path, t.user);

        }
    }
}
