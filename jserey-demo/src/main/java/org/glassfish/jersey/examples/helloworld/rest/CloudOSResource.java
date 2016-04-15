package org.glassfish.jersey.examples.helloworld.rest;

import javax.ws.rs.Path;

/**
 * Created by berk (zouzhberk@163.com)) on 4/13/16.
 */
@Path("cloudos")
public class CloudOSResource
{

    @Path("virtualmachine")
    public VirtualMachineResource getVirtualMachineResource()
    {
        return new VirtualMachineResource();
    }
}
