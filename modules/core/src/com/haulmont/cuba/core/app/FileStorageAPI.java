/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

/**
 * Interface to store and load files defined by {@link FileDescriptor}s.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface FileStorageAPI {

    String NAME = "cuba_FileStorage";

    /**
     * Save an InputStream contents into file storage.
     * @param fileDescr             file descriptor
     * @param inputStream           input stream
     * @throws FileStorageException if something goes wrong
     */
    void saveStream(FileDescriptor fileDescr, InputStream inputStream) throws FileStorageException;

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
     * @return                      input stream
     * @throws FileStorageException if something goes wrong
     */
    InputStream openFileInputStream(FileDescriptor fileDescr) throws FileStorageException;

    /**
     * Load a file contents into byte array.
     * @param fileDescr             file descriptor
     * @return                      file contents
     * @throws FileStorageException if something goes wrong
     */
    byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException;

    File[] getStorageRoots();

    File getStorageDir(File rootDir, Date createDate);
}
