package com.haulmont.cuba.web.widgets.client.radiobuttongroup;

import com.haulmont.cuba.web.widgets.CubaRadioButtonGroup;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.optiongroup.RadioButtonGroupConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Orientation;

@Connect(CubaRadioButtonGroup.class)
public class CubaRadioButtonGroupConnector extends RadioButtonGroupConnector {

    public static final String HORIZONTAL_ORIENTATION_STYLE = "horizontal";

    @Override
    public CubaRadioButtonGroupState getState() {
        return (CubaRadioButtonGroupState) super.getState();
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
