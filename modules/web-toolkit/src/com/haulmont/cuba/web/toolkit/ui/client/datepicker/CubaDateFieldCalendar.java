package com.haulmont.cuba.web.toolkit.ui.client.datepicker;

import com.vaadin.client.Focusable;
import com.vaadin.client.ui.VCalendarPanel;
import com.vaadin.client.ui.VDateFieldCalendar;

public class CubaDateFieldCalendar extends VDateFieldCalendar implements Focusable {

    @Override
    protected VCalendarPanel createCalendarPanel() {
        return new CubaCalendarPanel();
    }

    public void setTextualRangeStart(String rangeStart) {
        ((CubaCalendarPanel) calendarPanel).setTextualRangeStart(rangeStart);
    }

    public void setTextualRangeEnd(String rangeEnd) {
        ((CubaCalendarPanel) calendarPanel).setTextualRangeEnd(rangeEnd);
    }

    @Override
    public void focus() {
        // implemented for ignoring error in console
    }
}
