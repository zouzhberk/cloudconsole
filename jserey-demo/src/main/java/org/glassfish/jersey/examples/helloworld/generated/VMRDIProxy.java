package org.glassfish.jersey.examples.helloworld.generated;

import org.glassfish.jersey.examples.helloworld.rest.VirtualMachineResource;
import org.glassfish.jersey.examples.helloworld.fileproxy.Binding;
import org.glassfish.jersey.examples.helloworld.fileproxy.RSReadProxy;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
@Binding("/cloudos/virtualmachine/[^/]+/info")
public class VMRDIProxy extends RSReadProxy
{
    @Override
    public Object read()
    {
        String url = "/cloudos/virtualmachine/{name}";
        // /cloudos/virtualmachine/([^/]+)/info
        return getInstance(VirtualMachineResource.class).detailVM
                (getPathParam("VM"));

    }

}
