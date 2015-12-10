/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import groovy.lang.Binding;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ScriptingTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    protected Scripting scripting;

    @Before
    public void setUp() throws Exception {
        scripting = AppBeans.get(Scripting.class);
    }

    @Test
    public void testSimpleEvaluate() throws Exception {
        Integer intResult = scripting.evaluateGroovy("2 + 2", new Binding());
        assertEquals((Integer)4, intResult);

        Binding binding = new Binding();
        binding.setVariable("instance", new User());
        Boolean boolResult = scripting.evaluateGroovy("return PersistenceHelper.isNew(instance)", binding);
        assertTrue(boolResult);
    }

    @Test
    public void testImportsEvaluate() {
        String result = scripting.evaluateGroovy("import com.haulmont.bali.util.StringHelper\n" +
                                                 "return StringHelper.removeExtraSpaces(' Hello! ')", (Binding) null);
        assertNotNull(result);
    }

    @Test
    public void testPackageAndImportsEvaluate() {
        String result = scripting.evaluateGroovy("package com.haulmont.cuba.core\n" +
                "import com.haulmont.bali.util.StringHelper\n" +
                "return StringHelper.removeExtraSpaces(' Hello! ')", (Binding) null);
        assertNotNull(result);
    }

    @Test
    public void testPackageOnlyEvaluate() {
        Binding binding = new Binding();
        binding.setVariable("instance", new User());
        Boolean result = scripting.evaluateGroovy("package com.haulmont.cuba.core\n"+
                                       "return PersistenceHelper.isNew(instance)", binding);
        assertTrue(result);
    }
}
