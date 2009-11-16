/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 30.10.2009 14:11:59
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface FileStorageMBean {

    String OBJECT_NAME = "haulmont.cuba:service=FileStorage";

    FileStorageAPI getAPI();

    String getStoragePath();

    String findInvalidDescriptors();

    String findInvalidFiles();
}
