/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.bali.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class ReflectionHelperTest {

    @Test
    public void testNewInstance() throws Exception {
        MyObject myObject = ReflectionHelper.newInstance(MyObject.class, new MyParam());
        assertNotNull(myObject);

        myObject = ReflectionHelper.newInstance(MyObject.class, new MyParamExt());
        assertNotNull(myObject);

        try {
            ReflectionHelper.newInstance(MyObject.class, "str");
            fail();
        } catch (NoSuchMethodException e) {
            // ok
        }
    }

    public static class MyParam {
    }

    public static class MyParamExt extends MyParam {
    }

    public static class MyObject {
        public MyObject(MyParam param) {
        }
    }
}