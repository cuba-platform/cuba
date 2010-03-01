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

import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.toolkit.Timer;
import com.vaadin.Application;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.VariableOwner;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("serial")
public class CubaCommunicationManager extends CommunicationManager {

    private long timerIdSequence = 0;

    private Map<String, Timer> id2Timer = new HashMap<String, Timer>();

    private Map<Timer, String> timer2Id = new HashMap<Timer, String>();

    public CubaCommunicationManager(Application application, AbstractApplicationServlet applicationServlet) {
        super(application, applicationServlet);
    }

    /**
     * Handles UIDL request
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void handleUidlRequest(HttpServletRequest request,
            HttpServletResponse response,
            AbstractApplicationServlet applicationServlet) throws IOException,
            ServletException, InvalidUIDLSecurityKeyException {

        // repaint requested or session has timed out and new one is created
        boolean repaintAll = (request.getParameter(GET_PARAM_REPAINT_ALL) != null)
                || request.getSession().isNew();
        boolean analyzeLayouts = false;
        if (repaintAll) {
            // analyzing can be done only with repaintAll
            analyzeLayouts = (request.getParameter(GET_PARAM_ANALYZE_LAYOUTS) != null);
        }

        final OutputStream out = response.getOutputStream();
        final PrintWriter outWriter = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(out, "UTF-8")));

        // The rest of the process is synchronized with the application
        // in order to guarantee that no parallel variable handling is
        // made
        synchronized (application) {

            // Finds the window within the application
            Window window;
            if (application.isRunning()) {
                window = getApplicationWindow(request, applicationServlet,
                        application, null);
                // Returns if no window found
                if (window == null) {
                    // This should not happen, no windows exists but
                    // application is still open.
                    System.err
                            .println("Warning, could not get window for application with request URI "
                                    + request.getRequestURI());
                    return;
                }
            } else {
                // application has been closed
                endApplication(request, response, application);
                return;
            }

            processRequestedCookies(request, response);

            // Change all variables based on request parameters
            if (!handleVariables(request, response, applicationServlet,
                    application, window)) {

                // var inconsistency; the client is probably out-of-sync
                Application.SystemMessages ci = null;
                try {
                    Method m = application.getClass().getMethod(
                            "getSystemMessages", (Class[]) null);
                    ci = (Application.SystemMessages) m.invoke(null,
                            (Object[]) null);
                } catch (Exception e2) {
                    // FIXME: Handle exception
                    // Not critical, but something is still wrong; print
                    // stacktrace
                    e2.printStackTrace();
                }
                if (ci != null) {
                    String msg = ci.getOutOfSyncMessage();
                    String cap = ci.getOutOfSyncCaption();
                    if (msg != null || cap != null) {
                        ((CubaApplicationServlet) applicationServlet).sendCriticalNotification(request,
                                response, cap, msg, null, ci.getOutOfSyncURL());
                        // will reload page after this
                        return;
                    }
                }
                // No message to show, let's just repaint all.
                repaintAll = true;
            }

            processResponsedCookies(request, response);

            paintAfterVariablechanges(request, response, applicationServlet,
                    repaintAll, outWriter, window, analyzeLayouts);

            // Mark this window to be open on client
            currentlyOpenWindowsInClient.add(window.getName());
            if (closingWindowName != null) {
                currentlyOpenWindowsInClient.remove(closingWindowName);
                closingWindowName = null;
            }
        }

        out.flush();
        out.close();
    }

    protected void processRequestedCookies(HttpServletRequest request, HttpServletResponse response) {
        App app = App.getInstance();
        app.getCookies().processRequestedCookies(request);
    }

    protected void processResponsedCookies(HttpServletRequest request, HttpServletResponse response) {
        App app = App.getInstance();
        app.getCookies().processResponsedCookies(response);
    }

    @Override
    protected void paintAdditionalData(
            HttpServletRequest request,
            HttpServletResponse response,
            AbstractApplicationServlet applicationServlet,
            boolean repaintAll,
            PrintWriter writer,
            Window window,
            boolean analyzeLayouts
    ) throws PaintException {
        final App application = App.getInstance();

        writer.print(", \"timers\":[");

        final JsonPaintTarget paintTarget = new JsonPaintTarget(this, writer, false);

        final Set<Timer> timers = new HashSet<Timer>(application.getApplicationTimers());
        for (final Timer timer : timers) {
            if (repaintAll || timer.isDirty()) {
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
    protected boolean handleVariables(
            HttpServletRequest request,
            HttpServletResponse response,
            AbstractApplicationServlet applicationServlet,
            Application application2,
            Window window
    ) throws IOException, CommunicationManager.InvalidUIDLSecurityKeyException {
        boolean success = true;

        if (request.getContentLength() > 0) {
            String changes = readRequest(request);

            // Manage bursts one by one
            final String[] bursts = changes.split(VAR_BURST_SEPARATOR);

            // Security: double cookie submission pattern unless disabled by
            // property
            if (!"true"
                    .equals(application2
                            .getProperty(AbstractApplicationServlet.SERVLET_PARAMETER_DISABLE_XSRF_PROTECTION))) {
                if (bursts.length == 1 && "init".equals(bursts[0])) {
                    // initial request, no variable changes: send key
                    String seckey = (String) request.getSession().getAttribute(
                            ApplicationConnection.UIDL_SECURITY_HEADER);
                    if (seckey == null) {
                        seckey = "" + (int) (Math.random() * 1000000);
                    }
                    /*
                     * Cookie c = new Cookie(
                     * ApplicationConnection.UIDL_SECURITY_COOKIE_NAME, uuid);
                     * response.addCookie(c);
                     */
                    response.setHeader(
                            ApplicationConnection.UIDL_SECURITY_HEADER, seckey);
                    request.getSession().setAttribute(
                            ApplicationConnection.UIDL_SECURITY_HEADER, seckey);
                    return true;
                } else {
                    // check the key
                    String sessId = (String) request.getSession().getAttribute(
                            ApplicationConnection.UIDL_SECURITY_HEADER);
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
                    final VariableOwner owner = (VariableOwner) idPaintableMap
                            .get(variable[VAR_PID]);
                    if (owner != null && owner.isEnabled()) {
                        Map m;
                        if (nextVariable != null
                                && variable[VAR_PID]
                                        .equals(nextVariable[VAR_PID])) {
                            // we have more than one value changes in row for
                            // one variable owner, collect em in HashMap
                            m = new HashMap();
                            m.put(variable[VAR_NAME], convertVariableValue(
                                    variable[VAR_TYPE].charAt(0),
                                    variable[VAR_VALUE]));
                        } else {
                            // use optimized single value map
                            m = new SingleValueMap(variable[VAR_NAME],
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
                            handleChangeVariablesError(application2,
                                    (Component) owner, e, m);
                        }
                    } else {
                        Timer timer;
                        if ((timer = id2Timer.get(variable[VAR_PID])) != null && !timer.isStopped())
                        {
                            fireTimer(timer);
                        }
                        else
                        {
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
                            continue;
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
                    try {
                        paintAfterVariablechanges(request, response,
                                applicationServlet, true, outWriter, window,
                                false);
                    } catch (ServletException e) {
                        // We will ignore all servlet exceptions
                    }
                }

            }
        }
        return success;
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
}
