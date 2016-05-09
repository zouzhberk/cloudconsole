package com.oc.os.support.jaxrs.generator;

import com.oc.os.support.jaxrs.ResourceTreeNode;
import com.oc.os.support.jaxrs.fileproxy.Binding;
import com.oc.os.support.jaxrs.fileproxy.ElementalProxy;
import com.oc.os.support.jaxrs.fileproxy.RSWriteProxy;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.glassfish.jersey.examples.helloworld.bytebuddy.ByteBuddyDemo;

import java.io.File;
import java.util.Arrays;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * Created by berk (zouzhberk@163.com)) on 5/9/16.
 */
public class PostProxyGenerator extends BaseCodeGenerator
{
    private ResourceTreeNode info;

    public PostProxyGenerator(ResourceTreeNode node)
    {
        this.info = node;
    }

    @RuntimeType
    public void intercept(String content) throws Exception
    {
        System.out.println("hello," + content + ".");

    }

    @Override
    public Class<? extends ElementalProxy> generate() throws Exception
    {

        String bindingPath = info.getBindingPath();

        String className = info.getProxyClassName();
        DynamicType.Unloaded<RSWriteProxy> buddy = new ByteBuddy().subclass(RSWriteProxy.class)
                .annotateType(AnnotationDescription.Builder.ofType(Binding.class).define("value", bindingPath).build())
                .name(className)
                .method(named("post"))
                .intercept(MethodDelegation.to(this))
                .make();

        buddy.saveIn(new File("testjar1"));

        Class<? extends RSWriteProxy> object = buddy.load(ByteBuddyDemo.class.getClassLoader(), ClassLoadingStrategy
                .Default.WRAPPER)
                .getLoaded();
        System.out.println(Arrays.asList(object.getAnnotations()));
        return object;
    }


}
