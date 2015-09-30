/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(ServerInfoAPI.NAME)
public class ServerInfo implements ServerInfoAPI {

    public static final String CUBA_RELEASE_NUMBER_PATH = "/com/haulmont/cuba/core/global/release.number";
    public static final String CUBA_RELEASE_TIMESTAMP_PATH = "/com/haulmont/cuba/core/global/release.timestamp";

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected String releaseNumber = "?";
    protected String releaseTimestamp = "?";

    protected Configuration configuration;

    protected volatile String serverId;

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;

        InputStream stream = getClass().getResourceAsStream(CUBA_RELEASE_NUMBER_PATH);
        if (stream != null) {
            try {
                releaseNumber = IOUtils.toString(stream);
            } catch (IOException e) {
                log.warn("Unable to read release number", e);
            }
        }

        stream = getClass().getResourceAsStream(CUBA_RELEASE_TIMESTAMP_PATH);
        if (stream != null) {
            try {
                releaseTimestamp = IOUtils.toString(stream);
            } catch (IOException e) {
                log.warn("Unable to read release timestamp", e);
            }
        }
    }

    @Override
    public String getReleaseNumber() {
        return releaseNumber;
    }

    @Override
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