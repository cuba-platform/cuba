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

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.DataAwareComponentsTools;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.sys.TestIdManager;
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
import java.time.*;
import java.util.*;
import java.util.function.Consumer;

public class WebDateField<V extends Comparable<V>>
        extends WebAbstractViewComponent<CubaCssActionsLayout, LocalDateTime, V>
        implements DateField<V>, InitializingBean {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 2;

    protected DateTimeTransformations dateTimeTransformations;

    protected List<Consumer> validators; // lazily initialized list

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

    protected Subscription parentEditableChangeSubscription;

    protected DataAwareComponentsTools dataAwareComponentsTools;

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

    @Inject
    public void setDataAwareComponentsTools(DataAwareComponentsTools dataAwareComponentsTools) {
        this.dataAwareComponentsTools = dataAwareComponentsTools;
    }

    @Inject
    public void setDateTimeTransformations(DateTimeTransformations dateTimeTransformations) {
        this.dateTimeTransformations = dateTimeTransformations;
    }

    @Override
    public void afterPropertiesSet() {
        UserSessionSource userSessionSource = beanLocator.get(UserSessionSource.class);
        Locale locale = userSessionSource.getLocale();

        FormatStringsRegistry formatStringsRegistry = beanLocator.get(FormatStringsRegistry.NAME);

        dateField.setDateFormat(formatStringsRegistry.getFormatStringsNN(locale).getDateFormat());
        dateField.setResolution(DateResolution.DAY);

        timeField.setTimeFormat(formatStringsRegistry.getFormatStringsNN(locale).getTimeFormat());

        setResolution(Resolution.MIN);

        AppUI ui = AppUI.getCurrent();
        if (ui != null && ui.isTestMode()) {
            timeField.setCubaId("timepart");
            dateField.setCubaId("datepart");
        }
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
                LoggerFactory.getLogger(WebDateField.class)
                        .trace("Unable to convert presentation value to model", ce);

                setValidationError(ce.getLocalizedMessage());
                return;
            }

            V oldValue = internalValue;
            internalValue = value;

            if (!fieldValueEquals(value, oldValue)) {
                ValueChangeEvent<V> event = new ValueChangeEvent<>(this, oldValue, value, isUserOriginated);
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
        dataAwareComponentsTools.checkValueSourceDatatypeMismatch(datatype, getValueSource());

        this.datatype = datatype;
    }

    @Override
    public void setRangeStart(V value) {
        this.rangeStart = value;
        dateField.setRangeStart(value == null ? null : convertToLocalDateTime(value, zoneId).toLocalDate());
    }

    @Override
    public V getRangeStart() {
        return rangeStart;
    }

    @Override
    public void setRangeEnd(V value) {
        this.rangeEnd = value;
        dateField.setRangeEnd(value == null ? null : convertToLocalDateTime(value, zoneId).toLocalDate());
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
            Messages messages = beanLocator.get(Messages.NAME);
            Notifications notifications = ComponentsHelper.getScreenContext(this).getNotifications();

            notifications.create()
                    .withCaption(messages.getMainMessage("datePicker.dateOutOfRangeMessage"))
                    .withType(Notifications.NotificationType.TRAY)
                    .show();
        }

        setValueToPresentation(convertToLocalDateTime(value, zoneId));
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
            return (V) convertFromLocalDateTime(localDateTime, zoneId,
                    metaProperty.getRange().asDatatype().getJavaClass());
        }

        return (V) convertFromLocalDateTime(localDateTime, ZoneId.systemDefault(),
                datatype == null ? Date.class : datatype.getJavaClass());
    }

    @Override
    protected LocalDateTime convertToPresentation(V modelValue) throws ConversionException {
        if (modelValue == null) {
            return null;
        }
        return convertToLocalDateTime(modelValue, zoneId);
    }

    protected LocalDateTime convertToLocalDateTime(Object date, ZoneId zoneId) {
        Preconditions.checkNotNullArgument(date);
        ZonedDateTime zonedDateTime = dateTimeTransformations.transformToZDT(date);
        if (dateTimeTransformations.isDateTypeSupportsTimeZones(date.getClass())) {
            zonedDateTime = zonedDateTime.withZoneSameInstant(zoneId != null ? zoneId : ZoneId.systemDefault());
        }
        return zonedDateTime.toLocalDateTime();
    }

    protected Object convertFromLocalDateTime(LocalDateTime localDateTime, ZoneId fromZoneId, Class javaType) {
        if (fromZoneId == null || !dateTimeTransformations.isDateTypeSupportsTimeZones(javaType)) {
            fromZoneId = ZoneId.systemDefault();
        }
        ZonedDateTime zonedDateTime = localDateTime.atZone(fromZoneId);
        return dateTimeTransformations.transformFromZDT(zonedDateTime, javaType);
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
        dateField.setDescription(description);
        timeField.setDescription(description);
    }

    @Override
    public void commit() {
        if (valueBinding != null) {
            valueBinding.write();
        }
    }

    @Override
    public void discard() {
        if (valueBinding != null) {
            valueBinding.discard();
        }
    }

    @Override
    public boolean isBuffered() {
        return valueBinding != null
                && valueBinding.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        if (valueBinding != null) {
            valueBinding.setBuffered(buffered);
        }
    }

    @Override
    public boolean isModified() {
        return valueBinding != null
                && valueBinding.isModified();
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null) {
            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();
            timeField.setId(testIdManager.getTestId(id + "_time"));
            dateField.setId(testIdManager.getTestId(id + "_date"));
        }
    }

    @Override
    protected void valueBindingConnected(ValueSource<V> valueSource) {
        super.valueBindingConnected(valueSource);

        if (valueSource instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;
            DataAwareComponentsTools dataAwareComponentsTools = beanLocator.get(DataAwareComponentsTools.class);
            dataAwareComponentsTools.setupDateRange(this, entityValueSource);
            dataAwareComponentsTools.setupDateFormat(this, entityValueSource);
            dataAwareComponentsTools.setupZoneId(this, entityValueSource);
        }
    }

    @Override
    public void setParent(Component parent) {
        if (this.parent instanceof EditableChangeNotifier
                && parentEditableChangeSubscription != null) {
            parentEditableChangeSubscription.remove();
            parentEditableChangeSubscription = null;
        }

        super.setParent(parent);

        if (parent instanceof EditableChangeNotifier) {
            parentEditableChangeSubscription =
                    ((EditableChangeNotifier) parent).addEditableChangeListener(this::onParentEditableChange);

            Editable parentEditable = (Editable) parent;
            if (!parentEditable.isEditable()) {
                setEditableToComponent(false);
            }
        }
    }

    protected void onParentEditableChange(EditableChangeNotifier.EditableChangeEvent event) {
        boolean parentEditable = event.getSource().isEditable();
        boolean finalEditable = parentEditable && editable;
        setEditableToComponent(finalEditable);
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
        return (isEditableWithParent() && isRequired() && isEmpty())
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
    public void addValidator(Consumer<? super V> validator) {
        if (validators == null) {
            validators = new ArrayList<>(VALIDATORS_LIST_INITIAL_CAPACITY);
        }
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    @Override
    public void removeValidator(Consumer<V> validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Consumer<V>> getValidators() {
        if (validators == null) {
            return Collections.emptyList();
        }

        return (Collection) Collections.unmodifiableCollection(validators);
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

        if (isEmpty() && isRequired()) {
            String requiredMessage = getRequiredMessage();
            if (requiredMessage == null) {
                Messages messages = beanLocator.get(Messages.NAME);
                requiredMessage = messages.getMainMessage("validationFail.defaultRequiredMessage");
            }
            throw new RequiredValueMissingException(requiredMessage, this);
        }

        V value = getValue();
        triggerValidators(value);
    }

    protected void triggerValidators(V value) throws ValidationFailedException {
        if (validators != null) {
            try {
                for (Consumer validator : validators) {
                    validator.accept(value);
                }
            } catch (ValidationException e) {
                setValidationError(e.getDetailsMessage());

                throw new ValidationFailedException(e.getDetailsMessage(), this, e);
            }
        }
    }

    @Override
    protected boolean hasValidationError() {
        return dateField.getComponentError() instanceof UserError;
    }

    @Override
    protected void setValidationError(String errorMessage) {
        if (errorMessage == null) {
            dateField.setComponentError(null);
            timeField.setComponentError(null);
        } else {
            UserError userError = new UserError(errorMessage);
            dateField.setComponentError(userError);
            timeField.setComponentError(userError);
        }
    }
}