/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import org.apache.log4j.Level;

import java.util.List;

/**
 * This service is intended for operation with server log
 * <p>$Id$</p>
 *
 * @author shatokhin
 */
public interface LogManagerService {

    String NAME = "cuba_LogManagerService";

    /**
     * Returns the list of titles of logs
     * @return list of titles of logs
     */
    List<String> getLogFileNames();

    /**
     * Reads a log tail equal 50Kb at line
     * @param fileName name of a readable file
     * @return line of log records
     */
    String getTail(String fileName);

    /**
     * Reads current level of the logger
     * @param loggerName logger name
     * @return level of the logger
     */
    Level getLogLevel(String loggerName);

    /**
     * Writes down level for the specified logger
     * @param loggerName logger name
     * @param level level
     */
    void setLogLevel(String loggerName, Level level);

    /**
     * File archiving
     * @param fileName way to a file
     * @return way to archive
     */
    String packLog(String fileName);

    /**
     * deletes a file
     * @param filePath way to a file
     */
    void deleteTempFile(String filePath);
}
