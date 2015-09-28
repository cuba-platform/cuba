/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author abramov
 * @version $Id$
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