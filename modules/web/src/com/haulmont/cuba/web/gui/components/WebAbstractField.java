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
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.model.ItemAdapter;
import com.vaadin.ui.Component.HasContextHelp.ContextHelpIconClickListener;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import java.util.*;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

public abstract class WebAbstractField<T extends com.vaadin.v7.ui.AbstractField>
        extends WebAbstractComponent<T> implements Field, PropertyBoundComponent /* todo ds: move to Field */ {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 4;

    protected InstanceContainer instanceContainer;
    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected List<Field.Validator> validators; // lazily initialized list

    protected Object prevValue;

    protected boolean editable = true;

    protected ItemWrapper itemWrapper;
    protected ItemAdapter itemAdapter;

    protected Datasource.ItemChangeListener<Entity> securityItemChangeListener;
    protected WeakItemChangeListener securityWeakItemChangeListener;
    protected EditableChangeListener parentEditableChangeListener;

    protected Consumer<ContextHelpIconClickEvent> contextHelpIconClickHandler;
    protected ContextHelpIconClickListener contextHelpIconClickListener;

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
        // todo replace completely

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

    // todo remove
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

    // todo remove
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

    // todo remove
    protected void disableBeanValidator() {
        if (validators != null) {
            for (Validator validator : validators.toArray(new Validator[validators.size()])) {
                if (validator instanceof BeanValidator) {
                    validators.remove(validator);
                }
            }
        }
    }

    // todo remove
    protected void resolveMetaPropertyPath(MetaClass metaClass, String property) {
        metaPropertyPath = getResolvedMetaPropertyPath(metaClass, property);
        this.metaProperty = metaPropertyPath.getMetaProperty();
    }

    // todo remove
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
    public void addValidator(Field.Validator validator) {
        if (validators == null) {
            validators = new ArrayList<>(VALIDATORS_LIST_INITIAL_CAPACITY);
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
                for (Field.Validator validator : validators) {
                    validator.validate(value);
                }
            } catch (ValidationException e) {
                setValidationError(e.getDetailsMessage());

                throw new ValidationFailedException(e.getDetailsMessage(), this, e);
            }
        }
    }

    protected void commit() {
        component.commit();
    }

    protected void discard() {
        component.discard();
    }

    protected boolean isBuffered() {
        return component.isBuffered();
    }

    protected void setBuffered(boolean buffered) {
        component.setBuffered(buffered);
    }

    protected boolean isModified() {
        return component.isModified();
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
        return contextHelpIconClickHandler;
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        if (!Objects.equals(this.contextHelpIconClickHandler, handler)) {
            this.contextHelpIconClickHandler = handler;

            if (handler == null) {
//                todo vaadin8
//                component.removeContextHelpIconClickListener(contextHelpIconClickListener);
                contextHelpIconClickListener = null;
            } else {
                if (contextHelpIconClickListener == null) {
                    contextHelpIconClickListener = (ContextHelpIconClickListener) e -> {
                        ContextHelpIconClickEvent event = new ContextHelpIconClickEvent(WebAbstractField.this);
                        fireContextHelpIconClick(event);
                    };
                    component.addContextHelpIconClickListener(contextHelpIconClickListener);
                }
            }
        }
    }

    protected void fireContextHelpIconClick(ContextHelpIconClickEvent event) {
        if (contextHelpIconClickHandler != null) {
            contextHelpIconClickHandler.accept(event);
        }
    }



    @Override
    public InstanceContainer getEntityContainer() {
        return instanceContainer;
    }

    @Override
    public void setContainer(InstanceContainer container, String property) {
        if (this.instanceContainer != null) {
            metaProperty = null;
            metaPropertyPath = null;

            component.setPropertyDataSource(null);

            // todo dc
//            //noinspection unchecked
//            this.entityContainer.removeItemChangeListener(securityWeakItemChangeListener);
//            securityWeakItemChangeListener = null;

            this.instanceContainer = null;

            if (itemAdapter != null) {
                itemAdapter.unsubscribe();
            }

            disableBeanValidator();
        }

        if (container != null) {
            //noinspection unchecked
            this.instanceContainer = container;

            final MetaClass metaClass = container.getMetaClass();
            resolveMetaPropertyPath(metaClass, property);

            initFieldConverter();

            itemAdapter = createItemAdapter(container, Collections.singleton(metaPropertyPath));
            component.setPropertyDataSource(itemAdapter.getItemProperty(metaPropertyPath));

            initRequired(metaPropertyPath);

            if (metaProperty.isReadOnly()) {
                setEditable(false);
            }

            // todo dc
//            handleFilteredAttributes(this, this.datasource, metaPropertyPath);
//            securityItemChangeListener = e -> handleFilteredAttributes(this, this.datasource, metaPropertyPath);
//
//            securityWeakItemChangeListener = new WeakItemChangeListener(datasource, securityItemChangeListener);
//            //noinspection unchecked
//            this.datasource.addItemChangeListener(securityWeakItemChangeListener);

            initBeanValidator();
        }
    }

    protected ItemAdapter createItemAdapter(InstanceContainer container, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemAdapter(container, container.getMetaClass(), propertyPaths);
    }
}