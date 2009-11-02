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
import com.haulmont.cuba.core.sys.ServiceInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import java.io.InputStream;
import java.util.Date;

@Stateless(name = FileStorageService.JNDI_NAME)
@Interceptors({ServiceInterceptor.class})
@TransactionManagement(TransactionManagementType.BEAN)
public class FileStorageServiceBean implements FileStorageService {

    public void saveFile(FileDescriptor fileDescr, byte[] data) throws FileStorageException {
        FileStorageMBean mbean = Locator.lookupMBean(FileStorageMBean.class, FileStorageMBean.OBJECT_NAME);
        mbean.getAPI().saveFile(fileDescr, data);
    }

    public void removeFile(FileDescriptor fileDescr) throws FileStorageException {
        FileStorageMBean mbean = Locator.lookupMBean(FileStorageMBean.class, FileStorageMBean.OBJECT_NAME);
        mbean.getAPI().removeFile(fileDescr);
    }

    public byte[] loadFile(FileDescriptor fileDescr) throws FileStorageException {
        FileStorageMBean mbean = Locator.lookupMBean(FileStorageMBean.class, FileStorageMBean.OBJECT_NAME);
        return mbean.getAPI().loadFile(fileDescr);
    }
}
