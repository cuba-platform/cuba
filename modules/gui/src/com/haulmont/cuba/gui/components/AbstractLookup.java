/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.02.2009 12:00:29
 * $Id$
 */
package com.haulmont.cuba.gui.components;

/**
 * Base class for lookup screen controllers
 */
public class AbstractLookup extends AbstractWindow implements Window.Lookup {

    public AbstractLookup() {
    }

    public AbstractLookup(IFrame frame) {
        super(frame);
    }

    public Component getLookupComponent() {
        if (frame instanceof Window.Lookup) {
            return ((Lookup) frame).getLookupComponent();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setLookupComponent(Component lookupComponent) {
        if (frame instanceof Window.Lookup) {
            ((Lookup) frame).setLookupComponent(lookupComponent);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Handler getLookupHandler() {
        if (frame instanceof Window.Lookup) {
            return ((Lookup) frame).getLookupHandler();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setLookupHandler(Handler handler) {
        if (frame instanceof Window.Lookup) {
            ((Lookup) frame).setLookupHandler(handler);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public Validator getLookupValidator() {
        if (frame instanceof Window.Lookup) {
            return ((Lookup) frame).getLookupValidator();
        } else {
            throw new UnsupportedOperationException();
        }
    }


    public void setLookupValidator(Validator validator) {
        if (frame instanceof Window.Lookup) {
            ((Lookup) frame).setLookupValidator(validator);
        }
    }
}
