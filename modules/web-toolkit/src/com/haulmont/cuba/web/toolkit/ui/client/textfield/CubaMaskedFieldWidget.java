/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui.client.textfield;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VTextField;
import com.vaadin.shared.EventId;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CubaMaskedFieldWidget extends VTextField implements ShortcutActionHandler.ShortcutActionHandlerOwner {

    public static final String CLASSNAME = "c-maskedfield";

    protected static final String EMPTY_FIELD_CLASS = "c-maskedfield-empty";

    protected static final char PLACE_HOLDER = '_';

    protected StringBuilder valueBuilder;

    protected String nullRepresentation;

    protected String mask;

    protected List<Mask> maskTest;

    protected Map<Character, Mask> maskMap = new HashMap<>();

    protected boolean maskedMode = false;

    protected boolean sendNullRepresentation = true;

    protected MaskedKeyHandler keyHandler;

    protected boolean focused = false;

    protected boolean shiftPressed = false;
    protected int shiftPressPos = -1;

    protected boolean isTimeMask = false;

    protected ShortcutActionHandler shortcutHandler;

    public CubaMaskedFieldWidget() {
        setStylePrimaryName(CLASSNAME);
        setStyleName(CLASSNAME);
        valueBeforeEdit = "";

        initMaskMap();

        keyHandler = new MaskedKeyHandler();
        addKeyPressHandler(keyHandler);
        addKeyDownHandler(keyHandler);
        addKeyUpHandler(keyHandler);

        addInputHandler(getElement());
    }

    @Override
    public void onFocus(FocusEvent event) {
        super.onFocus(event);

        if (!this.focused) {
            this.focused = true;

            if (!isReadOnly() && isEnabled()) {
                if (mask != null && nullRepresentation != null && nullRepresentation.equals(super.getText())) {
                    addStyleName("c-focus-move");

                    Scheduler.get().scheduleDeferred(() -> {
                        if (!isReadOnly() && isEnabled() && focused) {
                            setSelectionRange(getPreviousPos(0), 0);
                        }

                        removeStyleName("c-focus-move");
                    });
                }
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        super.onBlur(event);

        this.shiftPressed = false;
        this.shiftPressPos = -1;
        this.focused = false;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);

        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        this.shortcutHandler = handler;
    }

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    @Override
    public void add(Widget w) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Iterator<Widget> iterator() {
        return new LinkedList<Widget>().iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return false;
    }

    public boolean isMaskedMode() {
        return maskedMode;
    }

    public void setMaskedMode(boolean maskedMode) {
        this.maskedMode = maskedMode;
    }

    public void setTimeMask(boolean isTimeMask) {
        this.isTimeMask = isTimeMask;
    }

    public boolean isSendNullRepresentation() {
        return sendNullRepresentation;
    }

    public void setSendNullRepresentation(boolean sendNullRepresentation) {
        this.sendNullRepresentation = sendNullRepresentation;
    }

    protected void initMaskMap() {
        maskMap.put('#', new NumericMask());
        maskMap.put('U', new UpperCaseMask());
        maskMap.put('L', new LowerCaseMask());
        maskMap.put('?', new LetterMask());
        maskMap.put('A', new AlphanumericMask());
        maskMap.put('*', new WildcardMask());
        maskMap.put('H', new UpperCaseHexMask());
        maskMap.put('h', new LowerCaseHexMask());
        maskMap.put('~', new SignMask());
    }

    protected void updateCursor(int pos) {
        setCursorPos(getNextPos(pos));
    }

    protected int getNextPos(int pos) {
        while (++pos < maskTest.size() && maskTest.get(pos) == null) {
        }
        if (pos >= maskTest.size())
            return getPreviousPos(maskTest.size()) + 1;
        return pos;
    }

    int getPreviousPos(int pos) {
        while (--pos >= 0 && maskTest.get(pos) == null) {
        }
        if (pos < 0)
            return getNextPos(pos);
        return pos;
    }

    @Override
    public void setText(String value) {
        valueBuilder = maskValue(value);
        if (valueBuilder.toString().equals(nullRepresentation) || valueBuilder.length() == 0) {
            getElement().addClassName(EMPTY_FIELD_CLASS);
        } else {
            getElement().removeClassName(EMPTY_FIELD_CLASS);
        }
        super.setText(valueBuilder.toString());
    }

    public String getRawText() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maskTest.size(); i++) {
            Mask mask = maskTest.get(i);
            if (mask != null) {
                if (valueBuilder.charAt(i) != PLACE_HOLDER) {
                    result.append(valueBuilder.charAt(i));
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

        this.mask = mask;
        valueBuilder = new StringBuilder();
        maskTest = new ArrayList<>();

        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '\'') {
                maskTest.add(null);
                valueBuilder.append(mask.charAt(++i));
            } else if (maskMap.get(c) != null) {
                maskTest.add(maskMap.get(c));
                valueBuilder.append(PLACE_HOLDER);
            } else {
                maskTest.add(null);
                valueBuilder.append(c);
            }
        }
        nullRepresentation = valueBuilder.toString();
        setText(valueBuilder.toString());
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

    @Override
    public void valueChange(boolean blurred) {
        if (client != null && paintableId != null) {

            boolean sendBlurEvent = false;
            boolean sendValueChange = false;

            if (blurred && client.hasEventListeners(this, EventId.BLUR)) {
                sendBlurEvent = true;
                client.updateVariable(paintableId, EventId.BLUR, "", false);
            }

            String newText = getText();

            if (!prompting && newText != null
                    && !newText.equals(valueBeforeEdit)) {
                if (isTimeMask && newText.endsWith("__") && !newText.startsWith("__")) {
                    newText = newText.replaceAll("__", "00");
                }
                if (validateText(newText)) {
                    sendValueChange = immediate;
                    String value;
                    if (newText.equals(nullRepresentation)) {
                        value = isSendNullRepresentation() ? getText() : getRawText();
                    } else {
                        if (maskedMode) {
                            if (isTimeMask) {
                                value = newText;
                            } else {
                                value = getText();
                            }
                        } else {
                            value = getRawText();
                        }
                    }
                    client.updateVariable(paintableId, "text", value, false);
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

    @Override
    public void updateFieldContent(String text) {
        super.updateFieldContent(text);

        if (text == null || text.isEmpty()) {
            valueBeforeEdit = valueBuilder.toString();
        }
    }

    public void updateTextState() {
        if (valueBeforeEdit == null || !getText().equals(valueBeforeEdit)) {
            valueBeforeEdit = getText();
        }
    }

    protected native void addInputHandler(Element elementID)/*-{
        var temp = this;  // hack to hold on to 'this' reference

        var listener = $entry(function (e) {
            temp.@com.haulmont.cuba.web.toolkit.ui.client.textfield.CubaMaskedFieldWidget::handleInput(*)(e.inputType);
        });

        if (elementID.addEventListener) {
            elementID.addEventListener("input", listener, false);
        } else {
            elementID.attachEvent("input", listener);
        }
    }-*/;

    public void handleInput(String inputType) {
        String newText = getText();

        if (inputType == null) {
            handleCutAndPaste();
            return;
        }

        switch (inputType) {
            case "deleteByCut":
                handleCut(newText);
                break;
            case "insertFromPaste":
                handlePaste(newText);
                break;
            case "insertText":
            case "insertCompositionText": // in Chrome and Opera on Android when typing
                handleInsertText(newText);
                break;
            default:
                handleCutAndPaste();
                Logger.getLogger("CubaMaskedFieldWidget").log(Level.WARNING, "Unknown inputType: " + inputType);
        }
    }

    protected void handleCutAndPaste() {
        String newText = getText();
        if (newText.length() < valueBuilder.length()) {
            handleCut(newText);
        } else {
            handlePaste(newText);
        }
    }

    protected void handleCut(String newText) {
        int cursorPos = getCursorPos();
        if (cursorPos == 0 && newText.length() == 0) {
            valueBuilder.replace(0, valueBuilder.length(), nullRepresentation);
            super.setText(nullRepresentation);
            setCursorPos(getPreviousPos(0));
        } else {
            int cutLength = valueBuilder.length() - newText.length();

            StringBuilder resultValue = new StringBuilder(valueBuilder.substring(0, cursorPos));
            resultValue.append(nullRepresentation.substring(cursorPos, cursorPos + cutLength));
            resultValue.append(valueBuilder.substring(cursorPos + cutLength));

            valueBuilder.replace(0, valueBuilder.length(), resultValue.toString());
            super.setText(resultValue.toString());
            setCursorPos(cursorPos);
        }
    }

    protected void handleInsertText(String newText) {
        int pasteLength = newText.length() - valueBuilder.length();
        int pasteStart = getCursorPos() - pasteLength;

        StringBuilder maskedPart = maskValue(newText.substring(pasteStart, pasteStart + pasteLength), pasteStart, pasteStart + pasteLength);
        valueBuilder.replace(pasteStart, pasteStart + maskedPart.length(), maskedPart.toString());
        super.setText(valueBuilder.toString());

        if (maskedPart.length() == 0) {
            return;
        }

        if (maskedPart.toString().charAt(0) == PLACE_HOLDER) {
            setCursorPos(getNextPos(pasteStart - 1));
        } else {
            setCursorPos(getNextPos(pasteStart + pasteLength - 1));
        }
    }

    protected void handlePaste(String newText) {
        int pasteLength = newText.length() - valueBuilder.length();
        int pasteStart = getCursorPos() - pasteLength;

        if (newText.length() == valueBuilder.length()) {
            pasteStart = 0;
            pasteLength = valueBuilder.length();
        }

        StringBuilder maskedPart = maskValue(newText.substring(pasteStart, pasteStart + pasteLength), pasteStart, pasteStart + pasteLength);
        valueBuilder.replace(pasteStart, pasteStart + maskedPart.length(), maskedPart.toString());
        super.setText(valueBuilder.toString());
        setCursorPos(getNextPos(pasteStart + maskedPart.length() - 1));
    }

    protected void setRawCursorPosition(int pos) {
        if (pos >= 0 && pos <= maskTest.size())
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
            //it's not  proper way to recognize letters
            return (Character.toLowerCase(c) != Character.toUpperCase(c));
        }
    }

    /**
     * Represents a hex character, 0-9a-fA-F. a-f is mapped to A-F
     */
    public static class HexMask extends AbstractMask {
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
    }

    public static class LowerCaseHexMask extends HexMask {
        @Override
        public char getChar(char c) {
            if (Character.isDigit(c)) {
                return c;
            }
            return Character.toLowerCase(c);
        }
    }

    public static class UpperCaseHexMask extends HexMask {
        @Override
        public char getChar(char c) {
            if (Character.isDigit(c)) {
                return c;
            }
            return Character.toUpperCase(c);
        }
    }

    public static class LowerCaseMask extends LetterMask {
        @Override
        public boolean isValid(char c) {
            return (Character.toLowerCase(c) != Character.toUpperCase(c));
        }

        @Override
        public char getChar(char c) {
            return Character.toLowerCase(c);
        }
    }

    public static class UpperCaseMask extends LetterMask {
        @Override
        public boolean isValid(char c) {
            return (Character.toLowerCase(c) != Character.toUpperCase(c));
        }

        @Override
        public char getChar(char c) {
            return Character.toUpperCase(c);
        }
    }

    public static class AlphanumericMask extends AbstractMask {
        @Override
        public boolean isValid(char c) {
            return (Character.isDigit(c) || (Character.toLowerCase(c) != Character.toUpperCase(c)));
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

    public class MaskedKeyHandler implements KeyDownHandler, KeyUpHandler, KeyPressHandler {

        protected int getCursorPosSelection() {
            return getCursorPos() + getSelectionLength();
        }

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_SHIFT && !shiftPressed) {
                shiftPressed = true;
                shiftPressPos = getCursorPos();
            }
            if (isReadOnly())
                return;
            if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
                int beginPos = getCursorPos();
                int endPos = getCursorPosSelection();
                if (getSelectionLength() == 0) {
                    beginPos = getPreviousPos(beginPos);
                }

                removeCharsAtRange(beginPos, endPos);

                setCursorToInputPosition(beginPos);
                event.preventDefault();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_DELETE) {
                int beginPos = getCursorPos();
                int endPos = getCursorPosSelection();
                boolean isHasSelection = getSelectionLength() > 0;
                if (!isHasSelection) {
                    endPos = getNextPos(beginPos);
                }

                removeCharsAtRange(beginPos, endPos);

                if (isHasSelection) {
                    setCursorToInputPosition(beginPos);
                } else {
                    updateCursor(beginPos);
                }
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

        protected void removeCharsAtRange(int from, int to) {
            for (int pos = from; pos < to; pos++) {
                if (pos < maskTest.size()) {
                    Mask m = maskTest.get(pos);
                    if (m != null) {
                        valueBuilder.setCharAt(pos, PLACE_HOLDER);
                    }
                }
            }
            setValue(valueBuilder.toString());
        }

        protected void setCursorToInputPosition(int pos) {
            if (pos >= 0 && pos < maskTest.size()) {
                Mask m = maskTest.get(pos);
                if (m == null) {
                    pos = getNextPos(pos);
                }
            }
            setCursorPos(pos);
        }

        protected void updateSelectionRange() {
            if (shiftPressed) {
                if (getCursorPosSelection() > shiftPressPos) {
                    setSelectionRange(shiftPressPos, getCursorPosSelection() - shiftPressPos);
                } else {
                    setSelectionRange(getCursorPosSelection(), shiftPressPos - getCursorPosSelection());
                }
            }
        }

        @Override
        public void onKeyUp(KeyUpEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_SHIFT) {
                shiftPressed = false;
                shiftPressPos = -1;
            }
        }

        @Override
        public void onKeyPress(KeyPressEvent e) {
            if (isReadOnly())
                return;

            if (e.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
                    && !e.getNativeEvent().getAltKey()
                    && !e.getNativeEvent().getCtrlKey()
                    && !e.getNativeEvent().getShiftKey()) {
                valueChange(false);
            }

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
                //|| e.getNativeEvent().getMetaKey()
                    ) {

                e.preventDefault(); // KK: otherwise incorrectly handles combinations like Shift+'='
                return;
            } else if (BrowserInfo.get().isGecko() && e.getCharCode() == '\u0000'
                    || e.getNativeEvent().getCtrlKey()
                    || e.getNativeEvent().getMetaKey()) {
                //pressed tab in firefox or ctrl. because FF fires keyPressEvents on CTRL+C
                // MetaKey for Mac OS X for Cmd + key commands
                return;
            }
            if (getCursorPos() < maskTest.size()) {
                Mask m = maskTest.get(getCursorPos());
                if (m != null) {
                    if (m.isValid(e.getCharCode()) && e.getCharCode() != PLACE_HOLDER) {
                        int pos = getCursorPos();
                        valueBuilder.setCharAt(pos, m.getChar(e.getCharCode()));
                        setValue(valueBuilder.toString());
                        updateCursor(pos);
                    }
                } else {
                    updateCursor(getCursorPos());
                }
            }
            e.preventDefault();
        }
    }
}