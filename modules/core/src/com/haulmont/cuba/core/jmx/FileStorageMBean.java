/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.jmx;

import java.io.File;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.FileStorageAPI}.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface FileStorageMBean {

    /**
     * @return  the root directories of all registered storages
     */
    File[] getStorageRoots();

    /**
     * @return the list of file descriptors in the database which have no corresponding files in the storage
     */
    String findOrphanDescriptors();

    /**
     * @return the list of files in the storage which have no corresponding descriptors in the database
     */
    String findOrphanFiles();
}
