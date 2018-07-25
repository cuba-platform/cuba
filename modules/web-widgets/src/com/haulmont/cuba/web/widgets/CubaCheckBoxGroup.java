package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.checkboxgroup.CubaCheckBoxGroupState;
import com.vaadin.shared.ui.Orientation;
import com.vaadin.ui.CheckBoxGroup;

public class CubaCheckBoxGroup<T> extends CheckBoxGroup<T> {

    @Override
    protected CubaCheckBoxGroupState getState() {
        return ((CubaCheckBoxGroupState) super.getState());
    }

    @Override
    protected CubaCheckBoxGroupState getState(boolean markAsDirty) {
        return ((CubaCheckBoxGroupState) super.getState(markAsDirty));
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
