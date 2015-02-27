/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import javax.swing.*;
import java.awt.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class ExtendedComboBox extends JComboBox<Object> {

    private static final int MAX_LIST_WIDTH = 350;

    private boolean layingOut = false;
    private int widestLength = 0;

    private boolean hideButton = false;

    public ExtendedComboBox() {
    }

    public void updatePopupWidth() {
        widestLength = getWidestItemWidth();
    }

    @Override
    public void addItem(Object anObject) {
        super.addItem(anObject);
        updatePopupWidth();
    }

    @Override
    public void insertItemAt(Object anObject, int index) {
        super.insertItemAt(anObject, index);
        updatePopupWidth();
    }

    @Override
    public void removeItem(Object anObject) {
        super.removeItem(anObject);
        updatePopupWidth();
    }

    @Override
    public void removeAllItems() {
        super.removeAllItems();
        updatePopupWidth();
    }

    @Override
    public void removeItemAt(int anIndex) {
        super.removeItemAt(anIndex);
        updatePopupWidth();
    }

    @Override
    public Dimension getSize() {
        Dimension dim = super.getSize();
        if (!layingOut) {
            dim.width = Math.max(widestLength, dim.width);
            dim.width = Math.min(MAX_LIST_WIDTH, dim.width);
            dim.width = Math.max(getWidth(), dim.width);
        }
        return dim;
    }

    public void setButtonVisible(boolean buttonVisible) {
        for (Component child : getComponents()) {
            if (child instanceof JButton) {
                child.setVisible(buttonVisible);
                break;
            }
        }
    }

    @Override
    public void setEnabled(boolean b) {
        if (b == isEnabled()) {
            return;
        }

        setButtonVisible(!hideButton);

        super.setEnabled(b);
    }

    private int getWidestItemWidth() {
        if (isVisible() && !layingOut) {
            int numOfItems = this.getItemCount();
            Font font = this.getFont();
            FontMetrics metrics = this.getFontMetrics(font);
            int widest = 0;
            for (int i = 0; i < numOfItems; i++) {
                Object item = this.getItemAt(i);
                if (item != null) {
                    String itemString = item.toString();
                    if (itemString == null)
                        itemString = "";

                    int lineWidth = metrics.stringWidth(itemString);
                    widest = Math.max(widest, lineWidth);
                }
            }
            if (this.getItemCount() > 7)
                widest += 12;
            return widest + 15;
        } else
            return getWidth();
    }

    @Override
    public void doLayout() {
        try {
            layingOut = true;
            super.doLayout();
        } finally {
            layingOut = false;
        }
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (getEditor() != null && getEditor().getEditorComponent() != null) {
            getEditor().getEditorComponent().setBackground(bg);
        }
    }

    public boolean isHideButton() {
        return hideButton;
    }

    public void setHideButton(boolean hideButton) {
        this.hideButton = hideButton;
    }
}