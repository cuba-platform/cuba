/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author gorelov
 * @version $Id$
 */
public class TrustedLoginHandlerTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private TrustedLoginHandler trustedLoginHandler;

    @Before
    public void setUp() throws Exception {
        trustedLoginHandler = AppBeans.get(TrustedLoginHandler.class);
    }

    private boolean matches(String address) {
        return trustedLoginHandler.checkAddress(address);
    }

    @Test
    public void testFullMatch() {
        boolean isMatch = matches("127.0.0.1");
        assertTrue(isMatch);
    }

    @Test
    public void testPermittedIp() {
        boolean isMatch = matches("10.17.8.3");
        assertTrue(isMatch);
    }

    @Test
    public void testNotPermittedIp() {
        boolean isMatch = matches("127.10.0.1");
        assertFalse(isMatch);
    }

    @Test
    public void testNotANumber() {
        boolean isMatch = matches("10.17.8.a");
        assertFalse(isMatch);
    }

    @Test
    public void testClippedIp() {
        boolean isMatch = matches("10.17.8");
        assertFalse(isMatch);
    }

    @Test
    public void testOver255() {
        boolean isMatch = matches("10.17.8.999");
        assertFalse(isMatch);
    }

    @Test
    public void testWrongSeparator() {
        boolean isMatch = matches("10_17_8_3");
        assertFalse(isMatch);
    }
}
