/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.data;

import com.vaadin.data.Container;

/**
 * @author artamonov
 * @version $Id$
 */
public interface TableContainer extends Container.Sortable {

    void resetSortOrder();
}