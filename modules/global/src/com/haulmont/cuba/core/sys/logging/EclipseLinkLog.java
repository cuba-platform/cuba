/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EclipseLinkLog extends AbstractSessionLog {

    protected Map<String, Logger> logsCache = new ConcurrentHashMap<>();

    private Logger getLog(String category) {
        String logName = "eclipselink." + category;
        Logger log = logsCache.get(logName);
        if (log == null) {
            log = LoggerFactory.getLogger(logName);
            logsCache.put(logName, log);
        }
        return log;
    }

    @Override
    public void log(SessionLogEntry sessionLogEntry) {
        Logger log = getLog(sessionLogEntry.getNameSpace());
        if (log.isDebugEnabled()) {
            log.debug(sessionLogEntry.getMessage() +
                    (sessionLogEntry.getParameters() == null ? "" :
                            " " + Arrays.toString(sessionLogEntry.getParameters())));
        }
    }

    @Override
    public boolean shouldLog(int level, String category) {
        Logger log = getLog(category);
        return log.isDebugEnabled();
    }
}
