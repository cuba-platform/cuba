/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jmx;

import com.haulmont.cuba.core.sys.logging.LogControlException;

import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public interface JmxLogControlMBean {

    /**
     * Returns the list of titles of logs
     *
     * @return list of titles of logs
     */
    List<String> getLogFileNames();

    /**
     * Reads a log tail equal 50Kb at line
     *
     * @param fileName name of a readable file
     * @return line of log records
     */
    String getTail(String fileName) throws LogControlException;

    /**
     * Get URL for log file downloading
     *
     * @param fileName name of a log file
     * @return URL
     */
    String getLogFileLink(String fileName) throws LogControlException;

    /**
     * Get size for log file downloading
     *
     * @param fileName name of a log file
     * @return URL
     */
    long getLogFileSize(String fileName) throws LogControlException;

    /**
     * Get current loggers
     *
     * @return current logger names
     */
    List<String> getLoggers();

    /**
     * Reads current level of the logger
     *
     * @param loggerName logger name
     * @return level of the logger
     */
    String getLoggerLevel(String loggerName) throws LogControlException;

    /**
     * Writes down level for the specified logger
     *
     * @param loggerName logger name
     * @param level      level
     */
    void setLoggerLevel(String loggerName, String level) throws LogControlException;

    /**
     * Get current appenders
     *
     * @return current appender names
     */
    List<String> getAppenders();

    /**
     * Reads current threshold of the appender
     *
     * @param appenderName appender name
     * @return threshold of the logger
     */
    String getAppenderThreshold(String appenderName) throws LogControlException;

    /**
     * Writes threshold for the specified logger
     *
     * @param appenderName logger name
     * @param threshold    threshold level
     */
    void setAppenderThreshold(String appenderName, String threshold) throws LogControlException;
}