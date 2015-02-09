/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

    @Override
    public boolean fileExists(FileDescriptor fileDescr) {
        FileStorageAPI mbean = Locator.lookup(FileStorageAPI.NAME);
        return mbean.fileExists(fileDescr);
    }
}