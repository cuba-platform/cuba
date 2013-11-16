/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components.filter;

import javax.swing.*;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class ParamEditorComponent {

    protected JComponent component;

    protected ParamEditorComponent(JComponent component) {
        this.component = component;
    }

    public JComponent getComponent() {
        return component;
    }

    public abstract boolean isRequired();

    public abstract void setRequired(boolean required);
}