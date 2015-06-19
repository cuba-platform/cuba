/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.table;

/**
 * @author artamonov
 * @version $Id$
 */
public interface TableCellClickListener {
    void onClick(String columnKey, int rowKey);
}