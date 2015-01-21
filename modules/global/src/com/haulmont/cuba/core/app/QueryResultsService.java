/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import java.util.List;
import java.util.UUID;

/**
 * Interface provides functionality for working with previously selected query results
 * @author gorbunkov
 * @version $Id$
 */
public interface QueryResultsService {
    String NAME = "cuba_QueryResultsService";

    /**
     * Stores {@code idList} collection as {@link com.haulmont.cuba.core.entity.QueryResult} records
     */
    void insert(int queryKey, List<UUID> idList);

    /**
     * Deletes {@code QueryResult} records with given {@code queryKey}
     */
    void delete(int queryKey);
}
