/*
 * Copyright (c) 2008-2017 Haulmont.
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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import com.haulmont.cuba.gui.components.validators.BeanValidator;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.vaadin.v7.data.Property;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

// vaadin8 ??? Custom binding bridge for the new UI components ?
public abstract class WebV8AbstractField<T extends com.vaadin.ui.AbstractField, V>
        extends WebAbstractComponent<T> implements Field<V> {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 4;

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected List<Validator> validators; // lazily initialized list

    protected Object prevValue;

    protected boolean editable = true;

    protected ItemWrapper itemWrapper;

    protected Datasource.ItemChangeListener<Entity> securityItemChangeListener;
    protected WeakItemChangeListener securityWeakItemChangeListener;
    protected EditableChangeListener parentEditableChangeListener;

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        if ((datasource == null && property != null) || (datasource != null && property == null))
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");

        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null)))
            return;

        if (this.datasource != null) {
            metaProperty = null;
            metaPropertyPath = null;

            //noinspection unchecked
            this.datasource.removeItemChangeListener(securityWeakItemChangeListener);
            securityWeakItemChangeListener = null;

            this.datasource = null;

            if (itemWrapper != null) {
                itemWrapper.unsubscribe();
            }

            disableBeanValidator();
        }

        if (datasource != null) {
            //noinspection unchecked
            this.datasource = datasource;

            MetaClass metaClass = datasource.getMetaClass();
            resolveMetaPropertyPath(metaClass, property);

            initFieldConverter();

            itemWrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));

            Property itemProperty = itemWrapper.getItemProperty(metaPropertyPath);

            initRequired(metaPropertyPath);

            if (metaProperty.isReadOnly()) {
                setEditable(false);
            }

            handleFilteredAttributes(this, this.datasource, metaPropertyPath);
            securityItemChangeListener = e -> handleFilteredAttributes(this, this.datasource, metaPropertyPath);

            securityWeakItemChangeListener = new WeakItemChangeListener(datasource, securityItemChangeListener);
            //noinspection unchecked
            this.datasource.addItemChangeListener(securityWeakItemChangeListener);

            initBeanValidator();
        }
    }

    protected void initRequired(MetaPropertyPath metaPropertyPath) {
        MetaProperty metaProperty = metaPropertyPath.getMetaProperty();

        boolean newRequired = metaProperty.isMandatory();
        Object notNullUiComponent = metaProperty.getAnnotations().get(NotNull.class.getName() + "_notnull_ui_component");
        if (Boolean.TRUE.equals(notNullUiComponent)) {
            newRequired = true;
        }
        setRequired(newRequired);

        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaPropertyPath.getMetaClass(), metaPropertyPath.toString()));
        }
    }

    protected void initBeanValidator() {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        MetaClass propertyEnclosingMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);
        Class enclosingJavaClass = propertyEnclosingMetaClass.getJavaClass();

        if (enclosingJavaClass != KeyValueEntity.class
                && !DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
            BeanValidation beanValidation = AppBeans.get(BeanValidation.NAME);
            javax.validation.Validator validator = beanValidation.getValidator();
            BeanDescriptor beanDescriptor = validator.getConstraintsForClass(enclosingJavaClass);

            if (beanDescriptor.isBeanConstrained()) {
                addValidator(new BeanValidator(enclosingJavaClass, metaProperty.getName()));
            }
        }
    }

    protected void disableBeanValidator() {
        if (validators != null) {
            for (Validator validator : validators.toArray(new Validator[validators.size()])) {
                if (validator instanceof BeanValidator) {
                    validators.remove(validator);
                }
            }
        }
    }

    protected void resolveMetaPropertyPath(MetaClass metaClass, String property) {
        metaPropertyPath = getResolvedMetaPropertyPath(metaClass, property);
        this.metaProperty = metaPropertyPath.getMetaProperty();
    }

    protected MetaPropertyPath getResolvedMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                .resolveMetaPropertyPath(metaClass, property);
        Preconditions.checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        return metaPropertyPath;
    }

    protected void initFieldConverter() {
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths);
    }

    @Override
    public boolean isRequired() {
        return component.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequired(boolean required) {
        component.setRequiredIndicatorVisible(required);
    }

    @Override
    public void setRequiredMessage(String msg) {
//        vaadin8
//        component.setRequiredError(msg);
    }

    @Override
    public String getRequiredMessage() {
//        vaadin8
//        return component.getRequiredError();
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getValue() {
        return (V) component.getValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(V value) {
        component.setValue(value);
    }

    @Override
    public void setParent(Component parent) {
        if (this.parent instanceof EditableChangeNotifier
                && parentEditableChangeListener != null) {
            ((EditableChangeNotifier) this.parent).removeEditableChangeListener(parentEditableChangeListener);

            parentEditableChangeListener = null;
        }

        super.setParent(parent);

        if (parent instanceof EditableChangeNotifier) {
            parentEditableChangeListener = event -> {
                boolean parentEditable = event.getSource().isEditable();
                boolean finalEditable = parentEditable && editable;
                setEditableToComponent(finalEditable);
            };
            ((EditableChangeNotifier) parent).addEditableChangeListener(parentEditableChangeListener);

            Editable parentEditable = (Editable) parent;
            if (!parentEditable.isEditable()) {
                setEditableToComponent(false);
            }
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
        component.setReadOnly(!editable);
    }

    @Override
    public void addListener(ValueListener listener) {
        addValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void removeListener(ValueListener listener) {
        removeValueChangeListener(new ComponentValueListenerWrapper(listener));
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        getEventRouter().addListener(ValueChangeListener.class, listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        getEventRouter().removeListener(ValueChangeListener.class, listener);
    }

    protected void attachListener(T component) {
        component.addValueChangeListener(vEvent -> {
            final Object value = getValue();
            final Object oldValue = prevValue;
            prevValue = value;

            if (!InstanceUtils.propertyValueEquals(oldValue, value)) {
                if (hasValidationError()) {
                    setValidationError(null);
                }

                ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
                getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
            }
        });
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

        if (!isVisible() || !isEditableWithParent() || !isEnabled()) {
            return;
        }

        Object value = getValue();
        if (isEmpty(value)) {
            if (isRequired()) {
                throw new RequiredValueMissingException(getRequiredMessage(), this);
            } else {
                return;
            }
        }

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

    protected void commit() {
//        vaadin8
//        component.commit();
    }

    protected void discard() {
//        vaadin8
//        component.discard();
    }

    protected boolean isBuffered() {
//        vaadin8
//        return component.isBuffered();
        return false;
    }

    protected void setBuffered(boolean buffered) {
//        vaadin8
//        component.setBuffered(buffered);
    }

    protected boolean isModified() {
//        vaadin8
//        return component.isModified();
        return false;
    }

    protected boolean isEmpty(Object value) {
        return value == null;
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + "_" + datasource.getId() + "_" + metaPropertyPath.toString();
        }

        return getClass().getSimpleName();
    }

    @Override
    public String getContextHelpText() {
        return component.getContextHelpText();
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        component.setContextHelpText(contextHelpText);
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return component.isContextHelpTextHtmlEnabled();
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        component.setContextHelpTextHtmlEnabled(enabled);
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return null; // todo vaadin8
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        // todo vaadin8
    }
}