/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.remoting;

import com.haulmont.cuba.core.entity.FileDescriptor;

import java.io.File;
import java.io.InputStream;

/**
 * The service might be used for work with filestorage, when you use local service invocation
 * (web and core are deployed to same JVM, or web and core are packaged to single WAR file)
 *
 * @author degtyarjov
 * @version $Id$
 */
public interface LocalFileExchangeService {
    String NAME = "cuba_LocalFileExchangeService";

    void uploadFile(InputStream inputStream, FileDescriptor fileDescriptor);

    InputStream downloadFile(FileDescriptor fileDescriptor);
}
