/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.exception;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DialogWindow;
import com.haulmont.cuba.gui.AppConfig;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    @Override
    public boolean handle(Thread thread, Throwable exception) {
        final DialogWindow lastDialogWindow = App.getInstance().getMainFrame().getWindowManager().getLastDialogWindow();

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

    private String getMessage(String key) {
        return MessageProvider.getMessage(AppConfig.getMessagesPack(), key, App.getInstance().getLocale());
    }
}
