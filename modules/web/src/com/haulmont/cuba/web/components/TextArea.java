/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:12:13
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.itmill.toolkit.ui.RichTextArea;

public class TextArea
    extends
        AbstractField<RichTextArea>
    implements
        com.haulmont.cuba.gui.components.TextArea, Component.Wrapper {

    public TextArea() {
        this.component = new RichTextArea();
        component.setImmediate(true);
    }
}