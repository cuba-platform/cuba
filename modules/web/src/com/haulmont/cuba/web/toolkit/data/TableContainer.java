/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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