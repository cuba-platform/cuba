/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.06.2010 19:00:43
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import java.io.Serializable;

/**
 * Interface defining methods of communication in the middleware cluster.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ClusterManagerAPI {

    String NAME = "cuba_ClusterManager";

    /**
     * Send a message to all active cluster nodes.
     * @param message   serializable message
     */
    void send(Serializable message);

    /**
     * Subscribe to messages from other cluster nodes.
     * @param messageClass  the class of messages we want to be notified
     * @param listener      listener instance
     */
    void addListener(Class messageClass, ClusterListener listener);

    /**
     * Unsubscribe from messages from other cluster nodes.
     * @param messageClass  the class of messages we don't want to be notified anymore
     * @param listener      listener instance
     */
    void removeListener(Class messageClass, ClusterListener listener);

    /**
     * Inform whether the current node is currently the master node in the cluster. A middleware cluster always
     * elects one of its members as master, ususally it is the oldest one.
     * @return true if the current node is the master
     */
    boolean isMaster();

    /**
     * Return a string representation of a set of active nodes in the cluster. This string depends on clustering
     * implementation and should not be parsed or otherwise analyzed in the application.
     * @return  string representation of a set of active nodes
     */
    String getCurrentView();
}
