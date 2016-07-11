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

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class FileStorageProgressEntity extends FileEntity {

    private final long size;
    private final UUID fileId;
    private final FileUploading.UploadToStorageProgressListener listener;

    public FileStorageProgressEntity(File file, String contentType, UUID fileId,
                                     FileUploading.UploadToStorageProgressListener listener) {
        super(file, contentType);

        checkNotNullArgument(listener);
        checkNotNullArgument(fileId);

        this.listener = listener;
        this.size = file.length();
        this.fileId = fileId;
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        long transferredBytes = 0L;

        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }

        try (InputStream inStream = new FileInputStream(this.file)) {
            byte[] tmp = new byte[4096];
            int readedBytes;
            while ((readedBytes = inStream.read(tmp)) != -1) {
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedIOException();

                outStream.write(tmp, 0, readedBytes);

                transferredBytes += readedBytes;
                try {
                    listener.progressChanged(fileId, transferredBytes, size);
                } catch (InterruptedException e) {
                    throw new InterruptedIOException();
                }
            }
            outStream.flush();
        }
    }
}