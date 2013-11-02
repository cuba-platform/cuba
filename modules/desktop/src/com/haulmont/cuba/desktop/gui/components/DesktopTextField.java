/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTextField extends DesktopAbstractTextField<JTextComponent> implements TextField {

    public DesktopTextField() {
    }

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
        doc.setMaxLength(value);
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
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
        this.valueFormatter.setDatatype(datatype);
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        if (impl != null) {
            impl.setEditable(editable);
            updateMissingValueState();
        }
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return getImpl().getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        getImpl().setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    @Override
    public Formatter getFormatter() {
        return valueFormatter.getFormatter();
    }

    @Override
    public void setFormatter(Formatter formatter) {
        valueFormatter.setFormatter(formatter);
    }

    private class FlushableTextField extends JTextField implements Flushable {

        @Override
        public void flushValue() {
            flush();
        }
    }
}