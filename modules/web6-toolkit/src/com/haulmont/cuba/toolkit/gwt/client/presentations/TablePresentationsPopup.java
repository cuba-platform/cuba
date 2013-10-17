/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 28.09.2010 12:08:22
 *
 * $Id$
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
