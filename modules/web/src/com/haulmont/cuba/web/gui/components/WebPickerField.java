/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaPickerField;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEntityConverter;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;
import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

/**
 * @author abramov
 */
public class WebPickerField extends WebAbstractField<CubaPickerField>
        implements PickerField, Component.SecuredActionsHolder {

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected MetaClass metaClass;

    protected List<Action> actions = new ArrayList<>();

    protected WebPickerFieldActionHandler actionHandler;

    protected Metadata metadata = AppBeans.get(Metadata.NAME);

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    public WebPickerField() {
        component = new Picker(this);
        component.setImmediate(true);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        component.setCaptionFormatter(new StringToEntityConverter() {
            @Override
            public String convertToPresentation(Entity value, Class<? extends String> targetType, Locale locale)
                    throws ConversionException {
                if (captionMode == CaptionMode.PROPERTY
                        && StringUtils.isNotEmpty(captionProperty)
                        && (value != null)) {

                    Object propertyValue = value.getValue(captionProperty);

                    MetaClass metaClass = metadata.getClassNN(value.getClass());
                    MetaProperty property = metaClass.getProperty(captionProperty);
                    return metadataTools.format(propertyValue, property);
                }

                return super.convertToPresentation(value, targetType, locale);
            }
        });

        attachListener(component);

        initActionHandler();
    }

    public WebPickerField(CubaPickerField component) {
        this.component = component;
        attachListener(component);
        initActionHandler();
    }

    @Override
    public MetaClass getMetaClass() {
        final Datasource ds = getDatasource();
        if (ds != null) {
            return metaProperty.getRange().asClass();
        } else {
            return metaClass;
        }
    }

    @Override
    public void setMetaClass(MetaClass metaClass) {
        final Datasource ds = getDatasource();
        if (ds != null) {
            throw new IllegalStateException("Datasource is not null");
        }
        this.metaClass = metaClass;
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            if (datasource == null && metaClass == null) {
                throw new IllegalStateException("Datasource or metaclass must be set for field");
            }

            Class fieldClass = getMetaClass().getJavaClass();
            Class<?> valueClass = value.getClass();
            if (!fieldClass.isAssignableFrom(valueClass)) {
                throw new IllegalArgumentException(
                        String.format("Could not set value with class %s to field with class %s",
                                fieldClass.getCanonicalName(),
                                valueClass.getCanonicalName())
                );
            }
        }

        super.setValue(value);
    }

    @Override
    public LookupAction addLookupAction() {
        LookupAction action = new LookupAction(this);
        addAction(action);
        return action;
    }

    @Override
    public ClearAction addClearAction() {
        ClearAction action = new ClearAction(this);
        addAction(action);
        return action;
    }

    @Override
    public PickerField.OpenAction addOpenAction() {
        OpenAction action = new OpenAction(this);
        addAction(action);
        return action;
    }

    public void checkPropertyDatasource(Datasource datasource, String property){
        Preconditions.checkNotNullArgument(datasource);
        Preconditions.checkNotNullArgument(property);

        MetaPropertyPath metaPropertyPath = getResolvedMetaPropertyPath(datasource.getMetaClass(), property);
        if (!metaPropertyPath.getRange().isClass()) {
            throw new DevelopmentException(String.format("property '%s.%s' should have Entity type",  datasource.getMetaClass().getName(), property));
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.checkPropertyDatasource(datasource, property);

        this.datasource = datasource;
        metaPropertyPath = getResolvedMetaPropertyPath(datasource.getMetaClass(), property);
        metaProperty = metaPropertyPath.getMetaProperty();

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        final Property itemProperty = wrapper.getItemProperty(metaPropertyPath);

        component.setPropertyDataSource(itemProperty);

        //noinspection unchecked
        datasource.addItemChangeListener(e -> {
            Object newValue = InstanceUtils.getValueEx(e.getItem(), metaPropertyPath.getPath());
            setValue(newValue);
        });

        //noinspection unchecked
        datasource.addItemPropertyChangeListener(e -> {
            if (e.getProperty().equals(metaPropertyPath.toString())) {
                setValue(e.getValue());
            }
        });

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(metaPropertyPath.toString())) {
                Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
                setValue(newValue);
            }
        }

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
        }

        if (metaProperty.isReadOnly()) {
            setEditable(false);
        }

        handleFilteredAttributes(this.datasource, metaProperty, this);
        this.datasource.addItemChangeListener(e -> handleFilteredAttributes(this.datasource, metaProperty, this));
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
    }

    @Override
    public void addAction(Action action) {
        int index = findActionById(actions, action.getId());
        if (index < 0) {
            index = actions.size();
        }

        addAction(action, index);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        int oldIndex = findActionById(actions, action.getId());
        if (oldIndex >= 0) {
            removeAction(actions.get(oldIndex));
            if (index > oldIndex) {
                index--;
            }
        }

        actions.add(index, action);
        actionHandler.addAction(action, index);

        PickerButton pButton = new PickerButton();
        pButton.setAction(action);
        // no captions for picker buttons
        Button vButton = (Button) pButton.getComponent();
        vButton.setCaption("");

        component.addButton(vButton, index);

        // apply Editable after action owner is set
        if (action instanceof StandardAction) {
            ((StandardAction) action).setEditable(isEditable());
        }

        if (StringUtils.isNotEmpty(getDebugId())) {
            pButton.setDebugId(AppUI.getCurrent().getTestIdManager().getTestId(getDebugId() + "_" + action.getId()));
        }

        actionsPermissions.apply(action);
    }

    @Override
    public void setDebugId(String id) {
        super.setDebugId(id);

        if (id != null) {
            String debugId = getDebugId();

            TestIdManager testIdManager = AppUI.getCurrent().getTestIdManager();

            for (Action action : actions) {
                if (action.getOwner() != null && action.getOwner() instanceof WebButton) {
                    WebButton button = (WebButton) action.getOwner();
                    if (StringUtils.isEmpty(button.getDebugId())) {
                        button.setDebugId(testIdManager.getTestId(debugId + "_" + action.getId()));
                    }
                }
            }
        }
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (actions.remove(action)) {
            actionHandler.removeAction(action);
            //noinspection ConstantConditions
            if (action.getOwner() != null && action.getOwner() instanceof WebButton) {
                WebButton vButton = (WebButton) action.getOwner();
                vButton.setAction(null);
                Button button = (Button) vButton.getComponent();
                component.removeButton(button);
            }
        }
    }

    @Override
    public void removeAction(@Nullable String id) {
        Action action = getAction(id);
        if (action != null) {
            removeAction(action);
        }
    }

    @Override
    public void removeAllActions() {
        for (Action action : new ArrayList<>(actions)) {
            removeAction(action);
        }
    }

    @Override
    public void addFieldListener(FieldListener listener) {
        component.addFieldListener(listener);
    }

    @Override
    public void setFieldEditable(boolean editable) {
        component.setFieldReadOnly(!editable);
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        for (Action action : actions) {
            if (ObjectUtils.equals(id, action.getId())) {
                return action;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public Action getActionNN(String id) {
        Action action = getAction(id);
        if (action == null) {
            throw new IllegalStateException("Unable to find action with id " + id);
        }
        return action;
    }

    protected void initActionHandler() {
        actionHandler = new WebPickerFieldActionHandler(this);
        component.addActionHandler(actionHandler);
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }

    protected static class PickerButton extends WebButton {

        protected PickerButton() {
            component.setTabIndex(-1);
        }

        @Override
        public void setIcon(String icon) {
            if (StringUtils.isNotBlank(icon)) {
                component.setIcon(WebComponentsHelper.getIcon(icon));
            } else {
                component.setIcon(null);
            }
        }
    }

    public static class Picker extends CubaPickerField {

        private PickerField owner;

        public Picker(PickerField owner) {
            this.owner = owner;
        }

        public Picker(PickerField owner, AbstractField field) {
            super(field);
            this.owner = owner;
        }

        @Override
        public void setReadOnly(boolean readOnly) {
            super.setReadOnly(readOnly);

            for (Action action : owner.getActions()) {
                if (action instanceof StandardAction) {
                    ((StandardAction) action).setEditable(!readOnly);
                }
            }
        }
    }
}