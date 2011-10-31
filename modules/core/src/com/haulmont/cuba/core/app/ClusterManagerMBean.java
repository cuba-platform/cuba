/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.06.2010 19:00:16
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * JMX interface to control middleware clustering functionality.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ClusterManagerMBean {

    /**
     * Join a cluster.
     */
    void start();

    /**
     * Leave the cluster.
     */
    void stop();

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
