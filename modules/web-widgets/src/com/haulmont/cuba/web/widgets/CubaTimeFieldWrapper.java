/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.client.timefield.AmPm;
import com.haulmont.cuba.web.widgets.client.timefield.TimeMode;
import com.haulmont.cuba.web.widgets.client.timefield.TimeResolution;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.time.LocalTime;

/**
 * Composite component that combines {@link CubaTimeField} and AM / PM combobox.
 */
public class CubaTimeFieldWrapper extends CustomField<LocalTime> {

    public static final String TIME_FIELD_STYLENAME = "c-timefield-layout";
    public static final String AM_PM_FIELD_STYLE_NAME = "c-timefield-ampm";

    protected CubaCssActionsLayout container;
    protected CubaTimeField timeField;
    protected CubaComboBox<AmPm> amPmField;

    protected LocalTime internalValue;

    protected TimeMode timeMode = TimeMode.H_24;

    public CubaTimeFieldWrapper() {
        init();
        initTimeField();
        initAmPmField();
        initLayout();

        timeField.addValueChangeListener(this::componentValueChanged);
        amPmField.addValueChangeListener(this::amPmFieldValueChanged);
    }

    @Override
    public LocalTime getValue() {
        return internalValue;
    }

    public void setTimeFormat(String format) {
        timeField.setTimeFormat(format);
    }

    public String getTimeFormat() {
        return timeField.getTimeFormat();
    }

    public void setResolution(TimeResolution resolution) {
        timeField.setResolution(resolution);
    }

    public TimeResolution getResolution() {
        return timeField.getResolution();
    }

    public TimeMode getTimeMode() {
        return timeMode;
    }

    public void setTimeMode(TimeMode timeMode) {
        TimeMode oldMode = this.timeMode;

        this.timeMode = timeMode;

        if (oldMode != timeMode) {
            if (timeMode == TimeMode.H_12) {
                container.addComponent(amPmField);
            } else {
                container.removeComponent(amPmField);
            }

            LocalTime value = getValue();
            if (value != null) {
                setValueToPresentation(convertToPresentation(value));
            }
        }
    }

    public boolean isCaptionManagedByLayout() {
        return timeField.isCaptionManagedByLayout();
    }

    public void setCaptionManagedByLayout(boolean captionManagedByLayout) {
        timeField.setCaptionManagedByLayout(captionManagedByLayout);
    }

    @Override
    protected Component initContent() {
        return container;
    }

    @Override
    protected void doSetValue(LocalTime value) {
        this.internalValue = value;

        setValueToPresentation(convertToPresentation(value));
    }

    protected void init() {
        container = new CubaCssActionsLayout();
        container.setPrimaryStyleName(TIME_FIELD_STYLENAME);

        setWidthUndefined();
    }

    protected void initTimeField() {
        timeField = new CubaTimeField();
    }

    protected void initAmPmField() {
        amPmField = new CubaComboBox<>();
        amPmField.addStyleName(AM_PM_FIELD_STYLE_NAME);

        amPmField.setItems(AmPm.values());
        amPmField.setValue(AmPm.AM);

        amPmField.setEmptySelectionAllowed(false);
        amPmField.setTextInputAllowed(false);
        amPmField.setWidthUndefined();
    }

    protected void initLayout() {
        container.addComponent(timeField);
    }

    protected void componentValueChanged(HasValue.ValueChangeEvent<LocalTime> event) {
        if (event.isUserOriginated()) {
            LocalTime oldValue = this.internalValue;

            this.internalValue = constructModelValue(event.getValue());

            setValueToPresentation(convertToPresentation(this.internalValue));

            HasValue.ValueChangeEvent<LocalTime> valueChangeEvent =
                    new ValueChangeEvent<>(this, oldValue, true);
            fireEvent(valueChangeEvent);
        }
    }

    protected void amPmFieldValueChanged(HasValue.ValueChangeEvent<AmPm> event) {
        if (event.isUserOriginated() && getValue() != null) {
            LocalTime oldValue = this.internalValue;
            this.internalValue = convertFrom12hFormat(
                    new AmPmLocalTime(oldValue, event.getValue()));

            ValueChangeEvent<LocalTime> valueChangeEvent =
                    new ValueChangeEvent<>(this, oldValue, true);
            fireEvent(valueChangeEvent);
        }
    }

    protected LocalTime constructModelValue(LocalTime value) {
        if (timeMode == TimeMode.H_24) {
            return value;
        }

        AmPmLocalTime time = convertTo12hFormat(value);
        AmPm amPm = time.getTime().getHour() == value.getHour()
                ? amPmField.getValue()
                : time.getAmPm();

        return convertFrom12hFormat(new AmPmLocalTime(time.getTime(), amPm));
    }

    protected void setValueToPresentation(AmPmLocalTime value) {
        if (value == null) {
            timeField.setValue(null);
            amPmField.setValue(AmPm.AM);
        } else {
            timeField.setValue(value.getTime());
            amPmField.setValue(value.getAmPm());
        }
    }

    protected LocalTime convertToModel(AmPmLocalTime presentationValue) {
        if (timeMode == TimeMode.H_24) {
            return presentationValue.getTime();
        }
        return convertFrom12hFormat(presentationValue);
    }

    protected AmPmLocalTime convertToPresentation(LocalTime modelValue) {
        if (timeMode == TimeMode.H_24) {
            return new AmPmLocalTime(modelValue, AmPm.AM);
        }
        return convertTo12hFormat(modelValue);
    }

    protected static LocalTime convertFrom12hFormat(AmPmLocalTime amPmTime) {
        int hour;
        int sourceHour = amPmTime.getTime().getHour();

        if (amPmTime.getAmPm() == AmPm.AM) {
            hour = sourceHour % 12;
        } else {
            hour = sourceHour + 12;
            if (hour == 24) {
                hour = 12;
            }
        }

        return amPmTime.getTime().withHour(hour);
    }

    protected static AmPmLocalTime convertTo12hFormat(LocalTime time) {
        int hour = time.getHour() == 0 || time.getHour() == 12
                ? 12 : time.getHour() % 12;
        AmPm amPm = time.getHour() < 12
                ? AmPm.AM : AmPm.PM;
        return new AmPmLocalTime(time.withHour(hour), amPm);
    }

    /**
     * Immutable POJO to store time in AM/PM format.
     */
    public static class AmPmLocalTime {

        protected final LocalTime time;
        protected final AmPm amPm;

        public AmPmLocalTime(LocalTime time, AmPm amPm) {
            this.time = time;
            this.amPm = amPm;
        }

        public LocalTime getTime() {
            return time;
        }

        public AmPm getAmPm() {
            return amPm;
        }
    }
}
