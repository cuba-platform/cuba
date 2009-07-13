/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 11.03.2009 17:48:51
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.web.app.UIComponentsConfig;
import com.haulmont.cuba.web.gui.Window;
import com.itmill.toolkit.ui.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUploadField
    extends
        AbstractComponent<Upload>
    implements
        com.haulmont.cuba.gui.components.FileUploadField
{
    protected File file;
    private List<Listener> listeners = new ArrayList<Listener>();

    public FileUploadField() {
        UIComponentsConfig config = ConfigProvider.getConfig(UIComponentsConfig.class);
        final String uploadDir = config.getUploadDir();

        component = new Upload(MessageProvider.getMessage(Window.class, "msg://Upload"), new Upload.Receiver() {
            public OutputStream receiveUpload(String filename, String MIMEType) {
                FileOutputStream fos;
                file = new File(uploadDir != null ? uploadDir : "" + filename);
                try {
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    return null;
                }

                return fos;
            }
        });
        component.addListener(new Upload.StartedListener() {
            public void uploadStarted(Upload.StartedEvent event) {
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadStarted(e);
                }
            }
        });
        component.addListener(new Upload.FinishedListener() {
            public void uploadFinished(Upload.FinishedEvent event) {
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadFinished(e);
                }
            }
        });
        component.addListener(new Upload.SucceededListener() {
            public void uploadSucceeded(Upload.SucceededEvent event) {
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadSucceeded(e);
                }
            }
        });
        component.addListener(new Upload.FailedListener() {
            public void uploadFailed(Upload.FailedEvent event) {
                final Listener.Event e = new Listener.Event(event.getFilename());
                for (Listener listener : listeners) {
                    listener.uploadFailed(e);
                }
            }
        });
        component.addListener(new Upload.ProgressListener() {
            public void updateProgress(long readBytes, long contentLength) {
                for (Listener listener : listeners) {
                    listener.updateProgress(readBytes, contentLength);
                }
            }
        });
    }

    public <T> T getComponent() {
        return (T) component;
    }

    public boolean isUploading() {
        return component.isUploading();
    }

    public long getBytesRead() {
        return component.getBytesRead();
    }

    public void addListener(Listener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public File getFile() {
        return file;
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }
}
