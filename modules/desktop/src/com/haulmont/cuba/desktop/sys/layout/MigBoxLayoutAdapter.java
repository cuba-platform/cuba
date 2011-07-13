/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

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
        lc.hideMode(2); // The size of an invisible component will be set to 0, 0 and the gaps will also be set to 0 around it.
        AC rowConstr = new AC();
        AC colConstr = new AC();

        if (direction.equals(FlowDirection.X)) {
            rowConstr.align("top");
            lc.flowX();
            if (expandedComponent != null) {
                adjustExpanding(lc, colConstr);
            } else {
                lc.fillY();
                if (!expandLayout) {
                    colConstr.size("min!");
                }
            }
        } else {
            lc.flowY();
            if (expandedComponent != null) {
                adjustExpanding(lc, rowConstr);
            } else {
                lc.fillX();
                if (!expandLayout) {
                    rowConstr.size("min!");
                }
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
    public Object getCaptionConstraints() {
        CC cc = new CC();
        cc.split(2);
        cc.width("min!");
        cc.height("min!");
        return cc;
    }

    @Override
    public void expand(Component component, String height, String width) {
        super.expand(component, height, width);

        CC constraints = MigLayoutHelper.getExpandConstraints(width, height);
        layout.setComponentConstraints(component, constraints);
    }

    @Override
    public void updateConstraints(JComponent component, Object constraints) {
        layout.setComponentConstraints(component, constraints);
    }

    @Override
    public CC getConstraints(com.haulmont.cuba.gui.components.Component component) {
        CC constraints = MigLayoutHelper.getConstraints(component);
        return constraints;
    }
}
