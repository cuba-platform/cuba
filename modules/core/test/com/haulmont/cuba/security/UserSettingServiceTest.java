/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.security.app.UserSettingService;
import com.haulmont.cuba.testsupport.TestUserSessionSource;

public class UserSettingServiceTest extends CubaTestCase
{
    private UserSettingService uss;

    protected void setUp() throws Exception {
        super.setUp();
        uss = AppBeans.get(UserSettingService.NAME);
    }

    protected void tearDown() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q = em.createNativeQuery("delete from SEC_USER_SETTING where USER_ID = ?");
            q.setParameter(1, TestUserSessionSource.USER_ID);
            q.executeUpdate();
            tx.commit();
        } finally {
            tx.end();
        }
        super.tearDown();
    }

    public void test() {
        String val = uss.loadSetting(ClientType.WEB, "test-setting");
        assertNull(val);

        uss.saveSetting(ClientType.WEB, "test-setting", "test-value");

        val = uss.loadSetting(ClientType.WEB, "test-setting");
        assertEquals("test-value", val);

        val = uss.loadSetting("test-setting");
        assertNull(val);

        uss.saveSetting("test-setting", "test-value-1");

        val = uss.loadSetting("test-setting");
        assertEquals("test-value-1", val);

        val = uss.loadSetting(ClientType.WEB, "test-setting");
        assertEquals("test-value", val);
    }
}
