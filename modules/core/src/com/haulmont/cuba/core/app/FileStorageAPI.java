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
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.InputStream;

/**
 * Interface to store and load files defined by {@link FileDescriptor}s.
 *
 */
public interface FileStorageAPI {

    String NAME = "cuba_FileStorage";

    /**
     * Save an InputStream contents into file storage.
     * @param fileDescr             file descriptor
     * @param inputStream           input stream, must be closed in the calling code
     * @return                      number of bytes saved
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    long saveStream(FileDescriptor fileDescr, InputStream inputStream) throws FileStorageException;

    /**
     * Save a byte array into file storage.
     * @param fileDescr             file descriptor
     * @param data                  byte array
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException;

    /**
     * Remove a file from the file storage.
     * @param fileDescr             file descriptor
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    void removeFile(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Return an input stream to load a file contents.
     * @param fileDescr             file descriptor
     * @return                      input stream, must be closed after use
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    InputStream openStream(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Load a file contents into byte array.
     * @param fileDescr             file descriptor
     * @return                      file contents
     * @throws IllegalArgumentException if arguments are incorrect
     * @throws FileStorageException     if something goes wrong
     */
    byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Tests whether the file denoted by this file descriptor exists.
     * @param fileDescr file descriptor
     * @return           true if the file denoted by this file descriptor exists
     * @throws IllegalArgumentException if arguments are incorrect
     */
    boolean fileExists(FileDescriptor fileDescr);
}
