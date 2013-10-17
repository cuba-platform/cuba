/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.07.2009 11:14:04
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.QuasiComponent;

import java.util.Collection;

public abstract class WebAbstractQuasiComponent implements QuasiComponent {

    private String id;

    public abstract Collection<Component> getRealComponents();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebugId() {
        throw new UnsupportedOperationException();
    }

    public void setDebugId(String id) {
    }

    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }

    public void setEnabled(boolean enabled) {
        throw new UnsupportedOperationException();
    }

    public boolean isVisible() {
        throw new UnsupportedOperationException();
    }

    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    public void requestFocus() {
        throw new UnsupportedOperationException();
    }

    public float getHeight() {
        throw new UnsupportedOperationException();
    }

    public int getHeightUnits() {
        throw new UnsupportedOperationException();
    }

    public void setHeight(String height) {
        throw new UnsupportedOperationException();
    }

    public float getWidth() {
        throw new UnsupportedOperationException();
    }

    public int getWidthUnits() {
        throw new UnsupportedOperationException();
    }

    public void setWidth(String width) {
        throw new UnsupportedOperationException();
    }

    public Alignment getAlignment() {
        throw new UnsupportedOperationException();
    }

    public void setAlignment(Alignment alignment) {
        throw new UnsupportedOperationException();
    }

    public String getStyleName() {
        throw new UnsupportedOperationException();
    }

    public void setStyleName(String name) {
        throw new UnsupportedOperationException();
    }
}
