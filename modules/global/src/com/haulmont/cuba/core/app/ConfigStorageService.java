/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConfigStorageService {

    String NAME = "cuba_ConfigStorageService";

    String getConfigProperty(String name);

    void setConfigProperty(String name, String value);
}
