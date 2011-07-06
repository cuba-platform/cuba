/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

import com.haulmont.cuba.desktop.gui.components.AutoExpanding;
import com.haulmont.cuba.desktop.gui.data.ComponentSize;
import com.haulmont.cuba.gui.components.Component;
import net.miginfocom.layout.CC;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class MigLayoutConstraints {

    public static CC getSizeConstraints(String width, String height, boolean expandable) {
        ComponentSize w = ComponentSize.parse(width);
        ComponentSize h = ComponentSize.parse(height);

        int widthValue = (int) w.value;
        int widthUnits = w.unit;

        int heightValue = (int) h.value;
        int heightUnits = h.unit;

        return getSizeConstraints(widthValue, widthUnits, expandable, heightValue, heightUnits, expandable);
    }

    public static CC getSizeConstraints(Component component) {
        boolean expandX = false;
        boolean expandY = false;
        if (component instanceof AutoExpanding) {
            boolean expandable = component instanceof Component.Expandable
                    && ((Component.Expandable) component).isExpandable();

            AutoExpanding autoExpanding = (AutoExpanding) component;
            expandX = expandable && autoExpanding.expandsWidth();
            expandY = expandable && autoExpanding.expandsHeight();
        }

        int width = (int) component.getWidth();
        int widthUnits = component.getWidthUnits();

        int height = (int) component.getHeight();
        int heightUnits = component.getHeightUnits();

        return getSizeConstraints(width, widthUnits, expandX, height, heightUnits, expandY);
    }

    private static CC getSizeConstraints(int width, int widthUnits, boolean expandX, int height, int heightUnits, boolean expandY) {
        CC cc = new CC();

        if (width == -1) { // own size
            cc.growX(0);
        }
        else if (widthUnits == Component.UNITS_PERCENTAGE) {
            cc.growX();
            cc.width(width + "%");
        }
        else if (width != 0 && widthUnits == Component.UNITS_PIXELS) {
            cc.growX(0);
            cc.width(width + "!");  // min, pref, max size as specified
        }
        else {
            if (expandX) {
                cc.growX();
                cc.growPrioX(99); // lower grow priority
                cc.width("100%"); // preffered size to full container
            }
            else {
                cc.growX(0);
            }
        }

        if (height == -1) { // own size
            cc.growY(0.0f);
        }
        else if (heightUnits == Component.UNITS_PERCENTAGE) {
            cc.growY();
            cc.height(height + "%");
        }
        else if (height != 0 && heightUnits == Component.UNITS_PIXELS) {
            cc.growY(0.0f);
            cc.height(height + "!"); // min, pref, max size as specified
        }
        else {
            if (expandY) {
                cc.growY();
                cc.growPrioY(99); // lower grow priority
                cc.height("100%"); // preffered size to full container
            }
            else {
                cc.growY(0.0f);
            }
        }
        return cc;
    }

}
