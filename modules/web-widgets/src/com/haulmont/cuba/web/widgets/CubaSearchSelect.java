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

import com.haulmont.cuba.web.widgets.client.searchselect.CubaSearchSelectState;

import java.util.Map;
import java.util.function.Consumer;

public class CubaSearchSelect<V> extends CComboBox<V> {

    protected Consumer<String> filterHandler = null;

    public CubaSearchSelect() {
        setStyleName("c-searchselect");
    }

    @Override
    protected CubaSearchSelectState getState() {
        return (CubaSearchSelectState) super.getState();
    }

    @Override
    protected CubaSearchSelectState getState(boolean markAsDirty) {
        return (CubaSearchSelectState) super.getState(markAsDirty);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
    }

    @Override
    protected void filterChanged(String filter) {
        if (filterHandler != null) {
            filterHandler.accept(filter);
        }
    }

    @Override
    public boolean isTextInputAllowed() {
        return false;
    }

    @Override
    public NewItemProvider<V> getNewItemProvider() {
        return null;
    }

    @Override
    public void setNewItemProvider(NewItemProvider<V> newItemProvider) {
        if (newItemProvider != null) {
            throw new UnsupportedOperationException();
        }
    }

    public void setFilterHandler(Consumer<String> filterHandler) {
        this.filterHandler = filterHandler;
    }
}