/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.10.2009 17:36:16
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service(FileStorageService.NAME)
public class FileStorageServiceBean implements FileStorageService {

    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
        mbean.saveFile(fileDescr, data);
    }

    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
        mbean.removeFile(fileDescr);
    }

    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
        return mbean.loadFile(fileDescr);
    }
}
