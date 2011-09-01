/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.utils;

import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.WatchDog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Desktop implementation of {@link BackgroundWorker}
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class DesktopBackgroundWorker implements BackgroundWorker {

    private Log log = LogFactory.getLog(DesktopBackgroundWorker.class);

    private WatchDog watchDog;

    public DesktopBackgroundWorker(WatchDog watchDog) {
        this.watchDog = watchDog;
    }

    @Override
    public <T, V> BackgroundTaskHandler<V> handle(BackgroundTask<T, V> task) {
        checkNotNull(task);
        checkNotNull(task.getOwnerWindow());

        // create task handler
        TaskExecutor<T, V> taskExecutor = new DesktopTaskExecutor<T, V>(task);

        return new TaskHandler<T, V>(taskExecutor, watchDog);
    }

    /**
     * Task runner
     */
    private class DesktopTaskExecutor<T, V> extends SwingWorker<V, T> implements TaskExecutor<T, V> {

        private BackgroundTask<T, V> runnableTask;
        private V result;

        private DesktopTaskExecutor(BackgroundTask<T, V> runnableTask) {
            this.runnableTask = runnableTask;
            runnableTask.setProgressHandler(this);
        }

        @Override
        protected V doInBackground() throws Exception {
            runnableTask.setInterrupted(false);
            try {
                result = runnableTask.run();
            } catch (Exception ex) {
                log.error(ex);
            }
            return result;
        }

        @Override
        protected void process(List<T> chunks) {
            runnableTask.progress(chunks);
        }

        @Override
        protected void done() {
            if (!runnableTask.isInterrupted())
                runnableTask.done(result);
        }

        @Override
        public void startExecution() {
            execute();
        }

        @Override
        public boolean cancelExecution(boolean mayInterruptIfRunning) {
            runnableTask.setInterrupted(true);

            UUID userId = UserSessionProvider.getUserSession().getId();
            log.debug("Cancel task. User: " + userId);

            if (!isDone() && !isCancelled()) {
                return cancel(mayInterruptIfRunning);
            } else
                return false;
        }

        @Override
        public V getResult() {
            V result;
            try {
                result = get();
            } catch (InterruptedException e) {
                return null;
            } catch (ExecutionException e) {
                return null;
            }
            return result;
        }

        @Override
        public BackgroundTask<T, V> getTask() {
            return runnableTask;
        }

        @Override
        public void handleProgress(T... changes) {
            publish(changes);
        }
    }
}