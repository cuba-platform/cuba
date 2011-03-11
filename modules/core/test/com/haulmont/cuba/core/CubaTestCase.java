/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.11.2008 10:29:29
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.PersistenceConfigProcessor;
import com.haulmont.cuba.testsupport.TestContext;
import com.haulmont.cuba.testsupport.TestDataSource;
import com.haulmont.cuba.testsupport.TestTransactionManager;
import com.haulmont.cuba.testsupport.TestUserTransaction;
import junit.framework.TestCase;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public abstract class CubaTestCase extends TestCase
{
    private Log log = LogFactory.getLog(CubaTestCase.class);

    protected static boolean initialized;

    protected void setUp() throws Exception {
        super.setUp();
        if (!initialized) {
            System.setProperty("cuba.unitTestMode", "true");

            initDataSources();
            initPersistenceConfig();
            initAppProperties();
            initAppContext();
            initTxManager();

            initialized = true;
        }
    }

    protected void initDataSources() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        TestDataSource ds = new TestDataSource("jdbc:hsqldb:hsql://localhost/cubadb", "sa", "");
        TestContext.getInstance().bind("java:comp/env/jdbc/CubaDS", ds);
    }

    protected void initPersistenceConfig() {
        PersistenceConfigProcessor processor = new PersistenceConfigProcessor();
        processor.setSourceFiles(getPersistenceSourceFiles());

        File currentDir = new File(System.getProperty("user.dir"));
        processor.setOutputFile(currentDir + "/out/test/core/persistence.xml");

        processor.create();
    }

    protected List<String> getPersistenceSourceFiles() {
        List<String> list = new ArrayList<String>();
        list.add("cuba-persistence.xml");
        return list;
    }

    protected void initAppProperties() {
        File currentDir = new File(System.getProperty("user.dir"));
        File rootDir = currentDir.getParentFile();
        File serverDir = new File(rootDir, "tomcat");

        System.setProperty("catalina.home", serverDir.getAbsolutePath());

        final Properties properties;
        InputStream stream = getTestAppProperties();
        try {
            properties = new Properties();
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                //
            }
        }
        StrSubstitutor substitutor = new StrSubstitutor(new StrLookup() {
            @Override
            public String lookup(String key) {
                String subst = properties.getProperty(key);
                return subst != null ? subst : System.getProperty(key);
            }
        });
        for (Object key : properties.keySet()) {
            String value = substitutor.replace(properties.getProperty((String) key));
            AppContext.setProperty((String) key, value);
        }
    }

    protected InputStream getTestAppProperties() {
        return CubaTestCase.class.getResourceAsStream("/test-app.properties");
    }

    protected void initAppContext() {
        List<String> contextFiles = getTestAppContextFiles();
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
                contextFiles.toArray(new String[contextFiles.size()]));
        AppContext.setApplicationContext(appContext);
    }

    protected List<String> getTestAppContextFiles() {
        List<String> files = new ArrayList<String>();
        files.add("cuba-spring.xml");
        files.add("test-spring.xml");
        return files;
    }

    protected void initTxManager() throws NamingException {
        Locator.getJndiContext().bind("java:/TransactionManager", new TestTransactionManager());
        Locator.getJndiContext().bind("UserTransaction", new TestUserTransaction());
    }

    protected void deleteRecord(String table, UUID id) {
        String sql = "delete from " + table + " where ID = '" + id.toString() + "'";
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
        try {
            runner.update(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
