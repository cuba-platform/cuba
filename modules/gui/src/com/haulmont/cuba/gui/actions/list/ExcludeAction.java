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

package com.haulmont.cuba.gui.actions.list;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.RemoveOperation;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.Nested;
import com.haulmont.cuba.gui.screen.Install;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.function.Consumer;

/**
 * Standard action for excluding entity instances from the list. The excluded entities are not deleted.
 * <p>
 * Should be defined for a list component ({@code Table}, {@code DataGrid}, etc.) connected to a nested data container.
 * <p>
 * The action instance can be parameterized using the nested {@code properties} XML element or programmatically in the
 * screen controller.
 */
@ActionType(ExcludeAction.ID)
public class ExcludeAction extends SecuredListAction implements Action.DisabledWhenScreenReadOnly {

    public static final String ID = "exclude";

    @Inject
    protected RemoveOperation removeOperation;

    protected Boolean confirmation;
    protected String confirmationMessage;
    protected String confirmationTitle;
    protected Consumer<RemoveOperation.AfterActionPerformedEvent> afterActionPerformedHandler;
    protected Consumer<RemoveOperation.ActionCancelledEvent> actionCancelledHandler;

    public ExcludeAction() {
        super(ID);
    }

    public ExcludeAction(String id) {
        super(id);
    }

    /**
     * Returns true/false if the confirmation flag was set by {@link #setConfirmation(Boolean)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public Boolean getConfirmation() {
        return confirmation;
    }

    /**
     * Sets whether to ask confirmation from the user.
     */
    public void setConfirmation(Boolean confirmation) {
        this.confirmation = confirmation;
    }

    /**
     * Returns confirmation dialog message if it was set by {@link #setConfirmationMessage(String)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    /**
     * Sets confirmation dialog message.
     */
    public void setConfirmationMessage(String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    /**
     * Returns confirmation dialog title if it was set by {@link #setConfirmationTitle(String)} or in the screen XML.
     * Otherwise returns null.
     */
    @Nullable
    public String getConfirmationTitle() {
        return confirmationTitle;
    }

    /**
     * Sets confirmation dialog title.
     */
    public void setConfirmationTitle(String confirmationTitle) {
        this.confirmationTitle = confirmationTitle;
    }

    /**
     * Sets the handler to be invoked after excluding entities.
     * <p>
     * The preferred way to set the handler is using a controller method annotated with {@link Install}, e.g.:
     * <pre>
     * &#64;Install(to = "petsTable.exclude", subject = "afterActionPerformedHandler")
     * protected void petsTableExcludeAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent event) {
     *     System.out.println("Removed " + event.getItems());
     * }
     * </pre>
     */
    public void setAfterActionPerformedHandler(Consumer<RemoveOperation.AfterActionPerformedEvent> afterActionPerformedHandler) {
        this.afterActionPerformedHandler = afterActionPerformedHandler;
    }

    /**
     * Sets the handler to be invoked if the action was cancelled by the user.
     * <p>
     * The preferred way to set the handler is using a controller method annotated with {@link Install}, e.g.:
     * <pre>
     * &#64;Install(to = "petsTable.exclude", subject = "actionCancelledHandler")
     * protected void petsTableExcludeActionCancelledHandler(RemoveOperation.ActionCancelledEvent event) {
     *     System.out.println("Cancelled");
     * }
     * </pre>
     */
    public void setActionCancelledHandler(Consumer<RemoveOperation.ActionCancelledEvent> actionCancelledHandler) {
        this.actionCancelledHandler = actionCancelledHandler;
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.EXCLUDE_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.caption = messages.getMainMessage("actions.Exclude");
    }

    @Inject
    protected void setConfiguration(Configuration configuration) {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        setShortcut(clientConfig.getTableRemoveShortcut());
    }

    @Override
    protected boolean isPermitted() {
        if (target == null || !(target.getItems() instanceof ContainerDataUnit)) {
            return false;
        }

        ContainerDataUnit containerDataUnit = (ContainerDataUnit) target.getItems();

        MetaClass metaClass = containerDataUnit.getEntityMetaClass();
        if (metaClass == null) {
            return false;
        }

        if (containerDataUnit.getContainer() instanceof Nested) {
            Nested nestedContainer = (Nested) containerDataUnit.getContainer();

            MetaClass masterMetaClass = nestedContainer.getMaster().getEntityMetaClass();
            MetaProperty metaProperty = masterMetaClass.getPropertyNN(nestedContainer.getProperty());

            boolean attrPermitted = security.isEntityAttrUpdatePermitted(masterMetaClass, metaProperty.getName());
            if (!attrPermitted) {
                return false;
            }
        }

        return super.isPermitted();
    }

    @Override
    public void actionPerform(Component component) {
        // if standard behaviour
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            execute();
        } else {
            super.actionPerform(component);
        }
    }

    /**
     * Executes the action.
     */
    @SuppressWarnings("unchecked")
    public void execute() {
        if (target == null) {
            throw new IllegalStateException("ExcludeAction target is not set");
        }

        if (!(target.getItems() instanceof ContainerDataUnit)) {
            throw new IllegalStateException("ExcludeAction target items is null or does not implement ContainerDataUnit");
        }

        ContainerDataUnit items = (ContainerDataUnit) target.getItems();
        CollectionContainer container = items.getContainer();
        if (container == null) {
            throw new IllegalStateException("ExcludeAction target is not bound to CollectionContainer");
        }

        RemoveOperation.RemoveBuilder builder = removeOperation.builder(target);

        if (confirmation != null) {
            builder = builder.withConfirmation(confirmation);
        } else {
            builder = builder.withConfirmation(true);
        }

        if (confirmationMessage != null) {
            builder = builder.withConfirmationMessage(confirmationMessage);
        }

        if (confirmationTitle != null) {
            builder = builder.withConfirmationTitle(confirmationTitle);
        }

        if (afterActionPerformedHandler != null) {
            builder = builder.afterActionPerformed(afterActionPerformedHandler);
        }

        if (actionCancelledHandler != null) {
            builder = builder.onCancel(actionCancelledHandler);
        }

        builder.exclude();
    }
}