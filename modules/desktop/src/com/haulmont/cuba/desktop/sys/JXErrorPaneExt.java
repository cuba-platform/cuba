/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.TopLevelFrame;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorReporter;
import org.jdesktop.swingx.plaf.basic.BasicErrorPaneUI;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author zlatoverov
 * @version $Id$
 */
public class JXErrorPaneExt extends JXErrorPane {

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

        setUI(new ErrorPaneUIExt());

        if (StringUtils.isNotBlank(clientConfig.getSupportEmail())) {
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
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        TopLevelFrame mainFrame = App.getInstance().getMainFrame();
        Messages messages = AppBeans.get(Messages.NAME);
        Locale locale = App.getInstance().getLocale();

        try {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeSource.currentTimestamp());

            //noinspection StringBufferReplaceableByString
            StringBuilder sb = new StringBuilder("<html><body>");
            sb.append("<p>").append(date).append("</p>");
            sb.append("<p>").append(jXErrorPaneInfo.getBasicErrorMessage().replace("\n", "<br/>")).append("</p>");
            sb.append("<p>").append(getStackTrace(jXErrorPaneInfo.getErrorException())).append("</p>");
            sb.append("</body></html>");

            UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
            User user = userSessionSource.getUserSession().getUser();
            EmailInfo info = new EmailInfo(
                    clientConfig.getSupportEmail(),
                    "[" + clientConfig.getSystemID() + "] [" + user.getLogin() + "] Exception Report",
                    sb.toString());

            if (user.getEmail() != null) {
                info.setFrom(user.getEmail());
            }

            EmailService emailService = AppBeans.get(EmailService.NAME);
            emailService.sendEmail(info);

            mainFrame.showNotification(messages.getMainMessage("errorPane.emailSent", locale),
                    IFrame.NotificationType.TRAY);
        } catch (Throwable e) {
            mainFrame.showNotification(messages.getMainMessage("errorPane.emailSendingErr", locale),
                    IFrame.NotificationType.ERROR);
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

        String html = StringEscapeUtils.escapeHtml(ExceptionUtils.getStackTrace(throwable));
        html = StringUtils.replace(html, "\n", "<br/>");
        html = StringUtils.replace(html, " ", "&nbsp;");
        html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

        return html;
    }

    public static class ErrorPaneUIExt extends BasicErrorPaneUI {

        public void setEnabled(boolean enabled) {
            if (reportButton != null) {
                reportButton.setEnabled(enabled);
            }
        }
    }
}