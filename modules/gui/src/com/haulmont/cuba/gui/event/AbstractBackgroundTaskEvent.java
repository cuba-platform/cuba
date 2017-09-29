/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.event;

import com.haulmont.cuba.gui.executors.BackgroundTask;
import org.springframework.context.ApplicationEvent;

public abstract class AbstractBackgroundTaskEvent extends ApplicationEvent {
    private BackgroundTask task;
    private boolean stopPropagation;

    public AbstractBackgroundTaskEvent(Object source, BackgroundTask task) {
        super(source);
        this.task = task;
    }

    public BackgroundTask getTask() {
        return task;
    }

    public boolean isStopPropagation() {
        return stopPropagation;
    }

    public void setStopPropagation(boolean stopPropagation) {
        this.stopPropagation = stopPropagation;
    }
}
