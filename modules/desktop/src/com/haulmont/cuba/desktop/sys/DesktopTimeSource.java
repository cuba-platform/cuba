/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.Connection;
import com.haulmont.cuba.desktop.ConnectionListener;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.security.global.LoginException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.Date;

/**
 * Desktop client implementation of {@link TimeSource} interface.
 * <p>Can adjust returned time according to the middleware time.</p>
 *
 * @author krivopustov
 * @version $Id$
 */
public class DesktopTimeSource implements TimeSource, ConnectionListener {

    protected Log log = LogFactory.getLog(getClass());

    protected boolean useServerTime;

    protected volatile long timeOffset;

    @Inject
    protected ServerInfoService serverInfo;

    public DesktopTimeSource() {
        App app = App.getInstance();
        if (app != null) // can be null in tests
            app.getConnection().addListener(this);
    }

    @Inject
    public void setConfiguration(Configuration configuration) {
        useServerTime = configuration.getConfig(DesktopConfig.class).isUseServerTime();
    }


    @Override
    public void connectionStateChanged(Connection connection) throws LoginException {
        if (connection.isConnected() && useServerTime) {
            long serverTime = serverInfo.getTimeMillis();
            timeOffset = serverTime - System.currentTimeMillis();
            log.info("Using server time, offset=" + timeOffset + "ms");
        }
    }

    @Override
    public Date currentTimestamp() {
        return new Date(currentTimeMillis());
    }

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis() + timeOffset;
    }
}
