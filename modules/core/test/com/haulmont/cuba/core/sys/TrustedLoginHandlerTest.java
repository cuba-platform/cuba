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

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.sys.TrustedLoginHandler;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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