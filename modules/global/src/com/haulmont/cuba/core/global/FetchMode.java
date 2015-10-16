/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

/**
 * Specifies how to fetch a referenced entity from the database.
 *
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public enum FetchMode {
    /**
     * The platform will choose an optimal mode
     */
    AUTO,

    /**
     * Fetching will be performed according to JPA rules, which effectively means loading by a separate select
     */
    UNDEFINED,

    /**
     * Fetching in the same select by joining with referenced table
     */
    JOIN,

    /**
     * Fetching by one separate select for all referenced entities
     */
    BATCH
}
