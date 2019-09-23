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

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ActionType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.actions.ListAction;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.components.data.meta.EmptyDataUnit;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.HasLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Standard action for reloading a list of entities from the database.
 * <p>
 * Should be defined for a list component ({@code Table}, {@code DataGrid}, etc.) in a screen XML descriptor.
 */
@ActionType(RefreshAction.ID)
public class RefreshAction extends ListAction {

    public static final String ID = "refresh";

    protected Messages messages;

    private static final Logger log = LoggerFactory.getLogger(RefreshAction.class);

    public RefreshAction() {
        super(ID);
    }

    public RefreshAction(String id) {
        super(id);
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.REFRESH_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
        this.caption = messages.getMainMessage("actions.Refresh");
    }

    @Override
    public void actionPerform(Component component) {
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            execute();
        } else {
            super.actionPerform(component);
        }
    }

    /**
     * Executes the action.
     */
    public void execute() {
        if (target == null) {
            throw new IllegalStateException("RefreshAction target is not set");
        }

        if (target.getItems() instanceof EmptyDataUnit) {
            return;
        }

        if (!(target.getItems() instanceof ContainerDataUnit)) {
            throw new IllegalStateException("RefreshAction target is null or does not implement SupportsContainerBinding");
        }

        CollectionContainer container = ((ContainerDataUnit) target.getItems()).getContainer();
        if (container == null) {
            throw new IllegalStateException("RefreshAction target is not bound to CollectionContainer");
        }

        DataLoader loader = null;
        if (container instanceof HasLoader) {
            loader = ((HasLoader) container).getLoader();
        }
        if (loader != null) {
            loader.load();
        } else {
            log.warn("RefreshAction '{}' target container has no loader, refresh is impossible", getId());
        }
    }
}