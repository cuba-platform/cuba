/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgroup;

import com.google.gwt.core.client.GWT;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroup;
import com.haulmont.cuba.web.toolkit.ui.client.groupbox.CubaGroupBoxConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaFieldGroup.class)
public class CubaFieldGroupConnector extends CubaGroupBoxConnector {

    @Override
    public CubaFieldGroupWidget getWidget() {
        return (CubaFieldGroupWidget) super.getWidget();
    }

    @Override
    protected CubaFieldGroupWidget createWidget() {
        return GWT.create(CubaFieldGroupWidget.class);
    }

    @Override
    public CubaFieldGroupState getState() {
        return (CubaFieldGroupState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("borderVisible")) {
            getWidget().setBorderVisible(getState().borderVisible);
        }
    }
}