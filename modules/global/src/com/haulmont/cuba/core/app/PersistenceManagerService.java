/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 01.10.2010 17:23:45
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface PersistenceManagerService {

    String NAME = "cuba_PersistenceManagerService";

    boolean useLazyCollection(String entityName);

    int getFetchUI(String entityName);

    int getMaxFetchUI(String entityName);
}
