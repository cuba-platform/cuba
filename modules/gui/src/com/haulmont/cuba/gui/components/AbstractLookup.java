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

    /**
     * DEPRECATED - use default constructor!
     */
    @Deprecated
    public AbstractLookup(IFrame frame) {
        super(frame);
    }

    public Component getLookupComponent() {
        return ((Lookup) frame).getLookupComponent();
    }

    public void setLookupComponent(Component lookupComponent) {
        ((Lookup) frame).setLookupComponent(lookupComponent);
    }

    public Handler getLookupHandler() {
        return ((Lookup) frame).getLookupHandler();
    }

    public void setLookupHandler(Handler handler) {
        ((Lookup) frame).setLookupHandler(handler);
    }

    public Validator getLookupValidator() {
        return ((Lookup) frame).getLookupValidator();
    }

    public void setLookupValidator(Validator validator) {
        ((Lookup) frame).setLookupValidator(validator);
    }
}
