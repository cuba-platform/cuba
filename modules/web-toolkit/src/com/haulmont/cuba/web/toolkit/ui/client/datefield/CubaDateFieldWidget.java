/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.datefield;

import com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaMaskedFieldWidget;
import com.vaadin.client.ui.VPopupCalendar;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaDateFieldWidget extends VPopupCalendar {

    private static final String CLASSNAME = "cuba-datefield";

    private static final String EMPTY_FIELD_CLASS = "cuba-datefield-empty";

    public CubaDateFieldWidget() {
        setStylePrimaryName(CLASSNAME);
        setStyleName(CLASSNAME);
    }

    public CubaMaskedFieldWidget getImpl() {
        return (CubaMaskedFieldWidget) super.getImpl();
    }

    @Override
    protected CubaMaskedFieldWidget createImpl() {
        return new CubaMaskedFieldWidget() {
            public void valueChange(boolean blurred) {
                String newText = getText();
                if (!prompting && newText != null
                        && !newText.equals(valueBeforeEdit)) {
                    if (validateText(newText)) {
                        if (!newText.toString().equals(nullRepresentation)) {
                            getElement().removeClassName(EMPTY_FIELD_CLASS);
                        }
                        CubaDateFieldWidget.this.onChange(null);
                        valueBeforeEdit = newText;
                    } else {
                        setText(valueBeforeEdit);
                    }
                }
            }
        };
    }


}