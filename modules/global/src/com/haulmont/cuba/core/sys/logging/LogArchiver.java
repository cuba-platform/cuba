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

package com.haulmont.cuba.core.sys.logging;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.CRC32;

/**
 */
public class LogArchiver {

    private static final Logger log = LoggerFactory.getLogger(LogArchiver.class);

    private static final String ZIP_ENCODING = "CP866";
    public static final long LOG_TAIL_FOR_PACKING_SIZE = 20 * 1024 * 1024; // 20 MB

    public static void writeArchivedLogTailToStream(File logFile, OutputStream outputStream) throws IOException {
        if (!logFile.exists()) {
            throw new FileNotFoundException();
        }

        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream);
        zipOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
        zipOutputStream.setEncoding(ZIP_ENCODING);

        byte[] content = getTailBytes(logFile);

        ArchiveEntry archiveEntry = newTailArchive(logFile.getName(), content);
        zipOutputStream.putArchiveEntry(archiveEntry);
        zipOutputStream.write(content);

        zipOutputStream.closeArchiveEntry();
        zipOutputStream.close();
    }

    public static void writeArchivedLogToStream(File logFile, OutputStream outputStream) throws IOException {
        if (!logFile.exists()) {
            throw new FileNotFoundException();
        }

        File tempFile = File.createTempFile(FilenameUtils.getBaseName(logFile.getName()) + "_log_", ".zip");

        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(tempFile);
        zipOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
        zipOutputStream.setEncoding(ZIP_ENCODING);

        ArchiveEntry archiveEntry = newArchive(logFile);
        zipOutputStream.putArchiveEntry(archiveEntry);

        FileInputStream logFileInput = new FileInputStream(logFile);
        IOUtils.copyLarge(logFileInput, zipOutputStream);

        logFileInput.close();

        zipOutputStream.closeArchiveEntry();
        zipOutputStream.close();

        FileInputStream tempFileInput = new FileInputStream(tempFile);
        IOUtils.copyLarge(tempFileInput, outputStream);

        tempFileInput.close();

        FileUtils.deleteQuietly(tempFile);
    }

    private static byte[] getTailBytes(File logFile) throws FileNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buf = null;
        int len;
        int size = 1024;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(logFile, "r");
            long lengthFile = randomAccessFile.length();
            if (lengthFile >= LOG_TAIL_FOR_PACKING_SIZE) {
                randomAccessFile.seek(lengthFile - LOG_TAIL_FOR_PACKING_SIZE);
            }
            buf = new byte[size];
            while ((len = randomAccessFile.read(buf, 0, size)) != -1) {
                bos.write(buf, 0, len);
            }
            buf = bos.toByteArray();
        } catch (IOException e) {
            log.error("Unable to get tail for log file " + logFile.getName(), e);
        } finally {
            IOUtils.closeQuietly(bos);
        }
        return buf;
    }

    private static ArchiveEntry newTailArchive(String name, byte[] tail) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(tail.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(tail);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }

    private static ArchiveEntry newArchive(File file) throws IOException {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(file.getName());
        zipEntry.setSize(file.length());
        zipEntry.setCompressedSize(zipEntry.getSize());
        zipEntry.setCrc(FileUtils.checksumCRC32(file));
        return zipEntry;
    }
}