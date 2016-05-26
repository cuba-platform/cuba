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

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultLong;

/**
 * Middleware cluster configuration settings.
 *
 */
@Source(type = SourceType.APP)
public interface ClusterConfig extends Config {

    /**
     * @return timeout to receive state from cluster when node starts, in milliseconds
     */
    @Property("cuba.cluster.stateTransferTimeout")
    @DefaultLong(10000)
    long getStateReceiveTimeout();

    /**
     * @return Maximum size of thread pool which is used to send messages to the cluster members
     */
    @Property("cuba.cluster.messageSendingThreadPoolSize")
    @DefaultInt(100)
    int getClusterMessageSendingThreadPoolSize();

    /**
     * @return Maximum queue capacity which is used  by executor to store messages to the cluster members. By default is unbounded
     */
    @Property("cuba.cluster.messageSendingQueueCapacity")
    @DefaultInt(Integer.MAX_VALUE)
    int getClusterMessageSendingQueueCapacity();
}
