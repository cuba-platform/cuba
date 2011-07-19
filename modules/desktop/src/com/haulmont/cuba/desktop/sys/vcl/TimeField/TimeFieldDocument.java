/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl.TimeField;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class TimeFieldDocument extends PlainDocument {
    public static final char PLACE_HOLDER = '0';
    String mask;
    JTextField field;

    public TimeFieldDocument(JTextField field, String mask) {
        this.field = field;
        this.mask = mask;
        try {
            insertString(0, mask, null);
        } catch (BadLocationException e) {
            //offset in insertString == 0
        }
    }

    @Override
    public void replace(int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {
        if (length == 0 && (text == null || text.length() == 0)) {
            return;
        }

        if (text == null && length == mask.length())
            return;

        writeLock();
        try {
            if (length > 0) {
                int removeLength;
                if (text != null && text.length() < length) {
                    removeLength = text.length();
                } else removeLength = length;
                remove(offset, removeLength);
            }
            if (getText(0, 1).equals("\n"))
                super.insertString(offset, calculateFormattedString(offset, text), null);
            else {
                String formattedString = calculateFormattedString(offset, text);
                internalReplace(offset, formattedString.length(), formattedString, null);
            }
        } finally {
            writeUnlock();
        }
    }

    private void internalReplace(int offset, int length, String text,
                                 AttributeSet attrs) throws BadLocationException {
        if (length == 0 && (text == null || text.length() == 0)) {
            return;
        }

        writeLock();
        try {
            if (length > 0) {
                super.remove(offset, length);
            }
            if (text != null && text.length() > 0) {
                super.insertString(offset, text, attrs);
            }
        } finally {
            writeUnlock();
        }
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        super.insertString(offs, mask.substring(offs, offs + len), null);
        field.setCaretPosition(offs);
    }

    private String calculateFormattedString(int offset, String text) throws BadLocationException {
        if (text == null)
            return mask;
        StringBuilder result;

        int endPosition;
        if (offset + text.length() > mask.length())
            endPosition = mask.length();
        else
            endPosition = offset + text.length();

        result = new StringBuilder(mask.substring(offset, endPosition));

        int i = 0;
        int shift = 0;
        while (i + offset < mask.length() && i < text.length()) {
            if ((mask.charAt(i + offset) == PLACE_HOLDER)) {
                if ((Character.isDigit(text.charAt(i)))) {
                    int digit = Integer.parseInt(text.substring(i, i + 1));
                    switch (i + offset) {
                        case 0:
                            if (digit > 2) {
                                if (text.length() == 1) {
                                    result.deleteCharAt(i + shift);
                                    shift--;
                                } else {
                                    result.setCharAt(i + shift, '2');
                                }
                                break;
                            }
                            if (digit == 2) {
                                int digit2 = Integer.parseInt(getText(1, 1));
                                if (digit2 > 3) {
                                    if (result.length() > i + shift + 1)
                                        result.setCharAt(i + shift + 1, '0');
                                    else result.insert(i + shift + 1, '0');
                                    SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            field.setCaretPosition(field.getCaretPosition() - 1);
                                        }
                                    });
                                }
                            }
                            result.setCharAt(i + shift, text.charAt(i));
                            break;

                        case 1:
                            int prevDigit;
                            if (offset == 0) {
                                prevDigit = Integer.parseInt(result.substring(0, 1));
                            } else {
                                prevDigit = Integer.parseInt(getText(0, 1));
                            }
                            if (prevDigit == 2) {
                                if (digit > 3) {
                                    if (result.length() == 1)
                                        result.deleteCharAt(0);
                                    else
                                        result.setCharAt(i + shift, '3');
                                } else result.setCharAt(i + shift, text.charAt(i));
                            } else
                                result.setCharAt(i + shift, text.charAt(i));
                            break;

                        case 3:
                            if (digit > 5) {
                                if (result.length() == 1) {
                                    result.deleteCharAt(0);
                                    shift--;
                                } else {
                                    result.setCharAt(i + shift, '5');
                                }
                                break;
                            }
                            result.setCharAt(i + shift, text.charAt(i));
                            break;

                        default:
                            result.setCharAt(i + shift, text.charAt(i));
                            break;
                    }
                } else {
                    if (result.length() == 1) {
                        result.deleteCharAt(0);
                        shift--;
                    } else result.insert(i + shift, PLACE_HOLDER);
                }
            } else if ((mask.charAt(i + offset) != PLACE_HOLDER) && (Character.isDigit(text.charAt(i)))) {
                int digit = Integer.parseInt(text.substring(i, i + 1));
                if (digit > 5) {
                    if (result.length() > i + 1)
                        result.deleteCharAt(i + 1 + shift);
                } else {
                    result.insert(i + 1 + shift, text.charAt(i));
                    shift++;
                }
            }
            i++;
        }
        return result.toString();
    }
}