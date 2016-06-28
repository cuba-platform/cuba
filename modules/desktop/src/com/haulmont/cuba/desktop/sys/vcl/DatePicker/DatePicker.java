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

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Frame;
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
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatePicker extends JXDatePicker {
    protected String format;

    private static final char PLACE_HOLDER = '_';

    public DatePicker(){
        super();
        setUI(new CustomDatePickerUI());
    }

    @Override
    public void setEditor(final JFormattedTextField editor) {
        final int ENTER_CODE = 10;

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\u007F' && editor.getCaretPosition() < format.length()) {
                    editor.setCaretPosition(editor.getCaretPosition() + 1);
                }
            }

            @Override
            public void keyPressed(KeyEvent event) {
                if (ENTER_CODE == event.getKeyCode())
                    try {
                        editor.commitEdit();
                    } catch (ParseException e) {
                        //
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
            setFormats(Datatypes.getFormatStrings(AppBeans.get(UserSessionSource.class).getLocale()).getDateFormat());
        } else
            setFormats(format);
    }

    @Override
    public void setLinkDay(Date linkDay) {
        MessageFormat todayFormat = new MessageFormat(AppBeans.get(Messages.class).getMessage("com.haulmont.cuba.desktop", "DatePicker.linkFormat"));
        todayFormat.setFormat(0, new SimpleDateFormat(Datatypes.getFormatStrings(AppBeans.get(UserSessionSource.class).getLocale()).getDateFormat()));
        setLinkFormat(todayFormat);
        super.setLinkDay(linkDay);
    }

    @Override
    public void setFormats(String... formats) {
        super.setFormats(formats);
        format = formats[0];
        Object prevVal = getEditor().getValue();
        if (prevVal == null) {
            getEditor().setText(getMask(format));
        }
        getEditor().setDocument(new DatePickerDocument(getEditor(), format, getMask(format), PLACE_HOLDER));
        if (prevVal != null)
            getEditor().setValue(prevVal);
    }

    @Override
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

    @Override
    public DateFormat[] getFormats() {
        if (getEditor() != null) {
            return super.getFormats();
        } else
            return new DateFormat[0];
    }

    public class CustomDatePickerFormatter extends DatePickerFormatter {
        @Override
        public void install(final JFormattedTextField ftf) {
            try {
                if (valueToString(ftf.getValue()) == null && format != null) {
                    ftf.setText(getMask(format));
                    return;
                }
            } catch (ParseException e) {
                ftf.setText(getMask(format));
            }
            super.install(ftf);
            ftf.setCaretPosition(0);
        }

        public CustomDatePickerFormatter(DateFormat formats[], Locale locale) {
            super(formats, locale);
        }

        @Override
        public Object stringToValue(String text) throws ParseException {
            if (text == null || text.trim().length() == 0 || ObjectUtils.equals(getMask(format), text)) {
                return null;
            }
            try {
                return super.stringToValue(text);
            } catch (ParseException e) {
                DesktopComponentsHelper.getTopLevelFrame(getParent()).showNotification(
                        AppBeans.get(Messages.class).getMessage(AppConfig.getMessagesPack(), "validationFail"),
                        Frame.NotificationType.TRAY
                );
                cancelEdit();
                throw e;
            }
        }
    }
}