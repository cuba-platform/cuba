/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.RemoteException;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DialogWindow;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.config.WindowConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        JXErrorPane errorPane = new JXErrorPane();
        errorPane.setErrorInfo(createErrorInfo(exception));
        JDialog dialog = JXErrorPane.createDialog(App.getInstance().getMainFrame(), errorPane);
        dialog.setMinimumSize(new Dimension(600, (int) dialog.getMinimumSize().getHeight()));

        final DialogWindow lastDialogWindow = getLastDialogWindow();
        dialog.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        if (lastDialogWindow != null)
                            lastDialogWindow.enableWindow();
                        else
                            App.getInstance().getMainFrame().activate();
                    }
                }
        );
        dialog.setModal(false);

        if (lastDialogWindow != null)
            lastDialogWindow.disableWindow(null);
        else
            App.getInstance().getMainFrame().deactivate(null);

        dialog.setVisible(true);
        return true;
    }

    protected ErrorInfo createErrorInfo(Throwable exception) {
        Throwable rootCause = ExceptionUtils.getRootCause(exception);
        if (rootCause == null)
            rootCause = exception;

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

        return new ErrorInfo(
                getMessage("errorPane.title"), msg.toString(),
                null, null, rootCause, null, null);
    }

    private DialogWindow getLastDialogWindow() {
        try {
            return App.getInstance().getMainFrame().getWindowManager().getLastDialogWindow();
        } catch (Exception e) {
            // this may happen in case of initialization error
            return null;
        }
    }

    protected String getMessage(String key) {
        Messages messages = AppBeans.get(Messages.NAME);
        return messages.getMainMessage(key, App.getInstance().getLocale());
    }
}
