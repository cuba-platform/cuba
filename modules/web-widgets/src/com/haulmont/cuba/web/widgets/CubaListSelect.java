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

import com.haulmont.cuba.web.widgets.compatibility.CubaValueChangeEvent;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.v7.ui.ListSelect;

import java.util.function.Function;

public class CubaListSelect extends ListSelect {

    protected Function<Object, String> itemCaptionGenerator;

    public CubaListSelect() {
        setValidationVisible(false);

        setShowBufferedSourceException(false);
        this.resetValueToNullOnContainerChange = false;
    }

    public Function<Object, String> getItemCaptionGenerator() {
        return itemCaptionGenerator;
    }

    public void setItemCaptionGenerator(Function<Object, String> itemCaptionGenerator) {
        this.itemCaptionGenerator = itemCaptionGenerator;
    }

    @Override
    public String getItemCaption(Object itemId) {
        if (itemCaptionGenerator != null) {
            return itemCaptionGenerator.apply(itemId);
        }
        return super.getItemCaption(itemId);
    }

    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        if (!isReadOnly() && isRequired() && isEmpty()) {
            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.v7.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }
        return superError;
    }

    @Override
    protected void fireValueChange(boolean repaintIsNotNeeded) {
        fireEvent(new CubaValueChangeEvent(this, repaintIsNotNeeded));
        if (!repaintIsNotNeeded) {
            markAsDirty();
        }
    }
}