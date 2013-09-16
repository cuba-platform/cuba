/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

    /**
     * @return a component that displays data from the same datasource, usually a {@link Table}. Can be null.
     */
    ListComponent getOwner();
    void setOwner(ListComponent owner);
}
