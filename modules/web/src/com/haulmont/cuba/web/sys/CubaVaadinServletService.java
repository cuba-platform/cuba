/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.server.*;
import com.vaadin.server.communication.PublishedFileHandler;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaVaadinServletService extends VaadinServletService {

    protected WebConfig webConfig;

    protected final String webResourceTimestamp;

    public CubaVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        super(servlet, deploymentConfiguration);

        webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);

        String resourcesTimestampPath = webConfig.getResourcesTimestampPath();
        if (StringUtils.isNotEmpty(resourcesTimestampPath)) {
            String timestamp = AppBeans.get(Resources.class).getResourceAsString(resourcesTimestampPath);
            if (StringUtils.isNotEmpty(timestamp)) {
                this.webResourceTimestamp = timestamp;
            } else {
                this.webResourceTimestamp = "DEBUG";
            }
        } else {
            this.webResourceTimestamp = "DEBUG";
        }

        addSessionDestroyListener(new SessionDestroyListener() {
            @Override
            public void sessionDestroy(SessionDestroyEvent event) {
                App app = event.getSession().getAttribute(App.class);
                if (app != null) {
                    app.cleanupBackgroundTasks();
                }
            }
        });

        setSystemMessagesProvider(new SystemMessagesProvider() {
            @Override
            public SystemMessages getSystemMessages(SystemMessagesInfo systemMessagesInfo) {
                Locale locale = systemMessagesInfo.getLocale();

                String webContext = AppContext.getProperty("cuba.webContextName");

                CustomizedSystemMessages msgs = new CustomizedSystemMessages();

                if (AppContext.isStarted()) {
                    Messages messages = AppBeans.get(Messages.class);

                    msgs.setInternalErrorCaption(messages.getMainMessage("internalErrorCaption", locale));
                    msgs.setInternalErrorMessage(messages.getMainMessage("internalErrorMessage", locale));

                    msgs.setCommunicationErrorCaption(messages.getMainMessage("communicationErrorCaption", locale));
                    msgs.setCommunicationErrorMessage(messages.getMainMessage("communicationErrorMessage", locale));

                    msgs.setSessionExpiredCaption(messages.getMainMessage("sessionExpiredErrorCaption", locale));
                    msgs.setSessionExpiredMessage(messages.getMainMessage("sessionExpiredErrorMessage", locale));
                }
                msgs.setOutOfSyncNotificationEnabled(false);
                // todo replace with normal address from request
                msgs.setInternalErrorURL("/" + webContext + "?restartApp");

                return msgs;
            }
        });
    }

    @Override
    public String getConfiguredTheme(VaadinRequest request) {
        // vaadin7 return theme from user settings or use system default
        return webConfig.getAppWindowTheme();
    }

    @Override
    public String getApplicationVersion() {
        return webResourceTimestamp;
    }

    @Override
    protected List<RequestHandler> createRequestHandlers() throws ServiceException {
        List<RequestHandler> requestHandlers = super.createRequestHandlers();
        // replace PublishedFileHandler for support resources from VAADIN directory

        List<RequestHandler> cubaRequestHandlers = new ArrayList<>();
        for (RequestHandler handler : requestHandlers) {
            if (handler instanceof PublishedFileHandler) {
                // replace PublishedFileHandler with CubaPublishedFileHandler
                cubaRequestHandlers.add(new CubaPublishedFileHandler());
            } else {
                cubaRequestHandlers.add(handler);
            }
        }

        return cubaRequestHandlers;
    }

    // Add ability to load JS and CSS resources from VAADIN directory
    protected static class CubaPublishedFileHandler extends PublishedFileHandler {
        @Override
        protected InputStream getApplicationResourceAsStream(Class<?> contextClass, String fileName) {
            return VaadinServlet.getCurrent().getServletContext().getResourceAsStream("VAADIN/" + fileName);
        }
    }
}