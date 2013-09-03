/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextArea;

import javax.swing.*;
import java.awt.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopTextArea extends DesktopAbstractTextField<JTextArea> implements TextArea, Component.Wrapper {

    protected JComponent composition;

    @Override
    protected JTextArea createTextComponentImpl() {
        JTextArea impl = new JTextArea();

        impl.setLineWrap(true);
        impl.setWrapStyleWord(true);

        int height = (int) impl.getPreferredSize().getHeight();
        impl.setMinimumSize(new Dimension(0, height));

        composition = new JScrollPane(impl);
        composition.setPreferredSize(new Dimension(150, height));
        composition.setMinimumSize(new Dimension(0, height));

        doc.putProperty("filterNewlines", false);

        return impl;
    }

    @Override
    public JComponent getComposition() {
        return composition;
    }

    @Override
    public int getRows() {
        return impl.getRows();
    }

    @Override
    public void setRows(int rows) {
        impl.setRows(rows);
    }

    @Override
    public int getColumns() {
        return impl.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        impl.setColumns(columns);
    }

    @Override
    public int getMaxLength() {
        return doc.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
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
}