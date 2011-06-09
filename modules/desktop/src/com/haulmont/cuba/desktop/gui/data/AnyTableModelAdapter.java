/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.core.entity.Entity;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface AnyTableModelAdapter extends TableModel {

    void sort(List<? extends RowSorter.SortKey> sortKeys);

    Entity getItem(int rowIndex);
}
