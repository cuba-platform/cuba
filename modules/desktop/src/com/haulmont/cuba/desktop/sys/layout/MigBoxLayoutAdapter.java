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
    public void expand(Component component, String height, String width) {
        super.expand(component, height, width);

        // if not specified here, it means that full expand
        // while for other components it means own size
        if (StringUtils.isEmpty(width)) {
            width = "100%";
        }
        if (StringUtils.isEmpty(height)) {
            height = "100%";
        }
        CC constraints = MigLayoutConstraints.getSizeConstraints(width, height, true);
        layout.setComponentConstraints(component, constraints);
    }

    @Override
    public CC getConstraints(com.haulmont.cuba.gui.components.Component component) {
        CC constraints = MigLayoutConstraints.getSizeConstraints(component);
        return constraints;
    }
}
