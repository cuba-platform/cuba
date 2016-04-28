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

package com.haulmont.cuba.core.sys;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.LogControl;
import com.haulmont.cuba.core.sys.logging.AppenderThresholdNotSupported;
import com.haulmont.cuba.core.sys.logging.LogControlException;
import com.haulmont.cuba.core.sys.logging.LogFileNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 */
@Component(LogControl.NAME)
public class LogControlImpl implements LogControl {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(LogControl.class);

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
                Arrays.sort(files, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
                for (File f : files) {
                    if (f.isFile())
                        filenames.add(f.getName());
                }
            }
        }
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
                    str = new String(line.getBytes(), StandardCharsets.UTF_8.name());
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
    public List<String> getLoggers() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<String> list = new ArrayList<>();
        for (Logger log : lc.getLoggerList()) {
            if (log.getLevel() != null || hasAppenders(log)) {
                list.add(log.getName());
            }
        }
        return list;
    }

    private boolean hasAppenders(Logger logger) {
        return logger.iteratorForAppenders().hasNext();
    }

    @Override
    public Level getLoggerLevel(String loggerName) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLogger(loggerName).getLevel();
    }

    @Override
    public void setLoggerLevel(String loggerName, Level level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(loggerName);
        logger.setLevel(level);

        log.info(String.format("Level for logger '%s' set to '%s'", loggerName, level));
    }

    @Override
    public List<String> getAppenders() {
        return new ArrayList<>(getAllAppenders().keySet());
    }

    protected Map<String, Appender> getAllAppenders() {
        Map<String, Appender> map = new HashMap<>();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (Logger logger : context.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
                Appender<ILoggingEvent> appender = index.next();
                map.put(appender.getName(), appender);
            }
        }
        return map;
    }

    @Override
    public Level getAppenderThreshold(String appenderName) throws AppenderThresholdNotSupported {
        for (Map.Entry<String, Appender> entry : getAllAppenders().entrySet()) {
            if (entry.getKey().equals(appenderName)) {
                Appender appender = entry.getValue();
                for (Object filter : appender.getCopyOfAttachedFiltersList()) {
                    if (filter instanceof ThresholdFilter) {
                        try {
                            Field field = filter.getClass().getDeclaredField("level");
                            field.setAccessible(true);
                            return (Level) field.get(filter);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            log.error("Error getting appender " + appenderName + " level", e);
                            throw new AppenderThresholdNotSupported(appenderName);
                        }
                    }
                }
            }
        }
        throw new AppenderThresholdNotSupported(appenderName);
    }

    @Override
    public void setAppenderThreshold(String appenderName, Level threshold) throws AppenderThresholdNotSupported {
        for (Map.Entry<String, Appender> entry : getAllAppenders().entrySet()) {
            if (entry.getKey().equals(appenderName)) {
                Appender appender = entry.getValue();
                for (Object filter : appender.getCopyOfAttachedFiltersList()) {
                    if (filter instanceof ThresholdFilter) {
                        ((ThresholdFilter) filter).setLevel(threshold.levelStr);
                        return;
                    }
                }
            }
        }
        throw new AppenderThresholdNotSupported(appenderName);
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

        return new String(input.toByteArray(), StandardCharsets.UTF_8.name());
    }
}