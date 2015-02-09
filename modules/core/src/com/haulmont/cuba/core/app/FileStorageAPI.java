/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.File;
import java.io.InputStream;

/**
 * Interface to store and load files defined by {@link FileDescriptor}s.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface FileStorageAPI {

    String NAME = "cuba_FileStorage";

    /**
     * Save an InputStream contents into file storage.
     * @param fileDescr             file descriptor
     * @param inputStream           input stream, must be closed in the calling code
     * @return                      number of bytes saved
     * @throws FileStorageException if something goes wrong
     */
    long saveStream(FileDescriptor fileDescr, InputStream inputStream) throws FileStorageException;

    /**
     * Save a byte array into file storage.
     * @param fileDescr             file descriptor
     * @param data                  byte array
     * @throws FileStorageException if something goes wrong
     */
    void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException;

    /**
     * Relocate an existing file into storage.
     * @param fileDescr             file descriptor
     * @param file                  existing file
     * @throws FileStorageException if something goes wrong
     */
    void putFile(FileDescriptor fileDescr, File file) throws FileStorageException;

    /**
     * Remove a file from the file storage.
     * @param fileDescr             file descriptor
     * @throws FileStorageException if something goes wrong
     */
    void removeFile(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Return an input stream to load a file contents.
     * @param fileDescr             file descriptor
     * @return                      input stream, must be closed after use
     * @throws FileStorageException if something goes wrong
     */
    InputStream openStream(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Load a file contents into byte array.
     * @param fileDescr             file descriptor
     * @return                      file contents
     * @throws FileStorageException if something goes wrong
     */
    byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Tests whether the file denoted by this file descriptor exists.
     * @param fileDescr file descriptor
     * @return           true if the file denoted by this file descriptor exists
     */
    boolean fileExists(FileDescriptor fileDescr);
}
