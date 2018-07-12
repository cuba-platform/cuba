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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.datefield.CubaDateFieldState;
import com.vaadin.data.Result;
import com.vaadin.event.Action;
import com.vaadin.shared.ui.datefield.DateResolution;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CubaDateField extends com.vaadin.ui.DateField implements Action.Container {

    /**
     * Keeps track of the Actions added to this component, and manages the
     * painting and handling as well.
     */
//    protected ActionManager shortcutsManager;

    protected String dateString;

    public CubaDateField() {
        setStyleName("c-datefield");
    }

    @Override
    protected CubaDateFieldState getState() {
        return (CubaDateFieldState) super.getState();
    }

    @Override
    protected CubaDateFieldState getState(boolean markAsDirty) {
        return (CubaDateFieldState) super.getState(markAsDirty);
    }

    // VAADIN8: gg, do we need this method?
    /*@Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        lastInvalidDateString = (String) variables.get("lastInvalidDateString");
        dateString = (String) variables.get("dateString");
        super.changeVariables(source, variables);

        // Actions
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }*/


    @Override
    public void setDateFormat(String dateFormat) {
        super.setDateFormat(dateFormat);
        getState().dateMask = StringUtils.replaceChars(dateFormat, "dDMYy", "#####");
        markAsDirty();
    }

    @Override
    protected void updateInternal(String newDateString, Map<String, Integer> resolutions) {
        // CAUTION: copied from AbstractDateField
        Set<String> resolutionNames = getResolutions().map(Enum::name)
                .collect(Collectors.toSet());
        resolutionNames.retainAll(resolutions.keySet());
        if (!isReadOnly()
                && (!resolutionNames.isEmpty() || newDateString != null)) {

            // Old and new dates
            final LocalDate oldDate = getValue();

            LocalDate newDate;

            String mask = StringUtils.replaceChars(getState(false).dateMask, "#U", "__");
            if ("".equals(newDateString)
                    || mask.equals(newDateString)) {

                newDate = null;
            } else {
                newDate = reconstructDateFromFields(resolutions, oldDate);
            }

            boolean parseErrorWasSet = currentParseErrorMessage != null;
            boolean hasChanges = !Objects.equals(dateString, newDateString)
                    || !Objects.equals(oldDate, newDate)
                    || parseErrorWasSet;

            if (hasChanges) {
                dateString = newDateString;
                currentParseErrorMessage = null;
                if (newDateString == null || newDateString.isEmpty()) {
                    boolean valueChanged = setValue(newDate, true);
                    if(!valueChanged && parseErrorWasSet) {
                        doSetValue(newDate);
                    }
                } else {
                    // invalid date string
                    if (resolutions.isEmpty()) {
                        Result<LocalDate> parsedDate = handleUnparsableDateString(
                                dateString);
                        parsedDate.ifOk(v -> setValue(v, true));
                        if (parsedDate.isError()) {
                            dateString = null;
                            currentParseErrorMessage = parsedDate
                                    .getMessage().orElse("Parsing error");

                            if (!isDifferentValue(null)) {
                                doSetValue(null);
                            } else {
                                setValue(null, true);
                            }
                        }
                    } else {
                        setValue(newDate, true);
                    }
                }
            }
        }
    }

    @Override
    protected Result<LocalDate> handleUnparsableDateString(String dateString) {
        if (Objects.equals(dateString, StringUtils.replaceChars(getState(false).dateMask, "#U", "__"))) {
            return Result.ok(null);
        }

        return Result.error(getParseErrorMessage());
    }

    @Override
    public void setResolution(DateResolution resolution) {
        super.setResolution(resolution);
        // By default, only visual representation is updated after the resolution is changed.
        // As a result, the actual value and the visual representation are different values.
        // But we want to update the field value and fire value change event.
        if (getValue() != null) {
            setValue(reconstructDateFromFields(getState().resolutions, getValue()), true);
        }
    }

    // VAADIN8: gg, do we need this method?
    /*@Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (shortcutsManager != null) {
            shortcutsManager.paintActions(null, target);
        }
    }*/

    // VAADIN8: gg, do we need this method?
    /*@Override
    protected ActionManager getActionManager() {
        if (shortcutsManager == null) {
            shortcutsManager = new ActionManager(this);
        }
        return shortcutsManager;
    }*/

    // VAADIN8: gg, do we need this method?
    /*@Override
    public Registration addShortcutListener(ShortcutListener listener) {
        Objects.requireNonNull(listener, "Listener must not be null.");
        getActionManager().addAction(listener);
        return () -> getActionManager().removeAction(listener);
    }*/

    // VAADIN8: gg, remove?
    /*@Override
    public void removeShortcutListener(ShortcutListener listener) {
        getActionManager().removeAction(listener);
    }*/

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        getActionManager().removeActionHandler(actionHandler);
    }
}