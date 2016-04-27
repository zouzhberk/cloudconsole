package org.glassfish.jersey.examples.helloworld.rest;

import com.oc.os.support.jaxrs.LIST;
import org.glassfish.jersey.examples.helloworld.domain.FolderItems;
import org.glassfish.jersey.examples.helloworld.domain.VirtualMachineEntity;
import org.glassfish.jersey.examples.helloworld.domain.VolumeEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by berk (zouzhberk@163.com)) on 4/13/16.
 */
@Path("/cloudos/virtualmachine")
@Produces("application/json")
public class VirtualMachineResource
{
    @GET
    @LIST
    public Response listVirtualMachines()
    {
        System.out.println("berk, list vm ok");
        return Response.ok(FolderItems.asList("vm-name1", "vm-name2")).build();
    }

    @GET
    @Path("{name}")
    public Response detailVM(@PathParam("name") String name)
    {
        VirtualMachineEntity entity = new VirtualMachineEntity();
        entity.setDisplayName(name);
        entity.setUuid(UUID.randomUUID().toString());
        return Response.ok(entity).build();
    }

    @POST
    public Response deployVM()
    {
        System.out.println("deploy vm ok");
        return Response.ok().build();
    }

    @PUT
    public Response updateVM()
    {
        System.out.println("update vm ok");
        return Response.ok().build();
    }


    @DELETE
    public Response destroyVM()
    {
        System.out.println("delete vm ok");
        return Response.ok().build();
    }

    @Path("{vmname}/clouddisk")
    @GET
    public CloudDiskResource getCloudDiskResource(@PathParam("vmname") String
                                                              vmname)
    {
        return new CloudDiskResource(vmname);
    }

    @Path("{vmname}/clouddisk")
    @GET
    public Response listCloudDiskNames(@PathParam("vmname") String vmname)
    {
        return Response.ok(Arrays.asList("disk1,disk2")).build();
    }

    @Path("{vmname}/clouddisk/{diskname}")
    @GET
    public Response getCloudDiskInfo(@PathParam("vmname") String vmname,
                                     @PathParam("diskname") String diskname)
    {
        VolumeEntity info = new VolumeEntity();
        info.setUuid(UUID.randomUUID().toString());
        info.setDisplayName(diskname);
        info.setVmname(vmname);
        return Response.ok(info).build();
    }
}