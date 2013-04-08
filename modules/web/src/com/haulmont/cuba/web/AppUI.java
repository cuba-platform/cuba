/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author artamonov
 * @version $Id$
 */
@PreserveOnRefresh
public class AppUI extends UI implements ErrorHandler {

    public static final String APPLICATION_CLASS_CONFIG_KEY = "Application";

    private final static Log log = LogFactory.getLog(AppUI.class);

    protected boolean applicationInitRequired = false;

    public AppUI() {
        if (!App.isBound()) {
            App app = createApplication();
            VaadinSession.getCurrent().setAttribute(App.class, app);

            applicationInitRequired = true;
        }
    }

    @Override
    public void doInit(VaadinRequest request, int uiId) {
        super.doInit(request, uiId);
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

         // place login/main window
         App.getInstance().initView();

         setLocale(App.getInstance().getLocale());
    }

    public static AppUI getCurrent() {
        return (AppUI) UI.getCurrent();
    }

    public AppWindow getAppWindow() {
        Component currentUIView = getContent();
        if (currentUIView instanceof AppWindow)
            return (AppWindow) currentUIView;
        else
            return null;
    }

    @Override
    public void error(com.vaadin.server.ErrorEvent event) {
        if (App.isBound())
            App.getInstance().getExceptionHandlers().handle(event);
        else
            log.error(event.getThrowable());
    }
}