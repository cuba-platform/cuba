/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.AbstractSearchFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.presentations.Presentations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.UUID;

/**
 * @author gorbunkov
 * @version $Id$
 */
@ManagedBean(FilterHelper.NAME)
public class DesktopFilterHelper implements FilterHelper {

    private Log log = LogFactory.getLog(DesktopFilterHelper.class);

    @Inject
    protected DesktopFilterDragAndDropSupport dragAndDropSupport;

    @Override
    public void setLookupNullSelectionAllowed(LookupField lookupField, boolean value) {
    }

    @Override
    public void setLookupTextInputAllowed(LookupField lookupField, boolean value) {

    }

    @Override
    public AbstractSearchFolder saveFolder(AbstractSearchFolder folder) {
        return null;
    }

    @Override
    public void openFolderEditWindow(boolean isAppFolder, AbstractSearchFolder folder, Presentations presentations, Runnable commitHandler) {

    }

    @Override
    public boolean isFolderActionsEnabled() {
        return false;
    }

    @Override
    public void initConditionsDragAndDrop(Tree tree, ConditionsTree conditions) {
        dragAndDropSupport.initDragAndDrop(tree, conditions);
    }

    @Override
    public Object getFoldersPane() {
        return null;
    }

    @Override
    public void removeFolderFromFoldersPane(Folder folder) {

    }

    @Override
    public boolean isTableActionsEnabled() {
        return false;
    }

    @Override
    public void initTableFtsTooltips(Table table, Map<UUID, String> tooltips) {
        final JTable dTable = DesktopComponentsHelper.unwrap(table);
        Class<?> columnClass = dTable.getColumnClass(0);
        DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public String getToolTipText(MouseEvent event) {
                int rowIndex = dTable.rowAtPoint(event.getPoint());
//                dTable.getModel().getValueAt();
//                event.get
                return "hello";
            }
        };
        dTable.setDefaultRenderer(columnClass, tableCellRenderer);
    }

    @Override
    public void removeTableFtsTooltips(Table table) {

    }

}
