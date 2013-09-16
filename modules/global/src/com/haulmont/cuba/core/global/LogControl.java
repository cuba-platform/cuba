/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.logging.AppenderThresholdNotSupported;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import com.haulmont.cuba.core.sys.logging.LogFileNotFoundException;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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
    List<Logger> getLoggers();

    /**
     * Reads current level of the logger
     *
     * @param logger logger
     * @return level of the logger
     */
    Level getLoggerLevel(Logger logger);

    /**
     * Writes down level for the specified logger
     *
     * @param logger logger name
     * @param level  level
     */
    void setLoggerLevel(Logger logger, Level level);

    /**
     * Get current appenders
     *
     * @return current appender names
     */
    List<Appender> getAppenders();

    /**
     * Reads current threshold of the appender
     *
     * @param appender appender
     * @return threshold of the logger
     */
    Priority getAppenderThreshold(Appender appender) throws AppenderThresholdNotSupported;

    /**
     * Writes threshold for the specified logger
     *
     * @param appender  appender
     * @param threshold threshold level
     */
    void setAppenderThreshold(Appender appender, Level threshold) throws AppenderThresholdNotSupported;
}