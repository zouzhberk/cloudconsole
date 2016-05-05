package org.glassfish.jersey.examples.helloworld.bytebuddy;

/**
 * Created by berk (zouzhberk@163.com)) on 4/15/16.
 */
public class ByteBuddyDemo
{

    public static void main(String[] args) throws Exception
    {
//        ReadProxyGenerator readProxyGenerator = new ReadProxyGenerator();
//        System.out.println(readProxyGenerator.generate());

        DirectoryProxyGenerator directoryProxyGenerator = new
                DirectoryProxyGenerator();
        System.out.println(directoryProxyGenerator.generate());

    }


}
