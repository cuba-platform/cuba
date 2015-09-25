/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.searchselect.CubaSearchSelectState;
import com.vaadin.shared.ui.combobox.FilteringMode;

import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaSearchSelect extends CubaComboBox {

    protected FilterHandler filterHandler = null;
    protected boolean repaintOptions = false;

    public CubaSearchSelect() {
        super.setFilteringMode(FilteringMode.OFF);
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
    protected List<?> sanitetizeList(List<?> options, boolean needNullSelectOption) {
        // not needed to show null value in list
        return super.sanitetizeList(options, false);
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