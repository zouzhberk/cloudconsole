package com.oc.os.support.jaxrs;

import com.oc.os.support.jaxrs.fileproxy.ElementalProxy;
import com.oc.os.support.jaxrs.generator.GeneratorFactory;
import com.oc.os.support.jaxrs.utils.StringUtils;
import org.glassfish.jersey.examples.helloworld.rest.HelloWorldResource;
import org.glassfish.jersey.examples.helloworld.rest.VirtualMachineResource;

import javax.ws.rs.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/21/16.
 */
public class CodeGenerator
{

    private ResourceConfig config;

    public CodeGenerator(ResourceConfig config)
    {
        this.config = config;
    }

    public static String formatResourcePath(String resourcepath)
    {
        return Stream.of(resourcepath.split("/"))
                .filter(StringUtils::nonEmpty)
                .collect(Collectors.joining("/", "/", ""));
    }

    public static void main2(String[] args)
    {
        DirectorySupplier supplier = DirectorySupplier.empty();
        System.out.println(supplier.get());
        System.out.println(supplier.append("asdfasdf", "test2").get());
        System.out.println(supplier.append("asdfasdf", "test2").merge(DirectorySupplier.empty().append("berk")).get());
    }

    public static void main(String[] args)
    {


        ResourceConfig config = new ResourceConfig();
        config.registerClass(VirtualMachineResource.class);
        config.registerClass(HelloWorldResource.class);
        CodeGenerator generator = new CodeGenerator(config);

        generator.generate();


    }


    public Set<Class<? extends ElementalProxy>> generate()
    {

        Function<? super Class<?>, String> keyMapper = x -> {
            String path = x.getAnnotation(Path.class).value();
            return formatResourcePath(path);
        };

        Map<String, Class<?>> rootResources = config.getResourceClasses()
                .stream()
                .collect(Collectors.toMap(keyMapper, Function.identity()));

        System.out.println(rootResources);
        // common path
        // sub path
        //
        ResourceTreeNode rootNode = ResourceTreeNode.rootNode();
        rootResources.entrySet()
                .stream()
                .forEach(entry -> new ResourceNode(entry.getKey(), entry.getValue()).parse1(rootNode));

        System.out.println(rootNode.dump().map(x -> x.getResourcePath()).collect(Collectors.toList()));
        System.out.println(rootNode.dumpResourcePaths().collect(Collectors.toList()));
        return rootNode.dump().map(GeneratorFactory::get).filter(Objects::nonNull).map(x -> {
            try
            {
                return x.generate();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toSet());


    }

}
