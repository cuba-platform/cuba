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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.io.IOUtils;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.security.app.UserSessionsMBean;
import com.haulmont.cuba.deploymarker.DeployMarkerMBean;

import java.io.InputStream;
import java.io.IOException;

/**
 * CubaDeployer MBean implementation.
 * <p>
 * Intended to support other MBeans dependencies because it starts after all other platform MBeans.
 * Also holds some information about the system.
 */
public class CubaDeployer implements CubaDeployerMBean
{
    private Log log = LogFactory.getLog(CubaDeployer.class);

    private String releaseNumber = "?";
    private String releaseTimestamp = "?";

    public void start() {
        log.debug("start");

        ServerConfig config = ConfigProvider.getConfig(ServerConfig.class);
        UserSessionsMBean mBean = Locator.lookupMBean(UserSessionsMBean.class, UserSessionsMBean.OBJECT_NAME);
        mBean.setExpirationTimeoutSec(config.getUserSessionExpirationTimeoutSec());

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

        ScriptingProvider.addGroovyClassPath(config.getServerConfDir() + "/cuba");
    }

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public String getReleaseTimestamp() {
        return releaseTimestamp;
    }
}
