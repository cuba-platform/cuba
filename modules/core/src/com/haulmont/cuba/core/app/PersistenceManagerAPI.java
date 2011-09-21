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
 * API to {@link PersistenceManager}.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface PersistenceManagerAPI
{
    String NAME = "cuba_PersistenceManager";

    /**
     * Whether this database table supports soft deletion.
     * @param table table name
     * @return      true if this database table supports soft deletion
     */
    boolean isSoftDeleteFor(String table);

    /**
     * Whether to use a lazy collection datasource for this entity, based on current statistics.
     * @param entityName    entity name
     * @return              true if lazy collection datasource should be used for this entity
     */
    boolean useLazyCollection(String entityName);

    /**
     * Whether to use a lookup screen or a dropdown for this entity, based on current statistics.
     * @param entityName    entity name
     * @return              true if lookup screen should be used
     */
    boolean useLookupScreen(String entityName);

    /**
     * Return a limit of rows fetched for UI components in 'normal' conditions
     * @param entityName    entity name
     * @return              maximum number of rows
     */
    int getFetchUI(String entityName);

    /**
     * Return the absolute maximum number of rows that can be fetched for UI components
     * @param entityName    entity name
     * @return              maximum number of rows
     */
    int getMaxFetchUI(String entityName);
}
