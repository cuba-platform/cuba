package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VTextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VMaskedTextField extends VTextField {

    public static final String CLASSNAME = "v-maskedfield";

    private static final String MASKED_FIELD_CLASS = "v-maskedfield-onlymask";

    private static final boolean isDebug = false;

    private char placeholder = '_';

    protected StringBuilder string;

    protected String nullRepresentation;

    protected String mask;

    private List<Mask> maskTest;

    private Map<Character, Mask> maskMap = new HashMap<Character, Mask>();

    private boolean maskedMode = false;

    private void debug(String msg) {
        if (isDebug)
            VConsole.log(msg);
    }

    private KeyPressHandler keyPressHandler = new KeyPressHandler() {
        public void onKeyPress(KeyPressEvent e) {
            if (isReadOnly())
                return;
            debug("keyPressHandler.onKeyPress: " + e.toDebugString());
            if (e.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_END
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_HOME
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_PAGEDOWN
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_PAGEUP
                    || e.getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT
                    || e.getNativeEvent().getAltKey()
                    //|| e.getNativeEvent().getCtrlKey()
                    || e.getNativeEvent().getMetaKey()) {
                debug("keyPressHandler.onKeyPress: return immediately");
                e.preventDefault(); // KK: otherwise incorrectly handles combinations like Shift+'='
                return;
            } else if (BrowserInfo.get().isGecko() && e.getCharCode() == '\u0000' || e.getNativeEvent().getCtrlKey()) {
                //pressed tab in firefox or ctrl. because FF fires keyPressEvents on CTRL+C
                return;
            }
            if (getCursorPos() < maskTest.size()) {
                Mask m = maskTest.get(getCursorPos());
                if (m != null) {
                    if (m.isValid(e.getCharCode())) {
                        debug("keyPressHandler.onKeyPress: valid, m=" + m);
                        int pos = getCursorPos();
                        string.setCharAt(pos, m.getChar(e.getCharCode()));
                        setValue(string.toString());
                        updateCursor(pos);
                    }
                } else {
                    debug("keyPressHandler.onKeyPress: m=null");
                    updateCursor(getCursorPos());
                }

            }
            e.preventDefault();
        }
    };

    private KeyHandler keyHandler;

    public VMaskedTextField() {
        valueBeforeEdit = "";
        initMaskMap();
        addKeyPressHandler(keyPressHandler);
        keyHandler = new KeyHandler();
        addKeyDownHandler(keyHandler);
        addKeyUpHandler(keyHandler);
        addInputHandler(getElement());
        debug("VMaskedTextField created");
    }

    protected void initMaskMap() {
        maskMap.put('#', new NumericMask());
        maskMap.put('U', new UpperCaseMask());
        maskMap.put('L', new LowerCaseMask());
        maskMap.put('?', new LetterMask());
        maskMap.put('A', new AlphanumericMask());
        maskMap.put('*', new WildcardMask());
        maskMap.put('H', new HexMask());
        maskMap.put('~', new SignMask());
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        debug("updateFromUIDL: " + uidl);
        if (!(uidl.getBooleanAttribute("readonly"))) {
            String maskParam = uidl.getStringAttribute("mask");
            setMask(maskParam == null ? "" : maskParam);
        }
        maskedMode = uidl.getBooleanAttribute("maskedMode");
        super.updateFromUIDL(uidl, client);
    }


    public void setText(String value) {
        debug("setText: " + value);
        string = maskValue(value);
        if (string.toString().equals(nullRepresentation) || string.length() == 0) {
            getElement().addClassName(MASKED_FIELD_CLASS);
        } else {
            getElement().removeClassName(MASKED_FIELD_CLASS);
        }
        super.setText(string.toString());
    }

    public String getRawText() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maskTest.size(); i++) {
            Mask mask = maskTest.get(i);
            if (mask != null) {
                if (string.charAt(i) != placeholder) {
                    result.append(string.charAt(i));
                } else result.append("");
            }
        }
        return result.toString();
    }

    protected StringBuilder maskValue(String value) {
        if (maskTest == null) {
            return new StringBuilder(value);
        } else {
            return maskValue(value, 0, maskTest.size());
        }
    }

    protected StringBuilder maskValue(String value, int start, int end) {
        StringBuilder result = new StringBuilder();
        if (maskTest == null) {
            return result.append(value);
        }
        if (value.equals(nullRepresentation.substring(start, end))) {
            result.append(value);
            return result;
        }
        int valuePos = 0;
        for (int i = start; i < maskTest.size() && valuePos < end - start; i++) {
            if (valuePos >= value.length()) {
                result.append(nullRepresentation.charAt(i));
                valuePos++;
            } else {
                Mask mask = maskTest.get(i);
                if (mask == null) {
                    if (nullRepresentation.charAt(i) == value.charAt(valuePos)) {
                        result.append(value.charAt(valuePos));
                        valuePos++;
                    } else {
                        result.append(nullRepresentation.charAt(i));
                    }
                } else {
                    if (mask.isValid(value.charAt(valuePos))) {
                        result.append(mask.getChar(value.charAt(valuePos)));
                        valuePos++;
                    } else {
                        result.append(nullRepresentation.charAt(i));
                        valuePos++;
                    }
                }
            }
        }
        return result;
    }

    public void setMask(String mask) {
        if (mask.equals(this.mask)) {
            return;
        }
        debug("setMask: " + mask);
        this.mask = mask;
        string = new StringBuilder();
        maskTest = new ArrayList<Mask>();

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '\'') {
                maskTest.add(null);
                string.append(mask.charAt(++i));
            } else if (maskMap.get(c) != null) {
                maskTest.add(maskMap.get(c));
                string.append(placeholder);
            } else {
                maskTest.add(null);
                string.append(c);
            }
        }
        nullRepresentation = string.toString();
        setText(string.toString());
