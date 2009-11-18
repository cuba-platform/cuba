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
import groovy.lang.Binding;

public class ScriptingProviderTest extends CubaTestCase {

    public void test() {
        Binding binding = new Binding();
        ScriptingProvider.runGroovyScript("cuba/test/Test1.groovy", binding);
        assertEquals("ok", binding.getVariable("result"));
    }
}
