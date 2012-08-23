/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import junit.framework.TestCase;

/**
 * @author krivopustov
 * @version $Id$
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
