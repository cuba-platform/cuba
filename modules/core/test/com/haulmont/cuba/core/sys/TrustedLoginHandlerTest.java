/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;

/**
 * @author gorelov
 * @version $Id$
 */
public class TrustedLoginHandlerTest extends CubaTestCase {
    private TrustedLoginHandler trustedLoginHandler;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        trustedLoginHandler = AppBeans.get(TrustedLoginHandler.class);
    }

    private boolean matches(String address) {
        return trustedLoginHandler.checkAddress(address);
    }

    public void testFullMatch() {
        boolean isMatch = matches("127.0.0.1");
        assertTrue(isMatch);
    }

    public void testPermittedIp() {
        boolean isMatch = matches("10.17.8.3");
        assertTrue(isMatch);
    }

    public void testNotPermittedIp() {
        boolean isMatch = matches("127.10.0.1");
        assertFalse(isMatch);
    }

    public void testNotANumber() {
        boolean isMatch = matches("10.17.8.a");
        assertFalse(isMatch);
    }

    public void testClippedIp() {
        boolean isMatch = matches("10.17.8");
        assertFalse(isMatch);
    }

    public void testOver255() {
        boolean isMatch = matches("10.17.8.999");
        assertFalse(isMatch);
    }

    public void testWrongSeparator() {
        boolean isMatch = matches("10_17_8_3");
        assertFalse(isMatch);
    }
}
