/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.testsupport;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.AbstractAppContextLoader;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.AppContextLoader;
import com.haulmont.cuba.core.sys.CubaCoreApplicationContext;
import com.haulmont.cuba.core.sys.persistence.EclipseLinkCustomizer;
import com.haulmont.cuba.core.sys.persistence.PersistenceConfigProcessor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang.text.StrTokenizer;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;

/**
 * Container for integration tests.
 * <p>Usage of the common instance (time saving):</p>
 * <pre>
 *     {@literal@}ClassRule
 *     public static TestContainer cont = TestContainer.Common.INSTANCE;
 *
 *     {@literal@}Test
 *     public void testSomething() {
 *         try (Transaction tx = cont.persistence().createTransaction()) { ... }
 *     }
 * </pre>
 *
 * <p>Usage of a specific instance:</p>
 * <pre>
 *     {@literal@}ClassRule
 *     public static TestContainer cont = new TestContainer()
 *              .setAppPropertiesFiles(Arrays.asList("cuba-app.properties", "com/company/sample/core/my-test-app.properties"));
 *
 *     {@literal@}Test
 *     public void testSomething() {
 *         try (Transaction tx = cont.persistence().createTransaction()) { ... }
 *     }
 * </pre>
 *
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public class TestContainer extends ExternalResource {

    public static class Common extends TestContainer {

        public static final Common INSTANCE = new Common();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void before() throws Throwable {
            if (!initialized) {
                super.before();
                initialized = true;
            }
            cleanupContext();
        }

        @Override
        public void after() {
            setupContext();
            // never stops - do not call super
        }
    }

    private Logger log;

    protected String springConfig;
    protected List<String> appPropertiesFiles;
    protected String dbDriver;
    protected String dbUrl;
    protected String dbUser;
    protected String dbPassword;

    private ClassPathXmlApplicationContext springAppContext;
    private Map<String, String> appProperties = new HashMap<>();

    public TestContainer() {
        String property = System.getProperty("logback.configurationFile");
        if (StringUtils.isBlank(property)) {
            System.setProperty("logback.configurationFile", "test-logback.xml");
        }
        log = LoggerFactory.getLogger(TestContainer.class);

        springConfig = "test-spring.xml";
        appPropertiesFiles = Arrays.asList("cuba-app.properties", "test-app.properties");
        dbDriver = "org.hsqldb.jdbc.JDBCDriver";
        dbUrl = "jdbc:hsqldb:hsql://localhost/cubadb";
        dbUser = "sa";
        dbPassword = "";
    }

    public Persistence persistence() {
        return AppBeans.get(Persistence.class);
    }

    public EntityManager entityManager() {
        return persistence().getEntityManager();
    }

    public Metadata metadata() {
        return AppBeans.get(Metadata.class);
    }

    public void deleteRecord(String table, UUID... ids) {
        deleteRecord(table, "ID", ids);
    }

    public void deleteRecord(String table, String primaryKeyCol, UUID... ids) {
        for (UUID id : ids) {
            String sql = "delete from " + table + " where " + primaryKeyCol + " = '" + id.toString() + "'";
            QueryRunner runner = new QueryRunner(persistence().getDataSource());
            try {
                runner.update(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<String> getAppPropertiesFiles() {
        return appPropertiesFiles;
    }

    public String getSpringConfig() {
        return springConfig;
    }

    public TestContainer setSpringConfig(String springConfig) {
        this.springConfig = springConfig;
        return this;
    }

    public TestContainer setAppPropertiesFiles(List<String> appPropertiesFiles) {
        this.appPropertiesFiles = appPropertiesFiles;
        return this;
    }

    public TestContainer addAppPropertiesFile(String name) {
        ArrayList<String> list = new ArrayList<>(appPropertiesFiles);
        list.add(name);
        this.appPropertiesFiles = list;
        return this;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public TestContainer setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
        return this;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public TestContainer setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
        return this;
    }

    public String getDbUser() {
        return dbUser;
    }

    public TestContainer setDbUser(String dbUser) {
        this.dbUser = dbUser;
        return this;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public TestContainer setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
        return this;
    }

    public ClassPathXmlApplicationContext getSpringAppContext() {
        return springAppContext;
    }

    public Map<String, String> getAppProperties() {
        return appProperties;
    }

    @Override
    protected void before() throws Throwable {
        log.info("Starting test container " + this);
        System.setProperty("cuba.unitTestMode", "true");

        initAppProperties();
        for (Map.Entry<String, String> entry : appProperties.entrySet()) {
            AppContext.setProperty(entry.getKey(), entry.getValue());
        }

        initDataSources();
        initPersistenceConfig();
        initAppContext();
    }

    @Override
    protected void after() {
        log.info("Stopping test container " + this);
        try {
            ((ConfigurableApplicationContext) AppContext.getApplicationContext()).close();
            TestContext.getInstance().unbind(AppContext.getProperty("cuba.dataSourceJndiName"));
            AppContext.setApplicationContext(null);
            for (String name : AppContext.getPropertyNames()) {
                AppContext.setProperty(name, null);
            }
        } catch (Exception e) {
            log.warn("Error closing test container", e);
        }
    }

    protected void initDataSources() {
        try {
            Class.forName(dbDriver);
            TestDataSource ds = new TestDataSource(dbUrl, dbUser, dbPassword);
            TestContext.getInstance().bind(AppContext.getProperty("cuba.dataSourceJndiName"), ds);
        } catch (ClassNotFoundException | NamingException e) {
            throw new RuntimeException("Error initializing datasource", e);
        }
    }

    protected void initPersistenceConfig() {
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

        List<String> locations = getAppPropertiesFiles();
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
            appProperties.put((String) key, value);
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

    protected void initAppContext() {
        EclipseLinkCustomizer.initTransientCompatibleAnnotations();

        String configProperty = AppContext.getProperty(AbstractAppContextLoader.SPRING_CONTEXT_CONFIG);

        StrTokenizer tokenizer = new StrTokenizer(configProperty);
        List<String> locations = tokenizer.getTokenList();
        locations.add(getSpringConfig());

        springAppContext = new CubaCoreApplicationContext(locations.toArray(new String[locations.size()]));
        AppContext.setApplicationContext(springAppContext);
    }

    protected void cleanupContext() {
        try {
            TestContext.getInstance().unbind(AppContext.getProperty("cuba.dataSourceJndiName"));
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        AppContext.setApplicationContext(null);
        for (String name : AppContext.getPropertyNames()) {
            AppContext.setProperty(name, null);
        }
    }

    protected void setupContext() {
        AppContext.setApplicationContext(getSpringAppContext());
        for (Map.Entry<String, String> entry : getAppProperties().entrySet()) {
            AppContext.setProperty(entry.getKey(), entry.getValue());
        }
    }
}
