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
package com.haulmont.cuba.web.gui;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.*;
import com.vaadin.ui.Button;

import java.util.Collection;
import java.util.Collections;

/**
 */
public class SelectAction implements Button.ClickListener {
    protected Window.Lookup window;

    public SelectAction(Window.Lookup window) {
        this.window = window;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        Window.Lookup.Validator validator = window.getLookupValidator();
        if (validator != null && !validator.validate()) return;
        
        final Component lookupComponent = window.getLookupComponent();
        if (lookupComponent == null)
            throw new IllegalStateException("lookupComponent is not set");

        Collection selected;
        if (lookupComponent instanceof Table ) {
            selected = ((Table) lookupComponent).getSelected();
        } else if (lookupComponent instanceof Tree) {
            selected = ((Tree) lookupComponent).getSelected();
        } else if (lookupComponent instanceof LookupField) {
            selected = Collections.singleton(((LookupField) lookupComponent).getValue());
        } else if (lookupComponent instanceof PickerField) {
            selected = Collections.singleton(((PickerField) lookupComponent).getValue());
        } else if (lookupComponent instanceof OptionsGroup) {
            final OptionsGroup optionsGroup = (OptionsGroup) lookupComponent;
            Object value = optionsGroup.getValue();
			if (value instanceof Collection)
			    selected = (Collection)value;
			else
				selected = Collections.singleton(value);
        } else {
            throw new UnsupportedOperationException("Unsupported lookupComponent type: " + lookupComponent.getClass());
        }

        final Window.Lookup.Handler lookupHandler = window.getLookupHandler();

        window.close(Window.SELECT_ACTION_ID);
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        for (Object obj : selected) {
            if (obj instanceof Entity) {
                metadataTools.traverseAttributes((Entity) obj, (entity, property) -> entity.removeAllListeners());
            }
        }
        if (lookupHandler != null) {
            lookupHandler.handleLookup(selected);
        } else {
            throw new DevelopmentException("A Lookup.Handler was not passed to lookup window " + window.getId());
        }
    }
}