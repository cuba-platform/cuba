/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.bali.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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

    @Test
    public void testFindMethod() throws Exception {
        Method method = ReflectionHelper.findMethod(MyParamExt.class, "parentPrivateMethod");
        assertNotNull(method);

        method = ReflectionHelper.findMethod(MyParamExt.class, "privateMethod");
        assertNotNull(method);

        method = ReflectionHelper.findMethod(MyParamExt.class, "noSuchMethod");
        Assertions.assertNull(method, "Should be null");
    }

    public static class MyParam {
        private void parentPrivateMethod() {
        }
    }

    public static class MyParamExt extends MyParam {
        private void privateMethod() {
        }
    }

    public static class MyObject {
        public MyObject(MyParam param) {
        }
    }
}