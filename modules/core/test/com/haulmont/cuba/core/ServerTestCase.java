/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 10:29:29
 * $Id$
 */
package com.haulmont.cuba.core;

import junit.framework.TestCase;

public class ServerTestCase extends TestCase
{
    protected void setUp() throws Exception {
        super.setUp();
        TestContainer.start();
    }
}
