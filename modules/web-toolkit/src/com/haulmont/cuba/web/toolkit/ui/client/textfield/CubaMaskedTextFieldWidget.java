/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.google.gwt.event.dom.client.*;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VTextField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMaskedTextFieldWidget extends VTextField {

    public static final String CLASSNAME = "cuba-maskedfield";

    protected static final String EMPTY_FIELD_CLASS = "cuba-maskedfield-empty";

    protected static final char PLACE_HOLDER = '_';

    protected StringBuilder valueBuilder;

    protected String nullRepresentation;

    protected String mask;

    protected List<Mask> maskTest;

    public CubaMaskedTextFieldWidget() {
        setStylePrimaryName(CLASSNAME);

        MaskedTextFieldInputHandler inputHandler = new MaskedTextFieldInputHandler();

        addKeyPressHandler(inputHandler);
        addKeyDownHandler(inputHandler);
        addFocusHandler(inputHandler);
        addBlurHandler(inputHandler);
    }

    protected void updateCursor(int pos) {
        setCursorPos(getNextPos(pos));
    }

    protected int getNextPos(int pos) {
        while (++pos < maskTest.size() && maskTest.get(pos) == null) {
        }
        return pos;
    }

    int getPreviousPos(int pos) {
        while (--pos >= 0 && maskTest.get(pos) == null) {
        }
        if (pos < 0)
            return getNextPos(pos);
        return pos;
    }

    public void setText(String value) {
        if (value == null || "".equals(value))
            value = nullRepresentation;

        valueBuilder = new StringBuilder(value);
        if (value.equals(nullRepresentation))
            getElement().addClassName(EMPTY_FIELD_CLASS);
        else
            getElement().removeClassName(EMPTY_FIELD_CLASS);

        super.setText(value);
    }

    public void setMask(String mask) {
        if (mask == null)
            mask = "";

        this.mask = mask;

        valueBuilder = new StringBuilder();
        maskTest = new ArrayList<Mask>();

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);

            if (c == '\'') {
                maskTest.add(null);
                valueBuilder.append(mask.charAt(++i));
            } else if (c == '#') {
                maskTest.add(new NumericMask());
                valueBuilder.append(PLACE_HOLDER);
            } else if (c == 'U') {
                maskTest.add(new UpperCaseMask());
                valueBuilder.append(PLACE_HOLDER);
            } else if (c == 'L') {
                maskTest.add(new LowerCaseMask());
                valueBuilder.append(PLACE_HOLDER);
            } else if (c == '?') {
                maskTest.add(new LetterMask());
                valueBuilder.append(PLACE_HOLDER);
            } else if (c == 'A') {
                maskTest.add(new AlphanumericMask());
                valueBuilder.append(PLACE_HOLDER);
            } else if (c == '*') {
                maskTest.add(new WildcardMask());
                valueBuilder.append(PLACE_HOLDER);
            } else if (c == 'H') {
                maskTest.add(new HexMask());
                valueBuilder.append(PLACE_HOLDER);
            } else if (c == '~') {
                maskTest.add(new SignMask());
                valueBuilder.append(PLACE_HOLDER);
            } else {
                maskTest.add(null);
                valueBuilder.append(c);
            }
        }
        nullRepresentation = valueBuilder.toString();
        setText(valueBuilder.toString());
    }

    protected void setRawCursorPosition(int pos) {
        if (pos >=0 && pos <= maskTest.size())
            setCursorPos(pos);
    }

    public interface Mask {
        boolean isValid(char c);

        char getChar(char c);
    }

    public static abstract class AbstractMask implements Mask {
        @Override
        public char getChar(char c) {
            return c;
        }
    }

    public static class NumericMask extends AbstractMask {
        @Override
        public boolean isValid(char c) {
            return Character.isDigit(c);
        }
    }

    public static class LetterMask extends AbstractMask {
        @Override
        public boolean isValid(char c) {
            return Character.isLetter(c);
        }
    }

    /**
     * Represents a hex character, 0-9a-fA-F. a-f is mapped to A-F
     */
    public static class HexMask implements Mask {
        @Override
        public boolean isValid(char c) {
            return ((c == '0' || c == '1' ||
                    c == '2' || c == '3' ||
                    c == '4' || c == '5' ||
                    c == '6' || c == '7' ||
                    c == '8' || c == '9' ||
                    c == 'a' || c == 'A' ||
                    c == 'b' || c == 'B' ||
                    c == 'c' || c == 'C' ||
                    c == 'd' || c == 'D' ||
                    c == 'e' || c == 'E' ||
                    c == 'f' || c == 'F'));
        }

        @Override
        public char getChar(char c) {
            if (Character.isDigit(c)) {
                return c;
            }
            return Character.toUpperCase(c);
        }
    }

    public static class LowerCaseMask implements Mask {
        @Override
        public boolean isValid(char c) {
            return Character.isLetter(getChar(c));
        }

        @Override
        public char getChar(char c) {
            return Character.toLowerCase(c);
        }
    }

    public static class UpperCaseMask implements Mask {
        @Override
        public boolean isValid(char c) {
            return Character.isLetter(getChar(c));
        }

        @Override
        public char getChar(char c) {
            return Character.toUpperCase(c);
        }
    }

    public static class AlphanumericMask extends AbstractMask {
        @Override
        public boolean isValid(char c) {
            return Character.isLetter(c) || Character.isDigit(c);
        }
    }

    public static class WildcardMask extends AbstractMask {
        @Override
        public boolean isValid(char c) {
            return true;
        }
    }

    public static class SignMask extends AbstractMask {
        @Override
        public boolean isValid(char c) {
            return c == '-' || c == '+';
        }
    }

    private class MaskedTextFieldInputHandler implements KeyPressHandler, KeyDownHandler, FocusHandler, BlurHandler {

        @Override
        public void onKeyPress(KeyPressEvent event) {
            if (isReadOnly())
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
            if (getCursorPos() < maskTest.size()) {
                Mask m = maskTest.get(getCursorPos());
                if (m != null) {
                    if (m.isValid(event.getCharCode())) {
                        int pos = getCursorPos();
                        valueBuilder.setCharAt(pos, m.getChar(event.getCharCode()));
                        setValue(valueBuilder.toString());
                        updateCursor(pos);
                    }
                } else
                    updateCursor(getCursorPos());
            }
            event.preventDefault();
        }

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (isReadOnly())
                return;
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                int pos = getPreviousPos(getCursorPos());
                if (pos < maskTest.size() && pos >= 0) {
                    Mask m = maskTest.get(pos);
                    if (m != null) {
                        valueBuilder.setCharAt(pos, PLACE_HOLDER);
                        setValue(valueBuilder.toString());
                    }
                    setCursorPos(pos);
                }
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
                int pos = getCursorPos();
                if (pos < maskTest.size() && pos >= 0) {
                    Mask m = maskTest.get(pos);
                    if (m != null) {
                        valueBuilder.setCharAt(pos, PLACE_HOLDER);
                        setValue(valueBuilder.toString());
                    }
                    updateCursor(pos);
                }
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
                setRawCursorPosition(getNextPos(getCursorPos()));
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
                setRawCursorPosition(getPreviousPos(getCursorPos()));
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_HOME || event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                setRawCursorPosition(getPreviousPos(0));
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_END || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                setRawCursorPosition(getPreviousPos(getValue().length()) + 1);
                event.preventDefault();
            }
        }

        @Override
        public void onFocus(FocusEvent event) {
            if (isReadOnly())
                return;
            if (getValue().isEmpty()) {
                setMask(mask);
            } else {
                int previousPos = getPreviousPos(0);
                setRawCursorPosition(previousPos);
            }
        }

        @Override
        public void onBlur(BlurEvent event) {
            if (isReadOnly())
                return;
            for (int i = 0; i < valueBuilder.length(); i++) {
                char c = valueBuilder.charAt(i);

                if (maskTest.get(i) != null && c == PLACE_HOLDER) {
                    valueChange(true);
                    return;
                }
            }
        }
    }
}