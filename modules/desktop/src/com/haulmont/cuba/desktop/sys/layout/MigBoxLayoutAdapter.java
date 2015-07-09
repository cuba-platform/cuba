/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.layout;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MigBoxLayoutAdapter extends BoxLayoutAdapter {

    protected MigLayout layout;
    protected JComponent container;

    public MigBoxLayoutAdapter(JComponent container) {
        this(new MigLayout(), container);
    }

    public MigBoxLayoutAdapter(MigLayout layout, JComponent container) {
        this.layout = layout;
        this.container = container;
        update();
    }

    @Override
    public LayoutManager getLayout() {
        return layout;
    }

    @Override
    protected void update() {
        LC lc = new LC();
        lc.hideMode(2); //  Invisible components will not participate in the layout at all and it will for instance not take up a grid cell
        lc.fill(); // always give all space to components, otherwise align doesn't work
        AC rowConstr = new AC();
        AC colConstr = new AC();

        if (direction.equals(FlowDirection.X)) {
            rowConstr.align("top");
            lc.flowX();
            if (expandedComponent != null) {
                adjustExpanding(lc, colConstr);
            }
        } else {
            lc.flowY();
            if (expandedComponent != null) {
                adjustExpanding(lc, rowConstr);
            }
        }

        lc.setInsets(MigLayoutHelper.makeInsets(margins));

        if (!spacing) {
            if (direction.equals(FlowDirection.X))
                lc.gridGapX("0");
            else
                lc.gridGapY("0");
        }

        if (isDebug())
            lc.debug(1000);

        layout.setLayoutConstraints(lc);
        layout.setRowConstraints(rowConstr);
        layout.setColumnConstraints(colConstr);
    }

    private void adjustExpanding(LC lc, AC ac) {
        Component[] components = container.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (expandedComponent == components[i])
                ac.fill(i);
            else
                ac.size("min!", i);
        }
        lc.fill();
    }

    @Override
    public Object getCaptionConstraints(com.haulmont.cuba.gui.components.Component component) {
        CC cc = new CC();
        cc.split(2);
        cc.width("min!");
        cc.height("min!");
        MigLayoutHelper.applyAlignment(cc, component.getAlignment());
        return cc;
    }

    @Override
    public void expand(Component component, String height, String width) {
        super.expand(component, height, width);

        Object cc = layout.getComponentConstraints(component);
        if (cc instanceof CC) {
            if (direction == null || direction == BoxLayoutAdapter.FlowDirection.X
                    && (StringUtils.isEmpty(height) || "-1px".equals(height) || height.endsWith("%"))) {
                MigLayoutHelper.applyWidth((CC) cc, 100, com.haulmont.cuba.gui.components.Component.UNITS_PERCENTAGE, true);
            }
            if (direction == null || direction == BoxLayoutAdapter.FlowDirection.Y
                    && (StringUtils.isEmpty(width) || "-1px".equals(width) || width.endsWith("%"))) {
                MigLayoutHelper.applyHeight((CC) cc, 100, com.haulmont.cuba.gui.components.Component.UNITS_PERCENTAGE, true);
            }

        } else
            cc = MigLayoutHelper.getExpandConstraints(width, height, direction);
        layout.setComponentConstraints(component, cc);
    }

    @Override
    public void updateConstraints(JComponent component, Object constraints) {
        if (component == expandedComponent) {
            expand(component);
        } else {
            layout.setComponentConstraints(component, constraints);
            update();
        }
    }

    @Override
    public CC getConstraints(com.haulmont.cuba.gui.components.Component component) {
        CC constraints = MigLayoutHelper.getConstraints(component);
        return constraints;
    }
}
