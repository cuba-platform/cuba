/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.gridlayout;

import com.google.gwt.dom.client.Style;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.ManagedLayout;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;

/**
 * Component slot with horizontal layout for caption and component
 *
 * @author artamonov
 * @version $Id$
 */
public class CubaFieldGroupLayoutComponentSlot extends ComponentConnectorLayoutSlot {

    public CubaFieldGroupLayoutComponentSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);
    }

    @Override
    public void setCaption(VCaption caption) {
        super.setCaption(caption);

        if (caption != null) {
            // tune style, force horizontal layout
            Style style = caption.getElement().getStyle();
            style.setPosition(Style.Position.RELATIVE);
            style.setDisplay(Style.Display.INLINE_BLOCK);
            style.clearTop();
            style.clearLeft();
        }
    }

    @Override
    protected boolean isCaptionInline() {
        return true;
    }
}