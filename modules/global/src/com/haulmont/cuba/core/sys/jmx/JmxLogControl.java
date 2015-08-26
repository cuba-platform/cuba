/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jmx;

import ch.qos.logback.classic.Level;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.LogControl;
import com.haulmont.cuba.core.sys.jmx.exception.AppenderNotFoundException;
import com.haulmont.cuba.core.sys.jmx.exception.LoggerNotFoundException;
import com.haulmont.cuba.core.sys.jmx.exception.UnrecognizedLogLevelException;
import com.haulmont.cuba.core.sys.jmx.exception.UnrecognizedLogThresholdException;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import com.haulmont.cuba.core.sys.logging.LoggingHelper;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean("cuba_JmxLogControlMBean")
public class JmxLogControl implements JmxLogControlMBean {

    @Inject
    protected LogControl logControl;

    @Override
    public List<String> getLogFileNames() {
        return logControl.getLogFileNames();
    }

    @Override
    public String getTail(String fileName) throws LogControlException {
        return logControl.getTail(fileName);
    }

    @Override
    public String getLogFileLink(String fileName) throws LogControlException {
        // check log file exists
        logControl.getLogFile(fileName);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        GlobalConfig globalConfig = configuration.getConfig(GlobalConfig.class);

        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return globalConfig.getDispatcherBaseUrl() + "/log/" + encodedFileName;
    }

    @Override
    public long getLogFileSize(String fileName) throws LogControlException {
        // check log file exists
        File logFile = logControl.getLogFile(fileName);

        return logFile.length();
    }

    @Override
    public List<String> getLoggerNames() {
        return logControl.getLoggers();
    }

    @Override
    public Map<String, String> getLoggersLevels() {
        Map<String, String> result = new HashMap<>();
        for (String logName : logControl.getLoggers()) {
            Level level = logControl.getLoggerLevel(logName);
            result.put(logName, (level == null ? null : level.toString()));
        }
        return result;
    }

    @Override
    public String getLoggerLevel(String loggerName) throws LogControlException {
        List<String> loggers = logControl.getLoggers();
        if (!loggers.contains(loggerName))
            throw new LoggerNotFoundException(loggerName);

        Level loggerLevel = logControl.getLoggerLevel(loggerName);
        if (loggerLevel == null)
            return null;

        return loggerLevel.toString();
    }

    @Override
    public void setLoggerLevel(String loggerName, String level) throws LogControlException {
        List<String> loggers = logControl.getLoggers();
        if (!loggers.contains(loggerName))
            throw new LoggerNotFoundException(loggerName);

        Level logLevel = LoggingHelper.getLevelFromString(level);
        if (logLevel == null)
            throw new UnrecognizedLogLevelException(level);

        logControl.setLoggerLevel(loggerName, logLevel);
    }

    @Override
    public void setLoggersLevels(Map<String, String> updates) throws LogControlException {
        for (Map.Entry<String, String> logger : updates.entrySet()) {
            setLoggerLevel(logger.getKey(), logger.getValue());
        }
    }

    @Override
    public List<String> getAppenders() {
        return logControl.getAppenders();
    }

    @Override
    public String getAppenderThreshold(String appenderName) throws LogControlException {
        List<String> appenders = logControl.getAppenders();
        if (!appenders.contains(appenderName))
            throw new AppenderNotFoundException(appenderName);

        Level theshold = logControl.getAppenderThreshold(appenderName);
        if (theshold == null)
            return null;

        return theshold.toString();
    }

    @Override
    public void setAppenderThreshold(String appenderName, String threshold) throws LogControlException {
        List<String> appenders = logControl.getAppenders();
        if (!appenders.contains(appenderName))
            throw new AppenderNotFoundException(appenderName);

        Level appenderThreshold = LoggingHelper.getLevelFromString(threshold);
        if (appenderThreshold == null)
            throw new UnrecognizedLogThresholdException(threshold);

        logControl.setAppenderThreshold(appenderName, appenderThreshold);
    }
}