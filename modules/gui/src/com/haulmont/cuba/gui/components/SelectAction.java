/*
 * Copyright (c) 2008-2017 Haulmont.
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
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.MetadataTools;

import java.util.Collection;

/**
 * An action used in the lookup screens to select an item.
 * <p>
 * This action is automatically added to a screen if it is open via the {@code openLookup()} method.
 */
public class SelectAction extends AbstractAction {

    protected Window.Lookup window;

    public SelectAction(Window.Lookup window) {
        super(WindowDelegate.LOOKUP_SELECT_ACTION_ID);
        this.window = window;
        setShortcut(AppBeans.get(Configuration.class).getConfig(ClientConfig.class).getCommitShortcut());
    }

    @Override
    public void actionPerform(Component component) {
        if (!validate())
            return;

        LookupComponent lookupComponent = getLookupComponent();
        Window.Lookup.Handler lookupHandler = getLookupHandler();

        window.close(Window.SELECT_ACTION_ID);

        Collection selected = getSelectedItems(lookupComponent);
        removeListeners(selected);
        lookupHandler.handleLookup(selected);
    }

    protected Collection getSelectedItems(LookupComponent lookupComponent) {
        return lookupComponent.getLookupSelectedItems();
    }

    protected boolean validate() {
        Window.Lookup.Validator validator = window.getLookupValidator();
        if (validator != null && !validator.validate())
            return false;
        return true;
    }

    protected LookupComponent getLookupComponent() {
        Component lookupComponent = window.getLookupComponent();
        if (lookupComponent == null)
            throw new IllegalStateException("lookupComponent is not set");
        if (!(lookupComponent instanceof LookupComponent))
            throw new UnsupportedOperationException("Unsupported lookupComponent type: " + lookupComponent.getClass());
        return (LookupComponent) lookupComponent;
    }

    protected Window.Lookup.Handler getLookupHandler() {
        Window.Lookup.Handler lookupHandler = window.getLookupHandler();
        if (lookupHandler == null)
            throw new DevelopmentException("Lookup.Handler was not passed to lookup window " + window.getId());
        return lookupHandler;
    }

    protected void removeListeners(Collection selected) {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        for (Object obj : selected) {
            if (obj instanceof Entity) {
                metadataTools.traverseAttributes((Entity) obj, (entity, property) -> entity.removeAllListeners());
            }
        }
    }
}