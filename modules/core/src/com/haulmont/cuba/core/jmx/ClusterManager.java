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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("cuba_ClusterManagerMBean")
public class ClusterManager implements ClusterManagerMBean {

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected ClusterManagerAPI clusterManager;

    @Override
    public String start() {
        try {
            clusterManager.start();
            return "Done";
        } catch (Throwable e) {
            log.error("Unable to start the cluster", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String stop() {
        try {
            clusterManager.stop();
            return "Done";
        } catch (Exception e) {
            log.error("Unable to stop the cluster", e);
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public boolean isStarted() {
        return clusterManager.isStarted();
    }



    @Override
    public boolean isMaster() {
        return clusterManager.isMaster();
    }

    @Override
    public String getCurrentView() {
        return clusterManager.getCurrentView();
    }

    @Override
    public int getActiveThreadsCount() {
        return clusterManager.getActiveThreadsCount();
    }

    @Override
    public int getMessagesCount() {
        return clusterManager.getMessagesCount();
    }

    @Override
    public String printSharedStateStat() {
        return clusterManager.printSharedStateStat();
    }

    @Override
    public String printMessagesStat() {
        return clusterManager.printMessagesStat();
    }

    @Override
    public long getSentMessages(String className) {
        return className == null ? -1 : clusterManager.getSentMessages(className);
    }

    @Override
    public long getSentBytes(String className) {
        return className == null ? -1 : clusterManager.getSentBytes(className);
    }

    @Override
    public long getReceivedMessages(String className) {
        return className == null ? -1 : clusterManager.getReceivedMessages(className);
    }

    @Override
    public long getReceivedBytes(String className) {
        return className == null ? -1 : clusterManager.getReceivedBytes(className);
    }
}
