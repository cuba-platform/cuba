/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.TextBox;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.ui.VPopupCalendar;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class VMaskedPopupCalendar extends VPopupCalendar {

    private static final String MASKED_FIELD_CLASS = "v-maskedfield-onlymask";

    private static final boolean isDebug = false;

    private char placeholder = '_';

    private StringBuilder string;

    protected String mask;

    private String prevString;

    private String nullRepresentation;

    private List<VMaskedTextField.Mask> maskTest;

    private TextBox textBox;

    private void debug(String msg) {
        if (isDebug)
            VConsole.log(msg);
    }

    private KeyPressHandler keyPressHandler = new KeyPressHandler() {
        public void onKeyPress(KeyPressEvent e) {
            if (isReadonly())
                return;
            debug("keyPressHandler.onKeyPress: " + e.toDebugString());
            if (e.getCharCode() == KeyCodes.KEY_BACKSPACE
                    || e.getCharCode() == KeyCodes.KEY_DELETE
                    || e.getCharCode() == KeyCodes.KEY_END
                    || e.getCharCode() == KeyCodes.KEY_ENTER
                    || e.getCharCode() == KeyCodes.KEY_ESCAPE
                    || e.getCharCode() == KeyCodes.KEY_HOME
                    || e.getCharCode() == KeyCodes.KEY_LEFT
                    || e.getCharCode() == KeyCodes.KEY_PAGEDOWN
                    || e.getCharCode() == KeyCodes.KEY_PAGEUP
                    || e.getCharCode() == KeyCodes.KEY_RIGHT
                    || e.isAltKeyDown()
                    || e.isControlKeyDown()
                    || e.isMetaKeyDown()) {
                debug("keyPressHandler.onKeyPress: return immediately");
                e.preventDefault(); // KK: otherwise incorrectly handles combinations like Shift+'='
                return;
            } else if (BrowserInfo.get().isGecko() && e.getCharCode() == '\u0000') { //pressed tab in firefox
                return;
            }

            if (textBox.getCursorPos() < maskTest.size()) {
                VMaskedTextField.Mask m = maskTest.get(textBox.getCursorPos());
                if (m != null) {
                    if (m.isValid(e.getCharCode())) {
                        debug("keyPressHandler.onKeyPress: valid, m=" + m);
                        int pos = textBox.getCursorPos();
                        string.setCharAt(pos, m.getChar(e.getCharCode()));
                        textBox.setValue(string.toString());
                        updateCursor(pos);
                    }
                } else {
                    debug("keyPressHandler.onKeyPress: m=null");
                    updateCursor(textBox.getCursorPos());
                }
            }
            e.preventDefault();
        }
    };

    private KeyDownHandler keyDownHandler = new KeyDownHandler() {
        public void onKeyDown(KeyDownEvent event) {
            if (isReadonly())
                return;
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                int pos = getPreviousPos(textBox.getCursorPos());
                VMaskedTextField.Mask m = maskTest.get(pos);
                if (m != null) {
                    string.setCharAt(pos, placeholder);
                    textBox.setValue(string.toString());
                }
                textBox.setCursorPos(pos);
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
                int pos = textBox.getCursorPos();

                VMaskedTextField.Mask m = maskTest.get(pos);
                if (m != null) {
                    string.setCharAt(pos, placeholder);
                    textBox.setValue(string.toString());
                }
                updateCursor(pos);
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
                textBox.setCursorPos(getNextPos(textBox.getCursorPos()));
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
                textBox.setCursorPos(getPreviousPos(textBox.getCursorPos()));
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_HOME || event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                textBox.setCursorPos(getPreviousPos(0));
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_END || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                textBox.setCursorPos(getPreviousPos(textBox.getValue().length()) + 1);
                event.preventDefault();
            }
        }
    };

    private FocusHandler focusHandler = new FocusHandler() {

        public void onFocus(FocusEvent event) {
            if (isReadonly())
                return;
            debug("focusHandler.onFocus");
            if (textBox.getValue().isEmpty())
                setMask(mask);
            else
                textBox.setCursorPos(getPreviousPos(0));
        }
    };

    private BlurHandler blurHandler = new BlurHandler() {

        public void onBlur(BlurEvent event) {
            if (isReadonly())
                return;
            if (!string.toString().equals(nullRepresentation)) {
                textBox.getElement().removeClassName(MASKED_FIELD_CLASS);
            }
            debug("blurHandler.onBlur");
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);

                if (maskTest.get(i) != null && c == placeholder) {
                    if (string.toString().equals(prevString)) {
                        return;
                    }
                    prevString = getText();
                    onChange(null);
                    return;
                }
            }
            prevString = string.toString();
            onChange(null);
        }
    };

    public VMaskedPopupCalendar() {
        super();
        textBox = getTextBox();
        textBox.addKeyPressHandler(keyPressHandler);
        textBox.addKeyDownHandler(keyDownHandler);
        textBox.addFocusHandler(focusHandler);
        textBox.addBlurHandler(blurHandler);
        debug("VMaskedTextField created");
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        debug("updateFromUIDL: " + uidl);
        if (!uidl.getBooleanAttribute("readonly")) {
            setMask(uidl.getStringAttribute("mask"));
        }
        super.updateFromUIDL(uidl, client);
    }


    public void setText(String value) {
        debug("setText: " + value);
        if (value.equals(nullRepresentation) || value.equals("")) {
            textBox.getElement().addClassName(MASKED_FIELD_CLASS);
        } else {
            textBox.getElement().removeClassName(MASKED_FIELD_CLASS);
        }
        if ("".equals(value) && !readonly) {
            setMask(mask);
            prevString = getText();
            return;
        }
        prevString = value;
        string = new StringBuilder(value);
        super.setText(value);
    }

    private void setMask(String mask) {
        if (mask == null) return;
        debug("setMask: " + mask);
        this.mask = mask;
        string = new StringBuilder();
        maskTest = new ArrayList<VMaskedTextField.Mask>();

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);

            if (c == '\'') {
                maskTest.add(null);
                string.append(mask.charAt(++i));
            } else if (c == '#') {
                maskTest.add(new VMaskedTextField.NumericMask());
                string.append(placeholder);
            } else if (c == 'U') {
                maskTest.add(new VMaskedTextField.UpperCaseMask());
                string.append(placeholder);
            } else {
                maskTest.add(null);
                string.append(c);
            }
        }
        nullRepresentation = string.toString();
        textBox.setText(string.toString());
    }

    private void updateCursor(int pos) {
        textBox.setCursorPos(getNextPos(pos));
    }

    private int getNextPos(int pos) {
        while (++pos < maskTest.size() && maskTest.get(pos) == null) ;
        return pos;
    }

    int getPreviousPos(int pos) {
        while (--pos >= 0 && maskTest.get(pos) == null) ;
        if (pos < 0)
            return getNextPos(pos);
        return pos;
    }
}
