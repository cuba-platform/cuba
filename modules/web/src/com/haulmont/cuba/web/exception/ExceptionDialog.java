/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.haulmont.cuba.web.gui.WebWindow;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.haulmont.cuba.web.toolkit.ui.CubaWindow;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This dialog can be used by exception handlers to show an information about error.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ExceptionDialog extends CubaWindow {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected VerticalLayout mainLayout;

    protected Panel stackTraceScrollablePanel;

    protected Button showStackTraceButton;

    protected boolean isStackTraceVisible = false;

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    protected ClientConfig clientConfig = AppBeans.<Configuration>get(Configuration.NAME).getConfig(ClientConfig.class);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    public ExceptionDialog(Throwable throwable) {
        this(throwable, null, null);
    }

    public ExceptionDialog(Throwable throwable, @Nullable String caption, @Nullable String message) {
        final AppUI ui = AppUI.getCurrent();

        setCaption(caption != null ? caption : messages.getMessage(ExceptionDialog.class, "exceptionDialog.caption"));

        ThemeConstants theme = ui.getApp().getThemeConstants();
        setWidth(theme.get("cuba.web.ExceptionDialog.width"));
        center();

        final String text = message != null ? message : getText(throwable);
        Throwable exception = removeRemoteException(throwable);
        final String stackTrace = getStackTrace(exception);
        final String htmlStackTrace = convertToHtml(stackTrace);

        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);

        TextArea textArea = new TextArea();
        textArea.setHeight(theme.get("cuba.web.ExceptionDialog.textArea.height"));
        textArea.setWidth(100, Unit.PERCENTAGE);
        textArea.setValue(text);
        textArea.setReadOnly(true);

        mainLayout.addComponent(textArea);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);
        buttonsLayout.setWidth("100%");
        mainLayout.addComponent(buttonsLayout);

        Button closeButton = new CubaButton(messages.getMessage(ExceptionDialog.class, "exceptionDialog.closeBtn"));
        closeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ExceptionDialog.this.close();
            }
        });
        buttonsLayout.addComponent(closeButton);

        showStackTraceButton = new CubaButton(messages.getMessage(ExceptionDialog.class, "exceptionDialog.showStackTrace"));
        showStackTraceButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setStackTraceVisible(!isStackTraceVisible);
            }
        });
        buttonsLayout.addComponent(showStackTraceButton);

        Label spacer = new Label();
        buttonsLayout.addComponent(spacer);
        buttonsLayout.setExpandRatio(spacer, 1);

        if (userSessionSource.getUserSession() != null) {
            if (!StringUtils.isBlank(clientConfig.getSupportEmail())) {
                final Button reportButton = new CubaButton(messages.getMessage(ExceptionDialog.class, "exceptionDialog.reportBtn"));
                reportButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        sendSupportEmail(text, htmlStackTrace);
                        reportButton.setEnabled(false);
                    }
                });
                buttonsLayout.addComponent(reportButton);

                if (ui.isTestMode()) {
                    reportButton.setCubaId("reportButton");
                }
            }
        }

        Button logoutButton = new CubaButton(messages.getMessage(ExceptionDialog.class, "exceptionDialog.logout"));
        logoutButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                logoutPrompt();
            }
        });
        buttonsLayout.addComponent(logoutButton);

        VerticalLayout scrollContent = new VerticalLayout();
        scrollContent.setSizeUndefined();

        stackTraceScrollablePanel = new Panel();
        stackTraceScrollablePanel.setStyleName("cuba-log-panel");
        stackTraceScrollablePanel.setHeight("100%");
        stackTraceScrollablePanel.setContent(scrollContent);

        Label stackTraceLabel = new Label();
        stackTraceLabel.setContentMode(ContentMode.HTML);

        stackTraceLabel.setValue(htmlStackTrace);
        stackTraceLabel.setSizeUndefined();
        stackTraceLabel.setStyleName("cuba-log-content");
        scrollContent.addComponent(stackTraceLabel);

        setContent(mainLayout);
        setResizable(false);

        if (ui.isTestMode()) {
            setId(ui.getTestIdManager().getTestId("exceptionDialog"));
            setCubaId("exceptionDialog");

            closeButton.setCubaId("closeButton");
            showStackTraceButton.setCubaId("showStackTraceButton");
            stackTraceLabel.setCubaId("stackTraceLabel");
            logoutButton.setCubaId("logoutButton");
        }
    }

    protected String getStackTrace(Throwable throwable) {
        return ExceptionUtils.getStackTrace(throwable);
    }

    protected String convertToHtml(String text) {
        String html = StringEscapeUtils.escapeHtml(text);
        html = StringUtils.replace(html, "\n", "<br/>");
        html = StringUtils.replace(html, " ", "&nbsp;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

        return html;
    }

    protected Throwable removeRemoteException(Throwable throwable) {
        if (throwable instanceof RemoteException) {
            RemoteException re = (RemoteException) throwable;
            for (int i = re.getCauses().size() - 1; i >= 0; i--) {
                //noinspection ThrowableResultOfMethodCallIgnored
                if (re.getCauses().get(i).getThrowable() != null) {
                    return re.getCauses().get(i).getThrowable();
                }
            }
        }
        return throwable;
    }

    protected String getText(Throwable rootCause) {
        StringBuilder msg = new StringBuilder();
        if (rootCause instanceof RemoteException) {
            RemoteException re = (RemoteException) rootCause;
            if (!re.getCauses().isEmpty()) {
                RemoteException.Cause cause = re.getCauses().get(re.getCauses().size() - 1);
                //noinspection ThrowableResultOfMethodCallIgnored
                if (cause.getThrowable() != null) {
                    rootCause = cause.getThrowable();
                } else {
                    // root cause is not supported by client
                    String className = cause.getClassName();
                    if (className != null && className.indexOf('.') > 0) {
                        className = className.substring(className.lastIndexOf('.') + 1);
                    }
                    msg.append(className).append(": ").append(cause.getMessage());
                }
            }
        }

        if (msg.length() == 0) {
            msg.append(rootCause.getClass().getSimpleName());
            if (!StringUtils.isBlank(rootCause.getMessage())) {
                msg.append(": ").append(rootCause.getMessage());
            }

            if (rootCause instanceof DevelopmentException) {
                Map<String, Object> params = new LinkedHashMap<>();
                if (rootCause instanceof GuiDevelopmentException) {
                    GuiDevelopmentException guiDevException = (GuiDevelopmentException) rootCause;
                    if (guiDevException.getFrameId() != null) {
                        params.put("Frame ID", guiDevException.getFrameId());
                        try {
                            WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
                            params.put("XML descriptor",
                                    windowConfig.getWindowInfo(guiDevException.getFrameId()).getTemplate());
                        } catch (Exception e) {
                            params.put("XML descriptor", "not found for " + guiDevException.getFrameId());
                        }
                    }
                }
                params.putAll(((DevelopmentException) rootCause).getParams());

                if (!params.isEmpty()) {
                    msg.append("\n\n");
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        msg.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                }
            }
        }
        return msg.toString();
    }

    public void setStackTraceVisible(boolean visible) {
        isStackTraceVisible = visible;

        ThemeConstants theme = App.getInstance().getThemeConstants();
        if (visible) {
            showStackTraceButton.setCaption(messages.getMessage(ExceptionDialog.class, "exceptionDialog.hideStackTrace"));

            mainLayout.addComponent(stackTraceScrollablePanel);
            mainLayout.setExpandRatio(stackTraceScrollablePanel, 1.0f);
            mainLayout.setHeight(100, Unit.PERCENTAGE);

            setWidth(theme.get("cuba.web.ExceptionDialog.expanded.width"));
            setHeight(theme.get("cuba.web.ExceptionDialog.expanded.height"));

            setResizable(true);
            center();
        } else {
            showStackTraceButton.setCaption(messages.getMessage(ExceptionDialog.class, "exceptionDialog.showStackTrace"));

            mainLayout.setHeight(-1, Unit.PIXELS);
            mainLayout.removeComponent(stackTraceScrollablePanel);

            setWidth(theme.get("cuba.web.ExceptionDialog.width"));
            setHeight(-1, Unit.PERCENTAGE);

            setResizable(false);
            center();

            setWindowMode(WindowMode.NORMAL);
        }
    }

    public void sendSupportEmail(String message, String stackTrace) {
        try {
            User user = userSessionSource.getUserSession().getUser();
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeSource.currentTimestamp());

            //noinspection StringBufferReplaceableByString
            StringBuilder sb = new StringBuilder("<html><body>");
            sb.append("<p>").append(date).append("</p>");
            sb.append("<p>").append(message.replace("\n", "<br/>")).append("</p>");
            sb.append("<p>").append(stackTrace).append("</p>");
            sb.append("</body></html>");

            EmailInfo info = new EmailInfo(
                    clientConfig.getSupportEmail(),
                    "[" + clientConfig.getSystemID() + "] [" + user.getLogin() + "] Exception Report",
                    sb.toString());
            if (user.getEmail() != null) {
                info.setFrom(user.getEmail());
            }

            EmailService emailService = AppBeans.get(EmailService.NAME);
            emailService.sendEmail(info);
            Notification.show(messages.getMessage(ExceptionDialog.class, "exceptionDialog.emailSent"));
        } catch (Throwable e) {
            log.error("Error sending exception report", e);
            Notification.show(messages.getMessage(ExceptionDialog.class, "exceptionDialog.emailSendingErr"));
        }
    }

    protected void logoutPrompt() {
        App app = AppUI.getCurrent().getApp();
        final WebWindowManager wm = app.getWindowManager();
        wm.showOptionDialog(
                messages.getMessage(ExceptionDialog.class, "exceptionDialog.logoutCaption"),
                messages.getMessage(ExceptionDialog.class, "exceptionDialog.logoutMessage"),
                Frame.MessageType.WARNING,
                new Action[]{
                        new AbstractAction(messages.getMessage(WebWindow.class, "closeApplication")) {
                            @Override
                            public void actionPerform(com.haulmont.cuba.gui.components.Component component) {
                                forceLogout();
                            }

                            @Override
                            public String getIcon() {
                                return "icons/ok.png";
                            }
                        },
                        new DialogAction(Type.CANCEL, Status.PRIMARY)
                }
        );
    }

    protected void forceLogout() {
        App app = AppUI.getCurrent().getApp();
        final WebWindowManager wm = app.getWindowManager();
        try {
            Connection connection = wm.getApp().getConnection();
            if (connection.isConnected()) {
                connection.logout();
            }
        } catch (Exception e) {
            log.warn("Exception on forced logout", e);
        } finally {
            // always restart UI
            String url = ControllerUtils.getLocationWithoutParams() + "?restartApp";

            Page.getCurrent().open(url, "_self");
        }
    }
}