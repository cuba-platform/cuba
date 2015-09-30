/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.ServerInfoAPI;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_ServerInfoMBean")
public class ServerInfo implements ServerInfoMBean {

    @Inject
    protected ServerInfoAPI serverInfo;

    @Override
    public String getReleaseNumber() {
        return serverInfo.getReleaseNumber();
    }

    @Override
    public String getReleaseTimestamp() {
        return serverInfo.getReleaseTimestamp();
    }

    @Override
    public String getServerId() {
        return serverInfo.getServerId();
    }
}
