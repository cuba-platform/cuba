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
package com.haulmont.cuba.gui.components;

/**
 * Progress bar is a component that visually displays the progress of some task.
 * <br/>
 * Component accepts float values from 0.0f to 1.0f. 0 means no progress, 1.0 - full progress.
 * <br/>
 * To indicate that a task of unknown length is executing, you can put a progress bar into indeterminate mode.
 *
 */
public interface ProgressBar extends Component.HasValue {
    String NAME = "progressBar";

    boolean isIndeterminate();
    void setIndeterminate(boolean indeterminate);
}
