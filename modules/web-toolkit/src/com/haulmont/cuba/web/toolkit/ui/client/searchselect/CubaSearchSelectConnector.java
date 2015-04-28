/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.searchselect;

import com.haulmont.cuba.web.toolkit.ui.CubaSearchSelect;
import com.vaadin.client.ui.combobox.ComboBoxConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaSearchSelect.class)
public class CubaSearchSelectConnector extends ComboBoxConnector {

    @Override
    public CubaSearchSelectState getState() {
        return (CubaSearchSelectState) super.getState();
    }

    @Override
    public CubaSearchSelectWidget getWidget() {
        return (CubaSearchSelectWidget) super.getWidget();
    }

    @Override
    protected void performSelection(String selectedKey) {
        super.performSelection(selectedKey);

        getWidget().updateEditState();
    }

    @Override
    protected void resetSelection() {
        if (getWidget().nullSelectionAllowed) {
            getWidget().currentSuggestion = null;
        }

        super.resetSelection();

        getWidget().updateEditState();
    }
}