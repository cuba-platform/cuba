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

package com.haulmont.cuba.desktop.sys;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.ExceptionReportService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorReporter;
import org.jdesktop.swingx.plaf.basic.BasicErrorPaneUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JXErrorPaneExt extends JXErrorPane {

    private static final Logger log = LoggerFactory.getLogger(JXErrorPaneExt.class);

    protected ActionListener copyToClipboardListener;

    protected Map<String, Object> additionalExceptionReportBinding = null;

    public JXErrorPaneExt() {

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        Messages messages = AppBeans.get(Messages.NAME);
        Locale locale = App.getInstance().getLocale();

        UIManager.put("JXErrorPane.details_expand_text",
                messages.getMainMessage("JXErrorPane.details_expand_text", locale));
        UIManager.put("JXErrorPane.details_contract_text",
                messages.getMainMessage("JXErrorPane.details_contract_text", locale));
        UIManager.put("JXErrorPane.ok_button_text",
                messages.getMainMessage("JXErrorPane.ok_button_text", locale));
        UIManager.put("JXErrorPane.fatal_button_text",
                messages.getMainMessage("JXErrorPane.fatal_button_text", locale));
        UIManager.put("JXErrorPane.report_button_text",
                messages.getMainMessage("JXErrorPane.report_button_text", locale));
        UIManager.put("JXErrorPane.copy_to_clipboard_button_text",
                messages.getMainMessage("JXErrorPane.copy_to_clipboard_button_text", locale));

        ErrorPaneUIExt ui = new ErrorPaneUIExt();
        setUI(ui);

        JButton copyButton = ui.getCopyToClipboardButton();
        copyToClipboardListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TopLevelFrame mainFrame = App.getInstance().getMainFrame();
                mainFrame.showNotification(messages.getMainMessage("errorPane.copingSuccessful", locale),
                        Frame.NotificationType.TRAY);
            }
        };
        copyButton.addActionListener(copyToClipboardListener);

        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
        Security security = AppBeans.get(Security.NAME);
        if (userSessionSource == null || !userSessionSource.checkCurrentUserSession()
                || !security.isSpecificPermitted("cuba.gui.showExceptionDetails")) {
            copyButton.setVisible(false);
        }

        String supportEmail = null;
        if (App.getInstance().getConnection().isConnected()) {
            supportEmail = clientConfig.getSupportEmail();
        }

        if (StringUtils.isNotBlank(supportEmail)) {
            setErrorReporter(new ErrorReporter() {
                @Override
                public void reportError(ErrorInfo info) throws NullPointerException {
                    sendSupportEmail(info);
                    ((ErrorPaneUIExt) getUI()).setEnabled(false);
                }
            });
        }
    }

    private void sendSupportEmail(ErrorInfo jXErrorPaneInfo) {

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ExceptionReportService reportService = AppBeans.get(ExceptionReportService.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        TopLevelFrame mainFrame = App.getInstance().getMainFrame();
        Messages messages = AppBeans.get(Messages.NAME);
        Locale locale = App.getInstance().getLocale();

        try {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeSource.currentTimestamp());

            UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
            User user = userSessionSource.getUserSession().getUser();

            Map<String, Object> binding = new HashMap<>();
            binding.put("timestamp", date);
            binding.put("errorMessage", jXErrorPaneInfo.getBasicErrorMessage());
            binding.put("stacktrace", getStackTrace(jXErrorPaneInfo.getErrorException()));
            binding.put("systemId", clientConfig.getSystemID());
            binding.put("userLogin", user.getLogin());

            if (MapUtils.isNotEmpty(additionalExceptionReportBinding)) {
                binding.putAll(additionalExceptionReportBinding);
            }

            reportService.sendExceptionReport(clientConfig.getSupportEmail(), ImmutableMap.copyOf(binding));

            mainFrame.showNotification(messages.getMainMessage("errorPane.emailSent", locale),
                    Frame.NotificationType.TRAY);
        } catch (Throwable e) {
            mainFrame.showNotification(messages.getMainMessage("errorPane.emailSendingErr", locale),
                    Frame.NotificationType.ERROR);
            log.error("Can't send error report", e);
        }
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

        return ExceptionUtils.getStackTrace(throwable);
    }

    public void setAdditionalExceptionReportBinding(Map<String, Object> binding) {
        additionalExceptionReportBinding = binding;
    }

    public Map<String, Object> getAdditionalExceptionReportBinding() {
        return additionalExceptionReportBinding;
    }

    public static class ErrorPaneUIExt extends BasicErrorPaneUI {

        public JButton getCopyToClipboardButton() {
            return copyToClipboardButton;
        }

        public void setEnabled(boolean enabled) {
            if (reportButton != null) {
                reportButton.setEnabled(enabled);
            }
        }

        @Override
        protected void uninstallComponents() {
            copyToClipboardButton.removeActionListener(copyToClipboardListener);
            super.uninstallComponents();
        }
    }
}