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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 */
public class DesktopTextField extends DesktopAbstractTextField<JTextComponent> implements TextField {

    protected String inputPrompt;

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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        refreshInputPrompt();
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);

        refreshInputPrompt();
    }

    private void refreshInputPrompt() {
        if (StringUtils.isNotBlank(inputPrompt)) {
            if (isEnabledWithParent() && isEditable()) {
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

        if ((!this.impl.isEditable() || !this.impl.isEnabled()) && StringUtils.isNotBlank(inputPrompt)) {
            return;
        }

        if (StringUtils.isNotBlank(inputPrompt)) {
            PromptSupport.setPrompt(inputPrompt, impl);
        } else {
            PromptSupport.setPrompt(null, impl);
        }
    }

    @Override
    public void setCursorPosition(int position) {
        impl.setSelectionStart(position);
        impl.setSelectionEnd(position);
    }

    protected class FlushableTextField extends JTextField implements Flushable {

        @Override
        public void flushValue() {
            flush();
        }
    }
}