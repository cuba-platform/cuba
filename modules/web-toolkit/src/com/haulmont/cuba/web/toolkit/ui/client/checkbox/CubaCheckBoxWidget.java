/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.checkbox;

import com.google.gwt.i18n.client.HasDirection;
import com.vaadin.client.ui.VCheckBox;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaCheckBoxWidget extends VCheckBox {

    protected boolean captionManagedByLayout = false;

    @Override
    public void setText(String text) {
        if (!captionManagedByLayout) {
            super.setText(text);
        }
    }

    @Override
    public void setText(String text, HasDirection.Direction dir) {
        if (!captionManagedByLayout) {
            super.setText(text, dir);
        }
    }
}
