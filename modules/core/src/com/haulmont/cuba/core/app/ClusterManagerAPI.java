/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.app;

import java.io.Serializable;

/**
 * Interface defining methods for communication in a middleware cluster.
 *
 */
public interface ClusterManagerAPI {

    String NAME = "cuba_ClusterManager";

    /**
     * Send a message to all active cluster nodes.
     * @param message   serializable message
     */
    void send(Serializable message);

    /**
     * Send a message to all active cluster nodes synchronously.
     * @param message   serializable message
     */
    void sendSync(Serializable message);

    /**
     * @return whether the synchronous sending is forced for the current thread
     * @see #setSyncSendingForCurrentThread(boolean)
     */
    boolean getSyncSendingForCurrentThread();

    /**
     * Forces synchronous sending for the current thread. It means that the {@link #send(Serializable)} method will
     * send the message in the current thread and block until returning from the clustering implementation.
     * @param sync true to force synchronous sending
     */
    void setSyncSendingForCurrentThread(boolean sync);

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

    /**
     * Join a cluster.
     */
    void start();

    /**
     * Leave the cluster.
     */
    void stop();

    /**
     * @return  true if clustering is started on this node
     */
    boolean isStarted();

    /**
     * @return threads count that are actively sending cluster messages
     */
    int getActiveThreadsCount();

    /**
     * @return cluster messages count queued to send
     */
    int getMessagesCount();
}
