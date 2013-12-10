/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.datefield;

import com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaMaskedFieldWidget;
import com.vaadin.client.ui.VPopupCalendar;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaDateFieldWidget extends VPopupCalendar {

    protected static final String CLASSNAME = "cuba-datefield";

    protected static final String EMPTY_FIELD_CLASS = "cuba-datefield-empty";

    public CubaDateFieldWidget() {
        setStylePrimaryName(CLASSNAME);
        setStyleName(CLASSNAME);
    }

    @Override
    public void setTextFieldEnabled(boolean textFieldEnabled) {
        super.setTextFieldEnabled(textFieldEnabled);

        calendarToggle.getElement().setTabIndex(-1);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        // Always set -1 tab index for calendarToggle
        calendarToggle.setTabIndex(-1);
    }

    @Override
    public CubaMaskedFieldWidget getImpl() {
        return (CubaMaskedFieldWidget) super.getImpl();
    }

    @Override
    protected CubaMaskedFieldWidget createImpl() {
        CubaMaskedFieldWidget cubaMaskedFieldWidget = new CubaMaskedFieldWidget() {

            @Override
            protected boolean validateText(String text) {
                if (text.equals(nullRepresentation)) {
                    return true;
                }

                boolean validMask = super.validateText(text);
                if (!validMask)
                    return false;

                try {
                    getDateTimeService().parseDate(getText(), getFormatString(), lenient);
                } catch (Exception e) {
                    return false;
                }

                return validMask;
            }

            @Override
            public void valueChange(boolean blurred) {
                String newText = getText();
                if (!prompting && newText != null
                        && !newText.equals(valueBeforeEdit)) {
                    if (validateText(newText)) {
                        if (!newText.equals(nullRepresentation)) {
                            getElement().removeClassName(CubaDateFieldWidget.EMPTY_FIELD_CLASS);
                        }
                        CubaDateFieldWidget.this.onChange(null);

                        valueBeforeEdit = newText;
                    } else {
                        setText(valueBeforeEdit);
                    }
                }
            }
        };
        cubaMaskedFieldWidget.setImmediate(isImmediate());

        return cubaMaskedFieldWidget;
    }
}