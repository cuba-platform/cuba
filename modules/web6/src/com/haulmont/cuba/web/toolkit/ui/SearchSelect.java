/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.gwt.client.ui.VSearchSelect;
import com.vaadin.ui.ClientWidget;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@ClientWidget(VSearchSelect.class)
public class SearchSelect extends FilterSelect {

    private FilterHandler filterHandler = null;
    private boolean repaintOptions = false;

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        this.repaintOptions = false;
        super.changeVariables(source, variables);
    }

    @Override
    protected void optionRepaint() {
        super.optionRepaint();
        if (!repaintOptions && currentPage < 0) {
            String aPrevFilter = this.prevfilterstring;
            String aFilter = this.filterstring;
            if (filterHandler != null && StringUtils.isNotEmpty(filterstring))
                filterHandler.onFilterChange(filterstring);
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
    public void setNewItemsAllowed(boolean allowNewOptions) {
        if (allowNewOptions)
            throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNewItemsAllowed() {
        return false;
    }

    public static interface FilterHandler {
        void onFilterChange(String newFilter);
    }

    public FilterHandler getFilterHandler() {
        return filterHandler;
    }

    public void setFilterHandler(FilterHandler filterHandler) {
        this.filterHandler = filterHandler;
    }
}