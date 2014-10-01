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

import java.text.SimpleDateFormat;

/**
 * @author zlatoverov
 * @version $Id$
 */
public class JXErrorPaneHelper {

    private static ClientConfig clientConfig = AppBeans.<Configuration>get(Configuration.NAME).getConfig(ClientConfig.class);

    public static JXErrorPane getDefaultPane() {
        final JXErrorPane errorPane = new JXErrorPane();
        errorPane.setUI(new CustomErrorPaneUI());

        if (StringUtils.isNotBlank(clientConfig.getSupportEmail())) {
            errorPane.setErrorReporter(new ErrorReporter() {
                @Override
                public void reportError(ErrorInfo info) throws NullPointerException {
                    sendSupportEmail(info);
                    ((CustomErrorPaneUI) errorPane.getUI()).setEnabled(false);
                }
            });
        }

        return errorPane;
    }

    private static void sendSupportEmail(ErrorInfo jXErrorPaneInfo) {
        TopLevelFrame mainFrame = App.getInstance().getMainFrame();
        try {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeSource.currentTimestamp());

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

            mainFrame.showNotification(getMessage("errorPane.emailSent"), IFrame.NotificationType.TRAY);
        } catch (Throwable e) {
            mainFrame.showNotification(getMessage("errorPane.emailSendingErr"), IFrame.NotificationType.ERROR);
        }
    }

    private static String getMessage(String key) {
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMainMessage(key, App.getInstance().getLocale());
    }

    private static String getStackTrace(Throwable throwable) {
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
}
