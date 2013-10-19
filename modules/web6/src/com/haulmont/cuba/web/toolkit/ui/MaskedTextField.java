/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VMaskedTextField;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

import java.util.Map;

@ClientWidget(VMaskedTextField.class)
public class MaskedTextField extends TextField {

	private String mask;

    private boolean maskedMode = false;

	public MaskedTextField() {
	}

    public boolean isMaskedMode() {
        return maskedMode;
    }

    public void setMaskedMode(boolean maskedMode) {
        this.maskedMode = maskedMode;
        requestRepaint();
    }

    public void setMask(String mask) {
		this.mask = mask;
		requestRepaint();
	}

    public String getMask() {
        return mask;
    }

    public void setReadOnly(boolean readOnly) {
        if (readOnly == isReadOnly())
            return;
        super.setReadOnly(readOnly);
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
    }

    @Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);

		if (mask != null) {
			target.addAttribute("mask", mask);
		}
        target.addAttribute("maskedMode", maskedMode);
	}
}
