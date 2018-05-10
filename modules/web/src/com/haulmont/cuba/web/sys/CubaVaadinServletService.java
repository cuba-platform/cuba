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

package com.haulmont.cuba.web.sys;

import com.google.common.hash.HashCode;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.auth.WebAuthConfig;
import com.haulmont.cuba.web.sys.events.WebSessionDestroyedEvent;
import com.haulmont.cuba.web.sys.events.WebSessionInitializedEvent;
import com.haulmont.cuba.web.widgets.CubaFileUpload;
import com.vaadin.server.*;
import com.vaadin.server.communication.*;
import com.vaadin.ui.Component;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.google.common.hash.Hashing.md5;

public class CubaVaadinServletService extends VaadinServletService
        implements AtmospherePushConnection.UidlWriterFactory {

    private final Logger log = LoggerFactory.getLogger(CubaVaadinServletService.class);

    protected WebConfig webConfig;
    protected WebAuthConfig webAuthConfig;

    protected final String webResourceTimestamp;

    protected boolean testMode;
    protected boolean performanceTestMode;

    protected Events events;

    public CubaVaadinServletService(VaadinServlet servlet, DeploymentConfiguration deploymentConfiguration)
            throws ServiceException {
        super(servlet, deploymentConfiguration);

        this.events = AppBeans.get(Events.NAME);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        webConfig = configuration.getConfig(WebConfig.class);
        webAuthConfig = configuration.getConfig(WebAuthConfig.class);
        testMode = configuration.getConfig(GlobalConfig.class).getTestMode();
        performanceTestMode = configuration.getConfig(GlobalConfig.class).getPerformanceTestMode();

        ServletContext sc = servlet.getServletContext();
        String resourcesTimestamp = sc.getInitParameter("webResourcesTs");
        if (StringUtils.isNotEmpty(resourcesTimestamp)) {
            this.webResourceTimestamp = resourcesTimestamp;
        } else {
            this.webResourceTimestamp = "DEBUG";
        }

        addSessionInitListener(event -> {
            WrappedSession wrappedSession = event.getSession().getSession();
            wrappedSession.setMaxInactiveInterval(webConfig.getHttpSessionExpirationTimeoutSec());

            HttpSession httpSession = wrappedSession instanceof WrappedHttpSession ?
                    ((WrappedHttpSession) wrappedSession).getHttpSession() : null;

            log.debug("HttpSession {} initialized, timeout={}sec",
                    httpSession, wrappedSession.getMaxInactiveInterval());

            events.publish(new WebSessionInitializedEvent(event.getSession()));
        });

        addSessionDestroyListener(event -> {
            WrappedSession wrappedSession = event.getSession().getSession();
            HttpSession httpSession = wrappedSession instanceof WrappedHttpSession ?
                    ((WrappedHttpSession) wrappedSession).getHttpSession() : null;

            log.debug("HttpSession destroyed: {}", httpSession);
            App app = event.getSession().getAttribute(App.class);
            if (app != null) {
                app.cleanupBackgroundTasks();
            }

            events.publish(new WebSessionDestroyedEvent(event.getSession()));
        });

        setSystemMessagesProvider(systemMessagesInfo -> {
            Locale locale = systemMessagesInfo.getLocale();

            CustomizedSystemMessages msgs = new CustomizedSystemMessages();

            if (AppContext.isStarted()) {
                try {
                    Messages messages = AppBeans.get(Messages.NAME);

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

            String redirectUri;
            if (RequestContext.get() != null) {
                HttpServletRequest request = RequestContext.get().getRequest();
                redirectUri = StringUtils.replace(request.getRequestURI(), "/UIDL", "");
            } else {
                String webContext = AppContext.getProperty("cuba.webContextName");
                redirectUri = "/" + webContext;
            }

            msgs.setInternalErrorURL(redirectUri + "?restartApp");

            return msgs;
        });
    }

    @Override
    public String getConfiguredTheme(VaadinRequest request) {
        return webConfig.getAppWindowTheme();
    }

/*  vaadin8 implement
    @Override
    public String getApplicationVersion() {
        return webResourceTimestamp;
    }
*/

    @Override
    protected List<RequestHandler> createRequestHandlers() throws ServiceException {
        List<RequestHandler> requestHandlers = super.createRequestHandlers();

        List<RequestHandler> cubaRequestHandlers = new ArrayList<>();

        ServletContext servletContext = getServlet().getServletContext();

        for (RequestHandler handler : requestHandlers) {
            if (handler instanceof UidlRequestHandler) {
                cubaRequestHandlers.add(new CubaUidlRequestHandler(servletContext));
            } else if (handler instanceof PublishedFileHandler) {
                // replace PublishedFileHandler with CubaPublishedFileHandler
                // for support resources from VAADIN directory
                cubaRequestHandlers.add(new CubaPublishedFileHandler());
            } else if (handler instanceof ServletBootstrapHandler) {
                // replace ServletBootstrapHandler with CubaApplicationBootstrapHandler
                cubaRequestHandlers.add(new CubaServletBootstrapHandler());
            } else if (handler instanceof HeartbeatHandler) {
                // replace HeartbeatHandler with CubaHeartbeatHandler
                cubaRequestHandlers.add(new CubaHeartbeatHandler());
            } else if (handler instanceof FileUploadHandler) {
                // add support for jquery file upload
                cubaRequestHandlers.add(handler);
                cubaRequestHandlers.add(new CubaFileUploadHandler());
            } else if (handler instanceof ServletUIInitHandler) {
                cubaRequestHandlers.add(new CubaServletUIInitHandler(servletContext));
            } else if (handler instanceof PushRequestHandler) {
                PushHandler pushHandler = ((PushRequestHandler) handler).getPushHandler();
                pushHandler.setLongPollingSuspendTimeout(webConfig.getPushLongPollingSuspendTimeoutMs());

                cubaRequestHandlers.add(handler);
            } else {
                cubaRequestHandlers.add(handler);
            }
        }

        cubaRequestHandlers.add(new CubaWebJarsHandler(servletContext));

        return cubaRequestHandlers;
    }

    @Override
    public UidlWriter createUidlWriter() {
        return new CubaUidlWriter(getServlet().getServletContext());
    }

    // Add ability to load JS and CSS resources from VAADIN directory
    protected static class CubaPublishedFileHandler extends PublishedFileHandler {
        @Override
        protected InputStream getApplicationResourceAsStream(Class<?> contextClass, String fileName) {
            ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
            return servletContext.getResourceAsStream("/VAADIN/" + fileName);
        }
    }

    // Add support for CubaFileUpload component with XHR upload mechanism
    protected static class CubaFileUploadHandler extends FileUploadHandler {

        private final Logger log = LoggerFactory.getLogger(CubaFileUploadHandler.class);

        @Override
        protected boolean isSuitableUploadComponent(ClientConnector source) {
            if (!(source instanceof CubaFileUpload)) {
                // this is not jquery upload request
                return false;
            }

            log.trace("Uploading file using jquery file upload mechanism");

            return true;
        }

        @Override
        protected void sendUploadResponse(VaadinRequest request, VaadinResponse response,
                                          String fileName, long contentLength) throws IOException {
            JsonArray json = Json.createArray();
            JsonObject fileInfo = Json.createObject();
            fileInfo.put("name", fileName);
            fileInfo.put("size", contentLength);

            // just fake addresses and parameters
            fileInfo.put("url", fileName);
            fileInfo.put("thumbnail_url", fileName);
            fileInfo.put("delete_url", fileName);
            fileInfo.put("delete_type", "POST");
            json.set(0, fileInfo);

            PrintWriter writer = response.getWriter();
            writer.write(json.toJson());
            writer.close();
        }
    }

    /**
     * Add ability to redirect to base application URL if we have unparsable path tail
     */
    protected static class CubaServletBootstrapHandler extends ServletBootstrapHandler {
        @Override
        public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response)
                throws IOException {
            String requestPath = request.getPathInfo();

            // redirect to base URL if we have unparsable path tail
            if (!Objects.equals("/", requestPath)) {
                response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                response.setHeader("Location", request.getContextPath());

                return true;
            }

            return super.handleRequest(session, request, response);
        }
    }

    // Add ability to handle heartbeats in App
    protected static class CubaHeartbeatHandler extends HeartbeatHandler {
        private final Logger log = LoggerFactory.getLogger(CubaHeartbeatHandler.class);

        @Override
        public boolean synchronizedHandleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response)
                throws IOException {
            boolean result = super.synchronizedHandleRequest(session, request, response);

            if (log.isTraceEnabled()) {
                log.trace("Handle heartbeat {} {}", request.getRemoteHost(), request.getRemoteAddr());
            }

            if (result && App.isBound()) {
                App.getInstance().onHeartbeat();
            }

            return result;
        }
    }

    @Override
    protected VaadinSession createVaadinSession(VaadinRequest request) throws ServiceException {
        if (performanceTestMode) {
            return new TestVaadinSession(this);
        } else {
            return super.createVaadinSession(request);
        }
    }

    /**
     * Generates non-random IDs for components, used for performance testing.
     */
    protected static class TestVaadinSession extends VaadinSession {
        public TestVaadinSession(VaadinService service) {
            super(service);
        }

        @Override
        public String createConnectorId(ClientConnector connector) {
            if (connector instanceof Component) {
                Component component = (Component) connector;
                String id = component.getId() == null ? super.createConnectorId(connector) : component.getId();
                UserSession session = getAttribute(UserSession.class);

                String login = null;
                String locale = null;

                if (session != null) {
                    login = session.getCurrentOrSubstitutedUser().getLogin();
                    if (session.getLocale() != null) {
                        locale = session.getLocale().toLanguageTag();
                    }
                }

                StringBuilder idParts = new StringBuilder();
                if (login != null) {
                    idParts.append(login);
                }
                if (locale != null) {
                    idParts.append(locale);
                }
                idParts.append(id);

                return toLongNumberString(idParts.toString());
            }
            return super.createConnectorId(connector);
        }

        protected String toLongNumberString(String data) {
            HashCode hashCode = md5().hashString(data, StandardCharsets.UTF_8);
            byte[] hashBytes = hashCode.asBytes();
            byte[] shortBytes = new byte[Long.BYTES];

            System.arraycopy(hashBytes, 0, shortBytes, 0, Long.BYTES);

            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.put(shortBytes);
            buffer.flip();
            return Long.toString(Math.abs(buffer.getLong()));
        }
    }

    /*
     * Uses CubaUidlWriter instead of default UidlWriter to support reloading screens that contain components
     * that use web resources from WebJars
     */
    protected static class CubaServletUIInitHandler extends ServletUIInitHandler {
        protected final ServletContext servletContext;

        public CubaServletUIInitHandler(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override
        protected UidlWriter createUidlWriter() {
            return new CubaUidlWriter(servletContext);
        }
    }

    /*
     * Uses CubaUidlWriter instead of default UidlWriter to support reloading screens that contain components
     * that use web resources from WebJars
     */
    protected static class CubaUidlRequestHandler extends UidlRequestHandler {
        protected final ServletContext servletContext;

        public CubaUidlRequestHandler(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override
        protected UidlWriter createUidlWriter() {
            return new CubaUidlWriter(servletContext);
        }
    }
}