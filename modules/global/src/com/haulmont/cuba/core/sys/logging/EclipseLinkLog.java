/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EclipseLinkLog extends AbstractSessionLog {

    protected Map<String, Log> logsCache = new ConcurrentHashMap<>();

    private Log getLog(String category) {
        String logName = "eclipselink." + category;
        Log log = logsCache.get(logName);
        if (log == null) {
            log = LogFactory.getLog(logName);
            logsCache.put(logName, log);
        }
        return log;
    }

    @Override
    public void log(SessionLogEntry sessionLogEntry) {
        Log log = getLog(sessionLogEntry.getNameSpace());
        if (log.isDebugEnabled()) {
            log.debug(sessionLogEntry.getMessage());
        }
    }

    @Override
    public boolean shouldLog(int level, String category) {
        Log log = getLog(category);
        return log.isDebugEnabled();
    }
}
