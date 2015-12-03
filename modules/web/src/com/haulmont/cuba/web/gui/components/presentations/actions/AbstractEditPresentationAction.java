/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author shishov
 * @version $Id$
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
