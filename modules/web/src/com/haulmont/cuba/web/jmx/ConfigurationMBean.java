/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.jmx;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ConfigurationMBean {

    String printAppProperties();

    String printAppProperties(String prefix);

    String getAppProperty(String name);

    String setAppProperty(String name, String value);

}
