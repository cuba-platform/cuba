/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report.formatters.oo;

/**
 * @author artamonov
 * @version $Id$
 */
public interface OOOConnectorMBean {

    String getAvailablePorts();

    void hardReloadAccessPorts();
}