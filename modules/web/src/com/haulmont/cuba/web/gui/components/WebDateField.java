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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.DateTimeUtils;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.widgets.CubaDateField;
import com.haulmont.cuba.web.widgets.CubaTimeField;
import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.Layout;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Consumer;

public class WebDateField<V extends Date> extends WebAbstractViewComponent<Layout, LocalDateTime, V>
        implements DateField<V>, InitializingBean {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 2;
    protected List<Validator> validators; // lazily initialized list

    protected Resolution resolution;

    protected boolean updatingInstance;

    protected CubaDateField dateField;
    protected CubaTimeField timeField;

    protected String dateTimeFormat;
    // TODO: gg, why we have this?
    protected String dateFormat;
    protected String timeFormat;

    protected TimeZone timeZone;

    protected boolean editable = true;

    protected ThemeConstants theme;

    public WebDateField() {
        component = new com.vaadin.ui.CssLayout();
        component.setPrimaryStyleName("c-datefield-layout");

        if (App.isBound()) {
            theme = App.getInstance().getThemeConstants();
        }

        dateField = new CubaDateField();
        timeField = new CubaTimeField();

        setWidthAuto();

        dateField.addValueChangeListener(createDateValueChangeListener());
        timeField.addValueChangeListener(createTimeValueChangeListener());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserSessionSource userSessionSource = applicationContext.getBean(UserSessionSource.class);
        Locale locale = userSessionSource.getLocale();

        dateField.setDateFormat(Datatypes.getFormatStringsNN(locale).getDateFormat());
        dateField.setResolution(DateResolution.DAY);

        timeField.setTimeFormat(Datatypes.getFormatStringsNN(locale).getTimeFormat());

        setResolution(Resolution.MIN);
    }

    protected HasValue.ValueChangeListener<LocalDate> createDateValueChangeListener() {
        return event ->
                componentValueChanged(event.isUserOriginated());
    }

    protected HasValue.ValueChangeListener<LocalTime> createTimeValueChangeListener() {
        return event ->
                componentValueChanged(event.isUserOriginated());
    }

    protected void componentValueChanged(boolean isUserOriginated) {
        if (isUserOriginated) {
            V value;

            try {
                value = constructModelValue();

                if (!checkRange(value)) {
                    return;
                }

                LocalDateTime presentationValue = convertToPresentation(value);
                setValueToPresentation(presentationValue);
            } catch (ConversionException ce) {
                LoggerFactory.getLogger(getClass()).trace("Unable to convert presentation value to model", ce);

                setValidationError(ce.getLocalizedMessage());
                return;
            }

            V oldValue = internalValue;
            internalValue = value;

            if (!fieldValueEquals(value, oldValue)) {
                ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value); // todo isUserOriginated
                getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
            }
        }
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
        setResolutionInternal(resolution);
        updateLayout();
    }

    protected void setResolutionInternal(Resolution resolution) {
        dateField.setResolution(WebWrapperUtils.convertDateTimeResolution(resolution));

        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            timeField.setResolution(WebWrapperUtils.convertTimeResolution(resolution));
        } else {
            // Set time field value to zero in case of resolution without time.
            // If we don't set value to zero then after changing resolution back to
            // resolution with time, we will get some value in time field
            timeField.setValue(null);
        }
    }

    @Override
    public void setRangeStart(Date value) {
        dateField.setRangeStart(DateTimeUtils.asLocalDate(value));
    }

    @Override
    public Date getRangeStart() {
        return dateField.getRangeStart() != null ? DateTimeUtils.asDate(dateField.getRangeStart()) : null;
    }

    @Override
    public void setRangeEnd(Date value) {
        dateField.setRangeEnd(DateTimeUtils.asLocalDate(value));
    }

    @Override
    public Date getRangeEnd() {
        return dateField.getRangeEnd() != null ? DateTimeUtils.asDate(dateField.getRangeEnd()) : null;
    }

    protected boolean checkRange(Date value) {
        if (updatingInstance) {
            return true;
        }

        if (value != null) {
            Date rangeStart = getRangeStart();
            if (rangeStart != null && value.before(rangeStart)) {
                handleDateOutOfRange(value);
                return false;
            }

            Date rangeEnd = getRangeEnd();
            if (rangeEnd != null && value.after(rangeEnd)) {
                handleDateOutOfRange(value);
                return false;
            }
        }

        return true;
    }

    protected void handleDateOutOfRange(Date value) {
        if (getFrame() != null) {
            Messages messages = AppBeans.get(Messages.NAME);
            getFrame().showNotification(messages.getMainMessage("datePicker.dateOutOfRangeMessage"),
                    Frame.NotificationType.TRAY);
        }

        setValueToPresentation(DateTimeUtils.asLocalDateTime(internalValue));
    }

    @Override
    public String getDateFormat() {
        return dateTimeFormat;
    }

    @Override
    public void setDateFormat(String dateFormat) {
        Preconditions.checkNotNullArgument(dateFormat);

        dateTimeFormat = dateFormat;

        StringBuilder date = new StringBuilder(dateFormat);
        StringBuilder time = new StringBuilder(dateFormat);
        int timeStartPos = findTimeStartPos(dateFormat);
        if (timeStartPos >= 0) {
            time.delete(0, timeStartPos);
            date.delete(timeStartPos, dateFormat.length());

            timeFormat = StringUtils.trimToEmpty(time.toString());
            timeField.setTimeFormat(timeFormat);
        }

        this.dateFormat = StringUtils.trimToEmpty(date.toString());
        dateField.setDateFormat(this.dateFormat);
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        TimeZone prevTimeZone = this.timeZone;
        Date value = getValue();
        this.timeZone = timeZone;
        dateField.setZoneId(timeZone.toZoneId());
        if (value != null && !Objects.equals(prevTimeZone, timeZone)) {
            setValueToPresentation(DateTimeUtils.asLocalDateTime(value));
        }
    }

    protected void updateLayout() {
        component.removeAllComponents();
        component.addComponent(dateField);

        if (resolution.ordinal() < Resolution.DAY.ordinal()) {
            component.addComponent(timeField);
            component.addStyleName("c-datefield-withtime");
        } else {
            component.removeStyleName("c-datefield-withtime");
        }
    }

    protected int findTimeStartPos(String dateTimeFormat) {
        List<Integer> positions = new ArrayList<>();

        char[] signs = new char[]{'H', 'h', 'm', 's'};
        for (char sign : signs) {
            int pos = dateTimeFormat.indexOf(sign);
            if (pos > -1) {
                positions.add(pos);
            }
        }
        return positions.isEmpty() ? -1 : Collections.min(positions);
    }

    @Override
    protected void setValueToPresentation(LocalDateTime value) {
        updatingInstance = true;
        try {
            if (value == null) {
                dateField.setValue(null);
                timeField.setValue(null);
            } else {
                dateField.setValue(value.toLocalDate());
                timeField.setValue(value.toLocalTime());
            }
        } finally {
            updatingInstance = false;
        }
    }

    @SuppressWarnings("unchecked")
    protected V constructModelValue() {
        LocalDate dateFieldValue = dateField.getValue();
        if (dateFieldValue == null) {
            return null;
        }

        LocalTime timeValue = timeField.getValue() != null
                ? timeField.getValue()
                : LocalTime.MIDNIGHT;
        LocalDateTime resultDateTime = LocalDateTime.of(dateFieldValue, timeValue);

        Date resultDate = DateTimeUtils.asDate(resultDateTime);

        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaPropertyPath metaPropertyPath = ((DatasourceValueSource) valueSource).getMetaPropertyPath();
            MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
            if (metaProperty != null) {
                Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
                if (javaClass.equals(java.sql.Date.class)) {
                    return (V) new java.sql.Date(resultDate.getTime());
                }
            }
        }

        return (V) resultDate;
    }

    @Override
    protected LocalDateTime convertToPresentation(V modelValue) throws ConversionException {
        return modelValue != null ? DateTimeUtils.asLocalDateTime(modelValue) : null;
    }

    @Override
    public void commit() {
        // VAADIN8: gg, implement
        /*if (updatingInstance) {
            return;
        }

        updatingInstance = true;
        try {
            if (getDatasource() != null && getMetaPropertyPath() != null) {
                Date value = constructDate();

                if (getDatasource().getItem() != null) {
                    InstanceUtils.setValueEx(getDatasource().getItem(), getMetaPropertyPath().getPath(), value);
                    setModified(false);
                }
            }
        } finally {
            updatingInstance = false;
        }

        Object newValue = getValue();
        fireValueChanged(newValue);*/
//        super.commit();
    }

    @Override
    public void discard() {
        // VAADIN8: gg, implement
        /*if (getDatasource() != null && getDatasource().getItem() != null) {
            Date value = getEntityValue(getDatasource().getItem());
            setValueToFields(value);
            fireValueChanged(value);
        }*/
    }

    @Override
    public boolean isBuffered() {
        // VAADIN8: gg, implement
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        // VAADIN8: gg, implement
//        this.buffered = buffered;
    }

    @Override
    public boolean isModified() {
        // VAADIN8: gg, implement
//        return dateField.isModified();
        return false;
    }

    protected void setModified(boolean modified) {
        // VAADIN8: gg,
//        dateField.setModified(modified);
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            timeField.setDebugId(testIdManager.getTestId(id + "_time"));
            dateField.setId(testIdManager.getTestId(id + "_date"));
        }
    }

    @Override
    public void setId(String id) {
        super.setId(id);

        if (id != null && AppUI.getCurrent().isTestMode()) {
            timeField.setId("timepart");
            dateField.setCubaId("datepart");
        }
    }

    @Override
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            DataAwareComponentsTools dataAwareComponentsTools = applicationContext.getBean(DataAwareComponentsTools.class);
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;

            dataAwareComponentsTools.setupDateRange(this, entityValueSource);
            dataAwareComponentsTools.setupDateFormat(this, entityValueSource);
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        if (this.editable == editable) {
            return;
        }

        this.editable = editable;

        boolean parentEditable = true;
        if (parent instanceof ChildEditableController) {
            parentEditable = ((ChildEditableController) parent).isEditable();
        }
        boolean finalEditable = parentEditable && editable;

        setEditableToComponent(finalEditable);
    }

    protected void setEditableToComponent(boolean editable) {
        timeField.setReadOnly(!editable);
        dateField.setReadOnly(!editable);
    }

    @Override
    public void focus() {
        dateField.focus();
    }

    @Override
    public int getTabIndex() {
        return dateField.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        dateField.setTabIndex(tabIndex);
        timeField.setTabIndex(tabIndex);
    }

    @Override
    public boolean isRequired() {
        return dateField.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequired(boolean required) {
        dateField.setRequiredIndicatorVisible(required);
        timeField.setRequiredIndicatorVisible(required);
    }

    @Override
    public String getRequiredMessage() {
        // VAADIN8: gg, implement
        return "";
    }

    @Override
    public void setRequiredMessage(String msg) {
        // VAADIN8: gg, implement
    }

    @Override
    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new ArrayList<>(VALIDATORS_LIST_INITIAL_CAPACITY);
        }
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    @Override
    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }

    @Override
    public Collection<Validator> getValidators() {
        if (validators == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableCollection(validators);
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    @Override
    public void validate() throws ValidationException {
        if (hasValidationError()) {
            setValidationError(null);
        }

        if (!isVisibleRecursive() || !isEditableWithParent() || !isEnabledRecursive()) {
            return;
        }

        if (isEmpty()) {
            if (isRequired()) {
                throw new RequiredValueMissingException(getRequiredMessage(), this);
            } else {
                // vaadin8 rework this PL-10701
                return;
            }
        }

        V value = getValue();
        triggerValidators(value);
    }

    protected void triggerValidators(V value) throws ValidationFailedException {
        if (validators != null) {
            try {
                for (Validator validator : validators) {
                    validator.validate(value);
                }
            } catch (ValidationException e) {
                setValidationError(e.getDetailsMessage());

                throw new ValidationFailedException(e.getDetailsMessage(), this, e);
            }
        }
    }

    @Override
    public String getContextHelpText() {
        // VAADIN8: gg, implement
        return null;
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        // VAADIN8: gg, implement
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        // VAADIN8: gg, implement
        return false;
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        // VAADIN8: gg, implement
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        // VAADIN8: gg, implement
        return null;
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        // VAADIN8: gg, implement
    }
}