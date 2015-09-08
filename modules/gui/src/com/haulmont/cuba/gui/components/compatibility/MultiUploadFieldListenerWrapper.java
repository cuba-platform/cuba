/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.FileMultiUploadField;
import com.haulmont.cuba.gui.components.UploadComponentSupport;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class MultiUploadFieldListenerWrapper implements UploadComponentSupport.FileUploadStartListener,
                                                        UploadComponentSupport.FileUploadErrorListener,
                                                        UploadComponentSupport.FileUploadFinishListener,
                                                        FileMultiUploadField.QueueUploadCompleteListener {

    private final FileMultiUploadField.UploadListener listener;

    public MultiUploadFieldListenerWrapper(FileMultiUploadField.UploadListener listener) {
        this.listener = listener;
    }

    @Override
    public void fileUploadError(UploadComponentSupport.FileUploadErrorEvent e) {
        //todo rewrite generated body
    }

    @Override
    public void fileUploadFinish(UploadComponentSupport.FileUploadFinishEvent e) {
        //todo rewrite generated body
    }

    @Override
    public void fileUploadStart(UploadComponentSupport.FileUploadStartEvent e) {
        //todo rewrite generated body
    }

    @Override
    public void queueUploadComplete() {
        //todo rewrite generated body
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        MultiUploadFieldListenerWrapper that = (MultiUploadFieldListenerWrapper) obj;

        return this.listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }
}