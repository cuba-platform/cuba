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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.PropertyBoundComponent;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.validators.BeanValidator;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.model.InstanceContainer;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public abstract class DesktopAbstractField<C extends JComponent> extends DesktopAbstractComponent<C> implements Field,
        DesktopComponent.HasContextHelpClickHandler, PropertyBoundComponent {

    protected List<ValueChangeListener> listeners = new ArrayList<>();

    protected boolean editable = true;

    protected boolean required;
    protected String requiredMessage;

    protected String contextHelpText;
    protected Boolean contextHelpTextHtmlEnable = false;

    protected Set<Validator> validators = new LinkedHashSet<>();

    // todo move nimbus constants to theme
    protected Color requiredBgColor = (Color) UIManager.get("cubaRequiredBackground");
    protected Color defaultBgColor = (Color) UIManager.get("nimbusLightBackground");

    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;
    
    protected EditableChangeListener parentEditableChangeListener;

    protected Consumer<ContextHelpIconClickEvent> contextHelpIconClickHandler;

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueChangeListener listener : new ArrayList<>(listeners)) {
            listener.valueChanged(new ValueChangeEvent(this, prevValue, value));
        }
    }

    @Override
    public Subscription addValueChangeListener(ValueChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        // todo
        return () -> {};
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addValidator(Validator validator) {
        if (!validators.contains(validator))
            validators.add(validator);
    }

    @Override
    public void removeValidator(Validator validator) {
        validators.remove(validator);
    }

    @Override
    public Collection<Validator> getValidators() {
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

    protected boolean isEmpty(Object value) {
        return value == null;
    }

    @Override
    public void validate() throws ValidationException {
        if (!isVisibleRecursive() || !isEditableWithParent() || !isEnabledRecursive())
            return;

        Object value = getValue();
        if (isEmpty(value)) {
            if (isRequired())
                throw new RequiredValueMissingException(requiredMessage, this);
            else
                return;
        }

        for (Validator validator : validators) {
            validator.validate(value);
        }
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
        updateMissingValueState();
    }

    public void updateMissingValueState() {
        if (impl != null) {
            decorateMissingValue(impl, required);
        }
    }

    protected void decorateMissingValue(JComponent jComponent, boolean missingValueState) {
        jComponent.setBackground(missingValueState ? requiredBgColor : defaultBgColor);
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
            for (Validator validator : new ArrayList<>(validators)) {
                if (validator instanceof BeanValidator) {
                    validators.remove(validator);
                }
            }
        }
    }

    @Override
    public void setRequiredMessage(String msg) {
        requiredMessage = msg;
    }

    @Override
    public String getRequiredMessage() {
        return requiredMessage;
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

    protected abstract void setEditableToComponent(boolean editable);

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        Datasource datasource = getDatasource();
        MetaPropertyPath metaPropertyPath = getMetaPropertyPath();

        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + "_" + datasource.getId() + "_" + metaPropertyPath.toString();
        }

        return getClass().getSimpleName();
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

    @Override
    public void setParent(com.haulmont.cuba.gui.components.Component parent) {
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
    public String getContextHelpText() {
        return contextHelpText;
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        if (!Objects.equals(this.contextHelpText, contextHelpText)) {
            this.contextHelpText = contextHelpText;

            requestContainerUpdate();

            if (parent instanceof DesktopFieldGroup) {
                ((DesktopFieldGroup) parent).updateContextHelp(this);
            }
        }
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return contextHelpTextHtmlEnable;
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        if (!Objects.equals(this.contextHelpTextHtmlEnable, enabled)) {
            contextHelpTextHtmlEnable = enabled;

            requestContainerUpdate();

            if (parent instanceof DesktopFieldGroup) {
                ((DesktopFieldGroup) parent).updateContextHelp(this);
            }
        }
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return contextHelpIconClickHandler;
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        if (!Objects.equals(this.contextHelpIconClickHandler, handler)) {
            this.contextHelpIconClickHandler = handler;

            requestContainerUpdate();

            if (parent instanceof DesktopFieldGroup) {
                ((DesktopFieldGroup) parent).updateContextHelp(this);
            }
        }
    }

    @Override
    public void fireContextHelpIconClickEvent(ContextHelpIconClickEvent event) {
        if (contextHelpIconClickHandler != null) {
            contextHelpIconClickHandler.accept(event);
        }
    }

    @Override
    public InstanceContainer getEntityContainer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setContainer(InstanceContainer container, String property) {
        throw new UnsupportedOperationException();
    }
}