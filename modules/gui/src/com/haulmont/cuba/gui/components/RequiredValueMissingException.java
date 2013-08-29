/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.components;

/**
 * @author krivopustov
 * @version $Id$
 */
public class RequiredValueMissingException extends ValidationException {

    private Component component;

    public RequiredValueMissingException() {
    }

    public RequiredValueMissingException(String message) {
        super(message);
    }

    public RequiredValueMissingException(String message, Component component) {
        super(message);
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}