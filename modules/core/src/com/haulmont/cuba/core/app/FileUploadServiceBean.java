/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Yuryi Artamonov
 * Created: 18.11.2010 14:50:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.TimeProvider;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

@Service(FileUploadService.NAME)
public class FileUploadServiceBean implements FileUploadService {

    public UUID saveFile(byte[] data) throws FileStorageException {
        FileUploadingApi uploadingApi = Locator.lookup(FileUploadingApi.NAME);
        return uploadingApi.saveFile(data);
    }

    public UUID saveFile(InputStream stream, UploadProgressListener listener)
            throws FileStorageException {
        FileUploadingApi uploadingApi = Locator.lookup(FileUploadingApi.NAME);
        return uploadingApi.saveFile(stream, listener);
    }

    public File getFile(UUID fileId) {
        FileUploadingApi uploadingApi = Locator.lookup(FileUploadingApi.NAME);
        return uploadingApi.getFile(fileId);
    }

    public FileDescriptor getFileDescriptor(UUID fileId, String name) {
        File file = getFile(fileId);
        int fileSize = (int) file.length();

        FileDescriptor fDesc = new FileDescriptor();
        String ext = "";
        int extIndex = name.lastIndexOf('.');
        if ((extIndex >= 0) && (extIndex < name.length()))
            ext = name.substring(extIndex + 1);

        fDesc.setSize(fileSize);
        fDesc.setExtension(ext);
        fDesc.setName(name);
        fDesc.setCreateDate(TimeProvider.currentTimestamp());

        return fDesc;
    }

    public InputStream loadFile(UUID fileId) throws FileNotFoundException {
        FileUploadingApi uploadingApi = Locator.lookup(FileUploadingApi.NAME);
        return uploadingApi.loadFile(fileId);
    }

    public void deleteFile(UUID fileId) throws FileStorageException {
        FileUploadingApi uploadingApi = Locator.lookup(FileUploadingApi.NAME);
        uploadingApi.deleteFile(fileId);
    }
}
