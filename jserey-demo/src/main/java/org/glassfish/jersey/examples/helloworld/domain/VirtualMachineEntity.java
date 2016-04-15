package org.glassfish.jersey.examples.helloworld.domain;

/**
 * Created by berk (zouzhberk@163.com)) on 4/13/16.
 */
public class VirtualMachineEntity
{
    private String displayName;
    private String uuid;


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
}
