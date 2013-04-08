/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.datefield;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.TextBox;
import com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaMaskedTextFieldWidget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VPopupCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaDateFieldWidget extends VPopupCalendar
        implements KeyDownHandler, FocusHandler, BlurHandler, KeyPressHandler {

    private static final String CLASSNAME = "cuba-datefield";

    private static final String MASKED_FIELD_CLASS = "cuba-datefield-masked-input";

    private static final char PLACE_HOLDER = '_';

    private StringBuilder dateBuilder;

    private String mask;

    private String prevString;

    private String nullRepresentation;

    private List<CubaMaskedTextFieldWidget.Mask> maskTest;

    public CubaDateFieldWidget() {
        setStyleName(CLASSNAME);

        text.addKeyPressHandler(this);
        text.addKeyDownHandler(this);
        text.addFocusHandler(this);
        text.addBlurHandler(this);
    }

    @Override
    public void setText(String value) {
        if (value.equals(nullRepresentation) || value.equals("")) {
            text.getElement().addClassName(MASKED_FIELD_CLASS);
        } else {
            text.getElement().removeClassName(MASKED_FIELD_CLASS);
        }
        if ("".equals(value) && !readonly) {
            setMask(mask);
            prevString = getText();
            return;
        }
        prevString = value;
        dateBuilder = new StringBuilder(value);
        super.setText(value);
    }

    public void setMask(String mask) {
        if (mask == null) return;
        this.mask = mask;
        dateBuilder = new StringBuilder();
        maskTest = new ArrayList<CubaMaskedTextFieldWidget.Mask>();

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);

            if (c == '\'') {
                maskTest.add(null);
                dateBuilder.append(mask.charAt(++i));
            } else if (c == '#') {
                maskTest.add(new CubaMaskedTextFieldWidget.NumericMask());
                dateBuilder.append(PLACE_HOLDER);
            } else if (c == 'U') {
                maskTest.add(new CubaMaskedTextFieldWidget.UpperCaseMask());
                dateBuilder.append(PLACE_HOLDER);
            } else {
                maskTest.add(null);
                dateBuilder.append(c);
            }
        }
        nullRepresentation = dateBuilder.toString();
        text.setText(dateBuilder.toString());
    }

    private void updateCursor(int pos) {
        text.setCursorPos(getNextPos(pos));
    }

    private int getNextPos(int pos) {
        while (++pos < maskTest.size() && maskTest.get(pos) == null) {
        }
        return pos;
    }

    private int getPreviousPos(int pos) {
        while (--pos >= 0 && maskTest.get(pos) == null) {
        }
        if (pos < 0)
            return getNextPos(pos);
        return pos;
    }

    @Override
    public void onBlur(BlurEvent event) {
        calendarToggle.removeStyleDependentName("focus");

        if (isReadonly())
            return;
        if (!dateBuilder.toString().equals(nullRepresentation)) {
            text.getElement().removeClassName(MASKED_FIELD_CLASS);
        }
        for (int i = 0; i < dateBuilder.length(); i++) {
            char c = dateBuilder.charAt(i);

            if (maskTest.get(i) != null && c == PLACE_HOLDER) {
                if (dateBuilder.toString().equals(prevString)) {
                    return;
                }
                prevString = getText();
                onChange(null);
                return;
            }
        }
        prevString = dateBuilder.toString();
        onChange(null);
    }

    @Override
    public void onFocus(FocusEvent event) {
        if (isReadonly())
            return;

        if (text.getValue().isEmpty())
            setMask(mask);
        else
            text.setCursorPos(getPreviousPos(0));

        calendarToggle.addStyleDependentName("focus");
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (isReadonly())
            return;

        if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
            int pos = getPreviousPos(text.getCursorPos());
            CubaMaskedTextFieldWidget.Mask m = maskTest.get(pos);
            if (m != null) {
                dateBuilder.setCharAt(pos, PLACE_HOLDER);
                text.setValue(dateBuilder.toString());
            }
            text.setCursorPos(pos);
            event.preventDefault();
        } else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
            int pos = text.getCursorPos();

            CubaMaskedTextFieldWidget.Mask m = maskTest.get(pos);
            if (m != null) {
                dateBuilder.setCharAt(pos, PLACE_HOLDER);
                text.setValue(dateBuilder.toString());
            }
            updateCursor(pos);
            event.preventDefault();
        } else if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
            text.setCursorPos(getNextPos(text.getCursorPos()));
            event.preventDefault();
        } else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
            text.setCursorPos(getPreviousPos(text.getCursorPos()));
            event.preventDefault();
        } else if (event.getNativeKeyCode() == KeyCodes.KEY_HOME || event.getNativeKeyCode() == KeyCodes.KEY_UP) {
            text.setCursorPos(getPreviousPos(0));
            event.preventDefault();
        } else if (event.getNativeKeyCode() == KeyCodes.KEY_END || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            text.setCursorPos(getPreviousPos(text.getValue().length()) + 1);
            event.preventDefault();
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (isReadonly())
            return;
        if (event.getCharCode() == KeyCodes.KEY_BACKSPACE
                || event.getCharCode() == KeyCodes.KEY_DELETE
                || event.getCharCode() == KeyCodes.KEY_END
                || event.getCharCode() == KeyCodes.KEY_ENTER
                || event.getCharCode() == KeyCodes.KEY_ESCAPE
                || event.getCharCode() == KeyCodes.KEY_HOME
                || event.getCharCode() == KeyCodes.KEY_LEFT
                || event.getCharCode() == KeyCodes.KEY_PAGEDOWN
                || event.getCharCode() == KeyCodes.KEY_PAGEUP
                || event.getCharCode() == KeyCodes.KEY_RIGHT
                || event.isAltKeyDown()
                || event.isControlKeyDown()
                || event.isMetaKeyDown()) {
            event.preventDefault(); // KK: otherwise incorrectly handles combinations like Shift+'='
            return;
        } else if (BrowserInfo.get().isGecko() && event.getCharCode() == '\u0000') { //pressed tab in firefox
            return;
        }

        if (text.getCursorPos() < maskTest.size()) {
            CubaMaskedTextFieldWidget.Mask m = maskTest.get(text.getCursorPos());
            if (m != null) {
                if (m.isValid(event.getCharCode())) {
                    int pos = text.getCursorPos();
                    dateBuilder.setCharAt(pos, m.getChar(event.getCharCode()));
                    text.setValue(dateBuilder.toString());
                    updateCursor(pos);
                }
            } else
                updateCursor(text.getCursorPos());
        }
        event.preventDefault();
    }
}