package org.glassfish.jersey.examples.helloworld.domain;

/**
 * Created by berk (zouzhberk@163.com)) on 4/14/16.
 */
public class VolumeEntity
{
    private String displayName;
    private String uuid;


    private String vmname;

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getVmname()
    {
        return vmname;
    }

    public void setVmname(String vmname)
    {
        this.vmname = vmname;
    }
}
