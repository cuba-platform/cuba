/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import com.haulmont.cuba.core.global.AppBeans;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UniqueNumbersTest extends CubaTestCase {
    public void test() {
        UniqueNumbersAPI mBean = AppBeans.get(UniqueNumbersAPI.NAME);
        long n = mBean.getNextNumber("test1");
        assertTrue(n >= 0);
    }

    public void testSequenceDeletion() throws Exception {
        UniqueNumbersAPI uniqueNumbersAPI = AppBeans.get(UniqueNumbersAPI.NAME);

        uniqueNumbersAPI.getCurrentNumber("s1");
        uniqueNumbersAPI.deleteDbSequence("s1");
        uniqueNumbersAPI.getCurrentNumber("s1");
    }
}