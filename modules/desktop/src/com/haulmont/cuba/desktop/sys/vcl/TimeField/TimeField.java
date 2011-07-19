/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl.TimeField;

import javax.swing.*;
import java.awt.event.*;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class TimeField extends JFormattedTextField {
    private String mask;

    public TimeField(String mask) {
        super();
        this.mask = mask;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\u007F' && getCaretPosition() < getMask().length()) {
                    setCaretPosition(getCaretPosition() + 1);
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setCaretPosition(0);
            }
        });
        setDocument(new TimeFieldDocument(this, mask));
    }


    public String getMask() {
        return mask;
    }

    public int getHours() {
        int hours = Integer.parseInt(getText().substring(0, 2));
        if (hours < 0 || hours > 23) {
            throw new NumberFormatException("Invalid hours: " + hours);
        }
        return hours;
    }

    public int getMinutes() {
        if (mask.length() == 5) {
            int min = Integer.parseInt(getText().substring(3, 5));
            if (min < 0 || min > 59) {
                throw new NumberFormatException("Invalid minutes: " + min);
            }
            return min;
        } else return 0;
    }
}
