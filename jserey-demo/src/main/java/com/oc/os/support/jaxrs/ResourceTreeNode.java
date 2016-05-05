package com.oc.os.support.jaxrs;

import com.oc.os.support.jaxrs.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by berk (zouzhberk@163.com)) on 4/21/16.
 */
public class ResourceTreeNode
{

    private String name;

    private Map<String, ResourceTreeNode> childrenMap = new LinkedHashMap<>();


    private ResourceTreeNode parent;


    private Method resourceMethod;

    public ResourceTreeNode(String name, ResourceTreeNode parent, Method resourceMethod)
    {
        this.name = name;
        this.parent = parent;
        this.resourceMethod = resourceMethod;
    }

    public static ResourceTreeNode NONE()
    {
        return new ResourceTreeNode(null, null, null);
    }

    public static ResourceTreeNode rootNode()
    {
        return new ResourceTreeNode("/", null, null);
    }

    public boolean isNone()
    {
        return name == null;
    }

    public ResourceTreeNode addChild(String resourcePath)
    {
        if (StringUtils.isEmpty(resourcePath))
        {
            return this;
        }

        if (resourcePath.startsWith("/"))
        {
            return this.getRoot().addChild(resourcePath.substring(1));
        }

        if (resourcePath.contains("/"))
        {
            int i = resourcePath.indexOf("/");
            String subpath = resourcePath.substring(0, i);


            ResourceTreeNode child = childrenMap.get(subpath);

            if (child == null)
            {
                addChild(new ResourceTreeNode(subpath, this, null));
                return addChild(resourcePath.substring(i + 1));
            }
            return child.addChild(resourcePath);
        }
        else
        {
            childrenMap.putIfAbsent(resourcePath, new ResourceTreeNode(resourcePath, this, null));
            return this;
        }

    }

    public ResourceTreeNode merge(ResourceTreeNode node)
    {
        if (this.name.equals(node.name))
        {
            if (this.resourceMethod == null)
            {
                this.resourceMethod = node.resourceMethod;
            }

            this.childrenMap.putAll(node.childrenMap);
            return this;

        }


        return this;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ResourceTreeNode getParent()
    {
        return parent;
    }

    public void setParent(ResourceTreeNode parent)
    {
        this.parent = parent;
    }

    public ResourceTreeNode addChild(ResourceTreeNode child)
    {
        child.setParent(this);
        this.childrenMap.put(child.getName(), child);
        return child;
    }

    public ResourceTreeNode getRoot()
    {
        return isRoot() ? this : parent.getRoot();
    }

    /**
     * Search child node with resource path,<p>
     * If resourcePath start with "/", starting from the Root Node to search.
     *
     * @param resourcePath The resource path.
     * @return The child node.
     */
    public ResourceTreeNode getChild(String resourcePath)
    {
        if (resourcePath == null)
        {
            return null;
        }

        if (resourcePath.startsWith("/"))
        {
            return getRoot().getChild(resourcePath.substring(1));
        }

        if (resourcePath.contains("/"))
        {
            int i = resourcePath.indexOf("/");
            String subpath = resourcePath.substring(0, i);
            ResourceTreeNode child = childrenMap.get(subpath);
            return child == null ? null : child.getChild(resourcePath.substring(i + 1));
        }
        else
        {
            return childrenMap.get(resourcePath);
        }
    }

    public String getResourcePath()
    {
        return isRoot() ? name : parent.getResourcePath() + "/" + name;
    }

    public boolean isDirectory()
    {
        return childrenMap.size() > 0;
    }


    public boolean isRoot()
    {
        return parent == null && "/".equals(name);
    }

}
