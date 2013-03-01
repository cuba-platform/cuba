/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Element;
import com.haulmont.cuba.web.toolkit.ui.CubaGroupBox;
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
@Connect(value = CubaGroupBox.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaGroupBoxConnector extends PanelConnector {

    @Override
    public CubaGroupBoxWidget getWidget() {
        return (CubaGroupBoxWidget) super.getWidget();
    }

    @Override
    protected CubaGroupBoxWidget createWidget() {
        CubaGroupBoxWidget groupBoxWidget = GWT.create(CubaGroupBoxWidget.class);
        groupBoxWidget.expandHandler = new CubaGroupBoxWidget.ExpandHandler() {
            @Override
            public void expand() {
                getRpcProxy(CubaGroupBoxServerRpc.class).expand();
            }

            @Override
            public void collapse() {
                getRpcProxy(CubaGroupBoxServerRpc.class).collapse();
            }
        };
        return groupBoxWidget;
    }

    @Override
    public CubaGroupBoxState getState() {
        return (CubaGroupBoxState) super.getState();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // replace VPanel classnames
        replaceClassNames(getWidget().captionNode);
        replaceClassNames(getWidget().contentNode);
        replaceClassNames(getWidget().bottomDecoration);
        replaceClassNames(getWidget().getElement());
    }

    @Override
    public void layout() {
        super.layout();

        // fix padding
        getWidget().legend.getStyle().clearMarginTop();

        Style style = getWidget().getElement().getStyle();
        style.clearPaddingTop();
        style.clearPaddingBottom();
    }

    private void replaceClassNames(Element element) {
        String className = element.getClassName();
        String newClassName = "";
        String[] classNames = className.split(" ");
        for (String classNamePart : classNames) {
            if (classNamePart.startsWith(VPanel.CLASSNAME + "-"))
                classNamePart = classNamePart.replace(VPanel.CLASSNAME + "-", CubaGroupBoxWidget.CLASSNAME + "-");
            else if (classNamePart.equals(VPanel.CLASSNAME))
                classNamePart = CubaGroupBoxWidget.CLASSNAME;

            newClassName = newClassName + " " + classNamePart;
        }
        element.setClassName(newClassName.trim());
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        getWidget().setCollapsable(getState().collapsable);
        getWidget().setExpanded(getState().expanded);
    }
}