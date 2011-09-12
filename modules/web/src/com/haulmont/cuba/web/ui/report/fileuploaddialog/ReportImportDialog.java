/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Vasiliy Fontanenko
 * Created: 01.07.2010 14:03:06
 *
 * $Id$
 */
package com.haulmont.cuba.web.ui.report.fileuploaddialog;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.Window;

import java.util.Map;

public class ReportImportDialog extends AbstractWindow {

    private static final long serialVersionUID = -8624761668385369711L;

    private byte[] bytes;

    public ReportImportDialog(IFrame frame) {
        super(frame);
    }

    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        final FileUploadField fileUploadField = getComponent("fileUpload");
        fileUploadField.addListener(new FileUploadField.Listener() {
            public void uploadStarted(Event event) {
            }

            public void uploadFinished(Event event) {
            }

            public void uploadSucceeded(Event event) {
                bytes = fileUploadField.getBytes();
                close(Window.COMMIT_ACTION_ID);
            }

            public void uploadFailed(Event event) {
            }

            public void updateProgress(long readBytes, long contentLength) {
            }
        });
    }
}
