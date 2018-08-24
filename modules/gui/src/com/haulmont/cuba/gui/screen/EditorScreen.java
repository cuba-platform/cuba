/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.Datasource;

import javax.annotation.Nullable;

/**
 * JavaDoc
 *
 * @param <T>
 */
public interface EditorScreen<T extends Entity> {
    /**
     * Name of action that commits changes.
     * <br> If the screen doesn't contain a component with {@link #WINDOW_COMMIT_AND_CLOSE} ID, this action also
     * closes the screen after commit.
     */
    String WINDOW_COMMIT = "windowCommit";

    /**
     * Name of action that commits changes and closes the screen.
     */
    String WINDOW_COMMIT_AND_CLOSE = "windowCommitAndClose";

    /**
     * Name of action that closes the screen.
     */
    String WINDOW_CLOSE = "windowClose";

    /**
     * JavaDoc
     *
     * @param entity
     */
    void setEntityToEdit(T entity);

    /**
     * JavaDoc
     *
     * @return
     */
    T getEditedEntity();

    /**
     * @return true if the edited item has been pessimistically locked when the screen is opened
     */
    boolean isLocked();
}