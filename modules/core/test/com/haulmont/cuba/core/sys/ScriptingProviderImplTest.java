/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.11.2010 16:34:57
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import junit.framework.TestCase;

public class ScriptingProviderImplTest extends TestCase {

    public void testRecompilation() throws Exception {
        GroovyScriptEngine gse = new GroovyScriptEngine(System.getProperty("user.dir") + "/modules/core/test/com/haulmont/cuba/core/sys");
        for (int i = 0; i < 3; i++) {
            gse.run("GseTest.groovy", new Binding());
        }
    }
}
