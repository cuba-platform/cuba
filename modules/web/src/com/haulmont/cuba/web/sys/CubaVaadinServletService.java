/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.auth.RequestContext;
import com.vaadin.server.*;
import com.vaadin.server.communication.HeartbeatHandler;
import com.vaadin.server.communication.PublishedFileHandler;
import com.vaadin.server.communication.ServletBootstrapHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaVaadinServletService extends VaadinServletService {

    private Log log = LogFactory.getLog(CubaVaadinServletService.class);

    protected WebConfig webConfig;

    protected final String webResourceTimestamp;

    public CubaVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        super(servlet, deploymentConfiguration);

        webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);

        String resourcesTimestamp = servlet.getServletContext().getInitParameter("webResourcesTs");
        if (StringUtils.isNotEmpty(resourcesTimestamp)) {
            this.webResourceTimestamp = resourcesTimestamp;
        } else {
            this.webResourceTimestamp = "DEBUG";
        }

        addSessionInitListener(new SessionInitListener() {
            @Override
            public void sessionInit(SessionInitEvent event) throws ServiceException {
                event.getSession().getSession().setMaxInactiveInterval(webConfig.getHttpSessionExpirationTimeoutSec());
                log.debug("HTTP session " + event.getSession() + " initialized, timeout="
                        + event.getSession().getSession().getMaxInactiveInterval() + "sec");
            }
        });

        addSessionDestroyListener(new SessionDestroyListener() {
            @Override
            public void sessionDestroy(SessionDestroyEvent event) {
                log.debug("HTTP session destroyed: " + event.getSession());
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

                CustomizedSystemMessages msgs = new CustomizedSystemMessages();

                if (AppContext.isStarted()) {
                    try {
                        Messages messages = AppBeans.get(Messages.class);

                        msgs.setInternalErrorCaption(messages.getMainMessage("internalErrorCaption", locale));
                        msgs.setInternalErrorMessage(messages.getMainMessage("internalErrorMessage", locale));

                        msgs.setCommunicationErrorCaption(messages.getMainMessage("communicationErrorCaption", locale));
                        msgs.setCommunicationErrorMessage(messages.getMainMessage("communicationErrorMessage", locale));

                        msgs.setSessionExpiredCaption(messages.getMainMessage("sessionExpiredErrorCaption", locale));
                        msgs.setSessionExpiredMessage(messages.getMainMessage("sessionExpiredErrorMessage", locale));
                    } catch (Exception e) {
                        log.error("Unable to set system messages", e);
                        throw new RuntimeException("Unable to set system messages. " +
                                "It usually happens when the middleware web application is not responding due to " +
                                "errors on start. See logs for details.", e);
                    }
                }
                msgs.setOutOfSyncNotificationEnabled(false);

                String redirectUri;
                if (RequestContext.get() != null) {
                    redirectUri = StringUtils.replace(RequestContext.get().getRequest().getRequestURI(), "/UIDL", "");
                } else {
                    String webContext = AppContext.getProperty("cuba.webContextName");
                    redirectUri = "/" + webContext;
                }

                msgs.setInternalErrorURL(redirectUri + "?restartApp");

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

        List<RequestHandler> cubaRequestHandlers = new ArrayList<>();

        for (RequestHandler handler : requestHandlers) {
            if (handler instanceof PublishedFileHandler) {
                // replace PublishedFileHandler with CubaPublishedFileHandler
                // for support resources from VAADIN directory
                cubaRequestHandlers.add(new CubaPublishedFileHandler());
            } else if (handler instanceof ServletBootstrapHandler) {
                // replace ServletBootstrapHandler with CubaApplicationBootstrapHandler
                cubaRequestHandlers.add(new CubaApplicationBootstrapHandler());
            } else if (handler instanceof HeartbeatHandler) {
                // replace HeartbeatHandler with CubaHeartbeatHandler
                cubaRequestHandlers.add(new CubaHeartbeatHandler());
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
            return VaadinServlet.getCurrent().getServletContext().getResourceAsStream("/VAADIN/" + fileName);
        }
    }

    // Add ability to redirect to base application URL if we have unparsable path tail
    protected static class CubaApplicationBootstrapHandler extends ServletBootstrapHandler {

        @Override
        public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response)
                throws IOException {
            String requestPath = request.getPathInfo();

            // redirect to base URL if we have unparsable path tail
            if (!StringUtils.equals("/", requestPath)) {
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", request.getContextPath());

                return true;
            }

            return super.handleRequest(session, request, response);
        }
    }

    // Add ability to handle hearbeats in App
    protected static class CubaHeartbeatHandler extends HeartbeatHandler {

        private Log log = LogFactory.getLog(CubaHeartbeatHandler.class);

        @Override
        public boolean synchronizedHandleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response)
                throws IOException {
            boolean result = super.synchronizedHandleRequest(session, request, response);

            log.trace("Handle heartbeat");

            if (result && App.isBound()) {
                App.getInstance().onHeartbeat();
            }

            return result;
        }
    }
}