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

import com.google.common.collect.HashMultimap;
import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.client.sys.cache.ClientCacheManager;
import com.haulmont.cuba.client.sys.cache.DynamicAttributesCacheStrategy;
import com.haulmont.cuba.client.testsupport.TestFullAccessRole;
import com.haulmont.cuba.client.testsupport.TestUserSessionSource;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCache;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.sys.UiControllersConfiguration;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.role.RoleDefinition;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.DefaultApp;
import com.haulmont.cuba.web.security.ConnectionImpl;
import com.haulmont.cuba.web.sys.AppCookies;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import com.haulmont.cuba.web.testsupport.ui.*;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.ConnectorTracker;
import com.vaadin.ui.UI;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.*;

import static com.haulmont.cuba.web.AppTestUtils.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.reflect.FieldUtils.getDeclaredField;

/**
 * Mocked UI environment for integration tests of Web tier. <br>
 * Automatically starts and cleans the corresponding test container instance. <br>
 * Provides {@link App}, {@link AppUI} and initialized {@link ClientUserSession}.
 *
 * Usually, you should use an additional mocking framework in order to mock middleware services, e.g. JMockit or Spock.
 *
 * <p>Usage:</p>
 * <pre>
 *     // setup environment, pass desired container instance to constructor
 *    {@literal @}Rule
 *     public TestUiEnvironment environment =
 *             new TestUiEnvironment(TestContainer.Common.INSTANCE)
 *                     .withLocale(Locale.ENGLISH)
 *                     .withUserLogin("admin");
 *
 *     // mock services
 *
 *    {@literal @}Mocked
 *     public UserManagementService userManagementService;
 *
 *    {@literal @}Before
 *     public void before() {
 *         new Expectations() {{
 *             userManagementService.getSubstitutedUsers((UUID) any); result = Collections.emptyList(); minTimes = 0;
 *         }};
 *         TestServiceProxy.mock(UserManagementService.class, userManagementService);
 *     }
 *
 *     // test something
 *
 *    {@literal @}Test
 *     public void openMainScreen() {
 *         // open screens, manipulate UI
 *         Screen screen = environment.getScreens()
 *                 .create(MainScreen.class, OpenMode.ROOT)
 *                 .show();
 *
 *         assertNotNull(screen);
 *         assertTrue(screen instanceof MainScreen);
 *     }
 * </pre>
 *
 * @see TestContainer
 */
public class TestUiEnvironment extends ExternalResource implements BeforeEachCallback, AfterEachCallback {

    public static final String USER_ID = "b8a050db-3ade-487e-817d-781a31918657";

    protected TestContainer container;
    protected TestUserSessionSource sessionSource;

    protected App app;
    protected AppUI ui;

    protected Logger log = LoggerFactory.getLogger(TestUiEnvironment.class);
    protected boolean sessionAuthenticated = true;
    protected Locale locale = Locale.ENGLISH;
    protected String userLogin = "test_admin";
    protected String userName = "Test Administrator";
    protected RoleDefinition roleDefinition;

    protected String[] screenPackages;

    public TestUiEnvironment(TestContainer container) {
        this.container = container;
    }

    @Override
    protected void before() throws Throwable {
        super.before();

        setupEnvironment();
    }

    @Override
    protected void after() {
        super.after();

        cleanupEnvironment();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        after();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        try {
            before();
        } catch (Throwable throwable) {
            log.error("TestContainer extension initialization failed.", throwable);
        }
    }

    protected void setupEnvironment() {
        try {
            container.before();
        } catch (Throwable throwable) {
            throw new RuntimeException("Unable to start container", throwable);
        }

        setupSession();

        setupWebBeans();

        setupVaadinUi();

        if (screenPackages != null) {
            exportScreens(screenPackages);
        }
    }

    protected void setupWebBeans() {
        TestServiceProxy.mock(PersistenceManagerService.class, new TestPersistenceManagerService());
        TestServiceProxy.mock(UserManagementService.class, new TestUserManagementService());

        TestCachingStrategy dynamicAttributesCacheStrategy = new TestCachingStrategy();
        DynamicAttributesCache dynamicAttributesCache = new DynamicAttributesCache(
                HashMultimap.create(), new HashMap<>(), new Date());

        dynamicAttributesCacheStrategy.setData(dynamicAttributesCache);
        ClientCacheManager clientCacheManager = container.getBean(ClientCacheManager.NAME);
        clientCacheManager.addCachedObject(DynamicAttributesCacheStrategy.NAME, dynamicAttributesCacheStrategy);
    }

    protected void setupSession() {
        sessionSource = container.getBean(UserSessionSource.NAME);

        UserSession serverSession = createSession();
        ClientUserSession session = new ClientUserSession(serverSession);
        session.setAuthenticated(isSessionAuthenticated());

        sessionSource.setSession(session);
    }

