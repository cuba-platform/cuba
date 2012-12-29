/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.global.ClusterNodeIdentifier;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_JmxNodeIdentifierMBean")
public class JmxNodeIdentifier implements JmxNodeIdentifierMBean {

    @Inject
    private ClusterNodeIdentifier clusterNodeIdentifier;

    @Override
    public String getClusterNodeName() {
        return clusterNodeIdentifier.getClusterNodeName();
    }
}