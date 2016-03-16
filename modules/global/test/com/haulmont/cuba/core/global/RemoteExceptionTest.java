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

package com.haulmont.cuba.core.global;

import junit.framework.TestCase;

/**
 */
public class RemoteExceptionTest extends TestCase {

    private static class MyException extends RuntimeException {
    }

    private void raise(RuntimeException exception) {
        throw exception;
    }

    public void test() {
        RemoteException re = null;
        try {
            raise(new MyException());
        } catch (Exception e) {
            re = new RemoteException(e);
        }
        assertNotNull(re);

        assertNotNull(re.getCause(MyException.class.getName()));
        assertTrue(re.contains(MyException.class));
        assertTrue(re.contains(MyException.class.getName()));

        assertNull(re.getCause(RuntimeException.class.getName()));
        assertFalse(re.contains(RuntimeException.class));
        assertFalse(re.contains(RuntimeException.class.getName()));
    }
}