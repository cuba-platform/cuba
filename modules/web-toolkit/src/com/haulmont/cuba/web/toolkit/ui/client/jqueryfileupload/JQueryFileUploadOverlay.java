/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.google.gwt.dom.client.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class JQueryFileUploadOverlay {

    protected Element fileInput;
    protected String uploadUrl;

    public JQueryFileUploadOverlay(Element fileInput) {
        this.fileInput = fileInput;

        init(fileInput);
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    protected native void init(Element fileInput) /*-{
        var upload = $wnd.jQuery(fileInput);

        upload.fileupload({
            dataType: 'json',
            autoUpload : false,
            sequentialUploads: true
        });

        var self = this;

        upload.bind('fileuploadadd', function (e, data) {
            console.log('Add file');

            data.url = self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadUrl;
            data.submit();
        });

        upload.bind('fileuploadsend', function (e, data) {
            $entry(
                self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::fileUploadStart(*)(data.files[0].name)
            );
        });

        upload.bind('fileuploadprogress', function (e, data) {
            console.log('Upload progress ' + data.loaded + ' / ' + data.total);

            $entry(
                self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadProgress(*)(data.loaded, data.total)
            );
        });

        upload.bind('fileuploadstart', function (e, data) {
            console.log('Start upload');

            $entry(
                self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::queueUploadStart()()
            );
        });

        upload.bind('fileuploadstop', function (e, data) {
            console.log('Stop upload');

            $entry(
                self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::queueUploadStop()()
            );
        });

        upload.bind('fileuploadfail', function (e, data) {
            console.log('Failed upload ' + data.errorThrown + ' ' + data.textStatus);

            $entry(
                self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadFailed(*)(data.textStatus, data.errorThrown)
            );
        });
    }-*/;

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