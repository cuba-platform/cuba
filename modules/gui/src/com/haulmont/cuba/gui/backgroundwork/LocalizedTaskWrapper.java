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
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenContext;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class LocalizedTaskWrapper<T, V> extends BackgroundTask<T, V> {

    private static final Logger log = LoggerFactory.getLogger(BackgroundWorker.class);

    protected BackgroundTask<T, V> wrappedTask;
    protected Screen screen;

    protected LocalizedTaskWrapper(BackgroundTask<T, V> wrappedTask, Screen screen) {
        super(wrappedTask.getTimeoutSeconds(), screen);
        this.wrappedTask = wrappedTask;
        this.screen = screen;
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

        if (handled || wrappedTask.getOwnerScreen() == null) {
            Screens screens = getScreenContext().getScreens();
            screens.remove(screen);
        } else {
            Screens screens = getScreenContext().getScreens();
            screens.remove(screen);

            showExecutionError(ex);

            log.error("Exception occurred in background task", ex);

            handled = true;
        }
        return handled;
    }

    @Override
    public boolean handleTimeoutException() {
        boolean handled = wrappedTask.handleTimeoutException();
        if (handled || wrappedTask.getOwnerScreen() == null) {
            Screens screens = getScreenContext().getScreens();
            screens.remove(screen);
        } else {
            Screens screens = getScreenContext().getScreens();
            screens.remove(screen);

            Notifications notifications = getScreenContext().getNotifications();
            Messages messages = AppBeans.get(Messages.NAME);

            notifications.create(Notifications.NotificationType.WARNING)
                    .withCaption(messages.getMessage(LocalizedTaskWrapper.class, "backgroundWorkProgress.timeout"))
                    .withDescription(messages.getMessage(LocalizedTaskWrapper.class, "backgroundWorkProgress.timeoutMessage"))
                    .show();

            handled = true;
        }
        return handled;
    }

    @Override
    public void done(V result) {
        Screens screens = getScreenContext().getScreens();
        screens.remove(screen);

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

    protected ScreenContext getScreenContext() {
        return UiControllerUtils.getScreenContext(screen);
    }

    protected void showExecutionError(Exception ex) {
        Screen ownerFrame = wrappedTask.getOwnerScreen();
        if (ownerFrame != null) {
            Dialogs dialogs = getScreenContext().getDialogs();

            Messages messages = AppBeans.get(Messages.class);

            dialogs.createExceptionDialog()
                    .withThrowable(ex)
                    .withCaption(messages.getMessage(LocalizedTaskWrapper.class, "backgroundWorkProgress.executionError"))
                    .withMessage(ex.getLocalizedMessage())
                    .show();
        }
    }
}