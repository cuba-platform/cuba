/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.gui.components.TextArea;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Set;

import static javax.swing.KeyStroke.getKeyStroke;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopTextArea extends DesktopAbstractTextField<JTextArea> implements TextArea {

    protected JComponent composition;

    @Override
    protected JTextArea createTextComponentImpl() {
        final JTextArea impl = new TextAreaFlushableField();

        if (isTabTraversal()) {
            Set<KeyStroke> forwardFocusKey = Collections.singleton(getKeyStroke(KeyEvent.VK_TAB, 0));
            impl.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardFocusKey);

            Set<KeyStroke> backwardFocusKey = Collections.singleton(getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK));
            impl.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardFocusKey);

            impl.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (isEnabled() && isEditable()
                            && e.getKeyCode() == KeyEvent.VK_TAB
                            && e.getModifiers() == KeyEvent.CTRL_MASK) {

                        if (StringUtils.isEmpty(impl.getText())) {
                            impl.setText("\t");
                        } else {
                            impl.append("\t");
                        }
                    }
                }
            });
        }

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

    protected boolean isTabTraversal() {
        return true;
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

    protected class TextAreaFlushableField extends JTextArea implements Flushable {

        @Override
        public void flushValue() {
            flush();
        }
    }
}