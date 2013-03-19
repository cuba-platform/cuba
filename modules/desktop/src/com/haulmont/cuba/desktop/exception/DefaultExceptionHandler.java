/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DialogWindow;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        final DialogWindow lastDialogWindow = getLastDialogWindow();

        ErrorInfo ei = new ErrorInfo(
                getMessage("errorPane.title"), getMessage("errorPane.message"),
                null, null, exception, null, null);
        JXErrorPane errorPane = new JXErrorPane();
        errorPane.setErrorInfo(ei);
        JDialog dialog = JXErrorPane.createDialog(App.getInstance().getMainFrame(), errorPane);
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

    private DialogWindow getLastDialogWindow() {
        try {
            return App.getInstance().getMainFrame().getWindowManager().getLastDialogWindow();
        } catch (Exception e) {
            // this may happen in case of initialization error
            return null;
        }
    }

    private String getMessage(String key) {
        return AppBeans.get(Messages.class).getMainMessage(key, App.getInstance().getLocale());
    }
}
