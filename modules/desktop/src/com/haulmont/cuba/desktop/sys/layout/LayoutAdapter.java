/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

import com.haulmont.cuba.gui.components.Layout;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class LayoutAdapter implements Layout.Margin, Layout.Spacing {

    public enum FlowDirection { X, Y }

    protected boolean[] margins = new boolean[4];
    protected boolean spacing;
    protected FlowDirection direction = FlowDirection.X;

    public static boolean isDebug() {
        String property = System.getProperty("cuba.desktop.debugLayouts");
        return Boolean.valueOf(property);
    }

    public static LayoutAdapter create(JComponent container) {
        MigLayoutAdapter layoutAdapter = new MigLayoutAdapter(container);
        container.setLayout(layoutAdapter.getLayout());
        return layoutAdapter;
    }

    public static LayoutAdapter create(LayoutManager layout, JComponent container) {
        if (layout instanceof MigLayout) {
            MigLayoutAdapter layoutAdapter = new MigLayoutAdapter((MigLayout) layout, container);
            container.setLayout(layoutAdapter.getLayout());
            return layoutAdapter;
        }
        else
            throw new UnsupportedOperationException("Unsupported layout manager: " + layout);
    }

    public abstract LayoutManager getLayout();

    public abstract void update();

    public abstract void expand(java.awt.Component component, String height, String width);

    public void expand(java.awt.Component component) {
        expand(component, null, null);
    }

    public void setMargin(boolean enable) {
        margins[0] = enable;
        margins[1] = enable;
        margins[2] = enable;
        margins[3] = enable;
        update();
    }

    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        margins[0] = topEnable;
        margins[1] = rightEnable;
        margins[2] = bottomEnable;
        margins[3] = leftEnable;
        update();
    }

    public void setSpacing(boolean enabled) {
        spacing = enabled;
        update();
    }

    public void setFlowDirection(FlowDirection direction) {
        this.direction = direction;
        update();
    }
}
