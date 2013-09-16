/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface AnyTableModelAdapter extends TableModel {

    void sort(List<? extends RowSorter.SortKey> sortKeys);

    Entity getItem(int rowIndex);

    int getRowIndex(Entity entity);

    void addGeneratedColumn(Table.Column column);

    void removeGeneratedColumn(Table.Column column);

    boolean isGeneratedColumn(Table.Column column);

    void addColumn(Table.Column column);

    void removeColumn(Table.Column column);

    Table.Column getColumn(int index);

    void addChangeListener(DataChangeListener changeListener);

    void removeChangeListener(DataChangeListener changeListener);

    interface DataChangeListener {
        void beforeChange();

        void afterChange();

        void dataSorted();
    }
}
