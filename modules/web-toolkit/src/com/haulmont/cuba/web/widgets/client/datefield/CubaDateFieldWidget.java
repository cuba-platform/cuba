/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.widgets.client.datefield;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.client.textfield.CubaMaskedFieldWidget;
import com.vaadin.client.DateTimeService;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VPopupCalendar;
import com.vaadin.shared.ui.datefield.DateResolution;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.Date;

public class CubaDateFieldWidget extends VPopupCalendar implements ShortcutActionHandler.ShortcutActionHandlerOwner {

    protected ShortcutActionHandler shortcutHandler;

    protected static final String EMPTY_FIELD_CLASS = "c-datefield-empty";

    protected static final String RANGE_DATE_FORMAT = "yyyy-MM-dd";
    protected static final String EMPTY_MASK = "__";

    protected int tabIndex;

    protected boolean autofill;

    protected Date rangeStart;
    protected Date rangeEnd;

    public CubaDateFieldWidget() {
        // handle shortcuts
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
    }

    @Override
    public void setTextFieldEnabled(boolean textFieldEnabled) {
        super.setTextFieldEnabled(textFieldEnabled);

        calendarToggle.getElement().setTabIndex(-1);
    }

    @Override
    public void updateValue(Date newDate) {
        DateResolution currentResolution = getCurrentResolution();
        if (getDate() == null
                && currentResolution != null
                && currentResolution.compareTo(DateResolution.DAY) > 0) {
            Date date = newDate;
            // Collects a map of current date values depending on date resolution
            Map<DateResolution, Integer> dateValues = getResolutions()
                    .collect(Collectors.toMap(Function.identity(),
                            res -> currentResolution.compareTo(res) <= 0
                                    ? res == DateResolution.MONTH
                                        ? date.getMonth() + 1
                                        : date.getYear() + 1900
                                    : null));
            if (!dateValues.isEmpty()) {
                newDate = makeDate(dateValues);
                calendar.setDate(newDate);
            }
        }
        super.updateValue(newDate);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        // Always set -1 tab index for calendarToggle
        calendarToggle.setTabIndex(-1);
    }

    @Override
    protected void buildDate(boolean forceValid) {
        super.buildDate(forceValid);
        // Update valueBeforeEdit and send onChange
        // in case of selecting date using Calendar popup
        getImpl().valueChange(false);
    }

    @Override
    public void buildDate() {
        super.buildDate();

        updateTextState();
    }

    @Override
    public void setReadonly(boolean readonly) {
        super.setReadonly(readonly);

        getImpl().setTabIndex(readonly ? -1 : tabIndex);
    }

    public void setAutofill(boolean autofill) {
        this.autofill = autofill;
    }

    public boolean isAutofill() {
        return autofill;
    }

    public void setDateRangeStart(String rangeStart) {
        this.rangeStart = parseRangeString(rangeStart);
    }

    public Date getDateRangeStart() {
        return rangeStart;
    }

    public void setDateRangeEnd(String rangeEnd) {
        this.rangeEnd = parseRangeString(rangeEnd);
    }

    public Date getDateRangeEnd() {
        return rangeEnd;
    }

    protected void updateTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public CubaMaskedFieldWidget getImpl() {
        return (CubaMaskedFieldWidget) super.getImpl();
    }

    @Override
    protected CubaMaskedFieldWidget createImpl() {

        return new CubaMaskedFieldWidget() {

            @Override
            protected boolean validateText(String text) {
                if (text.equals(nullRepresentation)) {
                    return true;
                }

                if (!super.validateText(text)) {
                    return false;
                }

                try {
                    getDateTimeService().parseDate(getText(), getFormatString(), lenient);
                } catch (Exception e) {
                    return false;
                }

                return true;
            }

            @Override
            public void valueChange(boolean blurred) {
                String newText = getText();
                if (newText != null
                        && !newText.equals(valueBeforeEdit)) {
                    if (isAutofill()) {
                        String filledValue = fillValue(newText);
                        if (!newText.equals(filledValue)) {
                            newText = filledValue;
                            setText(newText);
                        }
                    }

                    if (validateText(newText)) {
                        if (!newText.equals(nullRepresentation)) {
                            getElement().removeClassName(CubaDateFieldWidget.EMPTY_FIELD_CLASS);
                        }
                        CubaDateFieldWidget.this.onChange(null);

                        valueBeforeEdit = newText;
                    } else {
                        setText(valueBeforeEdit);
                    }
                }
            }
        };
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);

        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        this.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    public void updateTextState() {
        getImpl().updateTextState();
    }

    protected Date parseRangeString(String dateStr) {
        if (dateStr == null || "".equals(dateStr)) {
            return null;
        }
        return getDateTimeService().parseDate(dateStr, RANGE_DATE_FORMAT, lenient);
    }

    protected String fillValue(String value) {
        if (!value.startsWith(EMPTY_MASK) && value.endsWith(EMPTY_MASK)) {
            Date date = new Date();
            String dateString = getDateTimeService().formatDate(date, getFormatString());

            StringBuilder stringBuilder = new StringBuilder(value);
            int index = stringBuilder.indexOf(EMPTY_MASK);
            while (index != -1) {
                stringBuilder.replace(index, index + 2, dateString.substring(index, index + 2));
                index = stringBuilder.indexOf(EMPTY_MASK);
            }
            String filledValue = stringBuilder.toString();
            try {
                filledValue = adjustStringValue(filledValue);
            } catch (IllegalArgumentException e) {
                return value;
            }

            return filledValue;
        }
        return value;
    }

    protected String adjustStringValue(String value) {
        DateTimeService service = getDateTimeService();

        Date date = service.parseDate(value, getFormatString(), lenient);
        Date adjustedDate = adjustDateByRange(date, getDateRangeStart(), getDateRangeEnd());

        return date.equals(adjustedDate)
                ? value
                : service.formatDate(adjustedDate, getFormatString());
    }

    protected Date adjustDateByRange(Date date, Date rangeStart, Date rangeEnd) {
        if (rangeStart != null && date.before(rangeStart)) {
            return rangeStart;
        }
        if (rangeEnd != null && date.after(rangeEnd)) {
            return rangeEnd;
        }
        return date;
    }
}