/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This dialog can be used by exception handlers to show an information about error.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ExceptionDialog extends Window {

    protected Log log = LogFactory.getLog(getClass());

    protected VerticalLayout mainLayout;

    protected Panel stackTraceScrollablePanel;

    protected Button showStackTraceButton;

    protected TextArea messageTextArea;

    protected boolean isStackTraceVisible = false;

    protected Messages messages = AppBeans.get(Messages.class);

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

    protected WebConfig webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);

    public ExceptionDialog(Throwable throwable) {
        setCaption(messages.getMessage(getClass(), "exceptionDialog.caption"));
        setWidth(600, UNITS_PIXELS);
        setHeight(175, UNITS_PIXELS);
        center();

        final String text = getText(throwable);
        final String stackTrace = getStackTrace(throwable);

        mainLayout = new VerticalLayout();
        mainLayout.setHeight(100, UNITS_PERCENTAGE);
        mainLayout.setSpacing(true);

        messageTextArea = new TextArea();
        messageTextArea.setHeight(100, UNITS_PERCENTAGE);
        messageTextArea.setWidth(100, UNITS_PERCENTAGE);
        messageTextArea.setValue(text);
        messageTextArea.setReadOnly(true);

        mainLayout.addComponent(messageTextArea);
        mainLayout.setExpandRatio(messageTextArea, 1f);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);
        buttonsLayout.setWidth("100%");
        mainLayout.addComponent(buttonsLayout);

        HorizontalLayout leftButtonsLayout = new HorizontalLayout();
        leftButtonsLayout.setSpacing(true);
        buttonsLayout.addComponent(leftButtonsLayout);
        buttonsLayout.setComponentAlignment(leftButtonsLayout, Alignment.MIDDLE_LEFT);

        Button closeButton = new Button(messages.getMessage(getClass(), "exceptionDialog.closeBtn"));
        closeButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ExceptionDialog.this.close();
            }
        });
        leftButtonsLayout.addComponent(closeButton);

        showStackTraceButton = new Button(messages.getMessage(getClass(), "exceptionDialog.showStackTrace"));
        showStackTraceButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setStackTraceVisible(!isStackTraceVisible);
            }
        });
        leftButtonsLayout.addComponent(showStackTraceButton);

        if (!StringUtils.isBlank(webConfig.getSupportEmail()) && userSessionSource.getUserSession() != null) {
            final Button reportButton = new Button(messages.getMessage(getClass(), "exceptionDialog.reportBtn"));
            reportButton.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    sendSupportEmail(text, stackTrace);
                    reportButton.setEnabled(false);
                }
            });
            buttonsLayout.addComponent(reportButton);
            buttonsLayout.setComponentAlignment(reportButton, Alignment.MIDDLE_RIGHT);
        }

        VerticalLayout scrollContent = new VerticalLayout();
        scrollContent.setSizeUndefined();

        stackTraceScrollablePanel = new Panel();
        stackTraceScrollablePanel.setStyleName("cuba-log-panel");
        stackTraceScrollablePanel.setHeight("100%");
        stackTraceScrollablePanel.setContent(scrollContent);

        final Label stackTraceLabel = new Label();
        stackTraceLabel.setContentMode(Label.CONTENT_XHTML);

        stackTraceLabel.setValue(stackTrace);
        stackTraceLabel.setSizeUndefined();
        stackTraceLabel.setStyleName("cuba-log-content");
        scrollContent.addComponent(stackTraceLabel);

        setContent(mainLayout);
        setResizable(false);
    }

    protected String getStackTrace(Throwable throwable) {
        if (throwable instanceof RemoteException) {
            RemoteException re = (RemoteException) throwable;
            for (int i = re.getCauses().size() - 1; i >= 0; i--) {
                if (re.getCauses().get(i).getThrowable() != null) {
                    throwable = re.getCauses().get(i).getThrowable();
                    break;
                }
            }
        }

        String html = StringEscapeUtils.escapeHtml(ExceptionUtils.getStackTrace(throwable));
        html = StringUtils.replace(html, "\n", "<br/>");
        html = StringUtils.replace(html, " ", "&nbsp;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

        return html;
    }

    protected String getText(Throwable rootCause) {
        StringBuilder msg = new StringBuilder();
        if (rootCause instanceof RemoteException) {
            RemoteException re = (RemoteException) rootCause;
            if (!re.getCauses().isEmpty()) {
                RemoteException.Cause cause = re.getCauses().get(re.getCauses().size() - 1);
                if (cause.getThrowable() != null)
                    rootCause = cause.getThrowable();
                else {
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
            if (!StringUtils.isBlank(rootCause.getMessage()))
                msg.append(": ").append(rootCause.getMessage());

            if (rootCause instanceof DevelopmentException) {
                Map<String, Object> params = new LinkedHashMap<>();
                if (rootCause instanceof GuiDevelopmentException) {
                    GuiDevelopmentException guiDevException = (GuiDevelopmentException) rootCause;
                    if (guiDevException.getFrameId() != null) {
                        params.put("Frame ID", guiDevException.getFrameId());
                        try {
                            WindowConfig windowConfig = AppBeans.get(WindowConfig.class);
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

        if (visible) {
            messageTextArea.setHeight(110, UNITS_PIXELS);
            mainLayout.setExpandRatio(messageTextArea, 0);

            showStackTraceButton.setCaption(messages.getMessage(getClass(), "exceptionDialog.hideStackTrace"));

            mainLayout.addComponent(stackTraceScrollablePanel);
            mainLayout.setExpandRatio(stackTraceScrollablePanel, 1f);

            setWidth(750, UNITS_PIXELS);
            setHeight(650, UNITS_PIXELS);

            setResizable(true);
            center();
        } else {
            messageTextArea.setHeight(100, UNITS_PERCENTAGE);
            mainLayout.setExpandRatio(messageTextArea, 1f);

            showStackTraceButton.setCaption(messages.getMessage(getClass(), "exceptionDialog.showStackTrace"));

            mainLayout.removeComponent(stackTraceScrollablePanel);

            setWidth(600, UNITS_PIXELS);
            setHeight(175, UNITS_PIXELS);

            setResizable(false);
            center();
        }
    }

    public void sendSupportEmail(String message, String stackTrace) {
        try {
            User user = userSessionSource.getUserSession().getUser();
            TimeSource timeSource = AppBeans.get(TimeSource.class);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeSource.currentTimestamp());

            StringBuilder sb = new StringBuilder("<html><body>");
            sb.append("<p>").append(date).append("</p>");
            sb.append("<p>").append(message.replace("\n", "<br/>")).append("</p>");
            sb.append("<p>").append(stackTrace).append("</p>");
            sb.append("</body></html>");

            EmailInfo info = new EmailInfo(
                    webConfig.getSupportEmail(),
                    "[" + webConfig.getSystemID() + "] [" + user.getLogin() + "] Exception Report",
                    sb.toString());
            if (user.getEmail() != null)
                info.setFrom(user.getEmail());

            AppBeans.get(EmailService.class).sendEmail(info);

            showNotification(new Notification(messages.getMessage(getClass(), "exceptionDialog.emailSent"), Notification.TYPE_HUMANIZED_MESSAGE));
        } catch (Throwable e) {
            log.error("Error sending exception report", e);
            showNotification(new Notification(messages.getMessage(getClass(), "exceptionDialog.emailSendingErr"), Notification.TYPE_HUMANIZED_MESSAGE));
        }
    }
}