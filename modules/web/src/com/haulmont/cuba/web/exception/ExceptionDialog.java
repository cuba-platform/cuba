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
package com.haulmont.cuba.web.exception;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.ExceptionReportService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.controllers.ControllerUtils;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.haulmont.cuba.web.toolkit.ui.CubaCopyButtonExtension;
import com.haulmont.cuba.web.toolkit.ui.CubaWindow;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This dialog can be used by exception handlers to show an information about error.
 */
public class ExceptionDialog extends CubaWindow {
    private static final Logger log = LoggerFactory.getLogger(ExceptionDialog.class);

    protected VerticalLayout mainLayout;

    protected TextArea stackTraceTextArea;

    protected Button copyButton;

    protected Button showStackTraceButton;

    protected boolean isStackTraceVisible = false;

    protected Map<String, Object> additionalExceptionReportBinding = null;

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected ExceptionReportService reportService = AppBeans.get(ExceptionReportService.NAME);

    protected WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);

    protected ClientConfig clientConfig = AppBeans.<Configuration>get(Configuration.NAME).getConfig(ClientConfig.class);

    protected UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);

    protected TimeSource timeSource = AppBeans.get(TimeSource.NAME);

    protected Security security = AppBeans.get(Security.NAME);

    public ExceptionDialog(Throwable throwable) {
        this(throwable, null, null);
    }

    public ExceptionDialog(Throwable throwable, @Nullable String caption, @Nullable String message) {
        final AppUI ui = AppUI.getCurrent();

        String closeShortcut = clientConfig.getCloseShortcut();
        KeyCombination closeCombination = KeyCombination.create(closeShortcut);

        com.vaadin.event.ShortcutAction closeShortcutAction = new com.vaadin.event.ShortcutAction(
                "closeShortcutAction",
                closeCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(closeCombination.getModifiers())
        );

        addActionHandler(new com.vaadin.event.Action.Handler() {
            @Override
            public com.vaadin.event.Action[] getActions(Object target, Object sender) {
                return new com.vaadin.event.Action[]{closeShortcutAction};
            }

            @Override
            public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
                if (Objects.equals(action, closeShortcutAction)) {
                    close();
                }
            }
        });

        setCaption(caption != null ? caption : messages.getMainMessage("exceptionDialog.caption"));

        ThemeConstants theme = ui.getApp().getThemeConstants();
        setWidth(theme.get("cuba.web.ExceptionDialog.width"));
        center();

        final String text = message != null ? message : getText(throwable);
        Throwable exception = removeRemoteException(throwable);
        final String stackTrace = getStackTrace(exception);

        mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);

        TextArea textArea = new TextArea();
        textArea.setHeight(theme.get("cuba.web.ExceptionDialog.textArea.height"));
        textArea.setWidth(100, Unit.PERCENTAGE);

        boolean showExceptionDetails = userSessionSource.getUserSession() != null
                && security.isSpecificPermitted("cuba.gui.showExceptionDetails");

        if (showExceptionDetails) {
            textArea.setValue(text);
        } else {
            textArea.setValue(messages.getMainMessage("exceptionDialog.contactAdmin"));
        }
        textArea.setReadOnly(true);

        mainLayout.addComponent(textArea);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);
        buttonsLayout.setWidth("100%");
        mainLayout.addComponent(buttonsLayout);

        Button closeButton = new CubaButton(messages.getMainMessage("exceptionDialog.closeBtn"));
        closeButton.addClickListener((Button.ClickListener) event ->
                this.close()
        );
        buttonsLayout.addComponent(closeButton);

        showStackTraceButton = new CubaButton(messages.getMainMessage("exceptionDialog.showStackTrace"));
        showStackTraceButton.addClickListener((Button.ClickListener) event ->
                setStackTraceVisible(!isStackTraceVisible)
        );
        buttonsLayout.addComponent(showStackTraceButton);
        showStackTraceButton.setVisible(showExceptionDetails);

        Label spacer = new Label();
        buttonsLayout.addComponent(spacer);
        buttonsLayout.setExpandRatio(spacer, 1);

        String cubaLogContentClass = "c-exception-dialog-log-content";
        String cubaCopyLogContentClass = cubaLogContentClass + "-" + UUID.randomUUID();

        if (CubaCopyButtonExtension.browserSupportCopy()) {
            copyButton = new CubaButton(messages.getMainMessage("exceptionDialog.copyStackTrace"));
            copyButton.setVisible(false);
            CubaCopyButtonExtension copyExtension = CubaCopyButtonExtension.copyWith(copyButton, cubaCopyLogContentClass);
            copyExtension.addCopyListener(event ->
                    Notification.show(messages.getMainMessage(
                            event.isSuccess() ? "exceptionDialog.copingSuccessful" : "exceptionDialog.copingFailed"),
                            Notification.Type.TRAY_NOTIFICATION));
            buttonsLayout.addComponent(copyButton);
        }

        if (userSessionSource.getUserSession() != null) {
            if (!StringUtils.isBlank(clientConfig.getSupportEmail())) {
                Button reportButton = new CubaButton(messages.getMainMessage("exceptionDialog.reportBtn"));
                reportButton.addClickListener((Button.ClickListener) event -> {
                    sendSupportEmail(text, stackTrace);
                    reportButton.setEnabled(false);
                });
                buttonsLayout.addComponent(reportButton);

                if (ui.isTestMode()) {
                    reportButton.setCubaId("errorReportButton");
                }
            }
        }

        Button logoutButton = new CubaButton(messages.getMainMessage("exceptionDialog.logout"));
        logoutButton.addClickListener((Button.ClickListener) event ->
                logoutPrompt()
        );
        buttonsLayout.addComponent(logoutButton);

        stackTraceTextArea = new TextArea();
        stackTraceTextArea.setSizeFull();
        stackTraceTextArea.setWordwrap(false);
        stackTraceTextArea.setValue(stackTrace);
        stackTraceTextArea.setStyleName(cubaLogContentClass);
        stackTraceTextArea.addStyleName(cubaCopyLogContentClass);
        stackTraceTextArea.setReadOnly(true);

        setContent(mainLayout);
        setResizable(false);

        if (ui.isTestMode()) {
            setCubaId("exceptionDialog");

            closeButton.setCubaId("closeButton");
            if (copyButton != null) {
                copyButton.setCubaId("copyStackTraceButton");
            }
            showStackTraceButton.setCubaId("showStackTraceButton");
            stackTraceTextArea.setCubaId("stackTraceTextArea");
            logoutButton.setCubaId("logoutButton");
        }

        if (ui.isPerformanceTestMode()) {
            setId(ui.getTestIdManager().getTestId("exceptionDialog"));
        }
    }

    protected String getStackTrace(Throwable throwable) {
        return ExceptionUtils.getStackTrace(throwable);
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
            if (copyButton != null) {
                copyButton.setVisible(true);
            }

            showStackTraceButton.setCaption(messages.getMainMessage("exceptionDialog.hideStackTrace"));

            mainLayout.addComponent(stackTraceTextArea);
            mainLayout.setExpandRatio(stackTraceTextArea, 1.0f);
            mainLayout.setHeight(100, Unit.PERCENTAGE);

            setWidth(theme.get("cuba.web.ExceptionDialog.expanded.width"));
            setHeight(theme.get("cuba.web.ExceptionDialog.expanded.height"));

            setResizable(true);
            center();
            stackTraceTextArea.focus();
            stackTraceTextArea.setCursorPosition(0);
        } else {
            if (copyButton != null) {
                copyButton.setVisible(false);
            }

            showStackTraceButton.setCaption(messages.getMainMessage("exceptionDialog.showStackTrace"));

            mainLayout.setHeight(-1, Unit.PIXELS);
            mainLayout.removeComponent(stackTraceTextArea);

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
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeSource.currentTimestamp());

            Map<String, Object> binding = new HashMap<>();
            binding.put("timestamp", date);
            binding.put("errorMessage", message);
            binding.put("stacktrace", stackTrace);
            binding.put("systemId", clientConfig.getSystemID());
            binding.put("userLogin", user.getLogin());

            if (MapUtils.isNotEmpty(additionalExceptionReportBinding)) {
                binding.putAll(additionalExceptionReportBinding);
            }

            reportService.sendExceptionReport(clientConfig.getSupportEmail(), MapUtils.unmodifiableMap(binding));

            Notification.show(messages.getMainMessage("exceptionDialog.emailSent"));
        } catch (Throwable e) {
            log.error("Error sending exception report", e);
            Notification.show(messages.getMainMessage("exceptionDialog.emailSendingErr"));
        }
    }

    protected void logoutPrompt() {
        App app = AppUI.getCurrent().getApp();
        final WebWindowManager wm = app.getWindowManager();
        wm.showOptionDialog(
                messages.getMainMessage("exceptionDialog.logoutCaption"),
                messages.getMainMessage("exceptionDialog.logoutMessage"),
                Frame.MessageType.WARNING,
                new Action[]{
                        new AbstractAction(messages.getMainMessage("closeApplication")) {
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

    public void setAdditionalExceptionReportBinding(Map<String, Object> binding) {
        additionalExceptionReportBinding = binding;
    }

    public Map<String, Object> getAdditionalExceptionReportBinding() {
        return additionalExceptionReportBinding;
    }
}