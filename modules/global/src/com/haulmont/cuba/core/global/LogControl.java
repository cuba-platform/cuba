/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import ch.qos.logback.classic.Level;
import com.haulmont.cuba.core.sys.logging.AppenderThresholdNotSupported;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import com.haulmont.cuba.core.sys.logging.LogFileNotFoundException;

import java.io.File;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public interface LogControl {

    String NAME = "cuba_LogControl";

    /**
     * Returns the list of titles of logs
     *
     * @return list of titles of logs
     */
    List<String> getLogFileNames();

    /**
     * Reads a log tail equals 50Kb
     *
     * @param fileName name of a readable file
     * @return line of log records
     */
    String getTail(String fileName) throws LogControlException;

    /**
     * Get file reference to log file
     *
     * @param fileName log file name
     * @return file reference
     * @throws LogFileNotFoundException
     */
    File getLogFile(String fileName) throws LogFileNotFoundException;

    /**
     * Get current loggers
     *
     * @return current logger names
     */
    List<String> getLoggers();

    /**
     * Reads current level of the logger
     *
     * @param loggerName logger
     * @return level of the logger
     */
    Level getLoggerLevel(String loggerName);

    /**
     * Writes down level for the specified logger
     *
     * @param loggerName logger name
     * @param level  level
     */
    void setLoggerLevel(String loggerName, Level level);

    /**
     * Get current appenders
     *
     * @return current appender names
     */
    List<String> getAppenders();

    /**
     * Reads current threshold of the appender
     *
     * @param appenderName appender
     * @return threshold of the logger
     */
    Level getAppenderThreshold(String appenderName) throws AppenderThresholdNotSupported;

    /**
     * Writes threshold for the specified logger
     *
     * @param appenderName  appender
     * @param threshold threshold level
     */
    void setAppenderThreshold(String appenderName, Level threshold) throws AppenderThresholdNotSupported;
}