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

package com.haulmont.cuba.desktop.sys.vcl;

import javax.swing.*;
import java.awt.*;

/**
 */
public class ExtendedComboBox extends JComboBox<Object> implements Flushable {

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

    @Override
    public void flushValue() {
    }
}