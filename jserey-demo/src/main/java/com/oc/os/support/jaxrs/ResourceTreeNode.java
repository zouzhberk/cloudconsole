package com.oc.os.support.jaxrs;

import com.google.common.collect.Lists;
import com.oc.os.support.jaxrs.utils.StringUtils;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by berk (zouzhberk@163.com)) on 4/21/16.
 */
public class ResourceTreeNode
{
    public static final String ROOT_PREFIX = "ROOT";

    private String name;
    private Map<String, ResourceTreeNode> childrenMap = new LinkedHashMap<>();
    private ResourceTreeNode parent;
    /**
     *
     */
    private Method resourceMethod;
    private RequestType type;


    private Set<String> dyamicPathNames = null;
//    /**
//     * Contain '{'and '}' or not.
//     */
//    private boolean isDyamicPath = false;

    public ResourceTreeNode(String name, ResourceTreeNode parent, Method resourceMethod)
    {
        this.name = name;
        this.parent = parent;
        this.resourceMethod = resourceMethod;
        this.type = RequestType.LIST;
    }

//    private List<String> dynamicNames;

    public ResourceTreeNode(String name, ResourceTreeNode parent, Method resourceMethod, RequestType type)
    {
        this.name = name;
        this.parent = parent;
        this.resourceMethod = resourceMethod;
        this.type = type;
    }

    public static ResourceTreeNode rootNode()
    {
        return new ResourceTreeNode(ROOT_PREFIX, null, null);
    }

    public boolean isDyamicPath()
    {
        return dyamicPathNames != null && !dyamicPathNames.isEmpty();
    }

    private List<ResourceTreeNode> privateDump()
    {
        List<ResourceTreeNode> nodes = Lists.newLinkedList();


        nodes.add(this);
        nodes.addAll(this.childrenMap.values()
                .stream()
                .flatMap(x -> x.privateDump().stream())
                .collect(Collectors.toList()));
        return nodes;
    }

    public RequestType getType()
    {
        return type;
    }

    public void setType(RequestType type)
    {
        this.type = type;
    }

    public Map<String, ResourceTreeNode> getChildrenMap()
    {
        return childrenMap;
    }

    //public static ResourceTreeNode NONE()
//    {
//        return new ResourceTreeNode(null, null, null);
//    }
    public Set<String> childrenNames()
    {
        return childrenMap.keySet();
    }

    public Stream<String> subResourcePaths()
    {
        return childrenMap.values().stream().map(ResourceTreeNode::getResourcePath);
    }

    public boolean isNone()
    {
        return name == null;
    }

    public Stream<String> dumpResourcePaths()
    {
        if (this.childrenMap.isEmpty())
        {
            return Stream.of(this.getResourcePath());
        }
        return Stream.concat(Stream.of(this.getResourcePath()), this.childrenMap.values()
                .stream()
                .flatMap(x -> x.dumpResourcePaths()));


    }

    public Stream<ResourceTreeNode> dump()
    {
        return privateDump().stream();
    }

    @Override
    public String toString()
    {
        return "ResourceTreeNode{" +
                "name='" + name + '\'' +
                ", resourceMethod=" + resourceMethod +
                ", type=" + type +
                ", childrenMap=" + childrenMap.keySet() +
                '}';
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
                child = addChild(new ResourceTreeNode(subpath, this, null));

            }
            return child.addChild(resourcePath.substring(i + 1));

        }
        else
        {
            return addChild(new ResourceTreeNode(resourcePath, this, null));
        }

    }

    public ResourceTreeNode update(ResourceTreeNode node)
    {
        if (!type.equals(node.type))
        {
            throw new RuntimeException("unsupport different type merge");
        }

        if (resourceMethod == null)
        {
            resourceMethod = node.resourceMethod;
        }
        childrenMap.putAll(node.childrenMap.values()
                .stream()
                .map(x -> x.setParent(this))
                .collect(Collectors.toMap(x -> x.getName(), Function.identity())));
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

    public ResourceTreeNode setParent(ResourceTreeNode parent)
    {
        this.parent = parent;
        return this;
    }

    public ResourceTreeNode addChild(ResourceTreeNode child)
    {

        child.setParent(this);

        // process dynamic path name.
        if (child.getName().contains("{"))
        {
            if (child.dyamicPathNames == null)
            {
                child.dyamicPathNames = new HashSet<>(2);
            }

            child.dyamicPathNames.add(child.getName());

            ResourceTreeNode dyamicChild = this.childrenMap.values()
                    .stream()
                    .filter(x -> x.isDyamicPath())
                    .findFirst()
                    .orElse(null);

            if (dyamicChild != null)
            {
                child = dyamicChild.update(child);
            }

            this.childrenMap.put(child.getName(), child);
            return child;
        }


        child = this.childrenMap.getOrDefault(child.getName(), child).update(child);
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
        return isRoot() ? "/" : privateGetResourcePath();
    }

    private String privateGetResourcePath()
    {
        return isRoot() ? "" : parent.privateGetResourcePath() + "/" + name;
    }

    public boolean isDirectory()
    {
        return childrenMap.size() > 0;
    }

    public boolean isRoot()
    {
        return parent == null && ROOT_PREFIX.equals(name);
    }

    public String getBindingPath()
    {
        return this.getResourcePath().replaceAll("\\{[^\\{]+\\}", "([^/]+)");
    }

    public String getProxyClassName()
    {
        return "com.berk.test." + "FP" + getPrefixName().toUpperCase() + Instant.now().getNano();
    }

    public String getShortName()
    {
        return name.startsWith("{") ? "C" : name.substring(0, 1);

    }

    private String getPrefixName()
    {

        return isRoot() ? "" : parent.getPrefixName() + getShortName();
    }

    public Method getResourceMethod()
    {
        return resourceMethod;
    }

    public void setResourceMethod(Method resourceMethod)
    {
        this.resourceMethod = resourceMethod;
    }

    public boolean hasPathParam(String name)
    {
        return this.dyamicPathNames == null ? false : this.dyamicPathNames.contains(name);
    }
}
