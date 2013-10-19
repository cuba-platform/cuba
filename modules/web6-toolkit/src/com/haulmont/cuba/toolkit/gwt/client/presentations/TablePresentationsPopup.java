/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.presentations;

import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.ui.VCustomComponent;

//todo gorodnov: need to think about a posibility make the following changes in VCustomComponent
public class TablePresentationsPopup extends VCustomComponent {
    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        if (getWidget() instanceof Container) {
            getWidget().setHeight(height);
        }
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if (getWidget() instanceof Container) {
            getWidget().setWidth(width);
        }
    }
}
