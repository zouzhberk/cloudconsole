package com.oc.os.support.jaxrs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ResourceTreeNode Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>May 6, 2016</pre>
 */
public class ResourceTreeNodeTest
{

    @Before
    public void before() throws Exception
    {
    }

    @After
    public void after() throws Exception
    {
    }

    /**
     * Method: NONE()
     */
    @Test
    public void testNONE() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: rootNode()
     */
    @Test
    public void testRootNode() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: isNone()
     */
    @Test
    public void testIsNone() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: addChild(String resourcePath)
     */
    @Test
    public void testAddChildResourcePath() throws Exception
    {
        ResourceTreeNode node = ResourceTreeNode.rootNode();
        node.addChild("/cloudos/virtualmachine");
        ResourceTreeNode node1 = node.getRoot().addChild("/cloudos/volume/{volumename}");

        System.out.println(node1.getResourcePath());
        System.out.println(node1.getShortName());
        System.out.println(node1.getProxyClassName());

        node.getRoot().dump().forEach(System.out::println);
        node.getRoot().dumpResourcePaths().forEach(System.out::println);
    }

    /**
     * Method: merge(ResourceTreeNode node)
     */
    @Test
    public void testMerge() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getName()
     */
    @Test
    public void testGetName() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: setName(String name)
     */
    @Test
    public void testSetName() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getParent()
     */
    @Test
    public void testGetParent() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: setParent(ResourceTreeNode parent)
     */
    @Test
    public void testSetParent() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: addChild(ResourceTreeNode child)
     */
    @Test
    public void testAddChildChild() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getRoot()
     */
    @Test
    public void testGetRoot() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getChild(String resourcePath)
     */
    @Test
    public void testGetChild() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getResourcePath()
     */
    @Test
    public void testGetResourcePath() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: isDirectory()
     */
    @Test
    public void testIsDirectory() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: isRoot()
     */
    @Test
    public void testIsRoot() throws Exception
    {
//TODO: Test goes here... 
    }


} 
