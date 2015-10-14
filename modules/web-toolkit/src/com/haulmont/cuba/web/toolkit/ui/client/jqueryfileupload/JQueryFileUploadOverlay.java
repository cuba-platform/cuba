/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
public class JQueryFileUploadOverlay {

    protected Element fileInput;
    protected String uploadUrl;

    protected List<JavaScriptObject> currentXHRs = new ArrayList<JavaScriptObject>();

    public JQueryFileUploadOverlay(Element fileInput) {
        this.fileInput = fileInput;

        init(fileInput);
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    protected native void init(Element fileInput) /*-{
        //noinspection JSUnresolvedFunction
        var upload = $wnd.jQuery(fileInput);

        upload.fileupload({
            dropZone: upload,
            dataType: 'json',
            autoUpload : false,
            sequentialUploads: true
        });

        var self = this;

        upload.bind('fileuploadadd', $entry(function (e, data) {
            data.url = self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadUrl;

            var file = data.files[0];
            var valid = self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::isValidFile(*)(file.name, file.size);

            if (valid) {
                self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::addPendingUpload(*)(data);

                data.submit();
            }
        }));

        upload.bind('fileuploaddone', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::removePendingUpload(*)(data)
        }));

        upload.bind('fileuploadsend', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::fileUploadStart(*)(data.files[0].name)
        }));

        upload.bind('fileuploadprogress', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadProgress(*)(data.loaded, data.total)
        }));

        upload.bind('fileuploadstart', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::queueUploadStart()()
        }));

        upload.bind('fileuploadstop', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::queueUploadStop()()
        }));

        upload.bind('fileuploadfail', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadFailed(*)(data.textStatus, data.errorThrown)
        }));
    }-*/;

    protected native void cancelXHR(JavaScriptObject jqXHR) /*-{
        jqXHR.abort();
    }-*/;

    protected void addPendingUpload(JavaScriptObject jqXHR) {
        currentXHRs.add(jqXHR);
    }

    protected void removePendingUpload(JavaScriptObject jqXHR) {
        currentXHRs.remove(jqXHR);
    }

    public void cancelUploading() {
        for (int i = currentXHRs.size() - 1; i >= 0; i--) {
            cancelXHR(currentXHRs.get(i));
        }
        currentXHRs.clear();
    }

    protected boolean isValidFile(String name, double size) {
        // check if file has valid extension and size
        return true;
    }

    protected void uploadProgress(double loaded, double total) {
        // change progress bar value
    }

    protected void uploadFailed(String textStatus, String errorThrown) {
        // show upload error
        // hide upload window
    }

    protected void queueUploadStop() {
        // hide upload window
    }

    protected void queueUploadStart() {
        // show upload window
    }

    protected void fileUploadStart(String fileName) {
        // change file name in upload window
    }
}