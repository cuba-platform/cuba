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
import com.haulmont.cuba.gui.components.Frame.NotificationType;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class LocalizedTaskWrapper<T, V> extends BackgroundTask<T, V> {

    private static final Logger log = LoggerFactory.getLogger(BackgroundWorker.class);

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
    public boolean handleException(Exception ex) {
        boolean handled = wrappedTask.handleException(ex);

        if (handled || wrappedTask.getOwnerFrame() == null) {
            window.close("", true);
        } else {
            window.closeAndRun("close", () ->
                    showExecutionError(ex)
            );

            log.error("Exception occurred in background task", ex);

            handled = true;
        }
        return handled;
    }

    @Override
    public boolean handleTimeoutException() {
        boolean handled = wrappedTask.handleTimeoutException();
        if (handled || wrappedTask.getOwnerFrame() == null) {
            window.close("", true);
        } else {
            window.closeAndRun("close", () -> {
                Messages messages = AppBeans.get(Messages.NAME);

                ((LegacyFrame) wrappedTask.getOwnerFrame()).showNotification(
                        messages.getMessage(LocalizedTaskWrapper.class, "backgroundWorkProgress.timeout"),
                        messages.getMessage(LocalizedTaskWrapper.class, "backgroundWorkProgress.timeoutMessage"),
                        NotificationType.WARNING);
            });
            handled = true;
        }
        return handled;
    }

    @Override
    public void done(V result) {
        window.close("", true);

        try {
            wrappedTask.done(result);
        } catch (Exception ex) {
            // we should show exception messages immediately
            showExecutionError(ex);
        }
    }

    @Override
    public void canceled() {
        try {
            wrappedTask.canceled();
        } catch (Exception ex) {
            // we should show exception messages immediately
            showExecutionError(ex);
        }
    }

    @Override
    public void progress(List<T> changes) {
        wrappedTask.progress(changes);
    }

    protected void showExecutionError(Exception ex) {
        Frame ownerFrame = wrappedTask.getOwnerFrame();
        if (ownerFrame != null) {
            window.getWindowManager().showExceptionDialog(
                    ex,
                    AppBeans.get(Messages.class).getMessage(LocalizedTaskWrapper.class, "backgroundWorkProgress.executionError"),
                    ex.getLocalizedMessage());
        }
    }
}