/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.upload;

import org.apache.http.entity.FileEntity;

import java.io.*;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author artamonov
 * @version $Id$
 */
public class FileStorageProgressEntity extends FileEntity {

    private final long size;
    private final UUID fileId;
    private final FileUploadingAPI.UploadToStorageProgressListener listener;

    public FileStorageProgressEntity(File file, String contentType, UUID fileId,
                                     FileUploadingAPI.UploadToStorageProgressListener listener) {
        super(file, contentType);

        this.listener = listener;
        this.size = file.length();
        this.fileId = fileId;

        checkNotNull(listener);
        checkNotNull(fileId);
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        long transferredBytes = 0L;

        if (outstream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }

        try (InputStream instream = new FileInputStream(this.file)) {
            byte[] tmp = new byte[4096];
            int readedBytes;
            while ((readedBytes = instream.read(tmp)) != -1) {
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedIOException();

                outstream.write(tmp, 0, readedBytes);

                transferredBytes += readedBytes;
                listener.progressChanged(fileId, transferredBytes, size);
            }
            outstream.flush();
        }
    }
}