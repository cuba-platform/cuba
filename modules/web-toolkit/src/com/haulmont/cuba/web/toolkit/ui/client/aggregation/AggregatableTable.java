/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.aggregation;

import com.vaadin.client.ui.VScrollTable;

/**
 * VScrollTable API wrapper for {@link TableAggregationRow}
 *
 * @author artamonov
 * @version $Id$
 */
public interface AggregatableTable {

    VScrollTable.TableHead getHead();

    String getStylePrimaryName();

    String[] getVisibleColOrder();

    String getColKeyByIndex(int index);

    int getColWidth(String colKey);

    void setColWidth(int colIndex, int w, boolean isDefinedWidth);

    boolean isTextSelectionEnabled();
}