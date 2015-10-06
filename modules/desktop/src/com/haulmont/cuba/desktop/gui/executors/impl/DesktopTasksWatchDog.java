/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.executors.impl;

import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import com.haulmont.cuba.gui.executors.impl.TasksWatchDog;

import org.springframework.stereotype.Component;
import javax.swing.*;

/**
 * @author artamonov
 * @version $Id$
 */
@Component(TasksWatchDog.NAME)
public class DesktopTasksWatchDog extends TasksWatchDog {

    @Override
    protected synchronized boolean checkHangup(long actualTimeMs, final TaskHandlerImpl taskHandler) {

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