/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

import net.miginfocom.layout.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.Component;
import java.awt.LayoutManager;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class MigLayoutAdapter extends LayoutAdapter {

    protected MigLayout layout;
    protected JComponent container;
    protected Component expandedComponent;

    public MigLayoutAdapter(JComponent container) {
        this(new MigLayout(), container);
    }

    public MigLayoutAdapter(MigLayout layout, JComponent container) {
        this.layout = layout;
        this.container = container;
        update();
    }

    @Override
    public LayoutManager getLayout() {
        return layout;
    }

    @Override
    public void update() {
//        List<String> list = new ArrayList<String>();
//
//        list.add("fill");
//
//        if (direction.equals(FlowDirection.Y))
//            list.add("flowy");
//
//        if (margins == null)
//            list.add("insets " + inset(margin));
//        else
//            list.add("insets " + inset(margins[0]) + " " + inset(margins[1]) + " " + inset(margins[2]) + " " + inset(margins[3]));
//
//        if (!spacing)
//            list.add("gap n");
//
//        if (isDebug())
//            list.add("debug");
//
//        String constraints = new StrBuilder().appendWithSeparators(list, ",").toString();
//        layout.setLayoutConstraints(constraints);

        LC lc = new LC();
        AC rowConstr = new AC();
        AC colConstr = new AC();

        if (direction.equals(FlowDirection.X)) {
            lc.flowX();
            if (expandedComponent != null) {
                adjustExpanding(lc, colConstr);
            } else
                lc.fillY();
        } else {
            lc.flowY();
            if (expandedComponent != null) {
                adjustExpanding(lc, rowConstr);
            } else
                lc.fillX();
        }

        lc.setInsets(makeInsets());

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
    public void expand(Component component, String height, String width) {
        expandedComponent = component;
        update();
        layout.setComponentConstraints(component, new CC().grow());
    }

    protected UnitValue[] makeInsets() {
        UnitValue[] unitValues = new UnitValue[4];
        for (int i = 0; i < unitValues.length; i++) {
            unitValues[i] = margins[i] ? PlatformDefaults.getPanelInsets(i) : new UnitValue(0);
        }
        return unitValues;
    }
}
