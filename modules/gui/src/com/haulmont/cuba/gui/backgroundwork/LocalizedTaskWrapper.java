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

package com.haulmont.cuba.gui.backgroundwork;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LocalizedTaskWrapper<T, V> extends BackgroundTask<T, V> {

    protected Logger log = LoggerFactory.getLogger(BackgroundWorker.class);

    protected BackgroundTask<T, V> wrappedTask;
    protected Window window;
    protected Messages messages = AppBeans.get(Messages.NAME);

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
            final Frame ownerFrame = wrappedTask.getOwnerFrame();
            if (ownerFrame != null) {
                window.closeAndRun("close", () -> showExecutionError(ex));

                log.error("Exception occurred in background task", ex);

                handled = true;
            } else {
                window.close("", true);
            }
        } else {
            window.close("", true);
        }
        return handled;
    }

    protected void showExecutionError(Exception ex) {
        final Frame ownerFrame = wrappedTask.getOwnerFrame();
        if (ownerFrame != null) {
            String localizedMessage = ex.getLocalizedMessage();
            if (StringUtils.isNotBlank(localizedMessage)) {
                ownerFrame.showNotification(messages.getMessage(getClass(), "backgroundWorkProgress.executionError"),
                        localizedMessage, Frame.NotificationType.WARNING);
            } else {
                ownerFrame.showNotification(messages.getMessage(getClass(), "backgroundWorkProgress.executionError"),
                        Frame.NotificationType.WARNING);
            }
        }
    }

    @Override
    public boolean handleTimeoutException() {
        boolean handled = wrappedTask.handleTimeoutException();
        if (!handled) {
            final Frame ownerFrame = wrappedTask.getOwnerFrame();
            if (ownerFrame != null) {
                window.closeAndRun("close", new Runnable() {
                    @Override
                    public void run() {
                        ownerFrame.showNotification(
                                messages.getMessage(getClass(), "backgroundWorkProgress.timeout"),
                                messages.getMessage(getClass(), "backgroundWorkProgress.timeoutMessage"),
                                Frame.NotificationType.WARNING);
                    }
                });
                handled = true;
            } else {
                window.close("", true);
            }
        } else {
            window.close("", true);
        }
        return handled;
    }

    @Override
    public void done(V result) {
        window.close("", true);

        try {
            // after window close we should show exception messages immediately
            wrappedTask.done(result);
        } catch (Exception ex) {
            showExecutionError(ex);
        }
    }

    @Override
    public void canceled() {
        window.close("", true);

        try {
            // after window close we should show exception messages immediately
            wrappedTask.canceled();
        } catch (Exception ex) {
            showExecutionError(ex);
        }
    }
}