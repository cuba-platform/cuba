/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.LogControl;
import com.haulmont.cuba.core.sys.logging.AppenderThresholdNotSupported;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import com.haulmont.cuba.core.sys.logging.LogFileNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.*;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(LogControl.NAME)
public class LogControlImpl implements LogControl {

    private static final Log log = LogFactory.getLog(LogControl.class);

    private static final long LOG_TAIL_AMOUNT_BYTES = 51200;

    protected File logDir;

    @Inject
    private void setConfiguration(Configuration configuration) {
        logDir = new File(configuration.getConfig(GlobalConfig.class).getLogDir());
    }

    @Override
    public List<String> getLogFileNames() {
        List<String> filenames = new ArrayList<>();
        if (logDir.isDirectory()) {
            File[] files = logDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile())
                        filenames.add(f.getName());
                }
            }
        }
        Collections.sort(filenames);
        return filenames;
    }

    @Override
    public String getTail(String fileName) throws LogControlException {
        // security check, supported only valid file names
        fileName = FilenameUtils.getName(fileName);

        StringBuilder sb = new StringBuilder();
        RandomAccessFile randomAccessFile = null;
        try {
            File logFile = new File(logDir, fileName);
            if (!logFile.exists())
                throw new LogFileNotFoundException(fileName);

            randomAccessFile = new RandomAccessFile(logFile, "r");
            long lengthFile = randomAccessFile.length();
            if (lengthFile >= LOG_TAIL_AMOUNT_BYTES) {
                randomAccessFile.seek(lengthFile - LOG_TAIL_AMOUNT_BYTES);
                skipFirstLine(randomAccessFile);
            }
            String str;
            while (randomAccessFile.read() != -1) {
                randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
                String line = readUtf8Line(randomAccessFile);
                if (line != null) {
                    str = new String(line.getBytes(), "UTF-8");
                    sb.append(str).append("\n");
                }
            }
        } catch (IOException e) {
            log.error("Error reading log file", e);
            throw new LogControlException("Error reading log file: " + fileName);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException ignored) {
                }
            }
        }
        return sb.toString();
    }

    @Override
    public File getLogFile(String fileName) throws LogFileNotFoundException {
        File logFile = new File(logDir, FilenameUtils.getName(fileName));
        if (!logFile.exists())
            throw new LogFileNotFoundException(fileName);

        return logFile;
    }

    @Override
    public List<Logger> getLoggers() {
        Enumeration currentLoggers = LogManager.getCurrentLoggers();
        List<Logger> loggers = new ArrayList<>();
        while (currentLoggers.hasMoreElements()) {
            Logger logger = (Logger) currentLoggers.nextElement();
            if (logger.getLevel() != null)
                loggers.add(logger);
        }
        return loggers;
    }

    @Override
    public Level getLoggerLevel(Logger logger) {
        return logger.getLevel();
    }

    @Override
    public void setLoggerLevel(Logger logger, Level level) {
        logger.setLevel(level);
        logger.setAdditivity(true);

        log.info(String.format("Level for logger '%s' set to '%s'", logger.getName(), level));
    }

    @Override
    public List<Appender> getAppenders() {
        return Collections.list(LogManager.getRootLogger().getAllAppenders());
    }

    @Override
    public Level getAppenderThreshold(Appender appender) throws AppenderThresholdNotSupported {
        if (appender instanceof AppenderSkeleton) {
            Priority threshold = ((AppenderSkeleton) appender).getThreshold();
            return (Level) threshold;
        } else
            throw new AppenderThresholdNotSupported(appender.getName());
    }

    @Override
    public void setAppenderThreshold(Appender appender, Level threshold) throws AppenderThresholdNotSupported {
        if (appender instanceof AppenderSkeleton) {
            ((AppenderSkeleton) appender).setThreshold(threshold);

            log.info(String.format("Threshold for appender '%s' set to '%s'", appender.getName(), threshold));
        } else
            throw new AppenderThresholdNotSupported(appender.getName());
    }

    protected void skipFirstLine(RandomAccessFile logFile) throws IOException {
        boolean eol = false;
        while (!eol) {
            switch (logFile.read()) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    long cur = logFile.getFilePointer();
                    if ((logFile.read()) != '\n') {
                        logFile.seek(cur);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    protected String readUtf8Line(RandomAccessFile logFile) throws IOException {
        int c = -1;
        boolean eol = false;
        ByteArrayOutputStream input = new ByteArrayOutputStream();

        while (!eol) {
            switch (c = logFile.read()) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    long cur = logFile.getFilePointer();
                    if ((logFile.read()) != '\n') {
                        logFile.seek(cur);
                    }
                    break;
                default:
                    input.write((byte)c);
                    break;
            }
        }
        if ((c == -1) && (input.size() == 0)) {
            return null;
        }

        return new String(input.toByteArray(), "UTF-8");
    }
}