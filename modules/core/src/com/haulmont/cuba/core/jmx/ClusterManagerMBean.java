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
package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.ClusterManagerAPI;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.ClusterManagerAPI}.
 *
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

    /**
     * @return threads count that are actively sending cluster messages
     * @see com.haulmont.cuba.core.app.ClusterManagerAPI#getActiveThreadsCount()
     */
    int getActiveThreadsCount();

    /**
     * @return message count queued to send
     * @see com.haulmont.cuba.core.app.ClusterManagerAPI#getMessagesCount()
     */
    int getMessagesCount();
}
