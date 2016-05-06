package org.glassfish.jersey.examples.helloworld.generated;

import com.oc.os.support.jaxrs.fileproxy.Binding;
import com.oc.os.support.jaxrs.fileproxy.RSDirectoryProxy;
import org.glassfish.jersey.examples.helloworld.rest.ResourceFactory;
import org.glassfish.jersey.examples.helloworld.rest.VirtualMachineResource;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 * <p>
 * org.glassfish.jersey.examples.helloworld
 * .VirtualMachineResource#listVirtualMachines()
 * <p>
 * List virtualmachine directory.
 */
@Binding("/cloudos/virtualmachine")
public class VirtualMachineProxy extends RSDirectoryProxy
{
    @Override
    public List<String> list()
    {
        Response response = ResourceFactory.getInstance
                (VirtualMachineResource.class)
                .listVirtualMachines();
        return (List<String>) response.getEntity();

    }
}
