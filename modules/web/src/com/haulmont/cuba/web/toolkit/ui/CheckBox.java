/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 08.07.2010 19:01:10
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

@SuppressWarnings("serial")
public class CheckBox extends com.vaadin.ui.CheckBox {

    private boolean layoutCaption = false;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (isLayoutCaption()) {
            target.addAttribute("manageCaption", isLayoutCaption());
        }
    }

    public boolean isLayoutCaption() {
        return layoutCaption;
    }

    public void setLayoutCaption(boolean layoutCaption) {
        this.layoutCaption = layoutCaption;
        requestRepaint();
    }
}
