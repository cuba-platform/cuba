/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
