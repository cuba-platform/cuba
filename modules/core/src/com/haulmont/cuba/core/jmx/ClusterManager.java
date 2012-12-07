/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.ClusterManagerAPI;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_ClusterManagerMBean")
public class ClusterManager implements ClusterManagerMBean {

    protected Log log = LogFactory.getLog(getClass());

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
