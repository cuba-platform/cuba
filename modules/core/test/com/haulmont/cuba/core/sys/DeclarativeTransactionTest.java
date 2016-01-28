/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.TestingService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertNull;

/**
 * @author Konstantin Krivopustov
 */
public class DeclarativeTransactionTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Test
    public void test() throws Exception {
        TestingService service = AppBeans.get(TestingService.class);

        service.declarativeTransaction();

        assertNull(cont.persistence().getEntityManagerContext().getAttribute("test"));
    }
}
