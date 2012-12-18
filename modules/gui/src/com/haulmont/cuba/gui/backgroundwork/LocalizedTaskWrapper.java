/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.backgroundwork;

import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class LocalizedTaskWrapper<T, V> extends BackgroundTask<T, V> {

    protected BackgroundTask<T, V> wrappedTask;
    protected Window window;

    protected LocalizedTaskWrapper(BackgroundTask<T, V> wrappedTask, Window window) {
        super(wrappedTask.getTimeoutSeconds(), window);
        this.wrappedTask = wrappedTask;
        this.window = window;
    }

    @Override
    public Map<String, Object> getParams() {
        return wrappedTask.getParams();
    }

    @Override
    public V run(TaskLifeCycle<T> lifeCycle) throws Exception {
        return wrappedTask.run(lifeCycle);
    }

    @Override
    public boolean handleException(final Exception ex) {
        boolean handled = wrappedTask.handleException(ex);
        if (!handled) {
            final Window ownerWindow = wrappedTask.getOwnerWindow();
            if (ownerWindow != null) {
                window.closeAndRun("close", new Runnable() {
                    @Override
                    public void run() {
                        String localizedMessage = ex.getLocalizedMessage();
                        if (StringUtils.isNotBlank(localizedMessage))
                            ownerWindow.showNotification(window.getMessage("backgroundWorkProgress.executionError"),
                                    localizedMessage, IFrame.NotificationType.WARNING);
                        else
                            ownerWindow.showNotification(window.getMessage("backgroundWorkProgress.executionError"),
                                    IFrame.NotificationType.WARNING);
                    }
                });
                handled = true;
            } else
                window.close("", true);
        } else
            window.close("", true);
        return handled;
    }

    @Override
    public boolean handleTimeoutException() {
        boolean handled = wrappedTask.handleTimeoutException();
        if (!handled) {
            final Window ownerWindow = wrappedTask.getOwnerWindow();
            if (ownerWindow != null) {
                window.closeAndRun("close", new Runnable() {
                    @Override
                    public void run() {
                        ownerWindow.showNotification(
                                window.getMessage("backgroundWorkProgress.timeout"),
                                window.getMessage("backgroundWorkProgress.timeoutMessage"),
                                IFrame.NotificationType.WARNING);
                    }
                });
                handled = true;
            } else
                window.close("", true);
        } else
            window.close("", true);
        return handled;
    }

    @Override
    public void done(V result) {
        window.close("", true);
        wrappedTask.done(result);
    }

    @Override
    public void canceled() {
        window.close("", true);
        wrappedTask.canceled();
    }
}