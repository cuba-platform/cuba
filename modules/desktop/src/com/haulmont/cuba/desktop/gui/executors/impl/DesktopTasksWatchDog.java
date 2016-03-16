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

package com.haulmont.cuba.desktop.gui.executors.impl;

import com.haulmont.cuba.gui.executors.impl.TaskHandlerImpl;
import com.haulmont.cuba.gui.executors.impl.TasksWatchDog;

import org.springframework.stereotype.Component;
import javax.swing.*;

/**
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