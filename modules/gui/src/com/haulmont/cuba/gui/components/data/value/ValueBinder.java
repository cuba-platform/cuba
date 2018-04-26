/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.data.value;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueBinding;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.validators.BeanValidator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import java.util.Collection;

// todo buffering support
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@org.springframework.stereotype.Component(ValueBinder.NAME)
public class ValueBinder {

    public static final String NAME = "cuba_ValueBinder";

    @Inject
    protected MessageTools messageTools;
    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected BeanValidation beanValidation;

    public <V> ValueBinding<V> bind(HasValue<V> component, ValueSource<V> valueSource) {
        ValueBindingImpl<V> binding = new ValueBindingImpl<>(component, valueSource);

        if (component instanceof Component.Editable) {
            ((Component.Editable) component).setEditable(!valueSource.isReadOnly());
        }

        if (valueSource instanceof EntityValueSource
                && component instanceof Field) {
            initRequired((Field) component, ((EntityValueSource) valueSource).getMetaPropertyPath());

            // todo reset required if the attribute is not available due to security rules
            // todo on item change - reset required if needed
        }

        binding.bind();

        if (valueSource instanceof EntityValueSource
                && component instanceof Field) {
            initBeanValidator((Field<?>) component, ((EntityValueSource) valueSource).getMetaPropertyPath());
        }

        return binding;
    }

    protected void initRequired(Field<?> component, MetaPropertyPath metaPropertyPath) {
        MetaProperty metaProperty = metaPropertyPath.getMetaProperty();

        boolean newRequired = metaProperty.isMandatory();
        Object notNullUiComponent = metaProperty.getAnnotations()
                .get(NotNull.class.getName() + "_notnull_ui_component");
        if (Boolean.TRUE.equals(notNullUiComponent)) {
            newRequired = true;
        }
        component.setRequired(newRequired);
        component.setRequiredMessage(messageTools.getDefaultRequiredMessage(
                metaPropertyPath.getMetaClass(), metaPropertyPath.toPathString())
        );
    }

    protected void initBeanValidator(Field<?> component, MetaPropertyPath metaPropertyPath) {
        MetaProperty metaProperty = metaPropertyPath.getMetaProperty();

        MetaClass propertyEnclosingMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);
        Class enclosingJavaClass = propertyEnclosingMetaClass.getJavaClass();

        if (enclosingJavaClass != KeyValueEntity.class
                && !DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
            javax.validation.Validator validator = beanValidation.getValidator();
            BeanDescriptor beanDescriptor = validator.getConstraintsForClass(enclosingJavaClass);

            if (beanDescriptor.isBeanConstrained()) {
                component.addValidator(new BeanValidator(enclosingJavaClass, metaProperty.getName()));
            }
        }
    }

    protected static class ValueBindingImpl<V> implements ValueBinding<V> {
        protected ValueSource<V> source;
        protected HasValue<V> component;

        protected Subscription componentValueChangeSubscription;

        protected Subscription sourceValueChangeSubscription;
        protected Subscription sourceStateChangeSupscription;

        public ValueBindingImpl(HasValue<V> component, ValueSource<V> source) {
            this.source = source;
            this.component = component;
        }

        @Override
        public ValueSource<V> getSource() {
            return source;
        }

        @Override
        public HasValue<V> getComponent() {
            return component;
        }

        @Override
        public void unbind() {
            if (this.componentValueChangeSubscription != null) {
                this.componentValueChangeSubscription.remove();
                this.componentValueChangeSubscription = null;
            }

            if (this.sourceValueChangeSubscription != null) {
                this.sourceValueChangeSubscription.remove();
                this.sourceValueChangeSubscription = null;
            }

            if (this.sourceStateChangeSupscription != null) {
                this.sourceStateChangeSupscription.remove();
                this.sourceStateChangeSupscription = null;
            }

            if (component instanceof Field) {
                disableBeanValidator((Field<?>) component);
            }
        }

        @Override
        public void activate() {
            if (source.getState() == BindingState.ACTIVE) {
                component.setValue(source.getValue());
            }
        }

        protected void disableBeanValidator(Field<?> component) {
            Collection<Field.Validator> validators = component.getValidators();

            for (Field.Validator validator : validators.toArray(new Field.Validator[0])) {
                if (validator instanceof BeanValidator) {
                    component.removeValidator(validator);
                }
            }
        }

        public void bind() {
            this.componentValueChangeSubscription = component.addValueChangeListener(this::componentValueChanged);

            // vaadin8 weak references on binding !
            this.sourceValueChangeSubscription = source.addValueChangeListener(this::sourceValueChanged);
            this.sourceStateChangeSupscription = source.addStateChangeListener(this::valueSourceStateChanged);
        }

        protected void valueSourceStateChanged(ValueSource.StateChangeEvent<V> event) {
            if (event.getState() == BindingState.ACTIVE) {
                // read value to component
                component.setValue(source.getValue());
            }
        }

        @SuppressWarnings("unchecked")
        protected void componentValueChanged(@SuppressWarnings("unused") HasValue.ValueChangeEvent event) {
            if (source.getState() == BindingState.ACTIVE
                    && !source.isReadOnly()) {
                source.setValue((V) event.getValue());
            }
        }

        @SuppressWarnings("unchecked")
        protected void sourceValueChanged(@SuppressWarnings("unused") ValueSource.ValueChangeEvent event) {
            component.setValue((V) event.getValue());
        }
    }
}