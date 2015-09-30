/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.NodeIdentifier;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;

import org.springframework.stereotype.Component;

/**
 * @author artamonov
 * @version $Id$
 */
@Component(NodeIdentifier.NAME)
public class NodeIdentifierImpl implements NodeIdentifier {
    @Override
    public String getNodeName() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
        return globalConfig.getWebHostName() + ":" + globalConfig.getWebPort();
    }
}