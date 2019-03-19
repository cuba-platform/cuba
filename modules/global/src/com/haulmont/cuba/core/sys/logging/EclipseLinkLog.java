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

package com.haulmont.cuba.core.sys.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.ImmutableMap;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EclipseLinkLog extends AbstractSessionLog {

    protected Map<String, Logger> logsCache = new ConcurrentHashMap<>();

    protected static final Map<Integer, Level> levels = new ImmutableMap.Builder<Integer, Level>()
            .put(SessionLog.OFF, Level.OFF)
            .put(SessionLog.SEVERE, Level.ERROR)
            .put(SessionLog.WARNING, Level.WARN)
            .put(SessionLog.INFO, Level.INFO)
            .put(SessionLog.CONFIG, Level.INFO)
            .put(SessionLog.FINE, Level.DEBUG)
            .put(SessionLog.FINER, Level.DEBUG)
            .put(SessionLog.FINEST, Level.TRACE)
            .put(SessionLog.ALL, Level.ALL)
            .build();

    private Logger getLog(String category) {
        String logName = "eclipselink." + category;
        Logger log = logsCache.get(logName);
        if (log == null) {
            log = (Logger) LoggerFactory.getLogger(logName);
            logsCache.put(logName, log);
        }
        return log;
    }

    @Override
    public void log(SessionLogEntry sessionLogEntry) {
        Level logbackLevel = levels.get(sessionLogEntry.getLevel());
        if (logbackLevel == null)
            return;
        Logger log = getLog(sessionLogEntry.getNameSpace());
        if (log.isEnabledFor(logbackLevel)) {
            String msg = sessionLogEntry.getMessage() +
                    (sessionLogEntry.getParameters() == null ? "" : " " + Arrays.toString(sessionLogEntry.getParameters()));
            switch (logbackLevel.toString()) {
                case "ERROR":
                    log.error(msg);
                    break;
                case "WARN":
                    log.warn(msg);
                    break;
                case "INFO":
                    log.info(msg);
                    break;
                case "DEBUG":
                    log.debug(msg);
                    break;
                case "TRACE":
                    log.trace(msg);
                    break;
            }
        }
    }

    @Override
    public boolean shouldLog(int level, String category) {
        if (category == null)
            return false;
        Level logbackLevel = levels.get(level);
        if (logbackLevel == null)
            return false;
        Logger log = getLog(category);
        return log.isEnabledFor(logbackLevel);
    }
}