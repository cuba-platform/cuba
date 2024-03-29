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

import com.google.common.base.Strings;
import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.MetadataObject;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.BeanLocatorAware;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.meta.ValueBinding;
import com.haulmont.cuba.gui.components.validators.BeanPropertyValidator;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.Nested;
import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.haulmont.cuba.core.entity.BaseEntityInternalAccess.getFilteredAttributes;

@org.springframework.stereotype.Component(ValueBinder.NAME)
public class ValueBinder {

    public static final String NAME = "cuba_ValueBinder";

    @Inject
    protected MessageTools messageTools;
    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected BeanValidation beanValidation;
    @Inject
    protected BeanLocator beanLocator;
    @Inject
    protected Security security;

    public <V> ValueBinding<V> bind(HasValue<V> component, ValueSource<V> valueSource) {
        if (valueSource instanceof BeanLocatorAware) {
            ((BeanLocatorAware) valueSource).setBeanLocator(beanLocator);
        }

        ValueBindingImpl<V> binding = new ValueBindingImpl<>(component, valueSource);

        if (component instanceof Component.Editable) {
            ((Component.Editable) component).setEditable(!valueSource.isReadOnly());
        }

        if (valueSource instanceof EntityValueSource) {
            EntityValueSource entityValueSource = (EntityValueSource) valueSource;

            MetaPropertyPath metaPropertyPath = entityValueSource.getMetaPropertyPath();

            if (component instanceof Field) {
                initRequired((Field) component, metaPropertyPath);

                initBeanValidator((Field<?>) component, metaPropertyPath);
            }

            if (entityValueSource.isDataModelSecurityEnabled()) {
                if (component instanceof Component.Editable) {
                    MetaClass metaClass = entityValueSource.getEntityMetaClass();
                    boolean permittedIfEmbedded = true;
                    if (entityValueSource instanceof ContainerValueSource) {
                        InstanceContainer container = ((ContainerValueSource) entityValueSource).getContainer();
                        if (container instanceof Nested && metadataTools.isEmbeddable(metaClass)) {
                            String embeddedProperty = ((Nested) container).getProperty();
                            MetaClass masterMetaClass = ((Nested) container).getMaster().getEntityMetaClass();
                            permittedIfEmbedded = security.isEntityAttrUpdatePermitted(masterMetaClass, embeddedProperty);
                        }
                        if (permittedIfEmbedded && metaPropertyPath.length() > 1) {
                            for (MetaProperty property : metaPropertyPath.getMetaProperties()) {
                                if (metadataTools.isEmbedded(property)
                                        && !security.isEntityAttrUpdatePermitted(property.getDomain(), property.getName())) {
                                    permittedIfEmbedded = false;
                                    break;
                                }
                            }
                        }
                    }

                    if (!security.isEntityAttrUpdatePermitted(metaPropertyPath) || !permittedIfEmbedded) {
                        ((Component.Editable) component).setEditable(false);
                    }
                }

                if (!security.isEntityAttrReadPermitted(metaPropertyPath)) {
                    component.setVisible(false);
                }
            }
        }

        if (valueSource instanceof LegacyCollectionDsValueSource) {
            LegacyCollectionDsValueSource entityValueSource = (LegacyCollectionDsValueSource) valueSource;

            MetaPropertyPath metaPropertyPath = entityValueSource.getMetaPropertyPath();

            if (metaPropertyPath != null) {
                if (component instanceof Field) {
                    initRequired((Field) component, metaPropertyPath);

                    initBeanValidator((Field<?>) component, metaPropertyPath);
                }

                if (component instanceof Component.Editable) {
                    if (!security.isEntityAttrUpdatePermitted(metaPropertyPath)) {
                        ((Component.Editable) component).setEditable(false);
                    }
                }

                if (!security.isEntityAttrReadPermitted(metaPropertyPath)) {
                    component.setVisible(false);
                }
            }
        }

        binding.bind();

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

        if (newRequired) {
            component.setRequired(true);
        }

        if (Strings.isNullOrEmpty(component.getRequiredMessage())) {
            component.setRequiredMessage(messageTools.getDefaultRequiredMessage(
                    metaPropertyPath.getMetaClass(), metaPropertyPath.toPathString())
            );
        }
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
                component.addValidator(beanLocator.getPrototype(BeanPropertyValidator.NAME, enclosingJavaClass, metaProperty.getName()));
            }
        }
    }

    protected static class ValueBindingImpl<V> implements ValueBinding<V> {
        protected ValueSource<V> source;
        protected HasValue<V> component;

        protected Subscription componentValueChangeSubscription;

        protected Subscription sourceValueChangeSubscription;
        protected Subscription sourceStateChangeSubscription;
        protected Subscription sourceInstanceChangeSubscription;

        protected boolean buffered = false;

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
            if (this.sourceStateChangeSubscription != null) {
                this.sourceStateChangeSubscription.remove();
                this.sourceStateChangeSubscription = null;
            }
            if (this.sourceInstanceChangeSubscription != null) {
                this.sourceInstanceChangeSubscription.remove();
                this.sourceInstanceChangeSubscription = null;
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

        @SuppressWarnings("unchecked")
        protected void disableBeanValidator(Field<?> component) {
            Collection<? extends Consumer<?>> validators = component.getValidators();

            for (Consumer validator : validators.toArray(new Consumer[0])) {
                if (validator instanceof BeanPropertyValidator) {
                    component.removeValidator(validator);
                }
            }
        }

        public void bind() {
            this.componentValueChangeSubscription = component.addValueChangeListener(this::componentValueChanged);

            this.sourceValueChangeSubscription = source.addValueChangeListener(this::sourceValueChanged);
            this.sourceStateChangeSubscription = source.addStateChangeListener(this::valueSourceStateChanged);
            if (source instanceof EntityValueSource) {
                EntityValueSource<Entity, V> entityValueSource = (EntityValueSource<Entity, V>) this.source;
                this.sourceInstanceChangeSubscription = entityValueSource.addInstanceChangeListener(this::sourceInstanceChanged);

                if (component instanceof Field) {
                    resetRequiredIfAttributeFiltered((Field) component, entityValueSource, entityValueSource.getMetaPropertyPath());
                }
            }
        }

        @Override
        public void write() {
            if (isBuffered()
                    && isModified()) {
                setValueToSource(component.getValue());
            }
        }

        @Override
        public void discard() {
            if (source.getState() == BindingState.ACTIVE
                    && isBuffered()
                    && isModified()) {
                component.setValue(source.getValue());
            }
        }

        @Override
        public boolean isBuffered() {
            return buffered;
        }

        @Override
        public void setBuffered(boolean buffered) {
            this.buffered = buffered;
            if (!buffered
                    && source.getState() == BindingState.ACTIVE) {
                // reset value of the component
                component.setValue(source.getValue());
            }
        }

        @Override
        public boolean isModified() {
            return !Objects.equals(component.getValue(), source.getValue());
        }

        protected void valueSourceStateChanged(ValueSource.StateChangeEvent event) {
            if (event.getState() == BindingState.ACTIVE) {
                // read value to component
                component.setValue(source.getValue());
            }
        }

        protected void componentValueChanged(@SuppressWarnings("unused") HasValue.ValueChangeEvent event) {
            if (!isBuffered()) {
                // do not use event.getValue() because it can be already stale due to another value change listener
                // always read value from component instead
                V componentValue = component.getValue();
                setValueToSource(componentValue);
            }
        }

        protected void setValueToSource(V value) {
            if (source.getState() == BindingState.ACTIVE
                    && !source.isReadOnly()) {
                source.setValue(value);
            }
        }

        protected void sourceValueChanged(@SuppressWarnings("unused") ValueSource.ValueChangeEvent<V> event) {
            if (!isBuffered()) {
                // do not use event.getValue() because it can be already stale due to another value change listener
                // always read value from source instead
                V sourceValue = source.getValue();
                component.setValue(sourceValue);
            }
        }

        protected void sourceInstanceChanged(@SuppressWarnings("unused") EntityValueSource.InstanceChangeEvent<Entity> event) {
            if (source.getState() == BindingState.ACTIVE
                    && !isBuffered()) {

                if (source instanceof EntityValueSource
                        && component instanceof Field) {
                    EntityValueSource entityValueSource = (EntityValueSource) this.source;

                    resetRequiredIfAttributeFiltered((Field) component, entityValueSource,
                            entityValueSource.getMetaPropertyPath());
                }
                // read value to component
                component.setValue(source.getValue());
            }
        }

        /**
         * Set field's "required" flag to false if the value has been filtered by Row Level Security.
         * This is necessary to allow user to submit form with filtered attribute even if attribute is required.
         */
        protected void resetRequiredIfAttributeFiltered(Field<?> field, EntityValueSource valueSource,
                                                        MetaPropertyPath metaPropertyPath) {
            if (field.isRequired()
                    && valueSource.getState() == BindingState.ACTIVE
                    && valueSource.getItem() != null
                    && metaPropertyPath.getMetaProperty().getRange().isClass()) {

                Entity rootItem = valueSource.getItem();
                Entity targetItem = rootItem;

                MetaProperty[] propertiesChain = metaPropertyPath.getMetaProperties();
                if (propertiesChain.length > 1) {
                    String basePropertyItem = Arrays.stream(propertiesChain)
                            .limit(propertiesChain.length - 1)
                            .map(MetadataObject::getName)
                            .collect(Collectors.joining("."));

                    targetItem = rootItem.getValueEx(basePropertyItem);
                }

                if (targetItem instanceof BaseGenericIdEntity) {
                    String metaPropertyName = metaPropertyPath.getMetaProperty().getName();
                    Object value = targetItem.getValue(metaPropertyName);

                    BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) targetItem;
                    String[] filteredAttributes = getFilteredAttributes(baseGenericIdEntity);

                    if (value == null && filteredAttributes != null
                            && ArrayUtils.contains(filteredAttributes, metaPropertyName)) {
                        field.setRequired(false);
                    }
                }
            }
        }
    }
}