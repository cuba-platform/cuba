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
import com.vaadin.v7.shared.ui.combobox.FilteringMode;

import java.util.List;
import java.util.Map;

public class CubaSearchSelect extends CubaComboBox {

    protected FilterHandler filterHandler = null;
    protected boolean repaintOptions = false;

    public CubaSearchSelect() {
        super.setFilteringMode(FilteringMode.OFF);

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
        this.repaintOptions = false;
        super.changeVariables(source, variables);
    }

    @Override
    protected void requestRepaintOptions(String caseSensitiveFilter) {
        if (!repaintOptions && currentPage < 0) {
            String aPrevFilter = this.prevfilterstring;
            String aFilter = this.filterstring;

            if (filterHandler != null) {
                filterHandler.onFilterChange(caseSensitiveFilter);
            }

            this.repaintOptions = true;
            this.currentPage = 0;
            this.prevfilterstring = aPrevFilter;
            this.filterstring = aFilter;
        }
    }

    @Override
    protected List<?> sanitizeList(List<?> options, boolean needNullSelectOption) {
        // not needed to show null value in list
        return super.sanitizeList(options, false);
    }

    @Override
    protected boolean isNullOptionVisible(boolean needNullSelectOption, boolean nullFilteredOut) {
        return false;
    }

    @Override
    public void setFilteringMode(FilteringMode filteringMode) {
        // ignore filter mode change
    }

    @Override
    public boolean isNewItemsAllowed() {
        return false;
    }

    @Override
    public void setNewItemsAllowed(boolean allowNewOptions) {
        if (allowNewOptions) {
            throw new UnsupportedOperationException();
        }
    }

    public FilterHandler getFilterHandler() {
        return filterHandler;
    }

    public void setFilterHandler(FilterHandler filterHandler) {
        this.filterHandler = filterHandler;
    }

    public interface FilterHandler {
        void onFilterChange(String newFilter);
    }
}