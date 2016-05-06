package com.oc.os.support.jaxrs.generator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

/**
 * DirectoryProxyGenerator Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>May 5, 2016</pre>
 */
public class DirectoryProxyGeneratorTest
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
     * Method: getResourceObject(Class<?> resourceClass)
     */
    @Test
    public void testGetResourceObject() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: processParameter(Parameter parameter, String
     * bindingPath, String realPath)
     */
    @Test
    public void testProcessParameter() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: findPathParamValue(String resourcePath, String realPath, String
     * pathParam)
     */
    @Test
    public void testFindPathParamValue() throws Exception
    {
        String resourcePath = "/cloudos/virtualmachine/{vmname}";
        String realPath = "/cloudos/virtualmachine/vmname1/";
        String pathParam = "vmname";
        DirectoryProxyGenerator.findPathParamValue(resourcePath, realPath,
                pathParam)
                .ifPresent(System.out::println);
    }

    /**
     * Method: generate()
     */
    @Test
    public void testGenerate() throws Exception
    {
//TODO: Test goes here... 
    }

    /**
     * Method: intercept(@This ElementalProxy t)
     */
    @Test
    public void testIntercept() throws Exception
    {
        Stream.of(DirectoryProxyGenerator.class.getMethods())
                .forEach(x -> System.out.println(x +
                        ", " +
                        x.getParameters().length));
    }


} 
