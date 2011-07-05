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

    // for mindless add(component), add(component), add(component)
    // calling with automatic extending, like it can be done in vaadin
    protected int cursorX, cursorY;

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

        lc.setInsets(MigLayoutHelper.makeInsets(margins));

        if (!spacing) {
            lc.gridGap("0", "0");
        }

        if (isDebug())
            lc.debug(1000);

        layout.setLayoutConstraints(lc);
    }

    @Override
    public void add(Component component, int col, int row, int col2, int row2) {
        int spanX = col2 - col;
        int spanY = row2 - row;
        CC cc = new CC().cell(col, row, spanX, spanY);
        container.add(component, cc);

        cursorX = col2 + 1;
        cursorY = row2;
        if (cursorX >= colCount) {
            cursorX = 0;          // new row
            cursorY++;
        }
    }

    @Override
    public void add(Component component) {
        int col = cursorX++;
        int row = cursorY;
        if (cursorX >= colCount) {
            cursorX = 0;          // new row
            cursorY++;
        }

        CC cc = new CC().cell(col, row, 1, 1);
        container.add(component, cc);
    }
}
