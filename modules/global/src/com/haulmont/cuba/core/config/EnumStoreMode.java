/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config;

/**
 * Identifies the way to store enum class values in the config storage.
 *
 * @author kozlov
 * @version $Id$
 */
public enum EnumStoreMode {

    /**
     * Store enum IDs.
     * Requires public static {@code fromId} class and work only with primitive ids
     * for which stringify and type factory instances can be inferred from class definitions.
     */
    ID,

    /**
     * Store enum names.
     */
    NAME
}