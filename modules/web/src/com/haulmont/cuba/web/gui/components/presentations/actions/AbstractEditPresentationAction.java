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

package com.haulmont.cuba.web.gui.components.presentations.actions;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.security.entity.Presentation;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.presentations.PresentationEditor;

import java.lang.reflect.Constructor;

/**
 */
public abstract class AbstractEditPresentationAction extends AbstractPresentationAction {

    protected Class<? extends PresentationEditor> editorClass;

    public AbstractEditPresentationAction(Table table, String id) {
        super(table, id);
    }

    protected void openEditor(Presentation presentation) {
        PresentationEditor window = createEditor(presentation);
        AppUI.getCurrent().addWindow(window);
        window.center();
    }

    protected PresentationEditor createEditor(Presentation presentation) {
        Class<? extends PresentationEditor> windowClass = getPresentationEditorClass();
        PresentationEditor window;
        try {
            Constructor<? extends PresentationEditor> windowConstructor = windowClass
                    .getConstructor(Presentation.class, Component.HasPresentations.class);
            window = windowConstructor.newInstance(presentation, table);
        } catch (Exception e) {
            throw new DevelopmentException("Invalid presentation's screen");
        }
        return window;
    }

    protected Class<? extends PresentationEditor> getPresentationEditorClass() {
        return editorClass == null ? PresentationEditor.class : editorClass;
    }

    public void setEditorClass(Class<? extends PresentationEditor> editorClass) {
        this.editorClass = editorClass;
    }
}
