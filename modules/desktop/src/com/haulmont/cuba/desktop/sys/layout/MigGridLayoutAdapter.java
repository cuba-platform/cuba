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

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.gui.components.LayoutSlot;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

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
        lc.hideMode(2); // The size of an invisible component will be set to 0, 0 and the gaps will also be set to 0 around it.
        lc.fill();

        lc.setInsets(MigLayoutHelper.makeInsets(margins));

        if (!spacing) {
            lc.gridGap("0", "0");
        }

        if (isDebug())
            lc.debug(1000);

        layout.setLayoutConstraints(lc);

        AC rowConstr = new AC();
        rowConstr.align("top");  // left-top align by default
        layout.setRowConstraints(rowConstr);

        AC colConstr = new AC();
        for (int i = 0; i < colCount; i++) {
            float ratio = columnRatio[i];
            colConstr.grow(ratio, i);
            colConstr.shrink(ratio != 0 ? (float) Math.sqrt(1.0f / ratio) : 100.0f, i);
        }
        layout.setColumnConstraints(colConstr);
    }

    @Override
    public CC getConstraints(com.haulmont.cuba.gui.components.Component component) {
        CC defaultContraints = MigLayoutHelper.getConstraints(component);

        Component composition = DesktopComponentsHelper.getComposition(component);
        Component layoutChild = composition;

        Container parent = composition.getParent();
        if (parent instanceof LayoutSlot) {
            parent = parent.getParent();
            layoutChild = composition.getParent();
        }

        if (parent == container) {
            // fill up span x span y
            if (layout.getComponentConstraints(layoutChild) instanceof CC) {
                CC componentConstraints = (CC) layout.getComponentConstraints(layoutChild);
                defaultContraints.setCellX(componentConstraints.getCellX());
                defaultContraints.setCellY(componentConstraints.getCellY());
                defaultContraints.setSpanX(componentConstraints.getSpanX());
                defaultContraints.setSpanY(componentConstraints.getSpanY());
            }
        }

        return defaultContraints;
    }

    @Override
    public CC getConstraints(com.haulmont.cuba.gui.components.Component component, int col, int row, int col2, int row2) {
        int spanX = col2 - col + 1;
        int spanY = row2 - row + 1;

        CC constraints = MigLayoutHelper.getConstraints(component);
        constraints.cell(col, row, spanX, spanY);

        return constraints;
    }

    @Override
    public Object getCaptionConstraints(com.haulmont.cuba.gui.components.Component component,
                                        int col, int row, int col2, int row2) {
        CC constraints = new CC();
        constraints.cell(col, row);
        constraints.split(2);
        constraints.flowY();
        constraints.width("min!");
        constraints.height("min!");
        MigLayoutHelper.applyAlignment(constraints, component.getAlignment());
        return constraints;
    }

    @Override
    public void updateConstraints(JComponent component, Object constraints) {
        layout.setComponentConstraints(component, constraints);
    }
}