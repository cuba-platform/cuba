/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.client.sys.fileupload;

import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

public class InputStreamProgressEntity extends AbstractHttpEntity {

    private final UploadProgressListener listener;
    private final InputStream content;

    public InputStreamProgressEntity(InputStream content,
                                     ContentType contentType,
                                     @Nullable UploadProgressListener listener) {
        this.listener = listener;
        this.content = content;

        setContentType(contentType.toString());
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isStreaming() {
        return true;
    }

    @Override
    public long getContentLength() {
        return -1;
    }

    @Override
    public InputStream getContent() throws IOException {
        return this.content;
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        if (outStream == null) {
            throw new IllegalArgumentException("Output stream may not be null");
        }

        long transferredBytes = 0;

        final byte[] buffer = new byte[OUTPUT_BUFFER_SIZE];
        int readBytes;

        while ((readBytes = this.content.read(buffer)) != -1) {
            // check interrupted only if listener is set
            if (listener != null && Thread.currentThread().isInterrupted()) {
                throw new InterruptedIOException();
            }

            outStream.write(buffer, 0, readBytes);

            transferredBytes += readBytes;

            if (listener != null) {
                listener.onUploadProgressChanged(transferredBytes);
            }
        }
    }

    public interface UploadProgressListener {
        void onUploadProgressChanged(long transferredBytes);
    }
}