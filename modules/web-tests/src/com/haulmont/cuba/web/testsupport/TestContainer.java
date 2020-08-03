/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.testsupport;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.ScreenProfilerService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ClientType;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.sys.AbstractAppContextLoader;
import com.haulmont.cuba.core.sys.AppComponents;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaClassPathXmlApplicationContext;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import com.haulmont.cuba.core.sys.persistence.EclipseLinkCustomizer;
import com.haulmont.cuba.core.sys.remoting.LocalServiceDirectory;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.web.sys.remoting.WebRemoteProxyBeanCreator;
import com.haulmont.cuba.web.testsupport.proxy.ConfigStorageServiceProxy;
import com.haulmont.cuba.web.testsupport.proxy.DataServiceProxy;
import com.haulmont.cuba.web.testsupport.proxy.ScreenProfilerServiceProxy;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Container for integration tests of Web tier.
 *
 * <p>It's recommended to use {@code TestContainer} with {@link TestUiEnvironment}, but you can also use it directly as
 * shown below.</p>
 *
 * <p>Usage of the common instance (time saving):</p>
 * <pre>
 *    {@literal @}RegisterExtension
 *     public static TestContainer cont = TestContainer.Common.INSTANCE;
 *
 *    {@literal @}Test
 *     public void testSomething() {
 *
 *     }
 * </pre>
 *
 * <p>Usage of a specific instance:</p>
 * <pre>
 *    {@literal @}RegisterExtension
 *     public static TestContainer cont = new TestContainer()
 *              .setAppPropertiesFiles(Arrays.asList(
 *                  "com/haulmont/cuba/web-app.properties",
 *                  "com/haulmont/cuba/web/testsupport/test-web-app.properties",
 *                  "com/company/sample/web/my-test-app.properties"));
 *
 *    {@literal @}Test
 *     public void testSomething() {
 *
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

    private ClassPathXmlApplicationContext springAppContext;
    private Map<String, String> appProperties = new HashMap<>();

    public TestContainer() {
        String property = System.getProperty("logback.configurationFile");
        if (StringUtils.isBlank(property)) {
            System.setProperty("logback.configurationFile", getLogbackConfigLocation());
        }
        log = LoggerFactory.getLogger(TestContainer.class);

        springConfig = "com/haulmont/cuba/web/testsupport/test-web-spring.xml";
        appComponents = Collections.emptyList();
        appPropertiesFiles = Arrays.asList(
                "com/haulmont/cuba/web-app.properties",
                "com/haulmont/cuba/web/testsupport/test-web-app.properties");
    }

    protected String getLogbackConfigLocation() {
        return "com/haulmont/cuba/web/testsupport/test-web-logback.xml";
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

    public ClassPathXmlApplicationContext getApplicationContext() {
        return springAppContext;
    }

    public Map<String, String> getAppProperties() {
        return appProperties;
    }

    public <T> T getBean(Class<T> beanType) {
        return getApplicationContext().getBean(beanType);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        return (T) getApplicationContext().getBean(name);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(String name, Object... args) {
        return (T) getApplicationContext().getBean(name, args);
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

        WebRemoteProxyBeanCreator.setServiceProxyClass(TestServiceProxy.class);

        initAppContext();
        LocalServiceDirectory.start();

        TestServiceProxy.setDefault(ConfigStorageService.class, new ConfigStorageServiceProxy());
        TestServiceProxy.setDefault(DataService.class, new DataServiceProxy(this));
        TestServiceProxy.setDefault(ScreenProfilerService.class, new ScreenProfilerServiceProxy(this));
    }

    @Override
    protected void after() {
        log.info("Stopping test container " + this);
        try {
            ((ConfigurableApplicationContext) AppContext.getApplicationContext()).close();
            AppContext.Internals.setApplicationContext(null);
            for (String name : AppContext.getPropertyNames()) {
                AppContext.setProperty(name, null);
            }
        } catch (Exception e) {
            log.warn("Error closing test container", e);
        }
    }

    protected void initAppComponents() {
        AppContext.Internals.setAppComponents(new AppComponents(getAppComponents(), "web"));
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
                    properties.load(stream);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to load properties file", e);
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

        AppContext.setProperty(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
        appProperties.put(AppConfig.CLIENT_TYPE_PROP, ClientType.WEB.toString());
    }

    protected void initAppContext() {
        EclipseLinkCustomizer.initTransientCompatibleAnnotations();

        String configProperty = AppContext.getProperty(AbstractAppContextLoader.SPRING_CONTEXT_CONFIG);

        StringTokenizer tokenizer = new StringTokenizer(configProperty);
        List<String> locations = tokenizer.getTokenList();

        StringTokenizer configTokenizer = new StringTokenizer(getSpringConfig());
        locations.addAll(configTokenizer.getTokenList());

        springAppContext = new CubaClassPathXmlApplicationContext(locations.toArray(new String[0]));
        AppContext.Internals.setApplicationContext(springAppContext);

        Events events = springAppContext.getBean(Events.NAME, Events.class);
        events.publish(new AppContextInitializedEvent(springAppContext));
    }

    protected void cleanupContext() {
        AppContext.Internals.setApplicationContext(null);
        for (String name : AppContext.getPropertyNames()) {
            AppContext.setProperty(name, null);
        }
    }

    protected void setupContext() {
        AppContext.Internals.setApplicationContext(getApplicationContext());
        for (Map.Entry<String, String> entry : getAppProperties().entrySet()) {
            AppContext.setProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns factory for creating entity instances for tests.
     *
     * @param entityClass entities of this class will be created by the factory
     * @param entityState entities with this state will be created by the factory
     * @return entity factory
     */
    public <E extends Entity> TestEntityFactory<E> getEntityFactory(Class<E> entityClass, TestEntityState entityState) {
        Metadata metadata = getBean(Metadata.class);

        return new TestEntityFactory<>(metadata, entityClass, entityState);
    }
}