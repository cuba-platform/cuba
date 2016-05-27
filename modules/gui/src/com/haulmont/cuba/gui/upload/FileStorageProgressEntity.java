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

package com.haulmont.cuba.gui.upload;

import org.apache.http.entity.FileEntity;

import java.io.*;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

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