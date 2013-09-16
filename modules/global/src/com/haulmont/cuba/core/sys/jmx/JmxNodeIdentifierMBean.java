/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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