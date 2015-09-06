/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EclipseLinkLog extends AbstractSessionLog {

    protected Map<String, Logger> logsCache = new ConcurrentHashMap<>();

    protected static Map<Integer, Level> levels = new HashMap<>();

    static {
        levels.put(SessionLog.OFF, Level.OFF);
        levels.put(SessionLog.SEVERE, Level.ERROR);
        levels.put(SessionLog.WARNING, Level.WARN);
        levels.put(SessionLog.INFO, Level.INFO);
        levels.put(SessionLog.CONFIG, Level.INFO);
        levels.put(SessionLog.FINE, Level.DEBUG);
        levels.put(SessionLog.FINER, Level.DEBUG);
        levels.put(SessionLog.FINEST, Level.TRACE);
        levels.put(SessionLog.ALL, Level.ALL);
    }

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
        Level logbackLevel = levels.get(level);
        if (logbackLevel == null)
            return false;
        Logger log = getLog(category);
        return log.isEnabledFor(logbackLevel);
    }
}
