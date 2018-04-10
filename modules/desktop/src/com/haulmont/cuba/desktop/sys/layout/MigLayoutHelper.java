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

package com.haulmont.cuba.desktop.sys.layout;

import com.haulmont.cuba.desktop.gui.components.AutoExpanding;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SizeUnit;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.UnitValue;
import org.apache.commons.lang.StringUtils;

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
            applyWidth(cc, 100, SizeUnit.PERCENTAGE, true);
        }
        if (direction == null || direction == BoxLayoutAdapter.FlowDirection.Y
                && (StringUtils.isEmpty(width) || "-1px".equals(width) || width.endsWith("%"))) {
            applyHeight(cc, 100, SizeUnit.PERCENTAGE, true);
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
        SizeUnit widthUnits = component.getWidthSizeUnit();

        int height = (int) component.getHeight();
        SizeUnit heightUnits = component.getHeightSizeUnit();

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

    /**
     * @deprecated Use {@link #applyHeight(CC, int, SizeUnit, boolean)}
     */
    @Deprecated
    public static void applyHeight(CC constraints, int height, int heightUnits, boolean expand) {
        applyHeight(constraints, height, ComponentsHelper.convertToSizeUnit(heightUnits), expand);
    }

    public static void applyHeight(CC constraints, int height, SizeUnit heightUnits, boolean expand) {
        if (height == -1) { // own size
            constraints.growY(0.0f);
        } else if (heightUnits == SizeUnit.PERCENTAGE) {
            constraints.height(height + "%");
        } else if (height != 0 && heightUnits == SizeUnit.PIXELS) {
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

    @Deprecated
    public static void applyWidth(CC constraints, int width, int widthUnits, boolean expand) {
        applyWidth(constraints, width, ComponentsHelper.convertToSizeUnit(widthUnits), expand);
    }

    public static void applyWidth(CC constraints, int width, SizeUnit widthUnits, boolean expand) {
        if (width == -1) { // own size
            constraints.growX(0);
        } else if (widthUnits == SizeUnit.PERCENTAGE) {
            constraints.width(width + "%");
        } else if (width != 0 && widthUnits == SizeUnit.PIXELS) {
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