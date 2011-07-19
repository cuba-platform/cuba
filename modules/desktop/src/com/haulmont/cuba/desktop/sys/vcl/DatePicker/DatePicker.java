/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl.DatePicker;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.UserSessionProvider;
import org.apache.commons.lang.ObjectUtils;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DatePickerFormatter;
import org.jdesktop.swingx.util.Contract;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class DatePicker extends JXDatePicker {
    protected String format;

    private static final char PLACE_HOLDER = '_';

    public void setEditor(final JFormattedTextField editor) {

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\u007F' && editor.getCaretPosition() < format.length()) {
                    editor.setCaretPosition(editor.getCaretPosition() + 1);
                }
            }
        });

        editor.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {

                editor.setCaretPosition(0);
            }
        });

        super.setEditor(editor);

        if (format == null) {
            setFormats(Datatypes.getFormatStrings(UserSessionProvider.getLocale()).getDateFormat());
        }
        editor.setDocument(new DatePickerDocument(editor, format, getMask(format), PLACE_HOLDER));
    }


    public void setFormats(String... formats) {
        super.setFormats(formats);
        format = formats[0];
        if (getEditor() != null)
            getEditor().setText(getMask(format));
    }

    public void setFormats(DateFormat... formats) {
        if (formats != null) {
            Contract.asNotNull(formats, "the array of formats " + "must not contain null elements");
        }
        DateFormat[] old = getFormats();
        for (DateFormat format : formats) {
            format.setLenient(false);
        }
        getEditor().setFormatterFactory(new DefaultFormatterFactory(
                new DatePicker.CustomDatePickerFormatter(formats, getLocale())));
        firePropertyChange("formats", old, getFormats());
    }

    protected String getMask(String format) {
        StringBuilder mask = new StringBuilder(format);
        for (int i = 0; i < mask.length(); i++) {
            char current = mask.charAt(i);
            current = Character.toLowerCase(current);
            if (current == 'd' || current == 'm' || current == 'y') {
                mask.setCharAt(i, PLACE_HOLDER);
            }
        }
        return mask.toString();
    }

    public DateFormat[] getFormats() {
        if (getEditor() != null) {
            return super.getFormats();
        } else
            return new DateFormat[0];
    }

    public class CustomDatePickerFormatter extends DatePickerFormatter {

        public CustomDatePickerFormatter(DateFormat formats[], Locale locale) {
            super(formats, locale);
        }

        public Object stringToValue(String text) throws ParseException {
            if (text == null || text.trim().length() == 0 || ObjectUtils.equals(getMask(format), text)) {
                return null;
            }
            return super.stringToValue(text);
        }
    }

}
