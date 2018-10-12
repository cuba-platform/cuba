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

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.events.TriggerOnce;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.validation.groups.UiCrossFieldChecks;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.util.OperationResult;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Base class for editor screens
 */
public abstract class StandardEditor<T extends Entity> extends Screen implements EditorScreen<T> {

    protected boolean commitActionPerformed = false;

    private T entityToEdit;
    private boolean crossFieldValidate = true;
    private boolean justLocked = false; // todo
    private boolean readOnly = false; // todo

    protected StandardEditor() {
        addInitListener(this::initActions);
        addBeforeShowListener(this::setupEntityToEdit);
    }

    protected void initActions(@SuppressWarnings("unused") InitEvent event) {
        Window window = getWindow();

        Configuration configuration = getBeanLocator().get(Configuration.NAME);
        Messages messages = getBeanLocator().get(Messages.NAME);

        String commitShortcut = configuration.getConfig(ClientConfig.class).getCommitShortcut();

        Action commitAndCloseAction = new BaseAction(WINDOW_COMMIT_AND_CLOSE)
                .withCaption(messages.getMainMessage("actions.Ok"))
                .withPrimary(true)
                .withShortcut(commitShortcut)
                .withHandler(this::commitAndClose);

        window.addAction(commitAndCloseAction);

        Action commitAction = new BaseAction(WINDOW_COMMIT)
                .withCaption(messages.getMainMessage("actions.Save"))
                .withHandler(this::commit);

        window.addAction(commitAction);

        Action closeAction = new BaseAction(WINDOW_CLOSE)
                .withCaption(messages.getMainMessage("actions.Cancel"))
                .withHandler(this::cancel);

        window.addAction(closeAction);
    }

    protected void setupEntityToEdit(@SuppressWarnings("unused") BeforeShowEvent event) {
        if (getEntityStates().isNew(entityToEdit) || doNotReloadEditedEntity()) {
            T mergedEntity = getScreenData().getDataContext().merge(entityToEdit);

            fireEvent(InitEntityEvent.class, new InitEntityEvent<>(this, mergedEntity));

            InstanceContainer<Entity> container = getEditedEntityContainer();
            container.setItem(mergedEntity);
        } else {
            InstanceLoader loader = getEditedEntityLoader();
            loader.setEntityId(entityToEdit.getId());
        }

        // todo pessimistic locking
        // todo security
    }

    protected boolean doNotReloadEditedEntity() {
        if (isEntityModifiedInParentContext()) {
            InstanceContainer<Entity> container = getEditedEntityContainer();
            if (getEntityStates().isLoadedWithView(entityToEdit, container.getView())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isEntityModifiedInParentContext() {
        DataContext parentDc = getScreenData().getDataContext().getParent();
        if (parentDc == null)
            return false;

        return isEntityModifiedRecursive(entityToEdit, parentDc, new HashSet<>());
    }

    protected boolean isEntityModifiedRecursive(Entity entity, DataContext dataContext, HashSet<Object> visited) {
        if (visited.contains(entity))
            return false;
        visited.add(entity);

        if (dataContext.isModified(entity) || dataContext.isRemoved(entity))
            return true;

        for (MetaProperty property : entity.getMetaClass().getProperties()) {
            if (property.getRange().isClass()) {
                if (getEntityStates().isLoaded(entity, property.getName())) {
                    Object value = entity.getValue(property.getName());
                    if (value != null) {
                        if (value instanceof Collection) {
                            for (Object item : ((Collection) value)) {
                                if (isEntityModifiedRecursive((Entity) item, dataContext, visited))
                                    return true;
                            }
                        } else {
                            if (isEntityModifiedRecursive((Entity) value, dataContext, visited))
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected InstanceLoader getEditedEntityLoader() {
        InstanceContainer<Entity> container = getEditedEntityContainer();
        if (container == null) {
            throw new IllegalStateException("Edited entity container not defined");
        }
        DataLoader loader = null;
        if (container instanceof HasLoader) {
            loader = ((HasLoader) container).getLoader();
        }
        if (loader == null) {
            throw new IllegalStateException("Loader of edited entity container not found");
        }
        if (!(loader instanceof InstanceLoader)) {
            throw new IllegalStateException(String.format(
                    "Loader %s of edited entity container %s must implement InstanceLoader", loader, container));
        }
        return (InstanceLoader) loader;
    }

    protected InstanceContainer<Entity> getEditedEntityContainer() {
        EditedEntityContainer annotation = getClass().getAnnotation(EditedEntityContainer.class);
        if (annotation == null || Strings.isNullOrEmpty(annotation.value())) {
            throw new IllegalStateException(
                    String.format("StandardEditor %s does not declare @EditedEntityContainer", getClass())
            );
        }

        return getScreenData().getContainer(annotation.value());
    }

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

    private EntityStates getEntityStates() {
        return getBeanLocator().get(EntityStates.NAME);
    }

    protected void commitAndClose(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        closeWithCommit();
    }

    protected void commit(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        commitChanges()
                .then(() -> commitActionPerformed = true);
    }

    protected void cancel(@SuppressWarnings("unused") Action.ActionPerformedEvent event) {
        close(commitActionPerformed ?
                WINDOW_COMMIT_AND_CLOSE_ACTION : WINDOW_DISCARD_AND_CLOSE_ACTION);
    }

    @TriggerOnce
    public static class InitEntityEvent<E> extends EventObject {
        private final E entity;

        public InitEntityEvent(Screen source, E entity) {
            super(source);
            this.entity = entity;
        }

        @Override
        public Screen getSource() {
            return (Screen) super.getSource();
        }

        public E getEntity() {
            return entity;
        }
    }

    protected Subscription addInitEntityListener(Consumer<InitEntityEvent> listener) {
        return getEventHub().subscribe(InitEntityEvent.class, listener);
    }
}