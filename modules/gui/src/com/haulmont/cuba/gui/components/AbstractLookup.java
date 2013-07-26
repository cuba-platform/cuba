/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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

    /**
     * DEPRECATED - use default constructor!
     */
    @Deprecated
    public AbstractLookup(IFrame frame) {
        super(frame);
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