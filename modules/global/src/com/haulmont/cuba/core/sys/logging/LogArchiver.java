/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.logging;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.zip.CRC32;

/**
 * @author artamonov
 * @version $Id$
 */
public class LogArchiver {

    private static final Log log = LogFactory.getLog(LogArchiver.class);

    private static final long LOG_TAIL_FOR_PACKING_SIZE = 20 * 1024 * 1024; // 20 MB

    public static void writeArchivedLogToStream(File logFile, OutputStream outputStream) throws IOException{
        final String ENCODING = "CP866";

        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(outputStream);
        zipOutputStream.setMethod(ZipArchiveOutputStream.DEFLATED);
        zipOutputStream.setEncoding(ENCODING);

        byte[] content = getTailBytes(logFile);

        ArchiveEntry archiveEntry = newArchive(logFile.getName(), content);
        zipOutputStream.putArchiveEntry(archiveEntry);
        zipOutputStream.write(content);

        zipOutputStream.closeArchiveEntry();
        zipOutputStream.close();
    }

    private static byte[] getTailBytes(File logFile) throws FileNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (!logFile.exists())
            throw new FileNotFoundException();

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
            while ((len = randomAccessFile.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        } catch (IOException e) {
            log.error("Pack error", e);
        } finally {
            IOUtils.closeQuietly(bos);
        }
        return buf;
    }

    private static ArchiveEntry newArchive(String name, byte[] data) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(name);
        zipEntry.setSize(data.length);
        zipEntry.setCompressedSize(zipEntry.getSize());
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        zipEntry.setCrc(crc32.getValue());
        return zipEntry;
    }
}