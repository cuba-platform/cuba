/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.backgroundtasks;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundTaskHandler;
import com.haulmont.cuba.gui.executors.BackgroundWorker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * BackgroundWorker Test
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class BackgroundTasksWindow extends AbstractWindow {

    protected BackgroundWorker backgroundWorker;
    protected BackgroundTaskHandler taskHandler;

    protected Button startBtn;
    protected Button stopBtn;
    protected Label statusLabel;

    public BackgroundTasksWindow(IFrame frame) {
        super(frame);
    }

    @Override
    protected void init(Map<String, Object> params) {
        super.init(params);

        backgroundWorker = AppConfig.getBackgroundWorker();

        final BackgroundTask<Integer> progressIndicator = new BackgroundTask<Integer>(this) {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException ignored) {
                    }
                    publish((Integer) i);
                }
            }

            @Override
            public void canceled() {
                statusLabel.setValue("Canceled");

                startBtn.setEnabled(true);
                stopBtn.setEnabled(false);

                taskHandler = null;
            }

            @Override
            public void done() {
                statusLabel.setValue("Done");

                startBtn.setEnabled(true);
                stopBtn.setEnabled(false);

                taskHandler = null;
            }

            @Override
            public void progress(List<Integer> changes) {
                int size = changes.size();
                if (size >= 0)
                    statusLabel.setValue(String.valueOf(changes.get(size - 1)));
            }
        };

        startBtn = getComponent("startTaskBtn");
        stopBtn = getComponent("stopTaskBtn");
        statusLabel = getComponent("statusLbl");

        startBtn.setAction(new AbstractAction("Start") {
            @Override
            public void actionPerform(Component component) {
                if (taskHandler == null) {
                    statusLabel.setValue("Started");
                    taskHandler = backgroundWorker.handle(progressIndicator);

                    startBtn.setEnabled(false);
                    stopBtn.setEnabled(true);

                    taskHandler.execute(5, TimeUnit.SECONDS);
                }
            }
        });

        stopBtn.setAction(new AbstractAction("Stop") {
            @Override
            public void actionPerform(Component component) {
                if (taskHandler != null) {
                    taskHandler.cancel(true);
                }
            }
        });
    }
}
