/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 14:43:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * API of {@link PersistenceManager} MBean.<br>
 */
public interface PersistenceManagerAPI
{
    String NAME = "cuba_PersistenceManager";

    boolean isSoftDeleteFor(String table);

    boolean useLazyCollection(String entityName);

    int getMaxFetchUI(String entityName);
}
