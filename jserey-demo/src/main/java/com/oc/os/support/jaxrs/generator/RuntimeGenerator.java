package com.oc.os.support.jaxrs.generator;

import com.oc.os.support.jaxrs.fileproxy.ElementalProxy;

/**
 * Created by berk (zouzhberk@163.com)) on 4/25/16.
 */
public interface RuntimeGenerator
{
    Class<? extends ElementalProxy> generate() throws Exception;
}
