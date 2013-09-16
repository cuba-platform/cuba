/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.backgroundwork;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
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
    protected Messages messages = AppBeans.get(Messages.class);

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
            final IFrame ownerFrame = wrappedTask.getOwnerFrame();
            if (ownerFrame != null) {
                window.closeAndRun("close", new Runnable() {
                    @Override
                    public void run() {
                        String localizedMessage = ex.getLocalizedMessage();
                        if (StringUtils.isNotBlank(localizedMessage))
                            ownerFrame.showNotification(messages.getMessage(getClass(), "backgroundWorkProgress.executionError"),
                                    localizedMessage, IFrame.NotificationType.WARNING);
                        else
                            ownerFrame.showNotification(messages.getMessage(getClass(), "backgroundWorkProgress.executionError"),
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
            final IFrame ownerFrame = wrappedTask.getOwnerFrame();
            if (ownerFrame != null) {
                window.closeAndRun("close", new Runnable() {
                    @Override
                    public void run() {
                        ownerFrame.showNotification(
                                messages.getMessage(getClass(), "backgroundWorkProgress.timeout"),
                                messages.getMessage(getClass(), "backgroundWorkProgress.timeoutMessage"),
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