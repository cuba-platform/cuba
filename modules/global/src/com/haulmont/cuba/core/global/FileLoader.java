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

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.FileDescriptor;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Interface to store and load files defined by {@link FileDescriptor}s. Can be used on both middle and client tiers.
 */
public interface FileLoader {
    String NAME = "cuba_FileLoader";

    /**
     * Save an InputStream contents into file storage on middleware.
     *
     * @param fd                  file descriptor
     * @param inputStreamSupplier input stream supplier
     *                            May throw {@link RetryUnsupportedException} if supplier cannot provide stream twice.
     *                            May throw {@link InputStreamSupplierException} in case of exception in supplier.
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    void saveStream(FileDescriptor fd, Supplier<InputStream> inputStreamSupplier) throws FileStorageException;

    /**
     * Save an InputStream contents into file storage on middleware.
     *
     * @param fd                  file descriptor
     * @param inputStreamSupplier input stream supplier.
     *                            May throw {@link RetryUnsupportedException} if supplier cannot provide stream twice.
     *                            May throw {@link InputStreamSupplierException} in case of exception in supplier.
     * @param streamingListener   streaming progress listener
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     * @throws InterruptedException     if current thread is interrupted during streaming process
     */
    void saveStream(FileDescriptor fd, Supplier<InputStream> inputStreamSupplier,
                    @Nullable StreamingProgressListener streamingListener) throws FileStorageException, InterruptedException;

    /**
     * Return an input stream to load a file contents from middleware.
     *
     * @param fd file descriptor
     * @return input stream, must be closed after use
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    InputStream openStream(FileDescriptor fd) throws FileStorageException;

    /**
     * Remove a file from the file storage.
     *
     * @param fd file descriptor
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    void removeFile(FileDescriptor fd) throws FileStorageException;

    /**
     * Tests whether the file denoted by this file descriptor exists.
     *
     * @param fd file descriptor
     * @return true if the file denoted by this file descriptor exists
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong@throws FileStorageException
     */
    boolean fileExists(FileDescriptor fd) throws FileStorageException;

    /**
     * Listener that is fired during file streaming to middleware.
     */
    interface StreamingProgressListener {
        void onStreamingProgressChanged(long transferredBytes);
    }

    /**
     * Simple implementation of {@code Supplier<InputStream>} that can provide input stream only once.
     */
    class SingleInputStreamSupplier implements Supplier<InputStream> {
        private final InputStream inputStream;
        private boolean provided = false;

        public SingleInputStreamSupplier(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public InputStream get() {
            if (provided) {
                throw new RetryUnsupportedException("InputStream cannot be provided twice");
            }

            this.provided = true;

            return inputStream;
        }
    }

    /**
     * Exception that can be thrown if implementation of {@code Supplier<InputStream>} cannot provide input stream
     * once again.
     */
    @SupportedByClient
    class RetryUnsupportedException extends RuntimeException {
        public RetryUnsupportedException(String message) {
            super(message);
        }
    }

    /**
     * Runtime exception that can be thrown from {@code Supplier<InputStream>} implementation.
     */
    @SupportedByClient
    class InputStreamSupplierException extends RuntimeException {
        public InputStreamSupplierException() {
        }

        public InputStreamSupplierException(String message) {
            super(message);
        }

        @SuppressWarnings("DangerousUseSupportedByClient")
        public InputStreamSupplierException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}