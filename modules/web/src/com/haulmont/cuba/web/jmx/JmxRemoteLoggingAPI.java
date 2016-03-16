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

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.core.entity.JmxInstance;
import com.haulmont.cuba.core.sys.logging.LogControlException;

import java.util.List;
import java.util.Map;

/**
 * Interface to provide JMX bridge to log control functionality for local and remote JMX interfaces
 *
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
     * Get URL for log file downloading.
     *
     * @param instance jmx connection
     * @param remoteContext remote web context
     * @param fileName name of a log file
     * @return URL
     */
    String getLogFileLink(JmxInstance instance, String remoteContext, String fileName) throws LogControlException;

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

    /**
     * @param instance jmx connection
     * @return host info
     */
    LoggingHostInfo getHostInfo(JmxInstance instance);

    /**
     * @param instance jmx connection
     * @return list of available web contexts on this instance
     */
    List<String> getAvailableContexts(JmxInstance instance);

    class LoggingHostInfo {
        private List<String> loggerNames;
        private List<String> appenders;
        private List<String> logFileNames;

        public LoggingHostInfo(List<String> loggerNames, List<String> appenders, List<String> logFileNames) {
            this.loggerNames = loggerNames;
            this.appenders = appenders;
            this.logFileNames = logFileNames;
        }

        public List<String> getLoggerNames() {
            return loggerNames;
        }

        public List<String> getAppenders() {
            return appenders;
        }

        public List<String> getLogFileNames() {
            return logFileNames;
        }
    }
}