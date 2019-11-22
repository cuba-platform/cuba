/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.testsupport;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.base.Strings;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.*;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import com.haulmont.cuba.core.sys.persistence.EclipseLinkCustomizer;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.StringTokenizer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
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
 *    {@literal @}ClassRule
 *     public static TestContainer cont = TestContainer.Common.INSTANCE;
 *
 *    {@literal @}Test
 *     public void testSomething() {
 *         try (Transaction tx = cont.persistence().createTransaction()) { ... }
 *     }
 * </pre>
 *
 * <p>Usage of a specific instance:</p>
 * <pre>
 *    {@literal @}ClassRule
 *     public static TestContainer cont = new TestContainer()
 *              .setAppPropertiesFiles(Arrays.asList(
 *                  "com/haulmont/cuba/app.properties",
 *                  "com/haulmont/cuba/testsupport/test-app.properties",
 *                  "com/company/sample/core/my-test-app.properties"));
 *
 *    {@literal @}Test
 *     public void testSomething() {
 *         try (Transaction tx = cont.persistence().createTransaction()) { ... }
 *     }
 * </pre>
 */
public class TestContainer extends ExternalResource implements BeforeAllCallback, AfterAllCallback {

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
            setupContext();
        }

        @Override
        public void after() {
            cleanupContext();
            // never stops - do not call super
        }
    }

    private Logger log;

    protected String springConfig;
    protected List<String> appComponents;
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
            System.setProperty("logback.configurationFile", "com/haulmont/cuba/testsupport/test-logback.xml");
        }
        log = LoggerFactory.getLogger(TestContainer.class);

        springConfig = "com/haulmont/cuba/testsupport/test-spring.xml";
        appComponents = Collections.emptyList();
        appPropertiesFiles = Arrays.asList(
                "com/haulmont/cuba/app.properties",
                "com/haulmont/cuba/testsupport/test-app.properties",
                "com/haulmont/cuba/test-app.properties");
        dbDriver = "org.hsqldb.jdbc.JDBCDriver";
        dbUrl = "jdbc:hsqldb:hsql://localhost:9111/cubadb";
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

    public void deleteRecord(String table, Object... ids) {
        deleteRecord(table, "ID", ids);
    }

    public void deleteRecord(String table, String primaryKeyCol, Object... ids) {
        for (Object id : ids) {
            String sql = "delete from " + table + " where " + primaryKeyCol + " = '" + id.toString() + "'";
            QueryRunner runner = new QueryRunner(persistence().getDataSource());
            try {
                runner.update(sql);
            } catch (SQLException e) {
                throw new RuntimeException(
                        String.format("Unable to delete record %s from %s", id.toString(), table), e);
            }
        }
    }

    public void deleteRecord(Entity... entities) {
        if (entities == null) {
            return;
        }

        for (Entity entity : entities) {
            if (entity == null) {
                continue;
            }

            MetadataTools metadataTools = metadata().getTools();
            MetaClass metaClass = metadata().getClassNN(entity.getClass());

            String table = metadataTools.getDatabaseTable(metaClass);
            String primaryKey = metadataTools.getPrimaryKeyName(metaClass);
            if (table == null || primaryKey == null) {
                throw new RuntimeException("Unable to determine table or primary key name for " + entity);
            }

            deleteRecord(table, primaryKey, entity.getId());
        }
    }

    public void setupLogging(String logger, Level level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(logger).setLevel(level);
    }

    public List<String> getAppComponents() {
        return appComponents;
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

    public TestContainer setAppComponents(List<String> appComponents) {
        this.appComponents = appComponents;
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
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        after();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        try {
            before();
        } catch (Throwable throwable) {
            log.error("TestContainer extension initialization failed.", throwable);
        }
    }

    @Override
    protected void before() throws Throwable {
        log.info("Starting test container " + this);
        System.setProperty("cuba.unitTestMode", "true");

        initAppComponents();
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
            ((PersistenceImpl) AppBeans.get(Persistence.class)).dispose();
            ((ConfigurableApplicationContext) AppContext.getApplicationContext()).close();
            TestContext.getInstance().unbind(AppContext.getProperty("cuba.dataSourceJndiName"));
            AppContext.Internals.setApplicationContext(null);
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
            String jndiName = AppContext.getProperty("cuba.dataSourceJndiName");
            if (!Strings.isNullOrEmpty(jndiName)) {
                TestContext.getInstance().bind(AppContext.getProperty("cuba.dataSourceJndiName"), ds);
            }
            TestDataSourceProvider.registerDataSource(Stores.MAIN, ds);
        } catch (ClassNotFoundException | NamingException e) {
            throw new RuntimeException("Error initializing datasource", e);
        }
    }

    protected void initPersistenceConfig() {
        Stores.getAll().forEach(AppContextLoader::createPersistenceXml);
    }

    protected void initAppComponents() {
        AppContext.Internals.setAppComponents(new AppComponents(getAppComponents(), "core"));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void initAppProperties() {
        Properties properties = new Properties();

        List<String> locations = getAppPropertiesFiles();
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        for (String location : locations) {
            Resource resource = resourceLoader.getResource(location);
            if (resource.exists()) {
                try (InputStream stream = resource.getInputStream()) {
                    BOMInputStream bomInputStream = new BOMInputStream(stream);
                    properties.load(bomInputStream);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read app properties " + location, e);
                }
            } else {
                log.warn("Resource {} not found, ignore it", location);
            }
        }

        StringSubstitutor substitutor = new StringSubstitutor(key -> {
            String subst = properties.getProperty(key);
            return subst != null ? subst : System.getProperty(key);
        });

        for (Object key : properties.keySet()) {
            String value = substitutor.replace(properties.getProperty((String) key));
            appProperties.put((String) key, value);
        }

        File dir;
        dir = new File(appProperties.get("cuba.confDir"));
        dir.mkdirs();
        dir = new File(appProperties.get("cuba.logDir"));
        dir.mkdirs();
        dir = new File(appProperties.get("cuba.tempDir"));
        dir.mkdirs();
        dir = new File(appProperties.get("cuba.dataDir"));
        dir.mkdirs();
    }

    protected void initAppContext() {
        EclipseLinkCustomizer.initTransientCompatibleAnnotations();

        String configProperty = AppContext.getProperty(AbstractAppContextLoader.SPRING_CONTEXT_CONFIG);

        StringTokenizer tokenizer = new StringTokenizer(configProperty);
        List<String> locations = tokenizer.getTokenList();
        locations.add(getSpringConfig());

        springAppContext = new CubaCoreApplicationContext(locations.toArray(new String[0]));
        AppContext.Internals.setApplicationContext(springAppContext);

        Events events = springAppContext.getBean(Events.class);
        events.publish(new AppContextInitializedEvent(springAppContext));
    }

    protected void cleanupContext() {
        try {
            TestContext.getInstance().unbind(AppContext.getProperty("cuba.dataSourceJndiName"));
        } catch (NamingException e) {
            throw new RuntimeException("Unable to unbind JNDI datasource", e);
        }
        AppContext.Internals.setApplicationContext(null);
        for (String name : AppContext.getPropertyNames()) {
            AppContext.setProperty(name, null);
        }
    }

    protected void setupContext() {
        AppContext.Internals.setApplicationContext(getSpringAppContext());
        for (Map.Entry<String, String> entry : getAppProperties().entrySet()) {
            AppContext.setProperty(entry.getKey(), entry.getValue());
        }
    }
}