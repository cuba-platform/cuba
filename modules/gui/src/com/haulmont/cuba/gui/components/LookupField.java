/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.01.2009 17:31:02
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;

public interface LookupField extends OptionsField {
    String getNullName();
    void setNullName(String nullName);

    void setFilterMode(FilterMode mode);
    FilterMode getFilterMode();

    enum FilterMode {
            NO,
            STARTS_WITH,
            CONTAINS

    }
}