//		updateCursor(0); // KK: commented out because leads to grab focus
    }

    protected boolean validateText(String text) {
        if (text.equals(nullRepresentation)) {
            return true;
        }
        for (int i = 0; i < maskTest.size(); i++) {
            Mask mask = maskTest.get(i);
            if (mask != null) {
                if (!mask.isValid(text.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void valueChange(boolean blurred) {
        if (client != null && id != null) {
            boolean sendBlurEvent = false;
            boolean sendValueChange = false;

            if (blurred && client.hasEventListeners(this, EventId.BLUR)) {
                sendBlurEvent = true;
                client.updateVariable(id, EventId.BLUR, "", false);
            }

            String newText = getText();
            if (!prompting && newText != null
                    && !newText.equals(valueBeforeEdit)) {
                if (validateText(newText)) {
                    sendValueChange = immediate;
                    client.updateVariable(id, "text", maskedMode ? getText() : getRawText(), false);
                    valueBeforeEdit = newText;
                } else {
                    setText(valueBeforeEdit);
                }
            }

            if (sendBlurEvent || sendValueChange) {
                client.sendPendingVariableChanges();
            }
        }
    }

    protected native void addInputHandler(Element elementID)/*-{
        var temp = this;  // hack to hold on to 'this' reference
        elementID.addEventListener("input", function (e) {
            temp.@com.haulmont.cuba.toolkit.gwt.client.ui.VMaskedTextField::handleInput()();
        }, false)
    }-*/;

    public void handleInput() {
        String newText = getText();
        if (newText.length() < string.length()) {
            super.setText(string.toString());
            setCursorPos(getNextPos(string.length()));
        } else {
            int pasteLength = newText.length() - string.length();
            int pasteStart;
            if (BrowserInfo.get().isGecko() || BrowserInfo.get().isIE()) {
                pasteStart = getCursorPos() - pasteLength;
            } else {
                pasteStart = getCursorPos();
            }

            StringBuilder maskedPart = maskValue(newText.substring(pasteStart, pasteStart + pasteLength), pasteStart, pasteStart + pasteLength);
            string.replace(pasteStart, pasteStart + maskedPart.length(), maskedPart.toString());
            super.setText(string.toString());
            setCursorPos(getNextPos(pasteStart + maskedPart.length()));
        }
    }

    private void updateCursor(int pos) {
        setCursorPos(getNextPos(pos));
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

    public static interface Mask {
        boolean isValid(char c);

        char getChar(char c);
    }

    public static abstract class AbstractMask implements Mask {

        protected Character maskChar;

        public AbstractMask(Character maskChar) {
            this.maskChar = maskChar;
        }

        public char getChar(char c) {
            return c;
        }
    }

    public static class NumericMask extends AbstractMask {

        public NumericMask() {
            super('#');
        }

        public boolean isValid(char c) {
            return Character.isDigit(c);
        }
    }

    public static class LetterMask extends AbstractMask {

        public LetterMask(Character mask) {
            super(mask);
        }

        public LetterMask() {
            super('?');
        }

        public boolean isValid(char c) {
            //it's not  proper way to recognize letters
            return (Character.toLowerCase(c) != Character.toUpperCase(c));
        }
    }

    public static class LowerCaseMask extends LetterMask {

        public LowerCaseMask() {
            super('L');
        }

        public char getChar(char c) {
            return Character.toLowerCase(c);
        }
    }

    public static class UpperCaseMask extends LetterMask {

        public UpperCaseMask() {
            super('U');
        }

        public char getChar(char c) {
            return Character.toUpperCase(c);
        }
    }

    public static class AlphanumericMask extends AbstractMask {

        public AlphanumericMask() {
            super('A');
        }

        public boolean isValid(char c) {
            return (Character.isDigit(c) || (Character.toLowerCase(c) != Character.toUpperCase(c)));
        }
    }

    public static class WildcardMask extends AbstractMask {

        public WildcardMask() {
            super('*');
        }

        public boolean isValid(char c) {
            return true;
        }
    }

    public static class SignMask extends AbstractMask {

        public SignMask() {
            super('~');
        }

        public boolean isValid(char c) {
            return c == '-' || c == '+';
        }
    }

    /**
     * Represents a hex character, 0-9a-fA-F. a-f is mapped to A-F
     */
    public static class HexMask extends AbstractMask {
        public HexMask() {
            super('H');
        }

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

        public char getChar(char c) {
            if (Character.isDigit(c)) {
                return c;
            }
            return Character.toUpperCase(c);
        }
    }

    public class KeyHandler implements KeyDownHandler, KeyUpHandler {
        private boolean shitPressed = false;
        private int shiftPressPos = -1;

        private int getCursorPosSelection() {
            return getCursorPos() + getSelectionLength();
        }

        public void onKeyDown(KeyDownEvent event) {
            debug("KEY DOWN");
            if (event.getNativeKeyCode() == KeyCodes.KEY_SHIFT && !shitPressed) {
                shitPressed = true;
                shiftPressPos = getCursorPos();
            }
            if (isReadOnly())
                return;
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                int pos = getPreviousPos(getCursorPos());
                Mask m = maskTest.get(pos);
                if (m != null) {
                    string.setCharAt(pos, placeholder);
                    setValue(string.toString());
                }
                setCursorPos(pos);
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
                int pos = getCursorPos();

                Mask m = maskTest.get(pos);
                if (m != null) {
                    string.setCharAt(pos, placeholder);
                    setValue(string.toString());
                }
                updateCursor(pos);
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT) {
                if (getCursorPosSelection() <= shiftPressPos) {
                    setCursorPos(getNextPos(getCursorPos()));
                } else {
                    setCursorPos(getNextPos(getCursorPosSelection()));
                }
                updateSelectionRange();
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT) {
                if (getCursorPosSelection() <= shiftPressPos) {
                    setCursorPos(getPreviousPos(getCursorPos()));
                } else {
                    setCursorPos(getPreviousPos(getCursorPosSelection()));
                }
                updateSelectionRange();
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_HOME || event.getNativeKeyCode() == KeyCodes.KEY_UP) {
                setCursorPos(getPreviousPos(0));
                updateSelectionRange();
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_END || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                setCursorPos(getPreviousPos(getValue().length()) + 1);
                updateSelectionRange();
                event.preventDefault();
            }
        }

        private void updateSelectionRange() {
            if (shitPressed) {
                if (getCursorPosSelection() > shiftPressPos) {
                    setSelectionRange(shiftPressPos, getCursorPosSelection() - shiftPressPos);
                } else {
                    setSelectionRange(getCursorPosSelection(), shiftPressPos - getCursorPosSelection());
                }
            }
        }

        @Override
        public void onKeyUp(KeyUpEvent event) {
            debug("KEY UP");
            if (event.getNativeKeyCode() == KeyCodes.KEY_SHIFT) {
                shitPressed = false;
                shiftPressPos = -1;
            }
        }
    }

}
