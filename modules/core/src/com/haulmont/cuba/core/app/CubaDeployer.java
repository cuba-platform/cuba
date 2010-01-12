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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.io.IOException;
import java.io.InputStream;

/**
 * CubaDeployer MBean implementation.
 * <p>
 * Intended to support other MBeans dependencies because it starts after all other platform MBeans.
 * Also holds some information about the system.
 */
@ManagedBean(CubaDeployerMBean.NAME)
public class CubaDeployer implements CubaDeployerMBean
{
    private Log log = LogFactory.getLog(CubaDeployer.class);

    private String releaseNumber = "?";
    private String releaseTimestamp = "?";

    public CubaDeployer() {
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
}
