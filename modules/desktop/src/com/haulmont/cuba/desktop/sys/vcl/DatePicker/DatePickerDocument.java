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

package com.haulmont.cuba.desktop.sys.vcl.DatePicker;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DatePickerDocument extends PlainDocument {
    private static final int MAX_DAYS = 31;

    protected char placeHolder;
    protected JTextField field;
    protected String mask;
    protected String dateFormat;

    private int month0 = -1;
    private int month1 = -1;
    private int day0 = -1;
    private int day1 = -1;

    public DatePickerDocument(final JTextField field, String dateFormat, String mask, char placeHolder) {
        this.field = field;
        this.dateFormat = dateFormat;
        parseFormat(dateFormat);
        this.mask = mask;
        this.placeHolder = placeHolder;

        try {
            super.insertString(0, mask, null);
        } catch (BadLocationException e) {
            //offset in insertString == 0
        }
    }

    private void parseFormat(String format) {
        for (int i = 0; i < format.length(); i++) {
            char current = format.charAt(i);
            switch (current) {
                case 'd':
                    if (day0 == -1)
                        day0 = i;
                    else day1 = i;
                    break;
                case 'M':
                    if (month0 == -1)
                        month0 = i;
                    else month1 = i;
                    break;
            }
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
        boolean inserted = false;
        while (i + offset < mask.length() && i < text.length()) {
            if ((mask.charAt(i + offset) == placeHolder)) {
                if ((Character.isDigit(text.charAt(i)))) {
                    int digit = Integer.parseInt(text.substring(i, i + 1));

                    if (i + offset == month0) {
                        if (digit > 1) {
                            if (text.length() == 1) {
                                result.deleteCharAt(i + shift);
                                shift--;
                            } else {
                                if (result.length() > i + shift)
                                    result.setCharAt(i + shift, '1');
                                else result.insert(i + shift, '1');
                            }
                            inserted = true;
                        } else if (digit == 1) {
                            char c = getText(month1, 1).charAt(0);
                            int digit2 = Character.isDigit(c) ? Integer.parseInt(getText(month1, 1)) : 0;
                            if (digit2 > 2) {
                                if (result.length() > i + shift + 1)
                                    result.setCharAt(i + shift + 1, '0');
                                else result.insert(i + shift + 1, '0');
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        field.setCaretPosition(field.getCaretPosition() - 1);
                                    }
                                });
                            }
                        }
                        if (!inserted)
                            result.setCharAt(i + shift, text.charAt(i));

                    } else if (i + offset == month1) {
                        int prevDigit;
                        try {
                            if (offset <= month0) {
                                prevDigit = Integer.parseInt(result.substring(month0 - offset, month0 - offset + 1));
                            } else {
                                prevDigit = Integer.parseInt(getText(month0, 1));
                            }
                        } catch (NumberFormatException e) {
                            prevDigit = 0;
                        }
                        if (prevDigit == 1) {
                            if (digit > 2) {
                                if (result.length() == 1)
                                    result.deleteCharAt(0);
                                else
                                    result.setCharAt(i + shift, '0');
                            } else result.setCharAt(i + shift, text.charAt(i));
                        } else
                            result.setCharAt(i + shift, text.charAt(i));
                        inserted = true;
                    } else if (i + offset == day0) {
                        if (result.length() == 1) {
                            if (digit > MAX_DAYS / 10) {
                                result.deleteCharAt(0);
                                shift--;
                            } else if (digit == MAX_DAYS / 10) {
                                char c = getText(day1, 1).charAt(0);
                                int digit2 = Character.isDigit(c) ? Integer.parseInt(getText(day1, 1)) : 0;
                                if (digit2 > MAX_DAYS % 10) {
                                    result.setCharAt(i + shift, text.charAt(i));
                                    result.insert(i + shift + 1, '0');
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            field.setCaretPosition(field.getCaretPosition() - 1);
                                        }
                                    });
                                } else result.setCharAt(i + shift, text.charAt(i));
                            } else result.setCharAt(i + shift, text.charAt(i));
                        } else result.setCharAt(i + shift, text.charAt(i));
                    } else if (i + offset == day1) {
                        if (result.length() == 1) {
                            char c = getText(day0, 1).charAt(0);
                            int prevDigit = Character.isDigit(c) ? Integer.parseInt(getText(day0, 1)) : 0;
                            if (prevDigit * 10 + digit > MAX_DAYS) {
                                result.deleteCharAt(0);
                                shift--;
                            } else result.setCharAt(i + shift, text.charAt(i));
                        } else result.setCharAt(i + shift, text.charAt(i));
                    } else {
                        result.setCharAt(i + shift, text.charAt(i));
                    }
                } else {
                    if (result.length() == 1) {
                        result.deleteCharAt(0);
                        shift--;
                    } else {
                        result.setCharAt(i + shift, placeHolder);
                    }
                }
            } else if ((mask.charAt(i + offset) != placeHolder) && (Character.isDigit(text.charAt(i)))) {
                int digit = Integer.parseInt(text.substring(i, i + 1));
                if (i + offset + 1 == month0) {
                    if (digit > 1) {
                        if (result.length() > i + 1)
                            result.deleteCharAt(i + 1 + shift);
                    } else if (digit == 1 && result.length() == 1) {
                        char c = getText(month1, 1).charAt(0);
                        int digit2 = Character.isDigit(c) ? Integer.parseInt(getText(month1, 1)) : 0;
                        if (digit2 > 2) {
                            result.insert(i + 1 + shift, text.charAt(i));
                            result.insert(i + shift + 1 + 1, '0');
                            shift++;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    field.setCaretPosition(field.getCaretPosition() - 1);
                                }
                            });
                        } else {
                            result.insert(i + 1 + shift, text.charAt(i));
                            shift++;
                        }
                    } else {
                        result.insert(i + 1 + shift, text.charAt(i));
                        shift++;
                    }
                } else if (i + offset + 1 == day0) {
                    if (digit > MAX_DAYS / 10) {
                        if (result.length() > i + 1)
                            result.deleteCharAt(i + 1 + shift);
                    } else if (digit == MAX_DAYS / 10 && result.length() == 1) {
                        char c = getText(day1, 1).charAt(0);
                        int digit2 = Character.isDigit(c) ? Integer.parseInt(getText(day1, 1)) : 0;
                        if (digit2 > MAX_DAYS % 10) {
                            result.insert(i + shift + 1, text.charAt(i));
                            result.insert(i + shift + 2, '0');
                            shift++;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    field.setCaretPosition(field.getCaretPosition() - 1);
                                }
                            });
                        } else {
                            result.insert(i + shift + 1, text.charAt(i));
                            shift++;
                        }
                    } else {
                        result.insert(i + 1 + shift, text.charAt(i));
                        shift++;
                    }
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