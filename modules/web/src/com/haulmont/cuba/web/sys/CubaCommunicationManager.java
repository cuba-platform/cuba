/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 28.09.2009 12:25:29
 *
 * $Id$
 */
package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.toolkit.Timer;
import com.haulmont.cuba.web.toolkit.ui.MultiUpload;
import com.haulmont.cuba.web.toolkit.ui.charts.*;
import com.vaadin.Application;
import com.vaadin.external.org.apache.commons.fileupload.*;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.Paintable;
import com.vaadin.terminal.UploadStream;
import com.vaadin.terminal.VariableOwner;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public class CubaCommunicationManager extends CommunicationManager {

    private long timerIdSequence = 0;

    private Map<String, Timer> id2Timer = new HashMap<String, Timer>();

    private Map<Timer, String> timer2Id = new HashMap<Timer, String>();

    private Log log = LogFactory.getLog(CubaCommunicationManager.class);

    public CubaCommunicationManager(Application application) {
        super(application);
    }

    @Override
    protected void paintAdditionalData(
            Request request,
            Response response,
            boolean repaintAll,
            PrintWriter writer,
            Window window,
            boolean analyzeLayouts
    ) throws PaintException {
        final App application = App.getInstance();

        writer.print(", \"timers\":[");

        final JsonPaintTarget paintTarget = new JsonPaintTarget(this, writer, false);

        final Set<Timer> timers = new HashSet<Timer>(application.getAppTimers(window));
        for (final Timer timer : timers) {
            if (repaintAll || timer != null && timer.isDirty()) {
                String timerId;
                if ((timerId = timer2Id.get(timer)) == null) {
                    timerId = timerId();
                    timer2Id.put(timer, timerId);
                    id2Timer.put(timerId, timer);
                }
                timer.paintTimer(paintTarget, timerId);
                if (timer.isStopped()) {
                    fireTimerStop(timer);
                }
            }
        }

        paintTarget.close();

        writer.print("]");
    }

    @Override
    protected boolean handleVariables(Request request, Response response,
                                      Callback callback, Application application2, Window window)
            throws IOException, InvalidUIDLSecurityKeyException {
        boolean success = true;
        int contentLength = request.getContentLength();

        if (contentLength > 0) {
            String changes = readRequest(request);

            // Manage bursts one by one
            final String[] bursts = changes.split(VAR_BURST_SEPARATOR);

            // Security: double cookie submission pattern unless disabled by
            // property
            if (!"true"
                    .equals(application2
                            .getProperty(AbstractApplicationServlet.SERVLET_PARAMETER_DISABLE_XSRF_PROTECTION))) {
                if (bursts.length == 1 && "init".equals(bursts[0])) {
                    // init request; don't handle any variables, key sent in
                    // response.
                    request.setAttribute(WRITE_SECURITY_TOKEN_FLAG, true);
                    return true;
                } else {
                    // ApplicationServlet has stored the security token in the
                    // session; check that it matched the one sent in the UIDL
                    String sessId = (String) request.getSession().getAttribute(
                            ApplicationConnection.UIDL_SECURITY_TOKEN_ID);

                    if (sessId == null || !sessId.equals(bursts[0])) {
                        throw new InvalidUIDLSecurityKeyException(
                                "Security key mismatch");
                    }
                }
            }

            for (int bi = 1; bi < bursts.length; bi++) {

                // extract variables to two dim string array
                final String[] tmp = bursts[bi].split(VAR_RECORD_SEPARATOR);
                final String[][] variableRecords = new String[tmp.length][4];
                for (int i = 0; i < tmp.length; i++) {
                    variableRecords[i] = tmp[i].split(VAR_FIELD_SEPARATOR);
                }

                for (int i = 0; i < variableRecords.length; i++) {
                    String[] variable = variableRecords[i];
                    String[] nextVariable = null;
                    if (i + 1 < variableRecords.length) {
                        nextVariable = variableRecords[i + 1];
                    }
                    final VariableOwner owner = getVariableOwner(variable[VAR_PID]);
                    if (owner != null && owner.isEnabled()) {
                        // TODO this should be Map<String, Object>, but the
                        // VariableOwner API does not guarantee the key is a
                        // string
                        Map<String, Object> m;
                        if (nextVariable != null
                                && variable[VAR_PID]
                                .equals(nextVariable[VAR_PID])) {
                            // we have more than one value changes in row for
                            // one variable owner, collect em in HashMap
                            m = new HashMap<String, Object>();
                            m.put(variable[VAR_NAME], convertVariableValue(
                                    variable[VAR_TYPE].charAt(0),
                                    variable[VAR_VALUE]));
                        } else {
                            // use optimized single value map
                            m = Collections.singletonMap(variable[VAR_NAME],
                                    convertVariableValue(variable[VAR_TYPE]
                                            .charAt(0), variable[VAR_VALUE]));
                        }

                        // collect following variable changes for this owner
                        while (nextVariable != null
                                && variable[VAR_PID]
                                .equals(nextVariable[VAR_PID])) {
                            i++;
                            variable = nextVariable;
                            if (i + 1 < variableRecords.length) {
                                nextVariable = variableRecords[i + 1];
                            } else {
                                nextVariable = null;
                            }
                            m.put(variable[VAR_NAME], convertVariableValue(
                                    variable[VAR_TYPE].charAt(0),
                                    variable[VAR_VALUE]));
                        }
                        try {
                            owner.changeVariables(request, m);

                            // Special-case of closing browser-level windows:
                            // track browser-windows currently open in client
                            if (owner instanceof Window
                                    && ((Window) owner).getParent() == null) {
                                final Boolean close = (Boolean) m.get("close");
                                if (close != null && close) {
                                    closingWindowName = ((Window) owner)
                                            .getName();
                                }
                            }
                        } catch (Exception e) {
                            if (owner instanceof Component) {
                                handleChangeVariablesError(application2,
                                        (Component) owner, e, m);
                            } else {
                                // TODO DragDropService error handling
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        Timer timer;
                        if ((timer = id2Timer.get(variable[VAR_PID])) != null && !timer.isStopped()) {
                            fireTimer(timer);
                        } else {
                            // Handle special case where window-close is called
                            // after the window has been removed from the
                            // application or the application has closed
                            if ("close".equals(variable[VAR_NAME])
                                    && "true".equals(variable[VAR_VALUE])) {
                                // Silently ignore this
                                continue;
                            }

                            // Ignore variable change
                            String msg = "Warning: Ignoring variable change for ";
                            if (owner != null) {
                                msg += "disabled component " + owner.getClass();
                                String caption = ((Component) owner).getCaption();
                                if (caption != null) {
                                    msg += ", caption=" + caption;
                                }
                            } else {
                                msg += "non-existent component, VAR_PID="
                                        + variable[VAR_PID];
                                success = false;
                            }
                            System.err.println(msg);
                        }
                    }
                }

                // In case that there were multiple bursts, we know that this is
                // a special synchronous case for closing window. Thus we are
                // not interested in sending any UIDL changes back to client.
                // Still we must clear component tree between bursts to ensure
                // that no removed components are updated. The painting after
                // the last burst is handled normally by the calling method.
                if (bi < bursts.length - 1) {

                    // We will be discarding all changes
                    final PrintWriter outWriter = new PrintWriter(
                            new CharArrayWriter());

                    paintAfterVariableChanges(request, response, callback,
                            true, outWriter, window, false);

                }

            }
        }
        return success;
    }

    @Override
    protected void doHandleFileUpload(Request request, Response response) throws IOException, FileUploadException {

        // Create a new file upload handler
        final FileUpload upload = createFileUpload();

        final UploadProgressListener pl = new UploadProgressListener();

        upload.setProgressListener(pl);

        // Parse the request
        FileItemIterator iter;

        try {
            iter = getUploadItemIterator(upload, request);
            /*
             * ATM this loop is run only once as we are uploading one file per
             * request.
             */
            while (iter.hasNext()) {
                final FileItemStream item = iter.next();
                final String name = item.getFieldName();
                // Should report only the filename even if the browser sends the
                // path
                final String filename = removePath(item.getName());
                final String mimeType = item.getContentType();
                final InputStream stream = item.openStream();
                if (item.isFormField()) {
                    // ignored, upload requests contains only files
                } else {
                    final UploadStream upstream = new UploadStream() {

                        public String getContentName() {
                            return filename;
                        }

                        public String getContentType() {
                            return mimeType;
                        }

                        public InputStream getStream() {
                            return stream;
                        }

                        public String getStreamName() {
                            return "stream";
                        }

                    };

                    if (name.startsWith("XHRFILE")) {
                        String[] split = item.getFieldName().substring(7)
                                .split("\\.");
                        DragAndDropWrapper ddw = (DragAndDropWrapper) idPaintableMap
                                .get(split[0]);

                        try {
                            ddw.receiveFile(upstream, split[1]);
                        } catch (Upload.UploadException e) {
                            synchronized (application) {
                                handleChangeVariablesError(application, ddw, e,
                                        new HashMap<String, Object>());
                            }
                        }

                    } else {

                        int separatorPos = name.lastIndexOf("_");
                        final String pid = name.substring(0, separatorPos);
                        final Upload uploadComponent = (Upload) idPaintableMap
                                .get(pid);
                        if (uploadComponent == null) {
                            throw new FileUploadException(
                                    "Upload component not found");
                        }
                        if (uploadComponent.isReadOnly()) {
                            throw new FileUploadException(
                                    "Warning: ignored file upload because upload component is set as read-only");
                        }
                        synchronized (application) {
                            // put upload component into receiving state
                            uploadComponent.startUpload();

                            // tell UploadProgressListener which component is
                            // receiving
                            // file
                            pl.setUpload(uploadComponent);

                            try {
                                uploadComponent.receiveUpload(upstream);
                            } catch (Upload.UploadException e) {
                                // error happened while receiving file. Handle the
                                // error in the same manner as it would have
                                // happened in
                                // variable change.
                                handleChangeVariablesError(application,
                                        uploadComponent, e,
                                        new HashMap<String, Object>());
                            }
                        }
                    }
                }
            }
        } catch (final FileUploadException e) {
            if (e instanceof FileUploadBase.SizeLimitExceededException) {
                String message = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "upload.fileTooBig.message");
                if (!StringUtils.isEmpty(message)) {
                    App.getInstance().getAppWindow().showNotification(
                            message,
                            com.vaadin.ui.Window.Notification.TYPE_WARNING_MESSAGE
                    );
                }
            }

            sendUploadFailed(request, response);

            throw e;
        }

        sendUploadResponse(request, response);
    }

    protected void sendUploadFailed(Request request, Response response) throws IOException {
        response.setContentType("text/html");
        final OutputStream out = response.getOutputStream();
        final PrintWriter outWriter = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(out, "UTF-8")));
        outWriter.print("<html><body>download failed</body></html>");
        outWriter.flush();
        out.close();
    }

    @Override
    protected FileUpload createFileUpload() {
        final Integer maxUploadSizeMb = ConfigProvider.getConfig(WebConfig.class).getMaxUploadSizeMb();

        FileUpload fileUpload = super.createFileUpload();
        fileUpload.setSizeMax(maxUploadSizeMb * 1048576);
        return fileUpload;
    }

    private String timerId() {
        return "TID" + ++timerIdSequence;
    }

    private void fireTimer(Timer timer) {
        final List<Timer.Listener> listeners = timer.getListeners();
        for (final Timer.Listener listener : listeners) {
            listener.onTimer(timer);
        }
    }

    private void fireTimerStop(Timer timer) {
        final List<Timer.Listener> listeners = new ArrayList<Timer.Listener>(timer.getListeners());
        for (final Timer.Listener listener : listeners) {
            listener.onStopTimer(timer);
            timer.removeListener(listener);
        }
    }

    public void handleChartRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            App app) {
        try {
            if (request.getSession() == null) {
                accessDenied(response);
                return;
            }

            String chartId = request.getParameter("id");
            if (chartId == null) {
                badRequest(response);
                return;
            }

            UserSession userSession = app.getConnection().getSession();
            if (userSession == null) {
                internalError(response);
                return;
            }

            Chart chart = (Chart)idPaintableMap.get(chartId);
            if (chart == null) {
                log.warn(String.format("Non-existent chart component, VAR_PID=%s", chartId));
                internalError(response);
                return;
            }

            WebSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());

            String vendor = chart.getVendor();
            ChartDataProvider dataProvider = ChartDataProviderFactory.getDataProvider(vendor);

            dataProvider.handleDataRequest(request, response, chart);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch(ChartException e) {
            log.error("Unable to handle data request: ", e);
            internalError(response);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            internalError(response);
        }
    }


    /**
     * Set response status to SC_UNAUTHORIZED
     *
     * @param response
     */
    protected void accessDenied(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * Set response status to SC_INTERNAL_SERVER_ERROR
     *
     * @param response
     */
    protected void internalError(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Set response status to SC_BAD_REQUEST
     *
     * @param response
     */
    protected void badRequest(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Handle multiple file upload
     *
     * @param request  HTTP request
     * @param response HTTP response
     */
    public synchronized void handleMultiUpload(HttpServletRequest request, HttpServletResponse response,
                                               App app) {
        try {
            if (request.getSession() == null)
                accessDenied(response);
            else {
                if (ServletFileUpload.isMultipartContent(request)) {
                    UserSession userSession = app.getConnection().getSession();
                    if (userSession == null) {
                        internalError(response);
                        return;
                    }

                    WebSecurityUtils.setSecurityAssociation(userSession.getUser().getLogin(), userSession.getId());

                    org.apache.commons.fileupload.FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);

                    String controlPid = request.getParameter("pid");
                    if (controlPid != null) {
                        // Get server-side component
                        final MultiUpload uploadComponent = (MultiUpload) idPaintableMap.get(controlPid);
                        if (uploadComponent != null) {
                            
                            org.apache.commons.fileupload.FileItemIterator iterator = upload.getItemIterator(request);
                            boolean find = false;
                            while (iterator.hasNext() && (!find)) {
                                org.apache.commons.fileupload.FileItemStream itemStream = iterator.next();
                                if (!itemStream.isFormField()) {
                                    uploadComponent.uploadingFile(itemStream, request.getContentLength());
                                    find = true;
                                }
                            }
                        }

                        response.setStatus(HttpServletResponse.SC_OK);
                    } else
                        badRequest(response);
                } else
                    badRequest(response);
            }
        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            internalError(response);
        }
    }
}
