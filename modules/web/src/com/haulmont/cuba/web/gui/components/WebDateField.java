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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.screen.compatibility.LegacyFrame;
import com.haulmont.cuba.gui.sys.TestIdManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DateComponents;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.widgets.CubaCssActionsLayout;
import com.haulmont.cuba.web.widgets.CubaDateField;
import com.haulmont.cuba.web.widgets.CubaTimeField;
import com.vaadin.data.HasValue;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.datefield.DateResolution;
import com.vaadin.ui.AbstractComponent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

public class WebDateField<V extends Comparable<V>> extends WebAbstractViewComponent<CubaCssActionsLayout, LocalDateTime, V>
        implements DateField<V>, InitializingBean {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 2;

    @Inject
    protected DateComponents dateComponents;

    protected List<Validator> validators; // lazily initialized list

    protected Resolution resolution;
    protected ZoneId zoneId;
    protected Datatype<V> datatype;
    protected V rangeStart;
    protected V rangeEnd;

    protected boolean updatingInstance;

    protected CubaDateField dateField;
    protected CubaTimeField timeField;

    protected String dateTimeFormat;

    protected boolean editable = true;

    protected ThemeConstants theme;

    public WebDateField() {
        component = createComponent();
        component.setPrimaryStyleName("c-datefield-layout");

        if (App.isBound()) {
            theme = App.getInstance().getThemeConstants();
        }

        dateField = createDateField();
        initDateField(dateField);
        timeField = createTimeField();
        initTimeField(timeField);

        setWidthAuto();

        dateField.addValueChangeListener(createDateValueChangeListener());
        timeField.addValueChangeListener(createTimeValueChangeListener());
    }

    protected CubaCssActionsLayout createComponent() {
        return new CubaCssActionsLayout();
    }

    protected CubaDateField createDateField() {
        return new CubaDateField();
    }

    protected void initDateField(CubaDateField dateField) {
        dateField.setCaptionManagedByLayout(false);
    }

    protected CubaTimeField createTimeField() {
        return new CubaTimeField();
    }

    protected void initTimeField(CubaTimeField timeField) {
        timeField.setCaptionManagedByLayout(false);
    }

    @Override
    public void afterPropertiesSet() {
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
                publish(ValueChangeEvent.class, event);
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
    public Datatype<V> getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype<V> datatype) {
        this.datatype = datatype;
    }

    @Override
    public void setRangeStart(V value) {
        this.rangeStart = value;
        dateField.setRangeStart(value == null ? null : dateComponents.convertToLocalDateTime(value, zoneId).toLocalDate());
    }

    @Override
    public V getRangeStart() {
        return rangeStart;
    }

    @Override
    public void setRangeEnd(V value) {
        this.rangeEnd = value;
        dateField.setRangeEnd(value == null ? null : dateComponents.convertToLocalDateTime(value, zoneId).toLocalDate());
    }

    @Override
    public V getRangeEnd() {
        return rangeEnd;
    }

    protected boolean checkRange(V value) {
        if (updatingInstance) {
            return true;
        }

        if (value != null) {
            V rangeStart = getRangeStart();
            if (rangeStart != null && rangeStart.compareTo(value) > 0) {
                handleDateOutOfRange(value);
                return false;
            }

            V rangeEnd = getRangeEnd();
            if (rangeEnd != null && rangeEnd.compareTo(value) < 0) {
                handleDateOutOfRange(value);
                return false;
            }
        }

        return true;
    }

    protected void handleDateOutOfRange(V value) {
        if (getFrame() != null) {
            Messages messages = applicationContext.getBean(Messages.NAME, Messages.class);
            LegacyFrame.of(this).showNotification(messages.getMainMessage("datePicker.dateOutOfRangeMessage"),
                    Frame.NotificationType.TRAY);
        }

        setValueToPresentation(dateComponents.convertToLocalDateTime(value, zoneId));
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
            timeField.setTimeFormat(StringUtils.trimToEmpty(time.toString()));
        }
        dateField.setDateFormat(StringUtils.trimToEmpty(date.toString()));
    }

    @Override
    public TimeZone getTimeZone() {
        return getZoneId() != null ? TimeZone.getTimeZone(getZoneId()) : null;
    }

    @Override
    public void setTimeZone(TimeZone timeZone) {
        setZoneId(timeZone == null ? null : timeZone.toZoneId());
    }

    @Override
    public ZoneId getZoneId() {
        return zoneId;
    }

    @Override
    public void setZoneId(ZoneId zoneId) {
        ZoneId prevZoneId = this.zoneId;
        V value = getValue();
        this.zoneId = zoneId;
        dateField.setZoneId(zoneId);
        if (value != null && !Objects.equals(prevZoneId, zoneId)) {
            setValueToPresentation(convertToPresentation(value));
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
        LocalDate dateValue = dateField.getValue();
        if (dateValue == null) {
            return null;
        }

        LocalTime timeValue = timeField.getValue() != null
                ? timeField.getValue()
                : LocalTime.MIDNIGHT;

        LocalDateTime localDateTime = LocalDateTime.of(dateValue, timeValue);

        ValueSource<V> valueSource = getValueSource();
        if (valueSource instanceof EntityValueSource) {
            MetaProperty metaProperty = ((EntityValueSource) valueSource).getMetaPropertyPath().getMetaProperty();
            return (V) dateComponents.convertFromLocalDateTime(localDateTime, zoneId,
                    metaProperty.getRange().asDatatype().getJavaClass());
        }

        return (V) dateComponents.convertFromLocalDateTime(localDateTime, ZoneId.systemDefault(),
                datatype == null ? Date.class : datatype.getJavaClass());
    }

    @Override
    protected LocalDateTime convertToPresentation(V modelValue) throws ConversionException {
        if (modelValue == null) {
            return null;
        }
        return dateComponents.convertToLocalDateTime(modelValue, zoneId);
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        dateField.setDescription(description);
        timeField.setDescription(description);
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
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;
            dateComponents.setupDateRange(this, entityValueSource);
            dateComponents.setupDateFormat(this, entityValueSource);
            dateComponents.setupZoneId(this, entityValueSource);
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
        // Set requiredIndicatorVisible to a component
        // in order to show required indicator
        component.setRequiredIndicatorVisible(required);
        // Set requiredIndicatorVisible to fields
        // in order to show required message and apply error styles
        dateField.setRequiredIndicatorVisible(required);
        timeField.setRequiredIndicatorVisible(required);

        setupComponentErrorProvider(required, dateField);
        setupComponentErrorProvider(required, timeField);
    }

    protected void setupComponentErrorProvider(boolean required, AbstractComponent component) {
        if (required) {
            component.setComponentErrorProvider(this::getErrorMessage);
        } else {
            component.setComponentErrorProvider(null);
        }
    }

    protected ErrorMessage getErrorMessage() {
        return (isEditable() && isRequired() && isEmpty())
                ? new UserError(getRequiredMessage())
                : null;
    }

    @Override
    public String getRequiredMessage() {
        return dateField.getRequiredError();
    }

    @Override
    public void setRequiredMessage(String msg) {
        dateField.setRequiredError(msg);
        timeField.setRequiredError(msg);
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