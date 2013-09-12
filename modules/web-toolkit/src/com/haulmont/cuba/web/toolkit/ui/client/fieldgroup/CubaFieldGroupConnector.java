/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgroup;

import com.google.gwt.core.client.GWT;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroup;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.panel.PanelConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaFieldGroup.class)
public class CubaFieldGroupConnector extends PanelConnector {

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
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // replace VPanel classnames
        Tools.replaceClassNames(getWidget().captionNode, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().captionWrap, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().contentNode, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().bottomDecoration, VPanel.CLASSNAME, getWidget().getStylePrimaryName());
        Tools.replaceClassNames(getWidget().getElement(), VPanel.CLASSNAME, getWidget().getStylePrimaryName());
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("borderVisible")) {
            getWidget().setBorderVisible(getState().borderVisible);
        }
    }
}