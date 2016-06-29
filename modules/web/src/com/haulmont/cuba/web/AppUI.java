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

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.haulmont.cuba.web.toolkit.ui.CubaClientManager;
import com.haulmont.cuba.web.toolkit.ui.client.appui.AppUIClientRpc;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.server.*;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Single window / page of web application. Root component of Vaadin layout.
 */
@Push(transport = Transport.WEBSOCKET_XHR)
@PreserveOnRefresh
public class AppUI extends UI implements ErrorHandler {

    public static final String APPLICATION_CLASS_CONFIG_KEY = "Application";

    public static final String LAST_REQUEST_ACTION_ATTR = "lastRequestAction";

    public static final String LAST_REQUEST_PARAMS_ATTR = "lastRequestParams";

    private final static Logger log = LoggerFactory.getLogger(AppUI.class);

    protected final App app;

    protected boolean applicationInitRequired = false;

    protected TestIdManager testIdManager = new TestIdManager();

    protected boolean testMode = false;

    protected String profilerMarker;

    protected Map<String, String> profiledScreens;

    protected CubaClientManager clientManager;

    public AppUI() {
        log.trace("Creating UI {}", this);
        if (!App.isBound()) {
            app = createApplication();

            VaadinSession vSession = VaadinSession.getCurrent();
            vSession.setAttribute(App.class, app);

            // set root error handler for all session
            vSession.setErrorHandler((ErrorHandler) event -> {
                try {
                    app.getExceptionHandlers().handle(event);
                    app.getAppLog().log(event);
                } catch (Throwable e) {
                    //noinspection ThrowableResultOfMethodCallIgnored
                    log.error("Error handling exception\nOriginal exception:\n{}\nException in handlers:\n{}",
                            ExceptionUtils.getStackTrace(event.getThrowable()), ExceptionUtils.getStackTrace(e)
                    );
                }
            });

            applicationInitRequired = true;
        } else {
            app = App.getInstance();
        }

        Configuration configuration = AppBeans.get(Configuration.NAME);
        testMode = configuration.getConfig(GlobalConfig.class).getTestMode();

        // do not grab focus
        setTabIndex(-1);

        initJsLibraries();

        initInternalComponents();
    }

    /**
     * Dynamically init external JS libraries.
     * You should create JavaScriptExtension class and extend UI object here. <br/>
     *
     * Example: <br/>
     * <pre><code>
     * JavaScriptExtension:
     *
     * {@literal @}JavaScript("resources/jquery/jquery-1.10.2.min.js")
     * public class JQueryIntegration extends AbstractJavaScriptExtension {
     *
     *     {@literal @}Override
     *     public void extend(AbstractClientConnector target) {
     *         super.extend(target);
     *     }
     *
     *     {@literal @}Override
     *     protected Class&lt;? extends ClientConnector&gt; getSupportedParentType() {
     *         return UI.class;
     *     }
     * }
     *
     * AppUI:
     *
     * protected void initJsLibraries() {
     *     new JQueryIntegration().extend(this);
     * }</code></pre>
     *
     * If you want to include scripts to generated page statically see {@link com.haulmont.cuba.web.sys.CubaBootstrapListener}.
     */
    protected void initJsLibraries() {
    }

    protected void initInternalComponents() {
        clientManager = new CubaClientManager();
        clientManager.extend(this);
    }

    protected App createApplication() {
        String applicationClass = getApplicationClass();
        App application;
        try {
            Class<?> aClass = getClass().getClassLoader().loadClass(applicationClass);
            application = (App) aClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new Error(String.format("Unable to create application '%s'", applicationClass), e);
        }

        return application;
    }

    protected String getApplicationClass() {
        DeploymentConfiguration vConf = VaadinService.getCurrent().getDeploymentConfiguration();
        return vConf.getApplicationOrSystemProperty(APPLICATION_CLASS_CONFIG_KEY,
                DefaultApp.class.getCanonicalName());
    }

    @Override
    protected void init(VaadinRequest request) {
        log.debug("Initializing AppUI");
        if (applicationInitRequired) {
            app.init();

            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            Locale locale = messageTools.trimLocale(request.getLocale());
            app.setLocale(locale);

            applicationInitRequired = false;
        }
        // init error handlers
        setErrorHandler(this);
        // open login or main window
        app.initView(this);

        processExternalLink(request);
    }

    @Override
    protected void refresh(VaadinRequest request) {
        super.refresh(request);

        // handle page refresh
        if (app.getConnection().isConnected()) {
            // Ping middleware session if connected
            log.debug("Check middleware session");

            try {
                UserSessionService service = AppBeans.get(UserSessionService.NAME);
                UserSession session = app.getConnection().getSession();
                if (session != null) {
                    service.getUserSession(session.getId());
                }
            } catch (Exception e) {
                app.exceptionHandlers.handle(new com.vaadin.server.ErrorEvent(e));
            }
        }
    }

