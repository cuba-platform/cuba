/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.presentations.actions;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.presentations.Presentations;
import com.haulmont.cuba.security.entity.Presentation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author shishov
 * @version $Id$
 */
public class PresentationActionsBuilder {

    public enum Type {
        SAVE,
        SAVE_AS,
        EDIT,
        DELETE,
        RESET
    }

    protected UserSessionSource userSessionSource;

    protected Table table;

    protected Collection actionTypes;

    public PresentationActionsBuilder(Table component) {
        table = component;
        userSessionSource = AppBeans.get(UserSessionSource.NAME);
    }

    public Collection<AbstractAction> build() {
        Collection<AbstractAction> actions = new ArrayList<>();
        for (Object type : getActionTypes()) {
            AbstractAction action = buildAction(type);
            if (action != null)
                actions.add(action);
        }
        return actions;
    }

    @Nullable
    public AbstractAction buildAction(@Nullable Object type) {
        if (type == null)
            return null;
        return buildActionByType(type);
    }

    public Collection getActionTypes() {
        if (actionTypes == null)
            actionTypes = Arrays.asList(Type.values());
        return actionTypes;
    }

    @SuppressWarnings("unused")
    public void setActionTypes(Collection actionTypes) {
        this.actionTypes = actionTypes;
    }

    @Nullable
    protected AbstractAction buildActionByType(@Nonnull Object type) {
        if (type instanceof Type) {
            switch ((Type) type) {
                case SAVE: return buildSaveAction();
                case SAVE_AS: return buildSaveAsAction();
                case EDIT: return buildEditAction();
                case DELETE: return buildDeleteAction();
                case RESET: return buildResetAction();
            }
        }
        return buildCustomAction(type);
    }

    @SuppressWarnings("unused")
    protected AbstractAction buildCustomAction(Object type) {
        return null;
    }

    protected AbstractAction buildSaveAction() {
        if (isGlobalPresentation())
            return new SavePresentationAction(table);
        return null;
    }

    protected AbstractAction buildSaveAsAction() {
        return new SaveAsPresentationAction(table);
    }

    protected AbstractAction buildEditAction() {
        if (isGlobalPresentation())
            return new EditPresentationAction(table);
        return null;
    }

    protected AbstractAction buildDeleteAction() {
        if (isGlobalPresentation())
            return new DeletePresentationAction(table);
        return null;
    }

    protected AbstractAction buildResetAction() {
        return new ResetPresentationAction(table);
    }

    protected boolean isGlobalPresentation() {
        Presentations presentations = table.getPresentations();
        Presentation presentation = presentations.getCurrent();
        return presentation != null && (!presentations.isGlobal(presentation) ||
                userSessionSource.getUserSession().isSpecificPermitted("cuba.gui.presentations.global"));
    }
}
