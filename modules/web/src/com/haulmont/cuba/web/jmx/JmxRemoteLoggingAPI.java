/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.sys.logging.LogControlException;

import java.util.List;
import java.util.Map;

/**
 * Interface to provide JMX bridge to log control functionality for local and remote JMX interfaces
 *
 * @author artamonov
 * @version $Id$
 */
public interface JmxRemoteLoggingAPI {
    String NAME = "cuba_JmxRemoteLogging";

    /**
     * Returns the list of titles of logs.
     *
     * @param instance jmx connection
     * @return list of titles of logs
     */
    List<String> getLogFileNames(JmxInstance instance);

    /**
     * Reads a log tail equal 50Kb at line.
     *
     * @param instance jmx connection
     * @param fileName name of a readable file
     * @return line of log records
     */
    String getTail(JmxInstance instance, String fileName) throws LogControlException;

    /**
     * Get URL for log file downloading.
     *
     * @param instance jmx connection
     * @param fileName name of a log file
     * @return URL
     */
    String getLogFileLink(JmxInstance instance, String fileName) throws LogControlException;

    /**
     * Get size for log file downloading.
     *
     * @param instance jmx connection
     * @param fileName name of a log file
     * @return URL
     */
    long getLogFileSize(JmxInstance instance, String fileName) throws LogControlException;

    /**
     * Get current loggers names.
     *
     * @param instance jmx connection
     * @return current logger names
     */
    List<String> getLoggerNames(JmxInstance instance);

    /**
     * Reads current level of the logger.
     *
     * @param instance   jmx connection
     * @param loggerName logger name
     * @return level of the logger
     */
    String getLoggerLevel(JmxInstance instance, String loggerName) throws LogControlException;

    /**
     * @param instance jmx connection
     * @return current loggers names and levels
     */
    Map<String, String> getLoggersLevels(JmxInstance instance);

    /**
     * Writes down level for the specified logger.
     *
     * @param instance   jmx connection
     * @param loggerName logger name
     * @param level      level
     */
    void setLoggerLevel(JmxInstance instance, String loggerName, String level) throws LogControlException;

    void setLoggersLevels(JmxInstance instance, Map<String, String> updates) throws LogControlException;

    /**
     * Get current appenders.
     *
     * @param instance jmx connection
     * @return current appender names
     */
    List<String> getAppenders(JmxInstance instance);

    /**
     * Reads current threshold of the appender.
     *
     * @param instance     jmx connection
     * @param appenderName appender name
     * @return threshold of the logger
     */
    String getAppenderThreshold(JmxInstance instance, String appenderName) throws LogControlException;

    /**
     * Writes threshold for the specified logger.
     *
     * @param instance     jmx connection
     * @param appenderName logger name
     * @param threshold    threshold level
     */
    void setAppenderThreshold(JmxInstance instance, String appenderName, String threshold) throws LogControlException;
}