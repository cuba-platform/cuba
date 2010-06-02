/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 20.11.2009 16:03:28
 *
 * $Id$
 */
package com.haulmont.cuba.web.filestorage;

import com.haulmont.cuba.web.rpt.ReportOutputWindow;
import com.haulmont.cuba.core.entity.FileDescriptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class FileWindow extends ReportOutputWindow {

    protected FileDescriptor fd;

    protected File f;

    protected transient Log log = LogFactory.getLog(getClass());

    public FileWindow(String windowName, FileDescriptor fd) {
        super(windowName);
        this.fd = fd;
    }

    protected FileWindow(String caption, File f) {
        super(caption);
        this.f = f;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        log = LogFactory.getLog(getClass());
        in.defaultReadObject();
    }
}
