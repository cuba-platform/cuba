/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

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

        return getSizeConstraints(expandable, widthValue, widthUnits, heightValue, heightUnits);
    }

    public static CC getSizeConstraints(Component component) {
        boolean expandable = component instanceof Component.Expandable
                && ((Component.Expandable) component).isExpandable();

        int width = (int) component.getWidth();
        int widthUnits = component.getWidthUnits();

        int height = (int) component.getHeight();
        int heightUnits = component.getHeightUnits();

        return getSizeConstraints(expandable, width, widthUnits, height, heightUnits);
    }

    private static CC getSizeConstraints(boolean expandable, int width, int widthUnits, int height, int heightUnits) {
        CC cc = new CC();

        if (width == -1) { // own size
            cc.growX(0);
        }
        else if (expandable && widthUnits == Component.UNITS_PERCENTAGE) {
            cc.growX();
            cc.width(width + "%");
        }
        else if (width != 0 && widthUnits == Component.UNITS_PIXELS) {
            cc.growX(0);
            cc.width(width + "!");  // min, pref, max size as specified
        }
        else {
            if (expandable) {  // todo uncertain about it
                cc.growX();
            }
            else {
                cc.growX(0);
            }
        }

        if (height == -1) { // own size
            cc.growY(0.0f);
        }
        else if (expandable && heightUnits == Component.UNITS_PERCENTAGE) {
            cc.growY();
            cc.height(height + "%");
        }
        else if (height != 0 && heightUnits == Component.UNITS_PIXELS) {
            cc.growY(0.0f);
            cc.height(height + "!"); // min, pref, max size as specified
        }
        else {
            if (expandable) {  // todo uncertain about it
                cc.growY();
            }
            else {
                cc.growY(0.0f);
            }
        }
        return cc;
    }

}
