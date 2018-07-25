package com.haulmont.cuba.web.widgets.client.checkboxgroup;

import com.haulmont.cuba.web.widgets.CubaCheckBoxGroup;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.optiongroup.CheckBoxGroupConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Orientation;

@Connect(CubaCheckBoxGroup.class)
public class CubaCheckBoxGroupConnector extends CheckBoxGroupConnector {

    public static final String HORIZONTAL_ORIENTATION_STYLE = "horizontal";

    @Override
    public CubaCheckBoxGroupState getState() {
        return (CubaCheckBoxGroupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("orientation")) {
            if (getState().orientation == Orientation.VERTICAL) {
                getWidget().removeStyleDependentName(HORIZONTAL_ORIENTATION_STYLE);
            } else {
                getWidget().addStyleDependentName(HORIZONTAL_ORIENTATION_STYLE);
            }
        }
    }
}
