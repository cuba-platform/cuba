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

/**
 */
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
}
