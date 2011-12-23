/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConfigStorageService {

    String NAME = "cuba_ConfigStorageService";

    Map<String, String> getDbProperties();

    String getDbProperty(String name);

    void setDbProperty(String name, String value);
}
