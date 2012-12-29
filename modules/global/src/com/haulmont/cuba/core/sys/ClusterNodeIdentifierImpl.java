/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ClusterNodeIdentifier;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;

import javax.annotation.ManagedBean;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ClusterNodeIdentifier.NAME)
public class ClusterNodeIdentifierImpl implements ClusterNodeIdentifier {
    @Override
    public String getClusterNodeName() {
        GlobalConfig globalConfig = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);
        return globalConfig.getWebHostName() + ":" + globalConfig.getWebPort();
    }
}
