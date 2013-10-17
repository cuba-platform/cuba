/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.sys.logging.LogControlException;

import java.util.List;

/**
 * Interface to provide JMX bridge to log control functionality for local and remote JMX interfaces
 *
 * @author artamonov
 * @version $Id$
 */
public interface JmxRemoteLoggingAPI {
    String NAME = "cuba_JmxRemoteLogging";

    /**
     * Returns the list of titles of logs
     *
     * @param instance
     * @return list of titles of logs
     */
    List<String> getLogFileNames(JmxInstance instance);

    /**
     * Reads a log tail equal 50Kb at line
     *
     * @param instance
     * @param fileName name of a readable file
     * @return line of log records
     */
    String getTail(JmxInstance instance, String fileName) throws LogControlException;

    /**
     * Get URL for log file downloading
     *
     * @param instance
     * @param fileName name of a log file
     * @return URL
     */
    String getLogFileLink(JmxInstance instance, String fileName) throws LogControlException;

    /**
     * Get current loggers
     *
     * @param instance
     * @return current logger names
     */
    List<String> getLoggers(JmxInstance instance);

    /**
     * Reads current level of the logger
     *
     * @param instance
     * @param loggerName logger name
     * @return level of the logger
     */
    String getLoggerLevel(JmxInstance instance, String loggerName) throws LogControlException;

    /**
     * Writes down level for the specified logger
     *
     * @param instance
     * @param loggerName logger name
     * @param level      level
     */
    void setLoggerLevel(JmxInstance instance, String loggerName, String level) throws LogControlException;

    /**
     * Get current appenders
     *
     * @param instance
     * @return current appender names
     */
    List<String> getAppenders(JmxInstance instance);

    /**
     * Reads current threshold of the appender
     *
     * @param instance
     * @param appenderName appender name
     * @return threshold of the logger
     */
    String getAppenderThreshold(JmxInstance instance, String appenderName) throws LogControlException;

    /**
     * Writes threshold for the specified logger
     *
     * @param instance
     * @param appenderName logger name
     * @param threshold    threshold level
     */
    void setAppenderThreshold(JmxInstance instance, String appenderName, String threshold) throws LogControlException;
}