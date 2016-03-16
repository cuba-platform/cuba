/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.sys;

import com.haulmont.cuba.core.app.ServerInfoService;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.DesktopConfig;
import com.haulmont.cuba.gui.executors.BackgroundTask;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import com.haulmont.cuba.gui.executors.TaskLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.TimeZone;

/**
 * Maintains desktop client time zone and time.
 * <p/>
 * Time zone is taken from server.
 * Time is synchronized after each login. Additionally it can be performed by scheduled task (using spring scheduler).
 * <p/>
 * All requests to server are performed in background thread to avoid delays in UI.
 *
 */
@Component(ClientTimeSynchronizer.NAME)
public class ClientTimeSynchronizer {
    public static final String NAME = "cuba_ClientTimeSynchronizer";

    protected static final int TIMEOUT_SEC = 60;

    private Logger log = LoggerFactory.getLogger(ClientTimeSynchronizer.class);

    @Inject
    protected ServerInfoService serverInfoService;
    @Inject
    protected Configuration configuration;
    @Inject
    protected BackgroundWorker backgroundWorker;
    @Inject
    protected DesktopTimeSource timeSource;

    /**
     * @see com.haulmont.cuba.desktop.DesktopConfig#isUseServerTimeZone()
     */
    public void syncTimeZone() {
        boolean useServerTimeZone = configuration.getConfig(DesktopConfig.class).isUseServerTimeZone();
        if (useServerTimeZone) {
            backgroundWorker.handle(new ObtainServerTimeZoneTask()).execute();
        }
    }

    /**
     * @see com.haulmont.cuba.desktop.DesktopConfig#isUseServerTime()
     */
    public void syncTime() {
        if (!AppContext.isStarted()) {
            return;
        }
        boolean useServerTime = configuration.getConfig(DesktopConfig.class).isUseServerTime();
        boolean connected = App.getInstance() != null && App.getInstance().getConnection() != null
                && App.getInstance().getConnection().isConnected();

        if (useServerTime && connected) {
            backgroundWorker.handle(new UpdateTimeOffsetTask()).execute();
        }
    }

    protected class ObtainServerTimeZoneTask extends BackgroundTask<Void, Void> {
        protected ObtainServerTimeZoneTask() {
            super(TIMEOUT_SEC);
        }

        @Override
        public Void run(TaskLifeCycle<Void> taskLifeCycle) throws Exception {
            TimeZone serverTimeZone = serverInfoService.getTimeZone();

            TimeZone.setDefault(serverTimeZone); // works OK from any thread
            log.info("Time zone set to " + serverTimeZone);
            return null;
        }
    }

    protected class UpdateTimeOffsetTask extends BackgroundTask<Void, Void> {
        public UpdateTimeOffsetTask() {
            super(TIMEOUT_SEC);
        }

        @Override
        public Void run(TaskLifeCycle<Void> taskLifeCycle) throws Exception {
            long serverTime = serverInfoService.getTimeMillis();
            long timeOffset = serverTime - System.currentTimeMillis();

            timeSource.setTimeOffset(timeOffset); // works OK from any thread
            log.info("Using server time, offset=" + timeOffset + "ms");
            return null;
        }
    }
}
