/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

/**
 * {@link ConfigStorage} JMX interface.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConfigStorageMBean {

    String printDbProperties();

    String printDbProperties(String prefix);

    String getDbPropertyJmx(String name);

    String setDbPropertyJmx(String name, String value);

    String removeDbPropertyJmx(String name);

    String printAppProperties();

    String printAppProperties(String prefix);

    String getAppProperty(String name);

    String setAppProperty(String name, String value);

    void clearCache();
}
