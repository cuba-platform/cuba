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

/**
 * Interface to be implemented by middleware cluster listeners. A cluster listener receives messages from other nodes
 * in the cluster.
 * @param <T>   type of message this listener receives
 *
 */
public interface ClusterListener<T> {

    /**
     * Receive a message from other cluster nodes.
     * @param message   message instance
     */
    void receive(T message);

    /**
     * Get state of this cluster node to send it to other nodes.
     * "State" here means data that must be shared between cluster nodes.
     *
     * <p>This method is invoked by clustering implementation when a new node joins the cluster and wants to receive the
     * state from this active node.</p>
     *
     * @return  byte array containing the state
     */
    byte[] getState();

    /**
     * Set state of this cluster node receiving it from other active node.
     * "State" here means data that must be shared between cluster nodes.
     *
     * <p>This method is invoked by clustering implementation once when this node joins the cluster.
     *
     * @param state byte array containing the state
     */
    void setState(byte[] state);
}
