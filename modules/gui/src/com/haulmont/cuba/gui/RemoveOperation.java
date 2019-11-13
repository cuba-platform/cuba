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

package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.Component.Focusable;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.data.DataUnit;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.gui.model.ScreenData;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.ScreenContext;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.screen.UiControllerUtils.getScreenContext;

/**
 * Class that provides fluent interface for removing entity instances. <br>
 * Inject the class into your screen controller and use {@link #builder(Class, FrameOwner)} method as an entry point.
 */
@Component(RemoveOperation.NAME)
public class RemoveOperation {

    public static final String NAME = "cuba_RemoveHelper";

    @Inject
    protected WindowConfig windowConfig;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Messages messages;
    @Inject
    protected ExtendedEntities extendedEntities;
    @Inject
    protected EntityStates entityStates;

    /**
     * Creates a remove builder.
     *
     * @param entityClass entity class
     * @param origin      invoking screen
     * @param <E>         type of entity
     */
    public <E extends Entity> RemoveBuilder<E> builder(Class<E> entityClass, FrameOwner origin) {
        checkNotNullArgument(entityClass);
        checkNotNullArgument(origin);

        return new RemoveBuilder<>(origin, entityClass, this::triggerAction);
    }

    /**
     * Creates a remove builder using list component, e.g. {@link Table} or {@link DataGrid}.
     *
     * @param listComponent list component
     * @param <E>           type of entity
     */
    public <E extends Entity> RemoveBuilder<E> builder(ListComponent<E> listComponent) {
        checkNotNullArgument(listComponent);
        checkNotNullArgument(listComponent.getFrame());

        FrameOwner frameOwner = listComponent.getFrame().getFrameOwner();
        Class<E> entityClass;
        DataUnit items = listComponent.getItems();
        if (items instanceof ContainerDataUnit) {
            entityClass = ((ContainerDataUnit) items).getEntityMetaClass().getJavaClass();
        } else {
            throw new IllegalStateException(String.format("Component %s is not bound to data", listComponent));
        }

        return builder(entityClass, frameOwner)
                .withListComponent(listComponent);
    }

    /**
     * Removes selected items from the list component with confirmation dialog. <br>
     * After confirmation removes items from DB if the bound container is not nested.
     *
     * @param target list component
     * @param <E>    entity type
     */
    public <E extends Entity> void removeSelected(ListComponent<E> target) {
        builder(target)
                .withConfirmation(true)
                .remove();
    }

    /**
     * Excludes selected items from the list component without confirmation. Works with nested containers only.
     *
     * @param target list component
     * @param <E>    entity type
     */
    public <E extends Entity> void excludeSelected(ListComponent<E> target) {
        builder(target)
                .withConfirmation(false)
                .exclude();
    }

    protected <E extends Entity> void triggerAction(RemoveBuilder<E> builder) {
        List<E> selectedItems = Collections.emptyList();
        if (builder.getItems() != null) {
            selectedItems = builder.getItems();
        } else if (builder.getListComponent() != null) {
            selectedItems = new ArrayList<>(builder.getListComponent().getSelected());
        }

        if (!selectedItems.isEmpty()) {
            if (builder.isConfirmationRequired()) {
                performActionWithConfirmation(builder, selectedItems);
            } else {
                performAction(builder, selectedItems);
            }
        }
    }

    protected <E extends Entity> void performAction(RemoveBuilder<E> builder, List<E> selectedItems) {
        if (builder.getBeforeActionPerformedHandler() != null) {
            BeforeActionPerformedEvent<E> event = new BeforeActionPerformedEvent<>(builder.getOrigin(), selectedItems);
            builder.getBeforeActionPerformedHandler().accept(event);

            if (event.isActionPrevented()) {
                // do not perform action
                return;
            }
        }

        if (builder.getOperation() == Operation.EXCLUDE) {
            excludeItems(builder, selectedItems);
        } else {
            removeItems(builder, selectedItems);
        }

        if (builder.getAfterActionPerformedHandler() != null) {
            AfterActionPerformedEvent<E> event = new AfterActionPerformedEvent<>(builder.origin, selectedItems);
            builder.getAfterActionPerformedHandler().accept(event);
        }
    }

    protected <E extends Entity> void removeItems(RemoveBuilder<E> builder, List<E> selectedItems) {
        FrameOwner origin = builder.getOrigin();
        ScreenData screenData = UiControllerUtils.getScreenData(origin);

        CollectionContainer<E> container = getCollectionContainer(builder);

        commitIfNeeded(selectedItems, container, screenData);

        if (selectedItems.size() == 1) {
            container.getMutableItems().remove(selectedItems.get(0));
        } else {
            container.getMutableItems().removeAll(selectedItems);
        }

        focusListComponent(builder);
    }

    protected <E extends Entity> void focusListComponent(RemoveBuilder<E> builder) {
        if (builder.getListComponent() instanceof Focusable) {
            ((Focusable) builder.getListComponent()).focus();
        }
    }

