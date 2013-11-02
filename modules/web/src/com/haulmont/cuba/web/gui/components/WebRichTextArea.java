/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.vaadin.ui.RichTextArea;

/**
 * @author abramov
 * @version $Id$
 */
public class WebRichTextArea
    extends
        WebAbstractField<RichTextArea>
    implements
        com.haulmont.cuba.gui.components.RichTextArea {

    public WebRichTextArea() {
        component = new RichTextArea();
        attachListener(component);

        component.setImmediate(true);
        component.setNullRepresentation("");
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
    }
}