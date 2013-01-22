package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Property;
import com.haulmont.cuba.toolkit.gwt.client.ui.VMaskedTextField;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TextField;

@ClientWidget(VMaskedTextField.class)
public class MaskedTextField extends TextField {
	private static final long serialVersionUID = -5168618178262041249L;

	private String mask;

    private String prevNullRepresentation;

	public MaskedTextField() {
	}

	public MaskedTextField(String string) {
		setCaption(string);
	}

	public MaskedTextField(String string, String mask) {
		setCaption(string);
		setMask(mask);
	}

	public MaskedTextField(Property dataSource) {
		super(dataSource);
	}

	public MaskedTextField(String caption, Property dataSource) {
		super(caption, dataSource);
	}

	public void setMask(String mask) {
		this.mask = mask;
		requestRepaint();
	}

    public void setReadOnly(boolean readOnly) {
        if (readOnly == isReadOnly())
            return;
        if (readOnly) {
            prevNullRepresentation = getNullRepresentation();
            setNullRepresentation("");
        } else
            setNullRepresentation(prevNullRepresentation);
        super.setReadOnly(readOnly);
    }

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);

		if (mask != null) {
			target.addAttribute("mask", mask);
		}
	}
}
