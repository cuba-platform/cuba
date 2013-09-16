/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PasswordEncryption;
import com.haulmont.cuba.core.sys.AbstractAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.AppContextLoader;
import com.haulmont.cuba.core.sys.persistence.PersistenceConfigProcessor;
import com.haulmont.cuba.testsupport.TestContext;
import com.haulmont.cuba.testsupport.TestDataSource;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Base class for middleware integration tests.
 * <p>This class provides full-functional middleware environment without web container, including:
 * <ul>
 *     <li>Connection to the database</li>
 *     <li>Spring container with all project beans inside</li>
 *     <li>All middleware infrastructure interfaces</li>
 * </ul>
 * </p>
 * <p>Descendant classes must override methods <code>initDataSources()</code> and <code>getTestAppProperties()</code>
 * to supply project-specific properties.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public abstract class CubaTestCase extends TestCase
{
    private Log log;

    protected static boolean initialized;

    protected Persistence persistence;
    protected Metadata metadata;
    protected PasswordEncryption passwordEncryption;

    protected CubaTestCase() {
        String property = System.getProperty("log4j.configuration");
        if (StringUtils.isBlank(property)) {
            System.setProperty("log4j.configuration", getTestLog4jConfig());
        }
        log = LogFactory.getLog(CubaTestCase.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (!initialized) {
            System.setProperty("cuba.unitTestMode", "true");

            initDataSources();
            initAppProperties();
            initPersistenceConfig();
            initAppContext();

            initialized = true;
        }
        persistence = AppBeans.get(Persistence.class);
        metadata = AppBeans.get(Metadata.class);
        passwordEncryption = AppBeans.get(PasswordEncryption.class);
    }

    protected void initDataSources() throws Exception {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        TestDataSource ds = new TestDataSource("jdbc:hsqldb:hsql://localhost/cubadb", "sa", "");
        TestContext.getInstance().bind("java:comp/env/jdbc/CubaDS", ds);
    }

    protected void initPersistenceConfig() {
        BaseUuidEntity.allowSetNotLoadedAttributes =
                Boolean.valueOf(AppContext.getProperty("cuba.allowSetNotLoadedAttributes"));

        String configProperty = AppContext.getProperty(AppContextLoader.PERSISTENCE_CONFIG);
        StrTokenizer tokenizer = new StrTokenizer(configProperty);

        PersistenceConfigProcessor processor = new PersistenceConfigProcessor();
        processor.setSourceFiles(tokenizer.getTokenList());

        String dataDir = AppContext.getProperty("cuba.dataDir");
        processor.setOutputFile(dataDir + "/persistence.xml");

        processor.create();
    }

    protected void initAppProperties() {
        final Properties properties = new Properties();

        List<String> locations = getTestAppProperties();
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        for (String location : locations) {
            Resource resource = resourceLoader.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    properties.load(stream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource " + location + " not found, ignore it");
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
        dir = new File(AppContext.getProperty("cuba.confDir"));
        dir.mkdirs();
        dir = new File(AppContext.getProperty("cuba.logDir"));
        dir.mkdirs();
        dir = new File(AppContext.getProperty("cuba.tempDir"));
        dir.mkdirs();
        dir = new File(AppContext.getProperty("cuba.dataDir"));
        dir.mkdirs();
    }

    protected List<String> getTestAppProperties() {
        String[] files = {
                "cuba-app.properties",
                "test-app.properties",
        };
        return Arrays.asList(files);
    }

    protected void initAppContext() {
        String configProperty = AppContext.getProperty(AbstractAppContextLoader.SPRING_CONTEXT_CONFIG);

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        List<String> locations = tokenizer.getTokenList();
        locations.add(getTestSpringConfig());

        ApplicationContext appContext = new ClassPathXmlApplicationContext(locations.toArray(new String[locations.size()]));
        AppContext.setApplicationContext(appContext);
    }

    protected String getTestSpringConfig() {
        return "test-spring.xml";
    }

    protected String getTestLog4jConfig() {
        return "test-log4j.xml";
    }

    protected void deleteRecord(String table, UUID... ids) {
        deleteRecord(table, "ID", ids);
    }

    protected void deleteRecord(String table, String primaryKeyCol, UUID... ids) {
        for (UUID id : ids) {
            String sql = "delete from " + table + " where " + primaryKeyCol + " = '" + id.toString() + "'";
            QueryRunner runner = new QueryRunner(persistence.getDataSource());
            try {
                runner.update(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
