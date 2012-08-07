/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.backgroundwork;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Modal window wrapping around background task. Displays title, message and optional cancel button.
 * Window lasts until task completes, timeout timer elapses or user presses cancel button.
 * <p/>
 * When cancelled by user, does not interrupt task thread.
 * <p/>
 * <p>$Id$</p>
 *
 * @author budarov
 */
public class BackgroundWorkWindow<T, V> extends AbstractWindow {

    @Inject
    private Label text;

    @Inject
    private Button cancelButton;

    @Inject
    private BackgroundWorker backgroundWorker;

    private BackgroundTaskHandler<V> taskHandler;

    private static final Integer DEFAULT_TIMEOUT_SEC = 60;

    /**
     * Show modal window with message which will last until task completes.
     * Optionally cancel button can be displayed. By pressing cancel button user can cancel task execution.
     *
     * @param task          background task containing long operation
     * @param title         window title, optional
     * @param message       window message, optional
     * @param timeoutSec    timeout in seconds, after passing timeout task will be cancelled automatically
     * @param cancelAllowed show or not cancel button
     * @param <T>           task progress unit
     * @param <V>           task result type
     */
    public static <T, V> void show(BackgroundTask<T, V> task, @Nullable String title, @Nullable String message,
                                   int timeoutSec, boolean cancelAllowed) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("task", task);
        params.put("title", title);
        params.put("message", message);
        params.put("cancelAllowed", cancelAllowed);
        params.put("timeoutSec", timeoutSec);
        task.getOwnerWindow().openWindow("core$BackgroundWorkWindow", WindowManager.OpenType.DIALOG, params);
    }

    /**
     * Show modal window with message which will last until task completes.
     * Cancel button is not shown. Timeout set to 60 seconds.
     *
     * @param task    background task containing long operation
     * @param title   window title, optional
     * @param message window message, optional
     * @param <T>     task progress unit
     * @param <V>     task result type
     */
    public static <T, V> void show(BackgroundTask<T, V> task, String title, String message) {
        show(task, title, message, DEFAULT_TIMEOUT_SEC, false);
    }

    /**
     * Show modal window with default title and message which will last until task completes.
     * Cancel button is not shown. Timeout set to 60 seconds.
     *
     * @param task background task containing long operation
     * @param <T>  task progress unit
     * @param <V>  task result type
     */
    public static <T, V> void show(BackgroundTask<T, V> task) {
        show(task, null, null, DEFAULT_TIMEOUT_SEC, false);
    }

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(500);
        BackgroundTask<T, V> task = (BackgroundTask<T, V>) params.get("task");
        String title = (String) params.get("title");
        if (title != null) {
            setCaption(title);
        }

        String message = (String) params.get("message");
        if (message != null) {
            text.setValue(message);
        }

        Boolean cancelAllowed = (Boolean) params.get("cancelAllowed");
        if (BooleanUtils.isFalse(cancelAllowed)) {
            cancelButton.setVisible(false);
        }
        Integer timeoutSec = (Integer) params.get("timeoutSec");
        if (timeoutSec == null) {
            timeoutSec = DEFAULT_TIMEOUT_SEC;
        }

        addAction(new CancelAction());

        task.addProgressListener(new BackgroundTask.ProgressListenerAdapter<T, V>() {
            @Override
            public void onDone(V result) {
                closeBackgroundWindow();
                super.onDone(result);
            }

            @Override
            public void onCancel() {
                closeBackgroundWindow();
                super.onCancel();
            }
        });

        taskHandler = backgroundWorker.handle(task);
        taskHandler.execute(timeoutSec, TimeUnit.SECONDS);
    }

    private void closeBackgroundWindow() {
        close("", true);
    }

    private class CancelAction extends AbstractAction {
        private CancelAction() {
            super("cancel");
        }

        @Override
        public void actionPerform(Component component) {
            taskHandler.cancel();
        }
    }
}