/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopRowsCount extends DesktopAbstractComponent<JPanel> implements RowsCount {

    private CollectionDatasource datasource;

    public DesktopRowsCount() {
        impl = new JPanel(new java.awt.FlowLayout());
        impl.setBorder(BorderFactory.createLineBorder(java.awt.Color.gray));
        impl.add(new JLabel("TODO: rowsCount"));
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource) {
        this.datasource = datasource;
    }
}
