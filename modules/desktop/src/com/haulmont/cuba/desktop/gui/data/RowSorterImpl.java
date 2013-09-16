/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.data;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public class RowSorterImpl extends RowSorter<AnyTableModelAdapter> {

    private AnyTableModelAdapter model;
    private SortKey sortKey;

    public RowSorterImpl(AnyTableModelAdapter model) {
        this.model = model;
    }

    @Override
    public AnyTableModelAdapter getModel() {
        return model;
    }

    @Override
    public void toggleSortOrder(int column) {
        if (model.isGeneratedColumn(model.getColumn(column)))
            return;

        SortKey key;
        if (sortKey != null && sortKey.getColumn() == column) {
            if (sortKey.getSortOrder() == SortOrder.ASCENDING) {
                key = new SortKey(sortKey.getColumn(), SortOrder.DESCENDING);
            } else {
                key = null;
            }
        } else {
            key = new SortKey(column, SortOrder.ASCENDING);
        }
        if (key == null)
            setSortKeys(Collections.<SortKey>emptyList());
        else
            setSortKeys(Collections.singletonList(key));
    }

    @Override
    public int convertRowIndexToModel(int index) {
        return index;
    }

    @Override
    public int convertRowIndexToView(int index) {
        return index;
    }

    @Override
    public void setSortKeys(List<? extends SortKey> keys) {
        if (keys != null && keys.size() > 0) {
            int max = model.getColumnCount();
            for (SortKey key : keys) {
                if (key == null || key.getColumn() < 0 || key.getColumn() >= max) {
                    throw new IllegalArgumentException("Invalid SortKey");
                }
            }
            this.sortKey = keys.get(0);
        } else {
            this.sortKey = null;
        }
        if (sortKey != null)
            model.sort(Collections.singletonList(sortKey));
    }

    @Override
    public List<? extends SortKey> getSortKeys() {
        if (sortKey == null)
            return Collections.emptyList();
        else
            return Collections.singletonList(sortKey);
    }

    @Override
    public int getViewRowCount() {
        return model.getRowCount();
    }

    @Override
    public int getModelRowCount() {
        return model.getRowCount();
    }

    @Override
    public void modelStructureChanged() {
    }

    @Override
    public void allRowsChanged() {
    }

    @Override
    public void rowsInserted(int firstRow, int endRow) {
    }

    @Override
    public void rowsDeleted(int firstRow, int endRow) {
    }

    @Override
    public void rowsUpdated(int firstRow, int endRow) {
    }

    @Override
    public void rowsUpdated(int firstRow, int endRow, int column) {
    }
}
