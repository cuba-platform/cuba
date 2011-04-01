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
import com.haulmont.cuba.core.sys.AppContextLoader;
import com.haulmont.cuba.core.sys.PersistenceConfigProcessor;
import com.haulmont.cuba.testsupport.TestContext;
import com.haulmont.cuba.testsupport.TestDataSource;
import com.haulmont.cuba.testsupport.TestTransactionManager;
import com.haulmont.cuba.testsupport.TestUserTransaction;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import javax.naming.NamingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

public abstract class CubaTestCase extends TestCase
{
    private Log log = LogFactory.getLog(CubaTestCase.class);

    protected static boolean initialized;

    protected void setUp() throws Exception {
        super.setUp();
        if (!initialized) {
            System.setProperty("cuba.unitTestMode", "true");

            initDataSources();
            initAppProperties();
            initPersistenceConfig();
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
        String configProperty = AppContext.getProperty(AppContextLoader.PERSISTENCE_CONFIG);
        StrTokenizer tokenizer = new StrTokenizer(configProperty);

        PersistenceConfigProcessor processor = new PersistenceConfigProcessor();

        processor.setBaseDir(AppContext.getProperty("cuba.confDir"));
        processor.setSourceFiles(tokenizer.getTokenList());

        String dataDir = AppContext.getProperty("cuba.dataDir");
        processor.setOutputFile(dataDir + "/persistence.xml");

        processor.create();
    }

    protected void initAppProperties() {
        final Properties properties = new Properties();

        List<String> fileNames = getTestAppProperties();
        for (String fileName : fileNames) {
            File file = new File(System.getProperty("user.dir") + fileName);
            InputStream stream = null;
            try {
                stream = new FileInputStream(file);
                properties.load(stream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (stream != null) stream.close();
                } catch (IOException e) {
                    //
                }
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

        File dir;
        dir = new File(AppContext.getProperty("cuba.logDir"));
        dir.mkdirs();
        dir = new File(AppContext.getProperty("cuba.tempDir"));
        dir.mkdirs();
        dir = new File(AppContext.getProperty("cuba.dataDir"));
        dir.mkdirs();
    }

    protected List<String> getTestAppProperties() {
        String[] files = {
                "/modules/core/src-conf/app.properties",
                "/modules/core/test/test-app.properties",
        };
        return Arrays.asList(files);
    }

    protected void initAppContext() {
        String configProperty = AppContext.getProperty(AppContextLoader.SPRING_CONTEXT_CONFIG);

        String baseDir = AppContext.getProperty("cuba.confDir");

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        String[] tokenArray = tokenizer.getTokenArray();
        List<String> locations = new ArrayList<String>(tokenArray.length + 1);
        for (int i = 0; i < tokenArray.length; i++) {
            locations.add(baseDir + "/" + tokenArray[i]);
        }
        locations.add(getTestSpringConfig());

        ApplicationContext appContext = new FileSystemXmlApplicationContext(locations.toArray(new String[locations.size()]));
        AppContext.setApplicationContext(appContext);
    }

    protected String getTestSpringConfig() {
        return "/modules/core/test/test-spring.xml";
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
