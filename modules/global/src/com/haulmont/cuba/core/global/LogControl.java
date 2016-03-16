/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.global;

import ch.qos.logback.classic.Level;
import com.haulmont.cuba.core.sys.logging.AppenderThresholdNotSupported;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import com.haulmont.cuba.core.sys.logging.LogFileNotFoundException;

import java.io.File;
import java.util.List;

/**
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