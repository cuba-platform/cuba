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
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Window that indicates progress of the background task, shows progress bar and processed items' message.
 * <p/>
 * Background task should have Integer as the progress measure unit. Progress measure passed to the publish() method
 * is displayed in processed items' message. Total number of items should be specified before task execution.
 * <p/>
 * <p>On error:
 * <ul>
 * <li>Executes handle exception in background task</li>
 * <li>Closes background window</li>
 * <li>Shows Warning message if for background task specified owner window</li>
 * </ul>
 * <p/>
 *
 * @author ovchinnikov
 * @version $Id$
 */
@SuppressWarnings("unused")
public class BackgroundWorkProgressWindow<V> extends AbstractWindow {
    private static final long serialVersionUID = -3073224246530486376L;

    @Inject
    private Label text;
    @Inject
    private Label progressText;
    @Inject
    private Button cancelButton;
    @Inject
    private BackgroundWorker backgroundWorker;
    @Inject
    private ProgressBar taskProgress;

    private BackgroundTaskHandler<V> taskHandler;

    /**
     * Show modal window with message which will last until task completes.
     * Optionally cancel button can be displayed. By pressing cancel button user can cancel task execution.
     *
     * @param task          background task containing long operation
     * @param title         window title, optional
     * @param message       window message, optional
     * @param total         total number of items, that will be processed
     * @param cancelAllowed show or not cancel button
     * @param <V>           task result type
     */
    public static <V> void show(BackgroundTask<Integer, V> task, @Nullable String title, @Nullable String message, Integer total,
                                boolean cancelAllowed) {
        Map<String, Object> params = new HashMap<>();
        params.put("task", task);
        params.put("title", title);
        params.put("message", message);
        params.put("total", total);
        params.put("cancelAllowed", cancelAllowed);
        task.getOwnerWindow().openWindow("core$BackgroundWorkProgressWindow", WindowManager.OpenType.DIALOG, params);
    }

    /**
     * Show modal window with message which will last until task completes.
     * Cancel button is not shown.
     *
     * @param task    background task containing long operation
     * @param title   window title, optional
     * @param message window message, optional
     * @param total   total number of items, that will be processed
     * @param <V>     task result type
     */
    public static <V> void show(BackgroundTask<Integer, V> task, String title, String message, Integer total) {
        show(task, title, message, total, false);
    }

    /**
     * Show modal window with default title and message which will last until task completes.
     * Cancel button is not shown.
     *
     * @param task          background task containing long operation
     * @param total         total number of items, that will be processed
     * @param cancelAllowed show or not cancel button
     * @param <V>           task result type
     */
    public static <V> void show(BackgroundTask<Integer, V> task, Integer total, boolean cancelAllowed) {
        show(task, null, null, total, cancelAllowed);
    }

    /**
     * Show modal window with default title and message which will last until task completes.
     * Cancel button is not shown.
     *
     * @param task  background task containing long operation
     * @param total total number of items, that will be processed
     * @param <V>   task result type
     */
    public static <V> void show(BackgroundTask<Integer, V> task, Integer total) {
        show(task, null, null, total, false);
    }

    @Override
    public void init(Map<String, Object> params) {
        getDialogParams().setWidth(500);
        final BackgroundTask<Integer, V> task = (BackgroundTask<Integer, V>) params.get("task");
        String title = (String) params.get("title");
        if (title != null) {
            setCaption(title);
        }

        String message = (String) params.get("message");
        if (message != null) {
            text.setValue(message);
        }

        Boolean cancelAllowedNullable = (Boolean) params.get("cancelAllowed");
        boolean cancelAllowed = BooleanUtils.isTrue(cancelAllowedNullable);
        cancelButton.setVisible(cancelAllowed);
        getDialogParams().setCloseable(cancelAllowed);

        final Integer total = (Integer) params.get("total");

        taskProgress.setValue(0);

        WrapperTask<V> wrapperTask = new WrapperTask<>(task, total);

        taskHandler = backgroundWorker.handle(wrapperTask);
        taskHandler.execute();

    }

    private void closeBackgroundWindow() {
        close("", true);
    }

    public void cancel() {
        if (taskHandler.isAlive())
            taskHandler.cancel();
        else
            close("close");
    }

    private class WrapperTask<V> extends BackgroundTask<Integer, V> {

        private BackgroundTask<Integer, V> wrappedTask;
        private Integer total;

        private WrapperTask(BackgroundTask<Integer, V> wrappedTask, Integer total) {
            super(wrappedTask.getTimeoutSeconds(), BackgroundWorkProgressWindow.this);
            this.wrappedTask = wrappedTask;
            this.total = total;
        }

        @Override
        public void progress(List<Integer> changes) {
            if (!changes.isEmpty()) {
                Integer last = changes.get(changes.size() - 1);
                taskProgress.setValue(last / (float) total);
                progressText.setValue(formatMessage("backgroundWorkProgress.progressTextFormat", last, total));
            }
        }

        @Override
        public void canceled() {
            closeBackgroundWindow();
            wrappedTask.canceled();
        }

        @Override
        public void done(V result) {
            closeBackgroundWindow();
            wrappedTask.done(result);
        }

        @Override
        public V run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {
            return wrappedTask.run(taskLifeCycle);
        }

        @Override
        public void handleException(final Exception ex) {
            wrappedTask.handleException(ex);
            final Window ownerWindow = wrappedTask.getOwnerWindow();
            if (ownerWindow != null)
                closeAndRun("close", new Runnable() {
                    @Override
                    public void run() {
                        String localizedMessage = ex.getLocalizedMessage();
                        if (StringUtils.isNotBlank(localizedMessage))
                            ownerWindow.showNotification(getMessage("backgroundWorkProgress.executionError"),
                                    localizedMessage, NotificationType.WARNING);
                        else
                            ownerWindow.showNotification(getMessage("backgroundWorkProgress.executionError"),
                                    NotificationType.WARNING);
                    }
                });
            else
                closeBackgroundWindow();
        }

        @Override
        public void timeoutExceeded() {
            wrappedTask.timeoutExceeded();
            final Window ownerWindow = wrappedTask.getOwnerWindow();
            if (ownerWindow != null)
                closeAndRun("close", new Runnable() {
                    @Override
                    public void run() {
                        ownerWindow.showNotification(
                                getMessage("backgroundWorkProgress.timeout"),
                                getMessage("backgroundWorkProgress.timeoutMessage"),
                                NotificationType.WARNING);
                    }
                });
            else
                closeBackgroundWindow();
        }
    }
}
