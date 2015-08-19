/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.layout;

import com.haulmont.cuba.desktop.gui.components.AutoExpanding;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Component;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.UnitValue;
import org.apache.commons.lang.StringUtils;

/**
 * @author krivopustov
 * @version $Id$
 */
public class MigLayoutHelper {

    public static UnitValue[] makeInsets(boolean[] margins) {
        UnitValue[] unitValues = new UnitValue[4];

        // at cuba it's       top, right, bottom, left
        // at MigLayout it's  top, left, bottom, right
        unitValues[0] = makeInsetValue(margins[0]);
        unitValues[1] = makeInsetValue(margins[3]);
        unitValues[2] = makeInsetValue(margins[2]);
        unitValues[3] = makeInsetValue(margins[1]);

        return unitValues;
    }

    private static UnitValue makeInsetValue(boolean margin) {
        return margin ? null : new UnitValue(0);
    }

    public static CC getExpandConstraints(String width, String height, BoxLayoutAdapter.FlowDirection direction) {
        CC cc = new CC();

        if (direction == null || direction == BoxLayoutAdapter.FlowDirection.X
                && (StringUtils.isEmpty(height) || "-1px".equals(height) || height.endsWith("%"))) {
            applyWidth(cc, 100, Component.UNITS_PERCENTAGE, true);
        }
        if (direction == null || direction == BoxLayoutAdapter.FlowDirection.Y
                && (StringUtils.isEmpty(width) || "-1px".equals(width) || width.endsWith("%"))) {
            applyHeight(cc, 100, Component.UNITS_PERCENTAGE, true);
        }

        return cc;
    }

    public static CC getConstraints(Component component) {
        boolean expandX = false;
        boolean expandY = false;

        // for latter comparing with AutoExpanding
        if (component instanceof AbstractFrame) {
            component = (Component) ((AbstractFrame) component).getComponent();
        }

        if (component instanceof AutoExpanding) {
            expandX = ((AutoExpanding) component).expandsWidth();
            expandY = ((AutoExpanding) component).expandsHeight();
        }

        int width = (int) component.getWidth();
        int widthUnits = component.getWidthUnits();

        int height = (int) component.getHeight();
        int heightUnits = component.getHeightUnits();

        CC cc = new CC();

        applyWidth(cc, width, widthUnits, expandX);
        applyHeight(cc, height, heightUnits, expandY);

        applyAlignment(cc, component.getAlignment());
        return cc;
    }

    public static void applyAlignment(CC cc, Component.Alignment align) {
        if (align == null) {
            align = Component.Alignment.TOP_LEFT; // same as for web
        }

        switch (align) {
            case TOP_RIGHT:
                cc.alignX("right").alignY("top");
                break;
            case TOP_LEFT:
                cc.alignX("left").alignY("top");
                break;
            case TOP_CENTER:
                cc.alignX("50%").alignY("top");
                break;
            case MIDDLE_RIGHT:
                cc.alignX("right").alignY("50%");
                break;
            case MIDDLE_LEFT:
                cc.alignX("left").alignY("50%");
                break;
            case MIDDLE_CENTER:
                cc.alignX("50%").alignY("50%");
                break;
            case BOTTOM_RIGHT:
                cc.alignX("right").alignY("bottom");
                break;
            case BOTTOM_LEFT:
                cc.alignX("left").alignY("bottom");
                break;
            case BOTTOM_CENTER:
                cc.alignX("50%").alignY("bottom");
                break;
        }
    }

    public static void applyHeight(CC constraints, int height, int heightUnits, boolean expand) {
        if (height == -1) { // own size
            constraints.growY(0.0f);
        } else if (heightUnits == Component.UNITS_PERCENTAGE) {
            constraints.height(height + "%");
        } else if (height != 0 && heightUnits == Component.UNITS_PIXELS) {
            constraints.growY(0.0f);
            constraints.height(height + "!"); // min, pref, max size as specified
        } else {
            if (expand) {
                constraints.growY();
                constraints.growPrioY(99); // lower grow priority
                constraints.height("100%"); // preffered size to full container
            } else {
                constraints.growY(0.0f);
            }
        }
    }

    public static void applyWidth(CC constraints, int width, int widthUnits, boolean expand) {
        if (width == -1) { // own size
            constraints.growX(0);
        } else if (widthUnits == Component.UNITS_PERCENTAGE) {
            constraints.width(width + "%");
        } else if (width != 0 && widthUnits == Component.UNITS_PIXELS) {
            constraints.growX(0);
            constraints.width(width + "!");  // min, pref, max size as specified
        } else {
            if (expand) {
                constraints.growX();
                constraints.growPrioX(99); // lower grow priority
                constraints.width("100%"); // preffered size to full container
            } else {
                constraints.growX(0);
            }
        }
    }
}