package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.FileMultiUploadField;

import java.util.function.Consumer;

@Deprecated
public class FileMultiUploadFieldQueueUploadCompleteListener
        implements Consumer<FileMultiUploadField.QueueUploadCompleteEvent> {

    protected final FileMultiUploadField.QueueUploadCompleteListener listener;

    public FileMultiUploadFieldQueueUploadCompleteListener(FileMultiUploadField.QueueUploadCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileMultiUploadFieldQueueUploadCompleteListener that = (FileMultiUploadFieldQueueUploadCompleteListener) o;

        return listener.equals(that.listener);
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }

    @Override
    public void accept(FileMultiUploadField.QueueUploadCompleteEvent queueUploadCompleteEvent) {
        listener.queueUploadComplete();
    }
}
