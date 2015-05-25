/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Supports configuration parameters framework functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ConfigStorageAPI {

    String NAME = "cuba_ConfigStorage";

    /**
     * Loads all properties stored in the database.
     *
     * @return the properties map
     */
    Map<String, String> getDbProperties();

    /**
     * Loads a property from the database.
     *
     * @param name property name
     * @return     property value or null if not found
     */
    @Nullable
    String getDbProperty(String name);

    /**
     * Saves a property into the database.
     *
     * <p>If an active transaction exists, it will be used without creating a new one. This allows you to include
     * saving properties into your business logic. If you want to separate the property saving, just start a new
     * transaction prior to calling this method.</p>
     *
     * @param name  property name
     * @param value property value
     */
    void setDbProperty(String name, String value);

    /**
     * Clear properties cache. Invoke this method if you changed the properties directly in the database.
     */
    void clearCache();
}
