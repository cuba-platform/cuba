/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.ui.ClientWidget;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.PaintException;
import com.haulmont.cuba.toolkit.gwt.client.ui.VIndicatedTextField;

@SuppressWarnings("serial")
@ClientWidget(VIndicatedTextField.class)
public class IndicatedTextField extends TextField {

    protected boolean indicator;

    public boolean isIndicator() {
        return indicator;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (isIndicator()) {
            target.addAttribute("indicator", true);
        }
    }

}
