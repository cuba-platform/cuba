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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.datefield.CubaDateFieldState;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.*;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 */
public class CubaDateField extends com.vaadin.ui.DateField implements Action.Container {

    /**
     * Keeps track of the Actions added to this component, and manages the
     * painting and handling as well.
     */
    protected ActionManager shortcutsManager;

    protected String lastInvalidDateString;

    protected String dateString;

    private final Date MARKER_DATE = new Date(0);

    protected Date prevValue;

    @Override
    protected CubaDateFieldState getState() {
        return (CubaDateFieldState) super.getState();
    }

    @Override
    protected CubaDateFieldState getState(boolean markAsDirty) {
        return (CubaDateFieldState) super.getState(markAsDirty);
    }

    @Override
    protected void setValue(Date newValue, boolean repaintIsNotNeeded) throws Converter.ConversionException {
        if (newValue == MARKER_DATE)
            super.setValue(prevValue, true);
        else {
            prevValue = newValue;
            super.setValue(newValue, repaintIsNotNeeded);
        }
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        lastInvalidDateString = (String) variables.get("lastInvalidDateString");
        dateString = (String) variables.get("dateString");
        super.changeVariables(source, variables);

        // Actions
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }

    @Override
    public void setDateFormat(String dateFormat) {
        super.setDateFormat(dateFormat);
        getState().dateMask = StringUtils.replaceChars(dateFormat, "dDMYy", "#####");
        markAsDirty();
    }

    @Override
    protected Date handleUnparsableDateString(String dateString) throws Converter.ConversionException {
        if (ObjectUtils.equals(dateString, StringUtils.replaceChars(getState(false).dateMask, "#U", "__"))) {
            return null;
        }

        markAsDirty();
        return MARKER_DATE;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (shortcutsManager != null) {
            shortcutsManager.paintActions(null, target);
        }
    }

    @Override
    protected ActionManager getActionManager() {
        if (shortcutsManager == null) {
            shortcutsManager = new ActionManager(this);
        }
        return shortcutsManager;
    }

    @Override
    public void addShortcutListener(ShortcutListener listener) {
        getActionManager().addAction(listener);
    }

    @Override
    public void removeShortcutListener(ShortcutListener listener) {
        getActionManager().removeAction(listener);
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        getActionManager().removeActionHandler(actionHandler);
    }
}