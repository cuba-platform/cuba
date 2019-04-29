package com.haulmont.cuba.web.toolkit.ui.client.datepicker;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.datefield.InlineDateFieldState;

public class CubaDatePickerState  extends InlineDateFieldState {

    @NoLayout
    public String textualRangeStart;

    @NoLayout
    public String textualRangeEnd;
}
