/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.exception;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.WebConfig;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    protected boolean isStackTraceVisible = false;

    protected Messages messages = AppBeans.get(Messages.class);

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.class);

    protected WebConfig webConfig = AppBeans.get(Configuration.class).getConfig(WebConfig.class);

    public ExceptionDialog(Throwable throwable) {
        super();
        setCaption(messages.getMessage(getClass(), "exceptionDialog.caption"));
        setWidth(600, Unit.PIXELS);
        center();

        final String text = getText(throwable);
        final String stackTrace = getStackTrace(throwable);

        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);

        TextArea textArea = new TextArea();
        textArea.setHeight(100, Unit.PIXELS);
        textArea.setWidth(100, Unit.PERCENTAGE);
        textArea.setValue(text);
        textArea.setReadOnly(true);

        mainLayout.addComponent(textArea);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);
        buttonsLayout.setWidth("100%");
        mainLayout.addComponent(buttonsLayout);

        HorizontalLayout leftButtonsLayout = new HorizontalLayout();
        leftButtonsLayout.setSpacing(true);
        buttonsLayout.addComponent(leftButtonsLayout);
        buttonsLayout.setComponentAlignment(leftButtonsLayout, Alignment.MIDDLE_LEFT);

        Button closeButton = new Button(messages.getMessage(getClass(), "exceptionDialog.closeBtn"));
        closeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ExceptionDialog.this.close();
            }
        });
        leftButtonsLayout.addComponent(closeButton);

        showStackTraceButton = new Button(messages.getMessage(getClass(), "exceptionDialog.showStackTrace"));
        showStackTraceButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setStackTraceVisible(!isStackTraceVisible);
            }
        });
        leftButtonsLayout.addComponent(showStackTraceButton);

        if (!StringUtils.isBlank(webConfig.getSupportEmail())) {
            final Button reportButton = new Button(messages.getMessage(getClass(), "exceptionDialog.reportBtn"));
            reportButton.addClickListener(new Button.ClickListener() {
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
        stackTraceLabel.setContentMode(ContentMode.HTML);

        stackTraceLabel.setValue(stackTrace);
        stackTraceLabel.setSizeUndefined();
        stackTraceLabel.setStyleName("cuba-log-content");
        scrollContent.addComponent(stackTraceLabel);

        setContent(mainLayout);
        setResizable(false);
    }

    private String getStackTrace(Throwable throwable) {
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

    private String getText(Throwable rootCause) {
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
            showStackTraceButton.setCaption(messages.getMessage(getClass(), "exceptionDialog.hideStackTrace"));

            mainLayout.addComponent(stackTraceScrollablePanel);
            mainLayout.setExpandRatio(stackTraceScrollablePanel, 1.0f);
            mainLayout.setHeight(100, Unit.PERCENTAGE);

            setWidth(750, Unit.PIXELS);
            setHeight(650, Unit.PIXELS);

            setResizable(true);
            center();
        } else {
            showStackTraceButton.setCaption(messages.getMessage(getClass(), "exceptionDialog.showStackTrace"));

            mainLayout.setHeight(-1, Unit.PIXELS);
            mainLayout.removeComponent(stackTraceScrollablePanel);

            setWidth(600, Unit.PIXELS);
            setHeight(-1, Unit.PERCENTAGE);

            setResizable(false);
            center();

            setWindowMode(WindowMode.NORMAL);
        }
    }

    public void sendSupportEmail(String message, String stackTrace) {
        try {
            User user = AppBeans.get(UserSessionSource.class).getUserSession().getUser();
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
            Notification.show(messages.getMessage(getClass(), "exceptionDialog.emailSent"));
        } catch (Throwable e) {
            log.error("Error sending exception report", e);
            Notification.show(messages.getMessage(getClass(), "exceptionDialog.emailSendingErr"));
        }
    }
}