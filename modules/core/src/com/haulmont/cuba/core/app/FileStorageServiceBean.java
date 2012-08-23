/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import org.springframework.stereotype.Service;

/**
 * @author krivopustov
 * @version $Id$
 */
@Service(FileStorageService.NAME)
public class FileStorageServiceBean implements FileStorageService {

    @Override
    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
        mbean.saveFile(fileDescr, data);
    }

    @Override
    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
        mbean.removeFile(fileDescr);
    }

    @Override
    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
        return mbean.loadFile(fileDescr);
    }
}