/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.UploadComponentSupport;

/**
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