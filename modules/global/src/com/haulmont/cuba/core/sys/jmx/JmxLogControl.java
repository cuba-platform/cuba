/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jmx;

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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

        GlobalConfig globalConfig = AppBeans.get(Configuration.class).getConfig(GlobalConfig.class);

        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return globalConfig.getDispatcherBaseUrl() + "/log/" + encodedFileName;
    }

    @Override
    public List<String> getLoggers() {
        final List<Logger> loggers = logControl.getLoggers();
        List<String> loggerNames = new LinkedList<>();
        for (Logger logger : loggers)
            loggerNames.add(logger.getName());
        return loggerNames;
    }

    @Override
    public String getLoggerLevel(String loggerName) throws LogControlException {
        Logger logger = getLogger(loggerName);
        if (logger == null)
            throw new LoggerNotFoundException(loggerName);

        Level loggerLevel = logControl.getLoggerLevel(logger);
        if (loggerLevel == null)
            return null;

        return loggerLevel.toString();
    }

    @Override
    public void setLoggerLevel(String loggerName, String level) throws LogControlException {
        Logger logger = getLogger(loggerName);
        if (logger == null)
            logger = Logger.getLogger(loggerName);

        Level logLevel = LoggingHelper.getLevelFromString(level);
        if (logLevel == null)
            throw new UnrecognizedLogLevelException(level);

        logControl.setLoggerLevel(logger, logLevel);
    }

    @Override
    public List<String> getAppenders() {
        final List<Appender> appenders = logControl.getAppenders();
        List<String> appenderNames = new LinkedList<>();
        for (Appender appender : appenders)
            appenderNames.add(appender.getName());
        return appenderNames;
    }

    @Override
    public String getAppenderThreshold(String appenderName) throws LogControlException {
        Appender appender = getAppender(appenderName);
        if (appender == null)
            throw new AppenderNotFoundException(appenderName);

        Level theshold = (Level) logControl.getAppenderThreshold(appender);
        if (theshold == null)
            return null;

        return theshold.toString();
    }

    @Override
    public void setAppenderThreshold(String appenderName, String threshold) throws LogControlException {
        Appender appender = getAppender(appenderName);
        if (appender == null)
            throw new AppenderNotFoundException(appenderName);

        Level appenderThreshold = LoggingHelper.getLevelFromString(threshold);
        if (appenderThreshold == null)
            throw new UnrecognizedLogThresholdException(threshold);

        logControl.setAppenderThreshold(appender, appenderThreshold);
    }

    protected Logger getLogger(String loggerName) {
        List<Logger> loggers = logControl.getLoggers();
        Logger logger = null;
        Iterator<Logger> loggerIterator = loggers.iterator();
        while (loggerIterator.hasNext() && logger == null) {
            Logger nextLogger = loggerIterator.next();
            if (StringUtils.equals(nextLogger.getName(), loggerName))
                logger = nextLogger;
        }
        return Logger.getLogger(loggerName);
    }

    protected Appender getAppender(String appenderName) {
        List<Appender> appenders = logControl.getAppenders();
        Appender appender = null;
        Iterator<Appender> appenderIterator = appenders.iterator();
        while (appenderIterator.hasNext() && appender == null) {
            Appender nextAppender = appenderIterator.next();
            if (StringUtils.equals(nextAppender.getName(), appenderName))
                appender = nextAppender;
        }
        return appender;
    }
}