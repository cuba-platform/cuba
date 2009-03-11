/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 11.03.2009 17:42:22
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import java.io.File;

public interface FileUploadField extends Component {
    interface Listener {
        class Event {
            String filename;

            public Event(String filename) {
                this.filename = filename;
            }

            public String getFilename() {
                return filename;
            }
        }

        void uploadStarted(Event event);
        void uploadFinished(Event event);

        void uploadSucceeded(Event event);
        void uploadFailed(Event event);

        void updateProgress(long readBytes, long contentLength);
    }

    boolean isUploading();
    File getFile();
    long getBytesRead();

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
