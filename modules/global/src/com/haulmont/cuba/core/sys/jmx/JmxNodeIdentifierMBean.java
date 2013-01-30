/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.jmx;

/**
 * MBean that is used by JMX contol facility to identify a JMX node.
 * If this MBean is not present in a JVM, the node is considered as unknown
 *
 * @author artamonov
 * @version $Id$
 */
public interface JmxNodeIdentifierMBean {

    String getNodeName();
}