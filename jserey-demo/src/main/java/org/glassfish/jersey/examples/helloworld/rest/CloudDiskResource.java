package org.glassfish.jersey.examples.helloworld.rest;

import org.glassfish.jersey.examples.helloworld.domain.VolumeEntity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by berk (zouzhberk@163.com)) on 4/26/16.
 */
@Path("clouddisk")
public class CloudDiskResource
{
    private final String vmname;

    public CloudDiskResource(String vmname)
    {
        this.vmname = vmname;
    }

    @GET
    public Response listCloudDiskNames()
    {
        return Response.ok(Arrays.asList("disk1,disk2")).build();
    }

    @Path("{diskname}")
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
