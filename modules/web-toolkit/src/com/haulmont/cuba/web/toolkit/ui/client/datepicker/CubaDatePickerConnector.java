package com.haulmont.cuba.web.toolkit.ui.client.datepicker;

import com.haulmont.cuba.web.toolkit.ui.CubaDatePicker;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.datefield.InlineDateFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaDatePicker.class)
public class CubaDatePickerConnector extends InlineDateFieldConnector {

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setTextualRangeStart(getState().textualRangeStart);
        getWidget().setTextualRangeEnd(getState().textualRangeEnd);
    }

    @Override
    public CubaDatePickerState getState() {
        return (CubaDatePickerState) super.getState();
    }

    @Override
    public CubaDateFieldCalendar getWidget() {
        return (CubaDateFieldCalendar) super.getWidget();
    }
}
