/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.checkbox.CubaCheckBoxState;
import com.vaadin.ui.CheckBox;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaCheckBox extends CheckBox {

    public CubaCheckBox() {
    }

    public CubaCheckBox(String caption) {
        super(caption);
    }

    @Override
    protected CubaCheckBoxState getState() {
        return (CubaCheckBoxState) super.getState();
    }

    @Override
    protected CubaCheckBoxState getState(boolean markAsDirty) {
        return (CubaCheckBoxState) super.getState(markAsDirty);
    }

    public boolean isCaptionManagedByLayout() {
        return getState(false).captionManagedByLayout;
    }

    public void setCaptionManagedByLayout(boolean captionManagedByLayout) {
        if (isCaptionManagedByLayout() != captionManagedByLayout) {
            getState().captionManagedByLayout = captionManagedByLayout;
        }
    }
}