/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.UploadComponentSupport;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class FileUploadFieldListenerWrapper implements UploadComponentSupport.FileUploadStartListener,
                                                       UploadComponentSupport.FileUploadFinishListener,
                                                       UploadComponentSupport.FileUploadErrorListener,
                                                       FileUploadField.FileUploadSucceedListener {
    private final FileUploadField.Listener listener;

    public FileUploadFieldListenerWrapper(FileUploadField.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void fileUploadError(UploadComponentSupport.FileUploadErrorEvent e) {
        listener.uploadFailed(new FileUploadField.Listener.Event(e.getFileName(), e.getCause()));
    }

    @Override
    public void fileUploadFinish(UploadComponentSupport.FileUploadFinishEvent e) {
        listener.uploadFinished(new FileUploadField.Listener.Event(e.getFileName()));
    }

    @Override
    public void fileUploadStart(UploadComponentSupport.FileUploadStartEvent e) {
        listener.uploadStarted(new FileUploadField.Listener.Event(e.getFileName()));
    }

    @Override
    public void fileUploadSucceed(FileUploadField.FileUploadSucceedEvent e) {
        listener.uploadSucceeded(new FileUploadField.Listener.Event(e.getFileName()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        FileUploadFieldListenerWrapper that = (FileUploadFieldListenerWrapper) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}