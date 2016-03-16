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

import com.haulmont.cuba.core.entity.AppFolder;
import com.haulmont.cuba.core.entity.Folder;
import com.haulmont.cuba.security.entity.SearchFolder;

import java.io.IOException;
import java.util.List;

/**
 * Folders pane support service.
 *
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