    protected void commitIfNeeded(Collection<? extends Entity> entitiesToRemove, CollectionContainer container,
                                  ScreenData screenData) {

        List<? extends Entity> entitiesToCommit = entitiesToRemove.stream()
                .filter(entity -> !entityStates.isNew(entity))
                .collect(Collectors.toList());

        boolean needCommit = !entitiesToCommit.isEmpty();
        if (container instanceof Nested) {
            InstanceContainer masterContainer = ((Nested) container).getMaster();
            String property = ((Nested) container).getProperty();

            MetaClass masterMetaClass = masterContainer.getEntityMetaClass();
            MetaProperty metaProperty = masterMetaClass.getPropertyNN(property);

            needCommit = needCommit && (metaProperty.getType() != MetaProperty.Type.COMPOSITION);
        }

        if (needCommit) {
            CommitContext commitContext = new CommitContext();
            for (Entity entity : entitiesToCommit) {
                commitContext.addInstanceToRemove(entity);
            }
            dataManager.commit(commitContext);
            for (Entity entity : entitiesToRemove) {
                screenData.getDataContext().evict(entity);
            }
        } else {
            for (Entity entity : entitiesToRemove) {
                screenData.getDataContext().remove(entity);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> void excludeItems(RemoveBuilder<E> builder, List<E> selectedItems) {
        CollectionContainer<E> container = getCollectionContainer(builder);

        if (!(container instanceof Nested)) {
            throw new IllegalArgumentException("Exclude action supports only Nested containers");
        }

        InstanceContainer masterDc = ((Nested) container).getMaster();

        String property = ((Nested) container).getProperty();
        Entity masterItem = masterDc.getItem();

        MetaProperty metaProperty = masterItem.getMetaClass().getPropertyNN(property);
        MetaProperty inverseMetaProperty = metaProperty.getInverse();

        if (inverseMetaProperty != null
                && !inverseMetaProperty.getRange().getCardinality().isMany()) {

            Class inversePropClass = extendedEntities.getEffectiveClass(inverseMetaProperty.getDomain());
            Class dcClass = extendedEntities.getEffectiveClass(container.getEntityMetaClass());

            if (inversePropClass.isAssignableFrom(dcClass)) {
                // update reference for One-To-Many
                for (Entity item : selectedItems) {
                    item.setValue(inverseMetaProperty.getName(), null);
                }
            }
        }

        container.getMutableItems().removeAll(selectedItems);

        focusListComponent(builder);
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> CollectionContainer<E> getCollectionContainer(RemoveBuilder<E> builder) {
        CollectionContainer<E> container;
        if (builder.getContainer() != null) {
            container = builder.getContainer();
        } else if (builder.getListComponent() != null) {
            DataUnit items = builder.getListComponent().getItems();
            container = ((ContainerDataUnit) items).getContainer();
        } else {
            throw new IllegalArgumentException("Neither container nor list component is specified");
        }
        return container;
    }

    @SuppressWarnings("CodeBlock2Expr")
    protected <E extends Entity> void performActionWithConfirmation(RemoveBuilder<E> builder, List<E> selectedItems) {
        ScreenContext screenContext = getScreenContext(builder.getOrigin());

        Dialogs dialogs = screenContext.getDialogs();

        String title = builder.getConfirmationTitle() != null ?
                builder.getConfirmationTitle() : messages.getMainMessage("dialogs.Confirmation");

        String message = builder.getConfirmationMessage() != null ?
                builder.getConfirmationMessage() : messages.getMainMessage("dialogs.Confirmation.Remove");

        dialogs.createOptionDialog()
                .withCaption(title)
                .withMessage(message)
                .withActions(
                        new DialogAction(DialogAction.Type.YES).withHandler(e -> {

                            performAction(builder, selectedItems);
                        }),
                        new DialogAction(DialogAction.Type.NO).withHandler(e -> {
                            focusListComponent(builder);

                            if (builder.getActionCancelledHandler() != null) {
                                ActionCancelledEvent<E> event =
                                        new ActionCancelledEvent<>(builder.getOrigin(), selectedItems);
                                builder.getActionCancelledHandler().accept(event);
                            }
                        })
                )
                .show();
    }

    /**
     * Remove builder.
     *
     * @param <E> entity type
     */
    public static class RemoveBuilder<E extends Entity> {

        protected final FrameOwner origin;
        protected final Class<E> entityClass;
        protected final Consumer<RemoveBuilder<E>> handler;

        protected Operation operation;

        protected ListComponent<E> listComponent;
        protected CollectionContainer<E> container;
        protected List<E> items;

        protected boolean confirmation = true;
        protected String confirmationMessage;
        protected String confirmationTitle;

        protected Consumer<BeforeActionPerformedEvent> beforeActionPerformedHandler;
        protected Consumer<AfterActionPerformedEvent> afterActionPerformedHandler;
        protected Consumer<ActionCancelledEvent> actionCancelledHandler;

        public RemoveBuilder(FrameOwner origin, Class<E> entityClass, Consumer<RemoveBuilder<E>> actionHandler) {
            this.origin = origin;
            this.entityClass = entityClass;
            this.handler = actionHandler;
        }

        public RemoveBuilder<E> withListComponent(ListComponent<E> listComponent) {
            this.listComponent = listComponent;
            return this;
        }

        public RemoveBuilder<E> withContainer(CollectionContainer<E> container) {
            this.container = container;
            return this;
        }

        public RemoveBuilder<E> withItems(List<E> items) {
            this.items = items;
            return this;
        }

        /**
         * Sets whether to ask confirmation from the user.
         */
        public RemoveBuilder<E> withConfirmation(boolean confirmation) {
            this.confirmation = confirmation;
            return this;
        }

        /**
         * Sets confirmation dialog message.
         */
        public RemoveBuilder<E> withConfirmationMessage(String confirmationMessage) {
            this.confirmationMessage = confirmationMessage;
            return this;
        }

        /**
         * Sets confirmation dialog title.
         */
        public RemoveBuilder<E> withConfirmationTitle(String confirmationTitle) {
            this.confirmationTitle = confirmationTitle;
            return this;
        }

        public RemoveBuilder<E> beforeActionPerformed(Consumer<BeforeActionPerformedEvent> handler) {
            this.beforeActionPerformedHandler = handler;
            return this;
        }

        public RemoveBuilder<E> afterActionPerformed(Consumer<AfterActionPerformedEvent> handler) {
            this.afterActionPerformedHandler = handler;
            return this;
        }

        public RemoveBuilder<E> onCancel(Consumer<ActionCancelledEvent> handler) {
            this.actionCancelledHandler = handler;
            return this;
        }

        public ListComponent<E> getListComponent() {
            return listComponent;
        }

        public CollectionContainer<E> getContainer() {
            return container;
        }

        public List<E> getItems() {
            return items;
        }

        public String getConfirmationTitle() {
            return confirmationTitle;
        }

        public String getConfirmationMessage() {
            return confirmationMessage;
        }

        public boolean isConfirmationRequired() {
            return confirmation;
        }

        public FrameOwner getOrigin() {
            return origin;
        }

        public Class<E> getEntityClass() {
            return entityClass;
        }

        public Operation getOperation() {
            return operation;
        }

        public Consumer<BeforeActionPerformedEvent> getBeforeActionPerformedHandler() {
            return beforeActionPerformedHandler;
        }

        public Consumer<AfterActionPerformedEvent> getAfterActionPerformedHandler() {
            return afterActionPerformedHandler;
        }

        public Consumer<ActionCancelledEvent> getActionCancelledHandler() {
            return actionCancelledHandler;
        }

        /**
         * Excludes items from relation: One-To-Many or Many-To-Many.
         */
        public void exclude() {
            this.operation = Operation.EXCLUDE;

            this.handler.accept(this);
        }

        /**
         * Removes items.
         */
        public void remove() {
            this.operation = Operation.REMOVE;

            this.handler.accept(this);
        }
    }

    protected enum Operation {
        REMOVE,
        EXCLUDE
    }

    public static class BeforeActionPerformedEvent<E> extends EventObject {
        protected final List<E> items;
        protected boolean actionPrevented = false;

        public BeforeActionPerformedEvent(FrameOwner origin, List<E> items) {
            super(origin);

            this.items = items;
        }

        @Override
        public FrameOwner getSource() {
            return (FrameOwner) super.getSource();
        }

        public FrameOwner getScreen() {
            return (FrameOwner) super.getSource();
        }

        public List<E> getItems() {
            return items;
        }

        public boolean isActionPrevented() {
            return actionPrevented;
        }

        public void preventAction() {
            this.actionPrevented = true;
        }
    }

    public static class AfterActionPerformedEvent<E> extends EventObject {
        protected final List<E> items;

        public AfterActionPerformedEvent(FrameOwner origin, List<E> items) {
            super(origin);

            this.items = items;
        }

        @Override
        public FrameOwner getSource() {
            return (FrameOwner) super.getSource();
        }

        public FrameOwner getScreen() {
            return (FrameOwner) super.getSource();
        }

        public List<E> getItems() {
            return items;
        }
    }

    public static class ActionCancelledEvent<E> extends EventObject {
        protected final List<E> items;

        public ActionCancelledEvent(FrameOwner origin, List<E> items) {
            super(origin);

            this.items = items;
        }

        @Override
        public FrameOwner getSource() {
            return (FrameOwner) super.getSource();
        }

        public FrameOwner getScreen() {
            return (FrameOwner) super.getSource();
        }

        public List<E> getItems() {
            return items;
        }
    }
}