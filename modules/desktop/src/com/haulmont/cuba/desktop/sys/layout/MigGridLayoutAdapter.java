/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.layout;

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
public class MigGridLayoutAdapter extends GridLayoutAdapter {

    protected MigLayout layout;
    protected JComponent container;

    public MigGridLayoutAdapter(JComponent container) {
        this(new MigLayout(), container);
    }

    public MigGridLayoutAdapter(MigLayout layout, JComponent container) {
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
        lc.setWrapAfter(getColumns());

        lc.setInsets(MigLayoutHelper.makeInsets(margins));

        if (!spacing) {
            lc.gridGap("0", "0");
        }

        if (isDebug())
            lc.debug(1000);

        layout.setLayoutConstraints(lc);

        // middle align in grid by default
        /*AC rowConstr = new AC();
        rowConstr.align("top");
        layout.setRowConstraints(rowConstr);*/
    }

    @Override
    public CC getConstraints(com.haulmont.cuba.gui.components.Component component) {
        return MigLayoutHelper.getConstraints(component);
    }

    @Override
    public CC getConstraints(com.haulmont.cuba.gui.components.Component component, int col, int row, int col2, int row2) {
        int spanX = col2 - col;
        int spanY = row2 - row;

        CC constraints = MigLayoutHelper.getConstraints(component);
        constraints.cell(col, row, spanX, spanY);

        return constraints;
    }

    @Override
    public void updateConstraints(JComponent component, Object constraints) {
        // todo delete, evidently no needed
        /*if (layout.isManagingComponent(component)
                && layout.getComponentConstraints(component) instanceof CC
                && constraints instanceof CC) {
            // trying to keep the same cell and row for component
            CC current = (CC) layout.getComponentConstraints(component);
            int col = current.getCellX();
            int row = current.getCellY();
            int spanX = current.getSpanX();
            int spanY = current.getSpanY();

            ((CC) constraints).cell(col, row, spanX, spanY);
        }*/
        layout.setComponentConstraints(component, constraints);
    }
}
