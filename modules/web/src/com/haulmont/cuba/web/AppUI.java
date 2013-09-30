/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.web.sys.LinkHandler;
import com.haulmont.cuba.web.toolkit.ui.CubaJQueryIntegration;
import com.haulmont.cuba.web.toolkit.ui.CubaSWFObjectIntegration;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    protected boolean applicationInitRequired = false;

    public AppUI() {
        if (!App.isBound()) {
            App app = createApplication();
            VaadinSession.getCurrent().setAttribute(App.class, app);

            new CubaJQueryIntegration().extend(this);
            new CubaSWFObjectIntegration().extend(this);

            applicationInitRequired = true;
        }
    }

    protected App createApplication() {
        String applicationClass = getApplicationClass();
        App application;
        try {
            Class<?> aClass = getClass().getClassLoader().loadClass(applicationClass);
            application = (App) aClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new Error(String.format("Unable to load class '%s' for Application", applicationClass));
        } catch (InstantiationException e) {
            throw new Error(String.format("Unable to instantiate Application '%s'", applicationClass));
        } catch (IllegalAccessException e) {
            throw new Error(String.format("Illegal access to Application class '%s'", applicationClass));
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
        // init error handlers
        setErrorHandler(this);

        if (applicationInitRequired) {
            App.getInstance().init();

            applicationInitRequired = false;
        }

        setLocale(App.getInstance().getLocale());

        // place login/main window
        App.getInstance().initView();

        processExternalLink(request);
    }

    @Override
    public void handleRequest(VaadinRequest request) {

        processExternalLink(request);
    }

    public static AppUI getCurrent() {
        return (AppUI) UI.getCurrent();
    }

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
        if (App.isBound()) {
            App.getInstance().getExceptionHandlers().handle(event);
            App.getInstance().getAppLog().log(event);
        } else {
            log.error(event.getThrowable());
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
}