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
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import com.haulmont.cuba.gui.components.validators.BeanValidator;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import org.apache.commons.lang.StringUtils;

import javax.validation.metadata.BeanDescriptor;
import java.util.*;

import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

public abstract class WebAbstractField<T extends com.vaadin.ui.AbstractField> extends WebAbstractComponent<T> implements Field {

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected List<Field.Validator> validators; // lazily initialized list

    protected Object prevValue;

    protected ItemWrapper itemWrapper;

    protected Datasource.ItemChangeListener<Entity> securityItemChangeListener;
    protected WeakItemChangeListener securityWeakItemChangeListener;

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

            component.setPropertyDataSource(null);

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

            final MetaClass metaClass = datasource.getMetaClass();
            resolveMetaPropertyPath(metaClass, property);

            initFieldConverter();

            itemWrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
            component.setPropertyDataSource(itemWrapper.getItemProperty(metaPropertyPath));

            setRequired(metaProperty.isMandatory());
            if (StringUtils.isEmpty(getRequiredMessage())) {
                MessageTools messageTools = AppBeans.get(MessageTools.NAME);
                setRequiredMessage(messageTools.getDefaultRequiredMessage(metaClass, property));
            }

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

    protected void initBeanValidator() {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        MetaClass propertyEnclosingMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);
        Class enclosingJavaClass = propertyEnclosingMetaClass.getJavaClass();

        if (enclosingJavaClass != KeyValueEntity.class
                && !DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
            BeanValidation beanValidation = AppBeans.get(BeanValidation.NAME);
            javax.validation.Validator validator = beanValidation.getValidator();
            BeanDescriptor beanDescriptor = validator.getConstraintsForClass(enclosingJavaClass);

            if (beanDescriptor.hasConstraints()) {
                addValidator(new BeanValidator(enclosingJavaClass, metaProperty.getName()));
            }
        }
    }

    protected void disableBeanValidator() {
        if (validators != null) {
            for (Validator validator : new ArrayList<>(validators)) {
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
        return component.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        component.setRequired(required);
    }

    @Override
    public void setRequiredMessage(String msg) {
        component.setRequiredError(msg);
    }

    @Override
    public String getRequiredMessage() {
        return component.getRequiredError();
    }

    @Override
    public <V> V getValue() {
        //noinspection unchecked
        return (V) component.getValue();
    }

    @Override
    public void setValue(Object value) {
        //noinspection unchecked
        component.setValueIgnoreReadOnly(value);
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
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

            if (!Objects.equals(oldValue, value)) {
                ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
                getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
            }
        });
    }

    @Override
    public void addValidator(Field.Validator validator) {
        if (validators == null) {
            validators = new ArrayList<>();
        }
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    @Override
    public void removeValidator(Field.Validator validator) {
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
        if (!isVisible() || !isEditable() || !isEnabled()) {
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
            for (Field.Validator validator : validators) {
                validator.validate(value);
            }
        }
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
}