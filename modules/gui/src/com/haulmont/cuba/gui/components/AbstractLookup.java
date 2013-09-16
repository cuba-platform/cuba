/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

/**
 * Base class for lookup screen controllers.
 *
 * @author Abramov
 * @version $Id$
 */
public class AbstractLookup extends AbstractWindow implements Window.Lookup {

    public AbstractLookup() {
    }

    @Override
    public Component getLookupComponent() {
        return ((Lookup) frame).getLookupComponent();
    }

    @Override
    public void setLookupComponent(Component lookupComponent) {
        ((Lookup) frame).setLookupComponent(lookupComponent);
    }

    @Override
    public Handler getLookupHandler() {
        return ((Lookup) frame).getLookupHandler();
    }

    @Override
    public void setLookupHandler(Handler handler) {
        ((Lookup) frame).setLookupHandler(handler);
    }

    @Override
    public Validator getLookupValidator() {
        return ((Lookup) frame).getLookupValidator();
    }

    @Override
    public void setLookupValidator(Validator validator) {
        if (frame instanceof Window.Lookup) {
            ((Lookup) frame).setLookupValidator(validator);
        }
    }
}