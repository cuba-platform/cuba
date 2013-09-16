/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.config.TestConfig;
import com.haulmont.cuba.core.entity.Config;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.User;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class ConfigProviderTest extends CubaTestCase
{
    protected void tearDown() throws Exception {
        cleanup();
        super.tearDown();
    }

    public void testAppProperties() {
        TestConfig config = AppBeans.get(Configuration.class).getConfig(TestConfig.class);

        // String property

        String stringProp = config.getStringProp();
        assertNull(stringProp);

        String stringPropDef = config.getStringPropDef();
        assertEquals("def_value", stringPropDef);

        config.setStringProp("test_value");
        stringProp = config.getStringProp();
        assertEquals("test_value", stringProp);

        // Integer property

        Integer integerProp = config.getIntegerProp();
        assertNull(integerProp);

        Integer integerPropDef = config.getIntegerPropDef();
        assertEquals(Integer.valueOf(100), integerPropDef);

        Integer integerPropDefRuntime = config.getIntegerPropDefRuntime(200);
        assertEquals(Integer.valueOf(200), integerPropDefRuntime);

        config.setIntegerProp(10);
        integerProp = config.getIntegerProp();
        assertEquals(Integer.valueOf(10), integerProp);

        // primitive int

        int intPropDef = config.getIntPropDef();
        assertEquals(0, intPropDef);

        config.setIntPropDef(1);
        intPropDef = config.getIntPropDef();
        assertEquals(1, intPropDef);

        int intPropDefRuntime = config.getIntPropDefRuntime(11);
        assertEquals(11, intPropDefRuntime);

        config.setIntPropDefRuntime(12);
        intPropDefRuntime = config.getIntPropDefRuntime(11);
        assertEquals(12, intPropDefRuntime);

        // Boolean property

        Boolean booleanProp = config.getBooleanProp();
        assertNull(booleanProp);

        config.setBooleanProp(true);
        booleanProp = config.getBooleanProp();
        assertTrue(booleanProp);

        Boolean booleanPropDef = config.getBooleanPropDef();
        assertTrue(booleanPropDef);

        // primitive boolean

        boolean boolProp = config.getBoolProp();
        assertFalse(boolProp);

        config.setBoolProp(true);
        boolProp = config.getBoolProp();
        assertTrue(boolProp);

        // UUID property

        UUID uuidProp = config.getUuidProp();
        assertNull(uuidProp);

        UUID uuid = UUID.randomUUID();
        config.setUuidProp(uuid);
        uuidProp = config.getUuidProp();
        assertEquals(uuid, uuidProp);

        // Entity property
        User adminUser = config.getAdminUser();
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getLogin());
    }

    public void testDatabaseProperties() throws Exception {
        TestConfig config = AppBeans.get(Configuration.class).getConfig(TestConfig.class);

        String dbProp = config.getDatabaseProp();
        assertNull(dbProp);

        config.setDatabaseProp("test_value");
        dbProp = config.getDatabaseProp();
        assertEquals("test_value", dbProp);

        config.setDatabaseProp("test_value_1");
        dbProp = config.getDatabaseProp();
        assertEquals("test_value_1", dbProp);

        AppContext.setProperty("cuba.test.databaseProp", "overridden_value");
        dbProp = config.getDatabaseProp();
        assertEquals("overridden_value", dbProp);

        AppContext.setProperty("cuba.test.databaseProp", "");
        dbProp = config.getDatabaseProp();
        assertEquals("test_value_1", dbProp);
    }

    public void testBooleanType() throws Exception {
        Method booleanMethod = TestConfig.class.getMethod("getBooleanProp");
        Class<?> booleanMethodReturnType = booleanMethod.getReturnType();
        assertFalse(Boolean.TYPE.equals(booleanMethodReturnType));

        Method boolMethod = TestConfig.class.getMethod("getBoolProp");
        Class<?> boolMethodReturnType = boolMethod.getReturnType();
        assertEquals(Boolean.TYPE, boolMethodReturnType);
    }

    private void cleanup() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select c from sys$Config c where c.name like ?1");
            query.setParameter(1, "cuba.test.%");
            List<Config> list = query.getResultList();
            for (Config config : list) {
                em.remove(config);
            }
            tx.commit();
        } finally {
            tx.end();
        }
    }
}