    @Override
    public void handleRequest(VaadinRequest request) {
        // on refresh page call
        processExternalLink(request);
    }

    public void showView(UIView view) {
        try {
            setContent(view);
            getPage().setTitle(view.getTitle());

            view.show();
        } catch (Exception e) {
            error(new com.vaadin.server.ErrorEvent(e));
        }
    }

    /**
     * @return current AppUI
     */
    public static AppUI getCurrent() {
        return (AppUI) UI.getCurrent();
    }

    /**
     * @return this App instance
     */
    public App getApp() {
        return app;
    }

    /**
     * @return AppWindow instance or null if not logged in
     */
    public AppWindow getAppWindow() {
        Component currentUIView = getContent();
        if (currentUIView instanceof AppWindow) {
            return (AppWindow) currentUIView;
        } else {
            return null;
        }
    }

    public TestIdManager getTestIdManager() {
        return testIdManager;
    }

    public boolean isTestMode() {
        return testMode;
    }

    @Override
    public void error(com.vaadin.server.ErrorEvent event) {
        try {
            app.getExceptionHandlers().handle(event);
            app.getAppLog().log(event);
        } catch (Throwable e) {
            //noinspection ThrowableResultOfMethodCallIgnored
            log.error("Error handling exception\nOriginal exception:\n{}\nException in handlers:\n{}",
                    ExceptionUtils.getStackTrace(event.getThrowable()),
                    ExceptionUtils.getStackTrace(e));
        }
    }

    public void processExternalLink(VaadinRequest request) {
        String action = (String) request.getWrappedSession().getAttribute(LAST_REQUEST_ACTION_ATTR);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        WebConfig webConfig = configuration.getConfig(WebConfig.class);
        if (webConfig.getLinkHandlerActions().contains(action)) {
            //noinspection unchecked
            Map<String, String> params =
                    (Map<String, String>) request.getWrappedSession().getAttribute(LAST_REQUEST_PARAMS_ATTR);
            if (params == null) {
                log.warn("Unable to process the external link: lastRequestParams not found in session");
                return;
            }

            try {
                LinkHandler linkHandler = AppBeans.getPrototype(LinkHandler.NAME, app, action, params);
                if (app.connection.isConnected()) {
                    linkHandler.handle();
                } else {
                    app.linkHandler = linkHandler;
                }
            } catch (Exception e) {
                error(new com.vaadin.server.ErrorEvent(e));
            }
        }
    }

    @Override
    public void detach() {
        log.trace("Detaching UI {}", this);
        super.detach();
    }

    /**
     * INTERNAL.
     */
    public void discardAccumulatedEvents() {
        getRpcProxy(AppUIClientRpc.class).discardAccumulatedEvents();
    }

    public String getProfilerMarker() {
        return profilerMarker;
    }

    public void setProfilerMarker(String profilerMarker) {
        this.profilerMarker = profilerMarker;
    }

    public void setProfiledScreen(String profilerMarker, String screen) {
        if (profiledScreens == null) {
            profiledScreens = new HashMap<>();
        }
        profiledScreens.put(profilerMarker, screen);
    }

    public String getProfiledScreen(String profilerMarker) {
        return profiledScreens.get(profilerMarker);
    }

    public void clearProfiledScreens(List<String> profilerMarkers) {
        for (String profilerMarker : profilerMarkers) {
            profiledScreens.remove(profilerMarker);
        }
    }

    protected void updateClientSystemMessages(Locale locale) {
        CubaClientManager.SystemMessages msgs = new CubaClientManager.SystemMessages();
        Messages messages = AppBeans.get(Messages.NAME);

        msgs.communicationErrorCaption = messages.getMainMessage("communicationErrorCaption", locale);
        msgs.communicationErrorMessage = messages.getMainMessage("communicationErrorMessage", locale);

        msgs.sessionExpiredErrorCaption = messages.getMainMessage("sessionExpiredErrorCaption", locale);
        msgs.sessionExpiredErrorMessage = messages.getMainMessage("sessionExpiredErrorMessage", locale);

        msgs.authorizationErrorCaption = messages.getMainMessage("authorizationErrorCaption", locale);
        msgs.authorizationErrorMessage = messages.getMainMessage("authorizationErrorMessage", locale);

        clientManager.updateSystemMessagesLocale(msgs);

        getReconnectDialogConfiguration().setDialogText(messages.getMainMessage("reconnectDialogText", locale));
        getReconnectDialogConfiguration().setDialogTextGaveUp(messages.getMainMessage("reconnectDialogTextGaveUp", locale));
    }
}