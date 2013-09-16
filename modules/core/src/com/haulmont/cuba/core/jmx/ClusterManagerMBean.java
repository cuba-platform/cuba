/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.ClusterManagerAPI}.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Controls Middleware cluster membership")
public interface ClusterManagerMBean {

    @ManagedOperation(description = "Join a cluster")
    String start();

    @ManagedOperation(description = "Leave the cluster")
    String stop();

    /**
     * @return  true if clustering started on this node
     */
    boolean isStarted();

    /**
     * @return  true if the current node is the master
     * @see     com.haulmont.cuba.core.app.ClusterManagerAPI#isMaster()
     */
    boolean isMaster();

    /**
     * @return  string representation of a set of active nodes
     * @see     com.haulmont.cuba.core.app.ClusterManagerAPI#getCurrentView()
     */
    String getCurrentView();
}
