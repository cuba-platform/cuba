/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.optiongroup;

import com.haulmont.cuba.web.toolkit.ui.CubaOptionGroup;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.optiongroup.OptionGroupConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaOptionGroup.class)
public class CubaOptionGroupConnector extends OptionGroupConnector {

    public static final String HORIZONTAL_ORIENTAION_STYLE = "horizontal";

    @Override
    public CubaOptionGroupState getState() {
        return (CubaOptionGroupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("orientation")) {
            if (getState().orientation == OptionGroupOrientation.VERTICAL)
                getWidget().removeStyleDependentName("horizontal");
            else
                getWidget().addStyleDependentName(HORIZONTAL_ORIENTAION_STYLE);
        }
    }
}