/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.11.2009 10:34:31
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.global.HsqlDbDialect;
import groovy.lang.Binding;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class ScriptingProviderTest extends CubaTestCase {

    public void testScript() {
        Binding binding = new Binding();
        ScriptingProvider.runGroovyScript("cuba/test/Test1.groovy", binding);
        assertEquals("ok", binding.getVariable("result"));
    }

    public void testClass() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class cls = ScriptingProvider.loadClass("cuba.test.TestClass1");
        Object obj = cls.newInstance();
        assertTrue(obj instanceof HsqlDbDialect);
        Method method = cls.getMethod("testMethod");
        Object value = method.invoke(obj);
        assertEquals("OK", value);
    }
}
