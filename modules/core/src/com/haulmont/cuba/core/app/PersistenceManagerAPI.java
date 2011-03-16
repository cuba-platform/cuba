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

    /** True if this database table supports soft deletion */
    boolean isSoftDeleteFor(String table);

    /** Whether to use a lazy collection datasource for this entity, based on current statistics */
    boolean useLazyCollection(String entityName);

    /** Whether to use a lookup screen or a dropdown for this entity, based on current statistics */
    boolean useLookupScreen(String entityName);

    /** Limit rows fetched for UI components in 'normal' conditions */
    int getFetchUI(String entityName);

    /** The absolute maximum number of rows that can be fetched for UI components */
    int getMaxFetchUI(String entityName);
}
