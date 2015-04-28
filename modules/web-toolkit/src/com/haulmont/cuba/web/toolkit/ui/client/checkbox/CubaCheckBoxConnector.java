/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.checkbox;

import com.haulmont.cuba.web.toolkit.ui.CubaCheckBox;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.checkbox.CheckBoxConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = CubaCheckBox.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaCheckBoxConnector extends CheckBoxConnector {

    @Override
    public boolean delegateCaptionHandling() {
        return getWidget().captionManagedByLayout;
    }

    @Override
    public CubaCheckBoxWidget getWidget() {
        return (CubaCheckBoxWidget) super.getWidget();
    }

    @Override
    public CubaCheckBoxState getState() {
        return (CubaCheckBoxState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        getWidget().captionManagedByLayout = getState().captionManagedByLayout;

        super.onStateChanged(stateChangeEvent);
    }
}