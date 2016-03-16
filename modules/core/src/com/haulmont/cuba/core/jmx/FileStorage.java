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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.FileStorageException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 */
@Component("cuba_FileStorageMBean")
public class FileStorage implements FileStorageMBean {

    @Inject
    protected Persistence persistence;

    @Override
    public File[] getStorageRoots() {
        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        if (fileStorageAPI instanceof com.haulmont.cuba.core.app.filestorage.FileStorage) {
            return ((com.haulmont.cuba.core.app.filestorage.FileStorage) fileStorageAPI).getStorageRoots();
        } else {
            return new File[0];
        }
    }

    @Override
    public String findOrphanDescriptors() {
        com.haulmont.cuba.core.app.filestorage.FileStorage fileStorage;
        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        if (fileStorageAPI instanceof com.haulmont.cuba.core.app.filestorage.FileStorage) {
            fileStorage = (com.haulmont.cuba.core.app.filestorage.FileStorage) fileStorageAPI;
        } else {
            return "<not supported>";
        }

        File[] roots = getStorageRoots();
        if (roots.length == 0)
            return "No storage directories defined";

        StringBuilder sb = new StringBuilder();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<FileDescriptor> query = em.createQuery("select fd from sys$FileDescriptor fd", FileDescriptor.class);
            List<FileDescriptor> fileDescriptors = query.getResultList();
            for (FileDescriptor fileDescriptor : fileDescriptors) {
                File dir = fileStorage.getStorageDir(roots[0], fileDescriptor.getCreateDate());
                File file = new File(dir, com.haulmont.cuba.core.app.filestorage.FileStorage.getFileName(fileDescriptor));
                if (!file.exists()) {
                    sb.append(fileDescriptor.getId())
                            .append(", ")
                            .append(fileDescriptor.getName())
                            .append(", ")
                            .append(fileDescriptor.getCreateDate())
                            .append("\n");
                }
            }
            tx.commit();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
        }
        return sb.toString();
    }

    @Override
    public String findOrphanFiles() {
        com.haulmont.cuba.core.app.filestorage.FileStorage fileStorage;
        FileStorageAPI fileStorageAPI = AppBeans.get(FileStorageAPI.class);
        if (!(fileStorageAPI instanceof com.haulmont.cuba.core.app.filestorage.FileStorage)) {
            return "<not supported>";
        }

        File[] roots = getStorageRoots();
        if (roots.length == 0)
            return "No storage directories defined";

        StringBuilder sb = new StringBuilder();

        File storageFolder = roots[0];
        if (!storageFolder.exists())
            return ExceptionUtils.getStackTrace(
                    new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, storageFolder.getAbsolutePath()));

        @SuppressWarnings("unchecked")
        Collection<File> systemFiles = FileUtils.listFiles(storageFolder, null, true);
        @SuppressWarnings("unchecked")
        Collection<File> filesInRootFolder = FileUtils.listFiles(storageFolder, null, false);
        //remove files of root storage folder (e.g. storage.log) from files collection
        systemFiles.removeAll(filesInRootFolder);

        List<FileDescriptor> fileDescriptors;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<FileDescriptor> query = em.createQuery("select fd from sys$FileDescriptor fd", FileDescriptor.class);
            fileDescriptors = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
        }

        Set<String> descriptorsFileNames = new HashSet<>();
        for (FileDescriptor fileDescriptor : fileDescriptors) {
            descriptorsFileNames.add(com.haulmont.cuba.core.app.filestorage.FileStorage.getFileName(fileDescriptor));
        }

        for (File file : systemFiles) {
            if (!descriptorsFileNames.contains(file.getName()))
                //Encode file path if it contains non-ASCII characters
                if (!file.getPath().matches("\\p{ASCII}+")) {
                    try {
                        String encodedFilePath = URLEncoder.encode(file.getPath(), "utf-8");
                        sb.append(encodedFilePath).append("\n");
                    } catch (UnsupportedEncodingException e) {
                        return ExceptionUtils.getStackTrace(e);
                    }
                } else {
                    sb.append(file.getPath()).append("\n");
                }
        }

        return sb.toString();
    }
}