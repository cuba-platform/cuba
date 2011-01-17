/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 22.11.2010 16:37:30
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.security.entity.TabHistory;

public interface TabHistoryService {
    String NAME = "cuba_TabHistoryService";

    @Deprecated
    String JNDI_NAME = NAME;

    int MAX_RECORDS = 100;
    int MAX_BUFFER = 150;

    int getCurrentUserTabHistoryCount();
    void deleteEndTabHistory();
    void saveTabHistoryEntity(TabHistory tabHistory);
}
