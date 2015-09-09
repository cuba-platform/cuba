/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestContext;
import com.haulmont.cuba.testsupport.TestDataSource;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * DEPRECATED. Use TestContainer and JUnit4 test annotations:
 * <pre>
 *     {@literal@}ClassRule
 *     public static TestContainer cont = TestContainer.Common.INSTANCE;
 *
 *     {@literal@}Test
 *     public void testSomething() {
 *     }
 * </pre>
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public abstract class CubaTestCase extends TestCase {

    protected static boolean initialized;

    protected static TestContainer cont;

    static {
        String property = System.getProperty("logback.configurationFile");
        if (StringUtils.isBlank(property)) {
            System.setProperty("logback.configurationFile", "test-logback.xml");
        }
    }

    protected Persistence persistence;
    protected Metadata metadata;
    protected PasswordEncryption passwordEncryption;

    private class CommonTestContainer extends TestContainer {
        @Override
        public void before() throws Throwable {
            super.before();
        }

        @Override
        public void after() {
            super.after();
        }

        @Override
        public void cleanupContext() {
            super.cleanupContext();
        }

        @Override
        public void setupContext() {
            super.setupContext();
        }

        @Override
        protected void initDataSources() {
            try {
                CubaTestCase.this.initDataSources();
            } catch (Exception e) {
                throw new RuntimeException("Error initializing datasource", e);
            }
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        try {
            if (!initialized) {
                System.setProperty("cuba.unitTestMode", "true");
                cont = new CommonTestContainer();
                cont.setAppPropertiesFiles(getTestAppProperties());
                cont.setSpringConfig(getTestSpringConfig());
                ((CommonTestContainer) cont).before();
                initialized = true;
            }

            ((CommonTestContainer) cont).setupContext();

            persistence = AppBeans.get(Persistence.class);
            metadata = AppBeans.get(Metadata.class);
            passwordEncryption = AppBeans.get(PasswordEncryption.class);
        } catch (Throwable throwable) {
            if (throwable instanceof Exception)
                throw (Exception) throwable;
            else
                throw new RuntimeException(throwable);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        TestContext.getInstance().unbind(AppContext.getProperty("cuba.dataSourceJndiName"));
        ((CommonTestContainer) cont).cleanupContext();
        super.tearDown();
    }

    protected void initDataSources() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        TestDataSource ds = new TestDataSource("jdbc:hsqldb:hsql://localhost/cubadb", "sa", "");
        TestContext.getInstance().bind(AppContext.getProperty("cuba.dataSourceJndiName"), ds);
    }

    protected List<String> getTestAppProperties() {
        String[] files = {
                "cuba-app.properties",
                "test-app.properties",
        };
        return Arrays.asList(files);
    }

    protected String getTestSpringConfig() {
        return "test-spring.xml";
    }

    protected void deleteRecord(String table, UUID... ids) {
        cont.deleteRecord(table, ids);
    }

    protected void deleteRecord(String table, String primaryKeyCol, UUID... ids) {
        cont.deleteRecord(table, primaryKeyCol, ids);
    }

    protected EntityManager entityManager() {
        return persistence.getEntityManager();
    }
}
