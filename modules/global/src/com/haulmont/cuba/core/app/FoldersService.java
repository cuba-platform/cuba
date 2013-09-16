/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.security.entity.SearchFolder;

import java.io.IOException;
import java.util.List;

/**
 * Folders pane support service.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface FoldersService {

    String NAME = "cuba_FoldersService";

    /**
     * Load application folders available to a user.
     *
     * @return folders list
     */
    List<AppFolder> loadAppFolders();

    /**
     * Reload quantity and style information for supplied application folders.
     *
     * @param folders folders to reload
     * @return reloaded folders list
     */
    List<AppFolder> reloadAppFolders(List<AppFolder> folders);

    /**
     * Load search folders for the current user.
     *
     * @return folders list
     */
    List<SearchFolder> loadSearchFolders();

    /**
     * Export folder as zip archive.
     *
     * @param folder exported folder
     * @return zip contents
     * @throws IOException
     */
    byte[] exportFolder(Folder folder) throws IOException;

    /**
     * Import folder
     * @param parentFolder
     * @param bytes
     * @return
     * @throws IOException
     */
    Folder importFolder(Folder parentFolder, byte[] bytes) throws IOException;
}
