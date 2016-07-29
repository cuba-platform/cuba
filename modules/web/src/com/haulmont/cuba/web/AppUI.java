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

import com.haulmont.cuba.client.ClientUserSession;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.Window.TopLevelWindow;
import com.haulmont.cuba.gui.theme.ThemeConstantsRepository;
import com.haulmont.cuba.gui.xml.layout.ExternalUIComponentsSource;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.app.UserSettingsTools;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.haulmont.cuba.web.toolkit.ui.*;
import com.haulmont.cuba.web.toolkit.ui.client.appui.AppUIClientRpc;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Extension;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedSession;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import java.util.*;

/**
 * Single window / page of web application. Root component of Vaadin layout.
 */
@org.springframework.stereotype.Component(AppUI.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Push(transport = Transport.WEBSOCKET_XHR)
@PreserveOnRefresh
public class AppUI extends UI implements ErrorHandler, CubaHistoryControl.HistoryBackHandler {

    public static final String NAME = "cuba_AppUI";

    public static final String LAST_REQUEST_ACTION_ATTR = "lastRequestAction";
    public static final String LAST_REQUEST_PARAMS_ATTR = "lastRequestParams";

    private static final Logger log = LoggerFactory.getLogger(AppUI.class);

    protected App app;

    @Inject
    protected Messages messages;

    @Inject
    protected GlobalConfig globalConfig;

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected ScreenProfilerConfig screenProfilerConfig;

    @Inject
    protected UserSettingsTools userSettingsTools;

    @Inject
    protected ThemeConstantsRepository themeConstantsRepository;

    @Inject
    protected ExternalUIComponentsSource externalUIComponentsSource;

    @Inject
    protected UserSessionSource userSessionSource;

    protected TestIdManager testIdManager = new TestIdManager();

    protected boolean testMode = false;

    protected String profilerMarker;

    protected Map<String, String> profiledScreens;

    protected CubaClientManager clientManager;

    protected ScreenClientProfilerAgent clientProfiler;

    protected CubaFileDownloader fileDownloader;

    protected CubaHistoryControl historyControl;

    protected TopLevelWindow topLevelWindow;

    public AppUI() {
    }

    /**
     * Dynamically init external JS libraries.
     * You should create JavaScriptExtension class and extend UI object here. <br/>
     * <p>
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
     * <p>
     * If you want to include scripts to generated page statically see {@link com.haulmont.cuba.web.sys.CubaBootstrapListener}.
     */
    protected void initJsLibraries() {
    }

    protected void initInternalComponents() {
        clientManager = new CubaClientManager();
        clientManager.extend(this);

        fileDownloader = new CubaFileDownloader();
        fileDownloader.extend(this);

        clientProfiler = new ScreenClientProfilerAgent();
        clientProfiler.extend(this);

        if (webConfig.getAllowHandleBrowserHistoryBack()) {
            historyControl = new CubaHistoryControl();
            historyControl.extend(this, this);
        }
    }

    protected App createApplication() {
        return AppBeans.getPrototype(App.NAME);
    }

    @Override
    protected void init(VaadinRequest request) {
        log.trace("Initializing UI {}", this);

        try {
            this.testMode = globalConfig.getTestMode();

            // init error handlers
            setErrorHandler(this);

            // do not grab focus
            setTabIndex(-1);

            initJsLibraries();

            initInternalComponents();

            externalUIComponentsSource.checkInitialized();

            if (!App.isBound()) {
                App app = createApplication();
                app.init(request.getLocale());

                this.app = app;
            } else {
                this.app = App.getInstance();
            }

            setupUI();
        } catch (Exception e) {
            log.error("Unable to init ui", e);

            // unable to connect to middle ware
            showCriticalExceptionMessage(e);
            return;
        }

        processExternalLink(request);
    }

    protected void showCriticalExceptionMessage(Exception e) {
        String initErrorCaption = messages.getMainMessage("app.initErrorCaption");
        String initErrorMessage = messages.getMainMessage("app.initErrorMessage");

        VerticalLayout content = new VerticalLayout();
        content.setStyleName("cuba-init-error-view");
        content.setSizeFull();

        VerticalLayout errorPanel = new VerticalLayout();
        errorPanel.setStyleName("cuba-init-error-panel");
        errorPanel.setWidthUndefined();
        errorPanel.setSpacing(true);

        Label captionLabel = new Label(initErrorCaption);
        captionLabel.setWidthUndefined();
        captionLabel.setStyleName("cuba-init-error-caption");
        captionLabel.addStyleName("h2");
        captionLabel.setValue(initErrorCaption);

        errorPanel.addComponent(captionLabel);

        Label messageLabel = new Label(initErrorCaption);
        messageLabel.setWidthUndefined();
        messageLabel.setStyleName("cuba-init-error-message");
        messageLabel.setValue(initErrorMessage);

        errorPanel.addComponent(messageLabel);

        Button retryButton = new Button(messages.getMainMessage("app.initRetry"));
        retryButton.setStyleName("cuba-init-error-retry");
        retryButton.addClickListener((Button.ClickListener) event -> {
            // always restart UI
            String url = ControllerUtils.getLocationWithoutParams() + "?restartApp";
            getPage().open(url, "_self");
        });

        errorPanel.addComponent(retryButton);
        errorPanel.setComponentAlignment(retryButton, Alignment.MIDDLE_CENTER);

        content.addComponent(errorPanel);
        content.setComponentAlignment(errorPanel, Alignment.MIDDLE_CENTER);

        setContent(content);
    }

    protected void setupUI() throws LoginException {
        if (!app.getConnection().isConnected() && !app.loginOnStart()) {
            app.getConnection().loginAnonymous(app.getLocale());
        } else {
            app.createTopLevelWindow(this);
        }
    }

    @Override
    protected void refresh(VaadinRequest request) {
        super.refresh(request);

        // handle page refresh
        if (app.getConnection().isAuthenticated()) {
            // Ping middleware session if connected
            log.debug("Ping middleware session");

            try {
                UserSessionService service = AppBeans.get(UserSessionService.NAME);
                UserSession session = app.getConnection().getSession();
                if (session instanceof ClientUserSession
                        && ((ClientUserSession) session).isAuthenticated()) {
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

    public TopLevelWindow getTopLevelWindow() {
        return topLevelWindow;
    }

    public void setTopLevelWindow(TopLevelWindow window) {
        if (this.topLevelWindow != window) {
            this.topLevelWindow = window;

            // unregister previous components
            setContent(null);

            setContent(topLevelWindow.unwrapComposition(Component.class));
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
        WrappedSession wrappedSession = request.getWrappedSession();

        String action = (String) wrappedSession.getAttribute(LAST_REQUEST_ACTION_ATTR);

        if (webConfig.getLinkHandlerActions().contains(action)) {
            //noinspection unchecked
            Map<String, String> params =
                    (Map<String, String>) wrappedSession.getAttribute(LAST_REQUEST_PARAMS_ATTR);
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
        if (profiledScreens != null) {
            for (String profilerMarker : profilerMarkers) {
                profiledScreens.remove(profilerMarker);
            }
        }
    }

    protected void updateClientSystemMessages(Locale locale) {
        CubaClientManager.SystemMessages msgs = new CubaClientManager.SystemMessages();

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

    @Override
    public void onHistoryBackPerformed() {
        TopLevelWindow topLevelWindow = getTopLevelWindow();
        if (topLevelWindow instanceof CubaHistoryControl.HistoryBackHandler) {
            ((CubaHistoryControl.HistoryBackHandler) topLevelWindow).onHistoryBackPerformed();
        }
    }

    protected AbstractComponent getTopLevelWindowComposition() {
        if (topLevelWindow == null) {
            throw new IllegalStateException("UI does not have top level window");
        }

        return topLevelWindow.unwrapComposition(AbstractComponent.class);
    }

    public List<CubaTimer> getTimers() {
        AbstractComponent timersHolder = getTopLevelWindowComposition();

        List<CubaTimer> timers = new ArrayList<>();
        for (Extension extension : timersHolder.getExtensions()) {
            if (extension instanceof CubaTimer) {
                timers.add((CubaTimer) extension);
            }
        }
        return timers;
    }

    public void addTimer(CubaTimer timer) {
        AbstractComponent timersHolder = getTopLevelWindowComposition();

        if (!timersHolder.getExtensions().contains(timer)) {
            timer.extend(timersHolder);
        }
    }

    public void removeTimer(CubaTimer timer) {
        AbstractComponent timersHolder = getTopLevelWindowComposition();

        timersHolder.removeExtension(timer);
    }

    public void beforeTopLevelWindowInit() {
        updateUiTheme();

        setProfilerParameters();

        updateClientSystemMessages(app.getLocale());

        getTestIdManager().reset();
    }

    protected void setProfilerParameters() {
        clientProfiler.setFlushEventsCount(screenProfilerConfig.getFlushEventsCount());
        clientProfiler.setFlushTimeout(screenProfilerConfig.getFlushTimeout());
    }

    protected void updateUiTheme() {
        UserSession userSession = userSessionSource.getUserSession();

        if (userSession instanceof ClientUserSession && ((ClientUserSession) userSession).isAuthenticated()) {
            // load theme from user settings
            String themeName = webConfig.getAppWindowTheme();

            themeName = userSettingsTools.loadAppWindowTheme() == null ? themeName : userSettingsTools.loadAppWindowTheme();

            if (!Objects.equals(themeName, getTheme())) {
                // check theme support
                Set<String> supportedThemes = themeConstantsRepository.getAvailableThemes();
                if (supportedThemes.contains(themeName)) {
                    app.applyTheme(themeName);
                    setTheme(themeName);
                }
            }
        }
    }

    public CubaFileDownloader getFileDownloader() {
        return fileDownloader;
    }
}