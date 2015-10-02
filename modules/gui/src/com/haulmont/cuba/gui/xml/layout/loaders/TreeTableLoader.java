/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.TreeTable;

/**
 * @author artamonov
 * @version $Id$
 */
public class TreeTableLoader extends AbstractTableLoader<TreeTable> {
    @Override
    public void createComponent() {
        resultComponent = (TreeTable) factory.createComponent(TreeTable.NAME);
        loadId(resultComponent, element);
        createButtonsPanel(resultComponent, element);
    }
}