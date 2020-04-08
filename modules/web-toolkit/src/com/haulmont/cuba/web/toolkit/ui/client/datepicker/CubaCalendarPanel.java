package com.haulmont.cuba.web.toolkit.ui.client.datepicker;

import com.vaadin.client.DateTimeService;
import com.vaadin.client.ui.VCalendarPanel;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CubaCalendarPanel extends VCalendarPanel {

    protected String rangeStart;
    protected String rangeEnd;

    protected Date parseTextualRangeDate(String rangeDate) throws NumberFormatException {
        if (rangeDate == null || rangeDate.isEmpty()) {
            return null;
        }

        int year = Integer.parseInt(rangeDate.substring(0, 4)) - 1900;
        int month = Integer.parseInt(rangeDate.substring(5, 7)) - 1;
        int day = Integer.parseInt(rangeDate.substring(8, 10));

        // add time part if exist or add 0
        int length = rangeDate.length();
        int hours = length < 13 ? 0 : Integer.parseInt(rangeDate.substring(11, 13));
        int minutes = length < 16 ? 0 : Integer.parseInt(rangeDate.substring(14, 16));
        int seconds = length < 19 ? 0 : Integer.parseInt(rangeDate.substring(17, 19));

        return new Date(year, month, day, hours, minutes, seconds);
    }

    @Override
    protected Date getConvertedRangeStart() {
        try {
            Date date = parseTextualRangeDate(rangeStart);
            return date == null ? super.getConvertedRangeStart() : date;
        } catch (NumberFormatException e) {
            Logger.getLogger("CubaDateFieldCalendar").log(Level.WARNING, "Can't parse date: " + rangeStart);
            return super.getConvertedRangeStart();
        }
    }

    @Override
    protected Date getConvertedRangeEnd() {
        try {
            Date date = parseTextualRangeDate(rangeEnd);
            return date == null ? super.getConvertedRangeStart() : date;
        } catch (NumberFormatException e) {
            Logger.getLogger("CubaDateFieldCalendar").log(Level.WARNING, "Can't parse date: " + rangeEnd);
            return super.getConvertedRangeEnd();
        }
    }

    public void setTextualRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public void setTextualRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    @Override
    protected boolean isSameDay(Date currdayDate, Date date) {
        return currdayDate != null && date != null
                && DateTimeService.isSameDay(currdayDate, date);
    }
}
