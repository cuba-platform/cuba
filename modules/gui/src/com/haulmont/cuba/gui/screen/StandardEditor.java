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

package com.haulmont.cuba.gui.screen;

import com.google.common.collect.Iterables;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.events.BeforeShowEvent;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.Set;

/**
 * Base class for editor screens
 */
public abstract class StandardEditor<T extends Entity> extends Screen implements EditorScreen<T> {

    private T entityToEdit;

    private boolean crossFieldValidate = true;
    private boolean justLocked = false; // todo
    private boolean readOnly = false; // todo

    protected StandardEditor() {
        addBeforeShowListener(this::setupEntityToEdit);
    }

    protected void setupEntityToEdit(@SuppressWarnings("unused") BeforeShowEvent event) {
        EntityStates entityStates = getBeanLocator().get(EntityStates.NAME);

        if (entityStates.isNew(entityToEdit)) {
            InstanceContainer<Entity> userCont = getEditedEntityContainer();
            userCont.setItem(entityToEdit);
            getScreenData().getDataContext().merge(entityToEdit);
        } else {
            InstanceLoader loader = getEditedEntityLoader();
            loader.setEntityId(entityToEdit.getId());
        }

        // todo pessimistic locking
        // todo security
    }

    protected InstanceLoader getEditedEntityLoader() {
        InstanceLoader loader = getScreenData().findLoaderOf(getEditedEntityContainer());
        if (loader == null) {
            throw new IllegalStateException("Edited entity loader is not defined");
        }
        return loader;
    }

    protected abstract InstanceContainer<Entity> getEditedEntityContainer();

    @SuppressWarnings("unchecked")
    @Override
    public T getEditedEntity() {
        return (T) getEditedEntityContainer().getItemOrNull();
    }

    @Override
    public void setEntityToEdit(T item) {
        this.entityToEdit = item;
    }

    @Override
    public boolean hasUnsavedChanges() {
        if (readOnly) {
            return false;
        }

        return getScreenData().getDataContext().hasChanges();
    }

    @Override
    protected OperationResult commitChanges() {
        ValidationErrors validationErrors = getValidationErrors();
        if (!validationErrors.isEmpty()) {
            showValidationErrors(validationErrors);

            focusProblemComponent(validationErrors);

            return OperationResult.fail();
        }

        getScreenData().getDataContext().commit();

        return OperationResult.success();
    }

    @Override
    public boolean isLocked() {
        return justLocked;
    }

    protected boolean isCrossFieldValidate() {
        return crossFieldValidate;
    }

    protected void setCrossFieldValidate(boolean crossFieldValidate) {
        this.crossFieldValidate = crossFieldValidate;
    }

    @Override
    protected ValidationErrors getValidationErrors() {
        ValidationErrors validationErrors = super.getValidationErrors();

        validateAdditionalRules(validationErrors);

        return validationErrors;
    }

    public void validateAdditionalRules(ValidationErrors errors) {
        // all previous validations return no errors
        if (isCrossFieldValidate() && errors.isEmpty()) {
            BeanValidation beanValidation = getBeanLocator().get(BeanValidation.NAME);

            Validator validator = beanValidation.getValidator();
            Set<ConstraintViolation<Entity>> violations = validator.validate(getEditedEntity(), UiCrossFieldChecks.class);

            violations.stream()
                    .filter(violation -> {
                        Path propertyPath = violation.getPropertyPath();

                        Path.Node lastNode = Iterables.getLast(propertyPath);
                        return lastNode.getKind() == ElementKind.BEAN;
                    })
                    .forEach(violation -> errors.add(violation.getMessage()));
        }
    }
}