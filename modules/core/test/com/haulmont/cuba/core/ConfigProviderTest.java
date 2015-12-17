/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.config.TestBeanToInjectConfig;
import com.haulmont.cuba.core.config.TestConfig;
import com.haulmont.cuba.core.entity.Config;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.RoleType;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ConfigProviderTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @After
    public void tearDown() throws Exception {
        cleanup();
    }

    @Test
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

        // Enum property
        RoleType roleType = config.getRoleTypeProp();
        assertTrue(roleType == RoleType.STANDARD);

        // Date property
        Date date = config.getDateProp();
        try {
            assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2013-12-12"), date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // List of Integer
        List<Integer> integerList = config.getIntegerListProp();
        assertEquals(Arrays.asList(1, 2, 3), integerList);

        // List of String
        List<String> stringList = config.getStringListProp();
        assertEquals(Arrays.asList("aaa", "bbb", "ccc"), stringList);
    }

    @Test
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

    @Test
    public void testBooleanType() throws Exception {
        Method booleanMethod = TestConfig.class.getMethod("getBooleanProp");
        Class<?> booleanMethodReturnType = booleanMethod.getReturnType();
        assertFalse(Boolean.TYPE.equals(booleanMethodReturnType));

        Method boolMethod = TestConfig.class.getMethod("getBoolProp");
        Class<?> boolMethodReturnType = boolMethod.getReturnType();
        assertEquals(Boolean.TYPE, boolMethodReturnType);
    }

    @Test
    public void testInjectedConfig() throws Exception {
        TestBeanToInjectConfig bean = AppBeans.get(TestBeanToInjectConfig.class);
        TestConfig config = bean.getConfig();
        assertNotNull(config);
        assertTrue(config.getBooleanPropDef());
    }

    @Test
    public void testInjectedConfigBySetter() throws Exception {
        TestBeanToInjectConfig bean = AppBeans.get(TestBeanToInjectConfig.class);
        TestConfig config = bean.getConfig2();
        assertNotNull(config);
        assertTrue(config.getBooleanPropDef());
    }

    @Test
    public void testSystemPropOverridesAppProp() throws Exception {
        TestConfig config = AppBeans.get(Configuration.class).getConfig(TestConfig.class);

        String value = config.getStringPropDef();
        assertEquals("def_value", value);

        System.setProperty("cuba.test.stringPropDef", "new_value");
        value = config.getStringPropDef();
        assertEquals("new_value", value);
    }

    private void cleanup() {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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
