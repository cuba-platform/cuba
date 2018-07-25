package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.radiobuttongroup.CubaRadioButtonGroupState;
import com.vaadin.shared.ui.Orientation;
import com.vaadin.ui.RadioButtonGroup;

public class CubaRadioButtonGroup<T> extends RadioButtonGroup<T> {

    @Override
    protected CubaRadioButtonGroupState getState() {
        return ((CubaRadioButtonGroupState) super.getState());
    }

    @Override
    protected CubaRadioButtonGroupState getState(boolean markAsDirty) {
        return ((CubaRadioButtonGroupState) super.getState(markAsDirty));
    }

    public Orientation getOrientation() {
        return getState(false).orientation;
    }

    public void setOrientation(Orientation orientation) {
        if (orientation != getOrientation()) {
            getState().orientation = orientation;
        }
    }
}
