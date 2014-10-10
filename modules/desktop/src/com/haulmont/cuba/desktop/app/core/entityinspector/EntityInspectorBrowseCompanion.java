/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.app.core.entityinspector;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.app.core.entityinspector.EntityInspectorBrowse;
import com.haulmont.cuba.gui.components.Table;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;

/**
 * @author zlatoverov
 * @version $Id$
 */
public class EntityInspectorBrowseCompanion implements EntityInspectorBrowse.Companion {
    @Override
    public void setHorizontalScrollEnabled(Table table, boolean enabled) {
        JXTable jxTable = DesktopComponentsHelper.unwrap(table);
        jxTable.setHorizontalScrollEnabled(enabled);
    }
}
