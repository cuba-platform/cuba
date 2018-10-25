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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.desktop.sys.vcl.JTextFieldCapsLockSubscriber;
import com.haulmont.cuba.gui.components.CapsLockIndicator;
import com.haulmont.cuba.gui.components.PasswordField;

import javax.swing.*;
import java.awt.*;

public class DesktopPasswordField extends DesktopAbstractTextField<JPasswordField> implements PasswordField {

    protected Boolean autocomplete = false;

    protected CapsLockIndicator capsLockIndicator;

    protected JTextFieldCapsLockSubscriber capsLockSubscriber = new JTextFieldCapsLockSubscriber();
    // just stub
    protected String htmlName;

    @Override
    protected JPasswordField createTextComponentImpl() {
        JPasswordField field = new PasswordFlushableField();
        int height = (int) field.getPreferredSize().getHeight();
        field.setPreferredSize(new Dimension(150, height));
        return field;
    }

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(int value) {
        maxLength = value;
        ((TextComponentDocument) doc).setMaxLength(value);
    }

    @Override
    public boolean isAutocomplete() {
        return autocomplete;
    }

    @Override
    public void setAutocomplete(Boolean value) {
        this.autocomplete = value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getValue() {
        return super.getValue();
    }

    @Override
    public void commit() {
        // do nothing
    }

    @Override
    public void discard() {
        // do nothing
    }

    @Override
    public boolean isBuffered() {
        // do nothing
        return false;
    }

    @Override
    public void setBuffered(boolean buffered) {
        // do nothing
    }

    @Override
    public boolean isModified() {
        // do nothing
        return false;
    }

    @Override
    public void setCapsLockIndicator(CapsLockIndicator capsLockIndicator) {
        this.capsLockIndicator = capsLockIndicator;

        if (capsLockIndicator != null) {
            capsLockSubscriber.subscribe(impl, capsLockIndicator);
        } else {
            capsLockSubscriber.unsubscribe(impl);
        }
    }

    @Override
    public CapsLockIndicator getCapsLockIndicator() {
        return capsLockIndicator;
    }

    // just stub
    @Override
    public void setHtmlName(String htmlName) {
        this.htmlName = htmlName;
    }

    // just stub
    @Override
    public String getHtmlName() {
        return htmlName;
    }

    protected class PasswordFlushableField extends JPasswordField implements Flushable {

        public PasswordFlushableField() {
        }

        @Override
        public void flushValue() {
            flush();
        }
    }
}