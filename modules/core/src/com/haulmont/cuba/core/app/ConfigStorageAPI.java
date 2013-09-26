/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import java.util.Map;

/**
 * Supports configuration parameters framework functionality.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface ConfigStorageAPI {

    String NAME = "cuba_ConfigStorage";

    Map<String, String> getDbProperties();

    String getDbProperty(String name);

    void setDbProperty(String name, String value);

    void clearCache();
}
