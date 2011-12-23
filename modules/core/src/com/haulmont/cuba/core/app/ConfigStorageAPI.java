/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import java.util.Map;

/**
 * Supports configuration parameters framework functionality.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConfigStorageAPI {

    String NAME = "cuba_ConfigStorage";

    Map<String, String> getDbProperties();

    String getDbProperty(String name);

    void setDbProperty(String name, String value);

    void clearCache();
}
