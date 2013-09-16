/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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
    protected TextFieldListener createTextListener() {
        return new TextFieldListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                 updateMissingValueState();
            }
        };
    }

    @Override
    public int getRows() {
        return impl.getRows();
    }

    @Override
    public void setRows(int rows) {
        impl.setRows(rows);

        int minHeight = impl.getPreferredSize().height;
        int minWidth = impl.getMinimumSize().width;

        Insets insets = impl.getInsets();
        minWidth += insets.left + insets.right;
        minHeight += insets.bottom + insets.top;

        composition.setMinimumSize(new Dimension(minWidth, minHeight));
    }

    @Override
    public int getColumns() {
        return impl.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        impl.setColumns(columns);

        if (columns > 1) {
            int minHeight = impl.getMinimumSize().height;
            int minWidth = impl.getPreferredSize().width;

            Insets insets = impl.getInsets();
            minWidth += insets.left + insets.right;
            minHeight += insets.bottom + insets.top;

            composition.setMinimumSize(new Dimension(minWidth, minHeight));
        } else {
            int minHeight = impl.getMinimumSize().height;

            Insets insets = impl.getInsets();
            minHeight += insets.bottom + insets.top;

            composition.setMinimumSize(new Dimension(0, minHeight));
        }
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