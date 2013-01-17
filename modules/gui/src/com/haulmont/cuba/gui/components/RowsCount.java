/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;

/**
 * Component that makes a {@link CollectionDatasource} to load data by pages. Usually used inside {@link Table}.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface RowsCount extends Component.BelongToFrame, Component.HasXmlDescriptor {

    enum State {
        FIRST_COMPLETE,     // "63 rows"
        FIRST_INCOMPLETE,   // "1-100 rows of [?] >"
        MIDDLE,             // "< 101-200 rows of [?] >"
        LAST                // "< 201-252 rows"
    }

    String NAME = "rowsCount";

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);
}
