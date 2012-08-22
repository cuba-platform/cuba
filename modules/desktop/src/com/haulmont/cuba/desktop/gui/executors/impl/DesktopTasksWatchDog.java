/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.executors.impl;

import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import com.haulmont.cuba.gui.executors.impl.TasksWatchDog;

import javax.swing.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopTasksWatchDog extends TasksWatchDog {

    @Override
    protected boolean checkHangup(long actualTimeMs, final TaskHandlerImpl taskHandler) {

        long timeout = taskHandler.getTimeoutMs();

        if (timeout > 0 && (actualTimeMs - taskHandler.getStartTimeStamp()) > timeout) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    taskHandler.timeoutExceeded();
                }
            });
        }

        // do not kill tasks on desktop, just cancel with timeoutExceeded event
        return false;
    }
}