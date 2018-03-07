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

import com.haulmont.cuba.web.widgets.client.sourcecodeeditor.CubaSourceCodeEditorClientRpc;
import com.haulmont.cuba.web.widgets.client.sourcecodeeditor.CubaSourceCodeEditorState;
import com.vaadin.server.ErrorMessage;
import org.vaadin.aceeditor.AceEditor;

public class CubaSourceCodeEditor extends AceEditor {

    public CubaSourceCodeEditor() {
        String aceLocation = "VAADIN/resources/ace";

        setBasePath(aceLocation);
        setThemePath(aceLocation);
        setWorkerPath(aceLocation);
        setModePath(aceLocation);

        setUseWorker(false);

/*      vaadin8 implement
        setTextChangeEventMode(TextInputField.TextChangeEventMode.LAZY);
        setTextChangeTimeout(200);

        setValidationVisible(false);
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);
*/

        setHandleTabKey(false);
        setFontSize("auto");
    }

    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        /* vaadin8 reimplement
        if (!isReadOnly() && isRequiredIndicatorVisible() && isEmpty()) {
            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.v7.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }
        */

        return superError;
    }

    @Override
    protected CubaSourceCodeEditorState getState() {
        return (CubaSourceCodeEditorState) super.getState();
    }

    @Override
    protected CubaSourceCodeEditorState getState(boolean markAsDirty) {
        return (CubaSourceCodeEditorState) super.getState(markAsDirty);
    }

    public void setHandleTabKey(boolean handleTabKey) {
        if (isHandleTabKey() != handleTabKey) {
            getState().handleTabKey = handleTabKey;
        }
    }

    public boolean isHandleTabKey() {
        return getState(false).handleTabKey;
    }

    public int getPrintMarginColumn() {
        return getState(false).printMarginColumn;
    }

    public void setPrintMarginColumn(int printMarginColumn) {
        if (getPrintMarginColumn() != printMarginColumn) {
            getState().printMarginColumn = printMarginColumn;
        }
    }

    public void resetEditHistory() {
        getRpcProxy(CubaSourceCodeEditorClientRpc.class).resetEditHistory();
    }
}