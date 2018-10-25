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

package com.haulmont.cuba.web.widgets.grid;

import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.Editor;

import java.util.EventObject;

/**
 * An event that is fired before a Grid editor is saved.
 *
 * @param <T> the bean type
 * @see CubaEditorBeforeSaveListener
 * @see CubaEditorImpl#addBeforeSaveListener(CubaEditorBeforeSaveListener)
 */
public class CubaEditorBeforeSaveEvent<T> extends EventObject {

    protected T bean;

    /**
     * Constructor for a editor save event.
     *
     * @param editor the source of the event
     * @param bean   the bean being edited
     */
    public CubaEditorBeforeSaveEvent(Editor<T> editor, T bean) {
        super(editor);
        this.bean = bean;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Editor<T> getSource() {
        return (Editor<T>) super.getSource();
    }

    /**
     * Gets the editor grid.
     *
     * @return the editor grid
     */
    public Grid<T> getGrid() {
        return getSource().getGrid();
    }

    /**
     * Gets the bean being edited.
     *
     * @return the bean being edited
     */
    public T getBean() {
        return bean;
    }
}
