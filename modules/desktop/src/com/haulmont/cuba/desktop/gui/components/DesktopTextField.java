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

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.desktop.sys.validation.ValidationAwareAction;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class DesktopTextField extends DesktopAbstractTextField<JTextComponent> implements TextField {

    protected String inputPrompt;
    // just stub
    protected CaseConversion caseConversion;
    // just stub
    protected int textChangeTimeout;
    // just stub
    protected TextChangeEventMode textChangeEventMode = TextChangeEventMode.LAZY;

    protected java.util.List<EnterPressListener> enterPressListeners = new ArrayList<>();

    protected boolean enterPressInitialized = false;
    // just stub
    protected String htmlName;

    @Override
    protected JTextField createTextComponentImpl() {
        JTextField field = new FlushableTextField();

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
    public boolean isTrimming() {
        return trimming;
    }

    @Override
    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }

    @Override
    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    public void updateEnabled() {
        super.updateEnabled();

        refreshInputPrompt();
    }

    @Override
    protected void setEditableToComponent(boolean editable) {
        super.setEditableToComponent(editable);

        refreshInputPrompt();
    }

    protected void refreshInputPrompt() {
        if (StringUtils.isNotBlank(inputPrompt)) {
            if (isEnabledWithParent() && isEditableWithParent()) {
                // Save old tooltipText value to use it later
                String toolTipText = this.impl.getToolTipText();

                PromptSupport.setPrompt(inputPrompt, impl);

                // Use old tooltipText value because it was overwritten in org.jdesktop.swingx.prompt.PromptSupport.setPrompt()
                this.impl.setToolTipText(toolTipText);
            } else {
                PromptSupport.setPrompt(null, impl);
            }
        }
    }

    @Override
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
        this.valueFormatter.setDatatype(datatype);
    }

    @Override
    public Formatter getFormatter() {
        return valueFormatter.getFormatter();
    }

    @Override
    public void setFormatter(Formatter formatter) {
        valueFormatter.setFormatter(formatter);
    }

    @Override
    public String getInputPrompt() {
        return inputPrompt;
    }

    @Override
    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;

        if ((!isEditableWithParent() || !this.impl.isEnabled()) && StringUtils.isNotBlank(inputPrompt)) {
            return;
        }

        if (StringUtils.isNotBlank(inputPrompt)) {
            // Save old tooltipText value to use it later
            String toolTipText = this.impl.getToolTipText();

            PromptSupport.setPrompt(inputPrompt, impl);

            // Use old tooltipText value because it was overwritten in org.jdesktop.swingx.prompt.PromptSupport.setPrompt()
            this.impl.setToolTipText(toolTipText);
        } else {
            PromptSupport.setPrompt(null, impl);
        }
    }

    @Override
    public void setCursorPosition(int position) {
        impl.setSelectionStart(position);
        impl.setSelectionEnd(position);
    }

    @Override
    public CaseConversion getCaseConversion() {
        return caseConversion;
    }

    @Override
    public void setCaseConversion(CaseConversion caseConversion) {
        this.caseConversion = caseConversion;
    }

    @Override
    public String getRawValue() {
        return impl.getText();
    }

    @Override
    public void selectAll() {
        impl.selectAll();
    }

    @Override
    public void setSelectionRange(int pos, int length) {
        impl.select(pos, pos + length);
    }

    @Override
    public void addTextChangeListener(TextChangeListener listener) {
    }

    @Override
    public void removeTextChangeListener(TextChangeListener listener) {
    }

    @Override
    public void setTextChangeTimeout(int timeout) {
        this.textChangeTimeout = timeout;
    }

    @Override
    public int getTextChangeTimeout() {
        return textChangeTimeout;
    }

    @Override
    public TextChangeEventMode getTextChangeEventMode() {
        return textChangeEventMode;
    }

    @Override
    public void setTextChangeEventMode(TextChangeEventMode mode) {
        Preconditions.checkNotNullArgument(mode);
        this.textChangeEventMode = mode;
    }

    @Override
    public void addEnterPressListener(EnterPressListener listener) {
        Preconditions.checkNotNullArgument(listener);

        if (!enterPressListeners.contains(listener)) {
            enterPressListeners.add(listener);
        }

        if (!enterPressInitialized) {
            impl.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter");
            impl.getActionMap().put("enter", new ValidationAwareAction() {
                @Override
                public void actionPerformedAfterValidation(ActionEvent e) {
                    EnterPressEvent event = new EnterPressEvent(DesktopTextField.this);
                    for (EnterPressListener enterPressListener : new ArrayList<>(enterPressListeners)) {
                        enterPressListener.enterPressed(event);
                    }
                }
            });
            enterPressInitialized = true;
        }
    }

    @Override
    public void removeEnterPressListener(EnterPressListener listener) {
        enterPressListeners.remove(listener);
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

    protected class FlushableTextField extends JTextField implements Flushable {

        @Override
        public void flushValue() {
            flush();
        }
    }
}