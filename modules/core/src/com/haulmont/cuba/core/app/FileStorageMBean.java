/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import java.io.File;

/**
 * JMX interface to the file storage.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface FileStorageMBean {

    /**
     * @return  the root directories of all registered storages
     */
    File[] getStorageRoots();

    /**
     * @return the list of file descriptors in the database which have no corresponding files in the storage
     */
    String findInvalidDescriptors();

    /**
     * @return the list of files in the storage which have no corresponding descriptors in the database
     */
    String findInvalidFiles();
}
