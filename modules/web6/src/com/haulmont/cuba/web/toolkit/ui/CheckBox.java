/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
