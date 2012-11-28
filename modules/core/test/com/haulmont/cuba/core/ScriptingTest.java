/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.security.entity.User;
import groovy.lang.Binding;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ScriptingTest extends CubaTestCase {

    protected Scripting scripting;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        scripting = AppBeans.get(Scripting.class);
    }

    public void testEvaluate() throws Exception {
        Integer intResult = scripting.evaluateGroovy("2 + 2", new Binding());
        assertEquals((Integer)4, intResult);

        Binding binding = new Binding();
        binding.setVariable("instance", new User());
        Boolean boolResult = scripting.evaluateGroovy("return PersistenceHelper.isNew(instance)", binding);
        assertTrue(boolResult);
    }
}
