/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_FileStorageMBean")
public class FileStorage implements FileStorageMBean {

    @Inject
    protected com.haulmont.cuba.core.app.FileStorage fileStorage;

    @Inject
    protected Persistence persistence;

    @Override
    public File[] getStorageRoots() {
        return fileStorage.getStorageRoots();
    }

    @Override
    public String findOrphanDescriptors() {
        File[] roots = getStorageRoots();
        if (roots.length == 0)
            return "No storage directories defined";

        StringBuilder sb = new StringBuilder();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select fd from sys$FileDescriptor fd");
            List<FileDescriptor> fileDescriptors = query.getResultList();
            for (FileDescriptor fileDescriptor : fileDescriptors) {
                File dir = fileStorage.getStorageDir(roots[0], fileDescriptor.getCreateDate());
                File file = new File(dir, com.haulmont.cuba.core.app.FileStorage.getFileName(fileDescriptor));
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
        File[] roots = getStorageRoots();
        if (roots.length == 0)
            return "No storage directories defined";

        StringBuilder sb = new StringBuilder();

        File storageFolder = roots[0];
        if (!storageFolder.exists())
            return ExceptionUtils.getStackTrace(
                    new FileStorageException(FileStorageException.Type.FILE_NOT_FOUND, storageFolder.getAbsolutePath()));

        Collection<File> systemFiles = FileUtils.listFiles(storageFolder, null, true);
        Collection<File> filesInRootFolder = FileUtils.listFiles(storageFolder, null, false);
        //remove files of root storage folder (e.g. storage.log) from files collection
        systemFiles.removeAll(filesInRootFolder);

        List<FileDescriptor> fileDescriptors = new ArrayList<>();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query query = em.createQuery("select fd from sys$FileDescriptor fd");
            fileDescriptors = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        } finally {
            tx.end();
        }

        Set<String> descriptorsFileNames = new HashSet<>();
        for (FileDescriptor fileDescriptor : fileDescriptors) {
            descriptorsFileNames.add(com.haulmont.cuba.core.app.FileStorage.getFileName(fileDescriptor));
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