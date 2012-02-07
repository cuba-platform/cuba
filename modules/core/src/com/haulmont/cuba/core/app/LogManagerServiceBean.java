/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.*;
import java.util.*;
import java.util.zip.CRC32;

/**
 * <p>$Id$</p>
 *
 * @author shatokhin
 */
@Service(LogManagerService.NAME)
public class LogManagerServiceBean implements LogManagerService {

    private Log log = LogFactory.getLog(getClass());

    protected String logDir;

    @Inject
    private void setConfiguration(Configuration configuration) {
        logDir = configuration.getConfig(GlobalConfig.class).getLogDir();
    }

    @Override
    public List<String> getLogFileNames() {
        File pathFileLogs = new File(logDir);
        List<String> listFileNames = new ArrayList<String>();
        if (pathFileLogs.isDirectory()) {
            listFileNames = Arrays.asList(pathFileLogs.list());
        }
        Collections.sort(listFileNames);
        return listFileNames;
    }

    @Override
    public String getTail(String fileName) {
        final long AMOUNT_BYTES = 51200;

        StringBuilder sb = new StringBuilder();
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(logDir + File.separator + fileName, "r");
            long lengthFile = randomAccessFile.length();
            if (lengthFile >= AMOUNT_BYTES) {
                randomAccessFile.seek(lengthFile - AMOUNT_BYTES);
            }

            String str;
            while (randomAccessFile.read() != -1) {
                randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
                str = shieldingTags(randomAccessFile.readLine());
                str = str.replaceFirst(Level.DEBUG.toString(), setStyleText(Level.DEBUG.toString(), "black"));
                str = str.replaceFirst(Level.INFO.toString(), setStyleText(Level.INFO.toString(), "blue"));
                str = str.replaceFirst(Level.ERROR.toString(), setStyleText(Level.ERROR.toString(), "red"));
                str = str.replaceFirst(Level.WARN.toString(), setStyleText(Level.WARN.toString(), "orange"));
                str = str.replaceFirst(Level.TRACE.toString(), setStyleText(Level.TRACE.toString(), "silver"));
                str = str.replaceFirst(Level.FATAL.toString(), setStyleText(Level.FATAL.toString(), "red"));
                sb.append(str).append("<br>");
            }
        } catch (IOException e) {
            log.error("Error reading log file", e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return sb.toString();
    }

    @Override
    public Level getLogLevel(String loggerName) {
        Logger logger = Logger.getLogger(loggerName);
        return logger.getLevel();
    }

    @Override
    public void setLogLevel(String loggerName, Level level) {
        Logger logger = Logger.getLogger(loggerName);
        logger.setLevel(level);
        logger.setAdditivity(true);
        log.info("Logger " + loggerName + " set to " + level);
    }

    @Override
    public String packLog(String fileName) {
        final String ENCODING = "CP866";
        final String FORMAT = ".zip";

        String pathTempDir = ConfigProvider.getConfig(GlobalConfig.class).getTempDir()
                + File.separator + UUID.randomUUID().toString() + FORMAT;
        String pathFileLogs = logDir + File.separator + fileName;

        File file = new File(pathFileLogs);
        if (!file.exists()) {
            return null;
        }

        ZipArchiveOutputStream zipOutputStream = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pathTempDir);
            zipOutputStream = new ZipArchiveOutputStream(fileOutputStream);
            zipOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
            zipOutputStream.setEncoding(ENCODING);

            InputStream in = new FileInputStream(pathFileLogs);
            byte[] bytes = IOUtils.toByteArray(in);

            ArchiveEntry archiveEntry = newEntry(fileName, bytes);
            zipOutputStream.putArchiveEntry(archiveEntry);
            zipOutputStream.write(bytes);
            log.info("Created temporary file " + fileName);
        } catch (IOException e) {
            log.error("Pack error", e);
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.closeArchiveEntry();
                    zipOutputStream.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return pathTempDir;
    }

    @Override
    public void deleteTempFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            log.info("Temporary file " + filePath + " removed successfully");
        }
    }

    private static ArchiveEntry newEntry(String name, byte[] data) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(data.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }

    private String setStyleText(String text, String color) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b><font color='").append(color).append("'>");
        sb.append(text).append("</font></b>");
        return sb.toString();
    }

    private String shieldingTags(String text) {
        text = text.replaceAll("<", "&lt");
        text = text.replaceAll(">", "&gt");
        return text;
    }

}
