package com.oc.os.support.jaxrs;

import org.glassfish.jersey.examples.helloworld.rest.HelloWorldResource;
import org.glassfish.jersey.examples.helloworld.rest.VirtualMachineResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * ResourceNode Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>May 6, 2016</pre>
 */
public class ResourceNodeTest
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
     * Method: parentPaths(String rootPath, String resPath)
     */
    @Test
    public void testParentPaths() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: parse1()
     */
    @Test
    public void testParse1() throws Exception
    {
        ResourceConfig config = new ResourceConfig();
        config.registerClass(VirtualMachineResource.class);
        config.registerClass(HelloWorldResource.class);
        CodeGenerator generator = new CodeGenerator(config);

        generator.generate();

    }

    /**
     * Method: parse()
     */
    @Test
    public void testParse() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: invokeMapper()
     */
    @Test
    public void testInvokeMapper() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: invoke(Method m, Object... args)
     */
    @Test
    public void testInvoke() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: getResourceObject()
     */
    @Test
    public void testGetResourceObject() throws Exception
    {
//TODO: Test goes here... 
    }


} 
