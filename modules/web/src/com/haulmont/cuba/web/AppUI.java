/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.haulmont.cuba.web.toolkit.ui.CubaJQueryIntegration;
import com.haulmont.cuba.web.toolkit.ui.CubaSWFObjectIntegration;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@PreserveOnRefresh
public class AppUI extends UI implements ErrorHandler {

    public static final String APPLICATION_CLASS_CONFIG_KEY = "Application";

    public static final String LAST_REQUEST_ACTION_ATTR = "lastRequestAction";

    public static final String LAST_REQUEST_PARAMS_ATTR = "lastRequestParams";

    private final static Log log = LogFactory.getLog(AppUI.class);

    protected App app;

    protected boolean applicationInitRequired = false;

    public AppUI() {
        log.trace("Creating UI " + this);
        if (!App.isBound()) {
            app = createApplication();
            VaadinSession.getCurrent().setAttribute(App.class, app);

            new CubaJQueryIntegration().extend(this);
            new CubaSWFObjectIntegration().extend(this);

            applicationInitRequired = true;

        } else {
            app = App.getInstance();
        }
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

            Locale locale = AppBeans.get(MessageTools.class).useLocaleLanguageOnly() ?
                    Locale.forLanguageTag(request.getLocale().getLanguage()) : request.getLocale();
            app.setLocale(locale);

            applicationInitRequired = false;
        }
        // open login or main window
        app.initView(this);
        // init error handlers
        setErrorHandler(this);

        processExternalLink(request);
    }

    @Override
    public void handleRequest(VaadinRequest request) {
        processExternalLink(request);
    }

    public void showView(UIView view) {
        setContent(view);
        getPage().setTitle(view.getTitle());
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

    @Override
    public void error(com.vaadin.server.ErrorEvent event) {
        try {
            app.getExceptionHandlers().handle(event);
            app.getAppLog().log(event);
        } catch (Throwable e) {
            //noinspection ThrowableResultOfMethodCallIgnored
            log.error("Error handling exception\nOriginal exception:\n"
                    + ExceptionUtils.getStackTrace(event.getThrowable())
                    + "\nException in handlers:\n"
                    + ExceptionUtils.getStackTrace(e)
            );
        }
    }

    public void processExternalLink(VaadinRequest request) {
        String action = (String) request.getWrappedSession().getAttribute(LAST_REQUEST_ACTION_ATTR);

        WebConfig webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);
        if (webConfig.getLinkHandlerActions().contains(action)) {
            //noinspection unchecked
            Map<String, String> params =
                    (Map<String, String>) request.getWrappedSession().getAttribute(LAST_REQUEST_PARAMS_ATTR);
            if (params == null) {
                log.warn("Unable to process the external link: lastRequestParams not found in session");
                return;
            }
            LinkHandler linkHandler = AppBeans.getPrototype(LinkHandler.NAME, App.getInstance(), action, params);
            if (App.getInstance().connection.isConnected()) {
                linkHandler.handle();
            } else {
                App.getInstance().linkHandler = linkHandler;
            }
        }
    }

    @Override
    public void detach() {
        log.trace("Detaching UI " + this);
        super.detach();
    }
}