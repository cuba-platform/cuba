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

import com.haulmont.cuba.web.toolkit.ui.client.textarea.CubaTextAreaState;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.TextArea;

import java.util.Objects;

public class CubaTextArea extends TextArea {
    public CubaTextArea() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);
    }

    @Override
    protected CubaTextAreaState getState() {
        return (CubaTextAreaState) super.getState();
    }

    @Override
    protected CubaTextAreaState getState(boolean markAsDirty) {
        return (CubaTextAreaState) super.getState(markAsDirty);
    }

    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        if (!isReadOnly() && isRequired() && isEmpty()) {
            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }
        return superError;
    }

    public CaseConversion getCaseConversion() {
        return CaseConversion.valueOf(getState(false).caseConversion);
    }

    public void setCaseConversion(CaseConversion caseConversion) {
        CaseConversion widgetCaseConversion = CaseConversion.valueOf(getState(false).caseConversion);
        if (!Objects.equals(caseConversion, widgetCaseConversion)) {
            getState(true).caseConversion = caseConversion.name();
        }
    }
}