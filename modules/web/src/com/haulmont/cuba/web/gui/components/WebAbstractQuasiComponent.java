/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.QuasiComponent;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class WebAbstractQuasiComponent implements QuasiComponent {

    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getDebugId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDebugId(String id) {
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEnabled(boolean enabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isVisible() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestFocus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHeightUnits() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeight(String height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getWidthUnits() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setWidth(String width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Alignment getAlignment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAlignment(Alignment alignment) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStyleName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStyleName(String name) {
        throw new UnsupportedOperationException();
    }
}