    protected void setupVaadinUi() {
        AutowireCapableBeanFactory injector = getInjector();

        app = new DefaultApp();
        setThemeConstants(app, new ThemeConstants(new HashMap<>()));
        setCookies(app, new AppCookies());

        Connection connection = new ConnectionImpl();
        injector.autowireBean(connection);

        setConnection(app, connection);

        VaadinSession vaadinSession = new TestVaadinSession(new WebBrowser(), getLocale());

        vaadinSession.setAttribute(App.class, app);
        vaadinSession.setAttribute(App.NAME, app);
        vaadinSession.setAttribute(Connection.class, connection);
        vaadinSession.setAttribute(Connection.NAME, connection);
        vaadinSession.setAttribute(UserSession.class, sessionSource.getSession());

        VaadinSession.setCurrent(vaadinSession);

        injector.autowireBean(app);

        ui = new AppUI();
        injector.autowireBean(ui);

        // setup UI

        ConnectorTracker connectorTracker = new TestConnectorTracker(ui);

        try {
            getDeclaredField(UI.class, "connectorTracker", true)
                    .set(ui, connectorTracker);
            getDeclaredField(UI.class, "session", true)
                    .set(ui, vaadinSession);
        } catch (Exception e) {
            throw new RuntimeException("Unable to init Vaadin UI state", e);
        }

        UI.setCurrent(ui);

        VaadinRequest vaadinRequest = new TestVaadinRequest();
        ui.getPage().init(vaadinRequest);

        initUi(ui, vaadinRequest);
    }

    protected AutowireCapableBeanFactory getInjector() {
        return container.getApplicationContext().getAutowireCapableBeanFactory();
    }

    protected boolean isSessionAuthenticated() {
        return sessionAuthenticated;
    }

    protected UserSession createSession() {
        User user = createUser();
        UserSession session = new UserSession(UUID.randomUUID(), user, emptyList(), getLocale(), false);
        session.setJoinedRole(roleDefinition != null ? roleDefinition : new TestFullAccessRole());
        return session;
    }

    protected Locale getLocale() {
        return locale;
    }

    protected User createUser() {
        User user = container.getBean(Metadata.class).create(User.class);
        user.setId(UUID.fromString(USER_ID));
        user.setLogin(userLogin);
        user.setName(userName);
        user.setPassword(DigestUtils.md5Hex(userLogin));
        return user;
    }

    protected void cleanupEnvironment() {
        resetScreens();

        UI.setCurrent(null);
        VaadinSession.setCurrent(null);

        TestServiceProxy.clear();

        sessionSource.setSession(null);

        TestClientCacheManager clientCacheManager = container.getBean(ClientCacheManager.NAME);
        clientCacheManager.getCache().remove(DynamicAttributesCacheStrategy.NAME);

        container.after();
    }

    protected void exportScreens(String... packages) {
        TestWindowConfig windowConfig = container.getBean(TestWindowConfig.class);

        UiControllersConfiguration configuration = new UiControllersConfiguration();
        getInjector().autowireBean(configuration);
        configuration.setBasePackages(Arrays.asList(packages));

        windowConfig.setConfigurations(singletonList(configuration));
        windowConfig.reset();
    }

    protected void resetScreens() {
        TestWindowConfig windowConfig = container.getBean(TestWindowConfig.class);
        windowConfig.setConfigurations(emptyList());
        windowConfig.reset();
    }

    /**
     * @return mocked user session source
     */
    public TestUserSessionSource getSessionSource() {
        return sessionSource;
    }

    /**
     * @return test Spring container
     */
    public TestContainer getContainer() {
        return container;
    }

    /**
     * @return user session from {@link UserSessionSource}
     */
    public UserSession getUserSession() {
        return sessionSource.getUserSession();
    }

    /**
     * @return UI
     */
    public AppUI getUI() {
        return ui;
    }

    /**
     * @return screens API
     */
    public Screens getScreens() {
        return ui.getScreens();
    }

    /**
     * @return dialogs API
     */
    public Dialogs getDialogs() {
        return ui.getDialogs();
    }

    /**
     * @return notifications API
     */
    public Notifications getNotifications() {
        return ui.getNotifications();
    }

    /**
     * Sets authenticated flag to the mocked user session.
     *
     * @param sessionAuthenticated true if user is authenticated
     * @return this
     */
    public TestUiEnvironment sessionAuthenticated(boolean sessionAuthenticated) {
        this.sessionAuthenticated = sessionAuthenticated;
        return this;
    }

    /**
     * Sets locale to the mocked user session.
     *
     * @param locale locale
     * @return this
     */
    public TestUiEnvironment withLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Sets user login to the mocked user session.
     *
     * @param userLogin user login
     * @return this
     */
    public TestUiEnvironment withUserLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    /**
     * Sets user name to the mocked user session.
     *
     * @param userName user name
     * @return this
     */
    public TestUiEnvironment withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * Sets the role definition to the mocked user session.
     *
     * @param roleDefinition role definition
     * @return this
     */
    public TestUiEnvironment withRoleDefinition(RoleDefinition roleDefinition) {
        this.roleDefinition = roleDefinition;
        return this;
    }

    /**
     * Overrides screen packages that will be scanned by {@link WindowConfig}.
     *
     * @param screenPackages screen packages
     * @return this
     */
    public TestUiEnvironment withScreenPackages(String... screenPackages) {
        this.screenPackages = screenPackages;
        return this;
    }
}