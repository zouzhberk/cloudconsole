package org.glassfish.jersey.examples.helloworld.bytebuddy;

import com.oc.os.support.jaxrs.FileProxyContext;
import com.oc.os.support.jaxrs.fileproxy.Binding;
import com.oc.os.support.jaxrs.fileproxy.ElementalProxy;
import com.oc.os.support.jaxrs.fileproxy.RSDirectoryProxy;
import com.oc.os.support.jaxrs.generator.RuntimeGenerator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 4/25/16.
 */
public class DirectoryProxyGenerator implements RuntimeGenerator
{

    @Override
    public Class<? extends ElementalProxy> generate() throws Exception
    {
        String bindingpath = "/virtualmachine/info";
        String generatedClassPath = "com.berk.VMReadProxy";

        DynamicType.Unloaded<RSDirectoryProxy> buddy = new ByteBuddy().subclass(RSDirectoryProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding.class).define("value", bindingpath).build())
                .name(generatedClassPath)
                .method(named("list"))
                .intercept(MethodDelegation.to(this))
                .make();

        buddy.saveIn(new File("testjar"));
        Class<? extends RSDirectoryProxy> object = buddy.load(ByteBuddyDemo.class.getClassLoader(),
                ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        System.out.println(Arrays.asList(object.getAnnotations()));

        System.out.println(object.getAnnotation(Binding.class).value());
        RSDirectoryProxy proxy = object.newInstance();
        proxy.user = "berk";
        System.out.println(proxy.list());

        return object;
    }

    @RuntimeType
    public List<String> intercept(@This ElementalProxy t) throws Exception
    {
        FileProxyContext.from(t);
        //ResourceInfo resourceInfo = new DefaultResourceInfo();
        Stream.of(t.getClass().getDeclaredMethods())
                .peek(System.out::println)
                .map(x -> x.getDeclaringClass())
                .forEach(System.out::println);


        System.out.println("hello," + "world" + ".");
        return Arrays.asList("hello", t.path, t.user);

    }

    public static class DefaultDirectoryProxyInterceptor1
    {
        private Method m;

        public List<String> intercept(@This ElementalProxy t) throws Exception
        {
            FileProxyContext.from(t);
            //ResourceInfo resourceInfo = new DefaultResourceInfo();
            Stream.of(t.getClass().getDeclaredMethods())
                    .peek(System.out::println)
                    .map(x -> x.getDeclaringClass())
                    .forEach(System.out::println);


            System.out.println("hello," + "world" + ".");
            return Arrays.asList("hello", t.path, t.user);

        }
    }

    public static class DefaultDirectoryProxyInterceptor
    {
        //@RuntimeType
        public static List<String> intercept(@This ElementalProxy t) throws Exception
        {
            FileProxyContext.from(t);
            //ResourceInfo resourceInfo = new DefaultResourceInfo();
            Stream.of(t.getClass().getDeclaredMethods())
                    .peek(System.out::println)
                    .map(x -> x.getDeclaringClass())
                    .forEach(System.out::println);

            System.out.println("hello," + "world" + ".");
            return Arrays.asList("hello", t.path, t.user);

        }
    }
}
