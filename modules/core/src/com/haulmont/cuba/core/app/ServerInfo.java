/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 17:43:12
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

/**
 * ServerInfo MBean implementation.
 *
 * <p>Holds the server parameters.</p>
 */
@ManagedBean(ServerInfoAPI.NAME)
public class ServerInfo implements ServerInfoAPI, ServerInfoMBean
{
    private Log log = LogFactory.getLog(ServerInfo.class);

    private String releaseNumber = "?";
    private String releaseTimestamp = "?";

    @Inject
    private Configuration configuration;

    private volatile String serverId;

    public ServerInfo() {
        InputStream stream = getClass().getResourceAsStream("/com/haulmont/cuba/core/global/release.number");
        if (stream != null)
            try {
                releaseNumber = IOUtils.toString(stream);
            } catch (IOException e) {
                log.warn("Unable to read release number", e);
            }

        stream = getClass().getResourceAsStream("/com/haulmont/cuba/core/global/release.timestamp");
        if (stream != null)
            try {
                releaseTimestamp = IOUtils.toString(stream);
            } catch (IOException e) {
                log.warn("Unable to read release timestamp", e);
            }
    }

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public String getReleaseTimestamp() {
        return releaseTimestamp;
    }

    @Override
    public String getServerId() {
        if (serverId == null) {
            GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);
            serverId = globalConfig.getWebHostName() + ":" + globalConfig.getWebPort() + "/" + globalConfig.getWebContextName();
        }
        return serverId;
    }
}
