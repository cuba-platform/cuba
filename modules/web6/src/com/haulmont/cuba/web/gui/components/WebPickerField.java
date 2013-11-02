/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractProperty;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class WebPickerField
        extends
            WebAbstractField<com.haulmont.cuba.web.toolkit.ui.PickerField>
        implements
            PickerField {

    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected Object value;

    protected MetaClass metaClass;

    protected List<Action> actions = new ArrayList<>();

    protected WebPickerFieldActionHandler actionHandler;

    public WebPickerField() {
        component = new Picker(this);
        component.setImmediate(true);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        component.setPropertyDataSource(new AbstractProperty() {
            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            }

            @Override
            public Class<?> getType() {
                return String.class;
            }

            @Override
            public String toString() {
                if (value instanceof Instance) {
                    if (CaptionMode.PROPERTY.equals(getCaptionMode()))
                        return String.valueOf(((Instance) value).getValue(getCaptionProperty()));
                    else
                        return ((Instance) value).getInstanceName();
                } else
                    return super.toString();
            }
        });

        attachListener(component);

        initActionHandler();
    }

    public WebPickerField(com.haulmont.cuba.web.toolkit.ui.PickerField component) {
        this.component = component;
        attachListener(component);
        initActionHandler();
    }

    private ItemWrapper createItemWrapper(final Object newValue) {
        return new ItemWrapper(newValue, metaClass) {
            @Override
            public String toString() {
                if (CaptionMode.PROPERTY.equals(getCaptionMode()) && (value instanceof Instance)) {
                    return String.valueOf(((Instance) value).getValue(getCaptionProperty()));
                } else {
                    return super.toString();
                }
            }
        };
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
        if (ds != null) throw new IllegalStateException("Datasource is not null");
        this.metaClass = metaClass;
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

    @Override
    public <T> T getValue() {
        if (component.getPropertyDataSource() != null) {
            return (T) super.getValue();
        } else {
            return (T) value;
        }
    }

    @Override
    public void setValue(Object value) {
        if (component.isReadOnly())
            return;
        if (component.getPropertyDataSource() != null) {
            this.value = value;
            super.setValue(value);
        } else {
            this.value = value;
            ItemWrapper wrapper = createItemWrapper(value);
            super.setValue(wrapper);
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        MetaClass metaClass = datasource.getMetaClass();
        this.metaProperty = metaClass.getProperty(property);
        if (metaProperty == null) {
            throw new RuntimeException(String.format("Property '%s' not found in class %s", property, metaClass));
        }

        final MetaPropertyPath propertyPath = new MetaPropertyPath(metaProperty.getDomain(), metaProperty);
        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(propertyPath));
        final Property itemProperty = wrapper.getItemProperty(propertyPath);

        component.setPropertyDataSource(itemProperty);

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {

                        Object prevValue = value;
                        Object newValue = InstanceUtils.getValueEx(item, propertyPath.getPath());
                        setValue(newValue);
                        fireValueChanged(prevValue, newValue);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {

                        if (property.equals(propertyPath.toString())) {
                            setValue(value);
                            fireValueChanged(prevValue, value);
                        }
                    }
                }
        );

        if (datasource.getState() == Datasource.State.VALID && datasource.getItem() != null) {
            if (property.equals(propertyPath.toString())) {
                Object prevValue = value;
                Object newValue = InstanceUtils.getValueEx(datasource.getItem(), propertyPath.getPath());
                setValue(newValue);
                fireValueChanged(prevValue, newValue);
            }
        }


        setRequired(metaProperty.isMandatory());

        this.metaClass = metaProperty.getRange().asClass();


    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    @Override
                    public Object getValue() {
                        if (value == null)
                            return super.getValue();
                        else
                            return value;
                    }

                    @Override
                    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                        if (newValue instanceof String)
                            return;
                        value = newValue;
                        super.setValue(newValue);
                    }

                    @Override
                    public String toString() {
                        if (CaptionMode.PROPERTY.equals(captionMode)) {
                            return String.valueOf(getValue() == null ? "" : ((Instance) getValue()).getValue(captionProperty));
                        } else {
                            return super.toString();
                        }
                    }
                };
            }
        };
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
        actions.add(action);
        actionHandler.addAction(action);
        PickerButton pButton = new PickerButton();
        pButton.setAction(action);
        // no captions for picker buttons
        pButton.<Button>getComponent().setCaption("");
        component.addButton(pButton.<Button>getComponent());
        // apply Editable after action owner is set
        if (action instanceof StandardAction)
            ((StandardAction) action).setEditable(isEditable());
    }

    @Override
    public void removeAction(Action action) {
        actions.remove(action);
        actionHandler.removeAction(action);
        if (action.getOwner() != null && action.getOwner() instanceof WebButton) {
            Button button = ((WebButton) action.getOwner()).getComponent();
            component.removeButton(button);
        }
    }

    @Override
    public void addFieldListener(FieldListener listener) {
        component.addFieldListener(listener);
    }

    @Override
    public void setFieldEditable(boolean editable) {
        if (isEditable())
            component.getField().setReadOnly(!editable);
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    public Action getAction(String id) {
        for (Action action : actions) {
            if (ObjectUtils.equals(id, action.getId()))
                return action;
        }
        return null;
    }

    protected void initActionHandler() {
        com.vaadin.event.Action.Container actionContainer = ((com.vaadin.event.Action.Container) component.getField());
        actionHandler = new WebPickerFieldActionHandler(this);
        actionContainer.addActionHandler(actionHandler);
    }

    private static class PickerButton extends WebButton {

        @Override
        public void setIcon(String icon) {
            if (!StringUtils.isBlank(icon)) {
                component.setIcon(new ThemeResource(icon));
                component.addStyleName(BaseTheme.BUTTON_LINK);
            } else {
                component.setIcon(null);
            }
        }
    }

    public static class Picker extends com.haulmont.cuba.web.toolkit.ui.PickerField {

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
            if (readOnly)
                field.setReadOnly(readOnly);
            for (Action action : owner.getActions()) {
                if (action instanceof StandardAction) {
                    ((StandardAction) action).setEditable(!readOnly);
                }
            }
        }
    }
}