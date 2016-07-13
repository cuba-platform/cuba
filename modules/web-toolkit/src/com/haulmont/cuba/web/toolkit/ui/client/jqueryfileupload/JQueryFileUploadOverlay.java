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

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import fi.jasoft.dragdroplayouts.client.ui.util.HTML5Support;

import java.util.ArrayList;
import java.util.List;

import static com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.CubaFileUploadWidget.CUBA_FILEUPLOAD_DROPZONE_CLASS;

public class JQueryFileUploadOverlay {

    protected static boolean globalDragDropHandlersAttached = false;
    protected static Timer dragStopTimer;

    protected Element fileInput;
    protected String uploadUrl;

    protected List<JavaScriptObject> currentXHRs = new ArrayList<JavaScriptObject>();

    protected static List<Element> dropZones = new ArrayList<Element>();

    private Element dropZoneElement;

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
            autoUpload: false,
            sequentialUploads: true
        });

        var self = this;

        upload.bind('fileuploadadd', $entry(function (e, data) {
            data.url = self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadUrl;

            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::addPendingUpload(*)(data);
        }));

        upload.bind('fileuploaddone', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::removePendingUpload(*)(data);
        }));

        upload.bind('fileuploadsend', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::fileUploadStart(*)(data.files[0].name);
        }));

        upload.bind('fileuploadprogress', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadProgress(*)(data.loaded, data.total);
        }));

        upload.bind('fileuploaddone', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::fileUploadSucceed(*)(data.files[0].name);
        }));

        upload.bind('fileuploadstop', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::fileUploadStop()();
        }));

        upload.bind('fileuploadfail', $entry(function (e, data) {
            self.@com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.JQueryFileUploadOverlay::uploadFailed(*)(data.textStatus, data.errorThrown);
        }));
    }-*/;

    protected native void submitXHR(JavaScriptObject jqXHR) /*-{
        jqXHR.submit();
    }-*/;

    protected native void cancelXHR(JavaScriptObject jqXHR) /*-{
        jqXHR.abort();
    }-*/;

    protected native int getOriginalFilesCount(JavaScriptObject jqXHR) /*-{
        return jqXHR.originalFiles.length;
    }-*/;

    protected native String getFileName(JavaScriptObject jqXHR) /*-{
        return jqXHR.files[0].name;
    }-*/;

    protected native double getFileSize(JavaScriptObject jqXHR) /*-{
        return jqXHR.files[0].size;
    }-*/;

    protected void addPendingUpload(JavaScriptObject jqXHR) {
        currentXHRs.add(jqXHR);

        if (currentXHRs.size() == getOriginalFilesCount(jqXHR)) {
            for (JavaScriptObject xhr : currentXHRs) {
                if (!isValidFile(getFileName(xhr), getFileSize(xhr))) {
                    currentXHRs.clear();
                    return;
                }
            }

            queueUploadStart();

            // all files added to pending queue, start uploading
            submitXHR(currentXHRs.get(0));
        }
    }

    protected void removePendingUpload(JavaScriptObject jqXHR) {
        if (!currentXHRs.isEmpty()) {
            currentXHRs.remove(0);
        }
    }

    public void continueUploading() {
        if (!currentXHRs.isEmpty()) {
            submitXHR(currentXHRs.get(0));
        }
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

    protected void fileUploadStop() {
        if (currentXHRs.isEmpty()) {
            queueUploadStop();
        }
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

    protected void fileUploadSucceed(String fileName) {
        // change file name in upload window
    }

    public void setDropZone(Element dropZoneElement) {
        setDropZone(fileInput, dropZoneElement);

        if (dropZoneElement != null) {
            if (!globalDragDropHandlersAttached) {
                subscribeGlobalDragDropHandlers();

                globalDragDropHandlersAttached = true;
            }

            if (!dropZones.contains(dropZoneElement)) {
                dropZones.add(dropZoneElement);
            }
        } else {
            dropZones.remove(this.dropZoneElement);
        }

        this.dropZoneElement = dropZoneElement;
    }

    protected void subscribeGlobalDragDropHandlers() {
        RootPanel.get().addBitlessDomHandler(new DragOverHandler() {
            @Override
            public void onDragOver(DragOverEvent event) {
                globalDocumentDragOver(event);

                if (dropZones.size() > 0) {
                    event.preventDefault();
                }
            }
        }, DragOverEvent.getType());

        RootPanel.get().addBitlessDomHandler(new DragLeaveHandler() {
            @Override
            public void onDragLeave(DragLeaveEvent event) {
                globalDocumentDragLeave(event);
            }
        }, DragLeaveEvent.getType());

        RootPanel.get().addBitlessDomHandler(new DragEndHandler() {
            @Override
            public void onDragEnd(DragEndEvent event) {
                globalDocumentDragEnd(event);
            }
        }, DragEndEvent.getType());

        RootPanel.get().addBitlessDomHandler(new DropHandler() {
            @Override
            public void onDrop(DropEvent event) {
                globalDocumentDrop(event);
            }
        }, DropEvent.getType());

        // prevent misses leading to opening of file inside browser
        RootPanel.get().addBitlessDomHandler(new DropHandler() {
            @Override
            public void onDrop(DropEvent event) {
                if (dropZones.size() > 0) {
                    event.preventDefault();
                }
            }
        }, DropEvent.getType());

        // CAUTION add compatibility layer with Vaadin DragDropLayouts
        HTML5Support.setGlobalDragOverHandler(new DragOverHandler() {
            @Override
            public void onDragOver(DragOverEvent event) {
                globalDocumentDragOver(event);
            }
        });

        HTML5Support.setGlobalDropHandler(new DropHandler() {
            @Override
            public void onDrop(DropEvent event) {
                globalDocumentDrop(event);
            }
        });
    }

    protected static void globalDocumentDrop(DropEvent event) {
        forceHideDropZones();
    }

    protected static void globalDocumentDragEnd(DragEndEvent event) {
        hideDropZones();
    }

    protected static void globalDocumentDragLeave(DragLeaveEvent event) {
        hideDropZones();
    }

    protected static void globalDocumentDragOver(DragOverEvent event) {
        showDropZones(event);
    }

    protected static void showDropZones(DragOverEvent event) {
        if (isDragEventContainsFiles(event.getNativeEvent())) {
            if (dragStopTimer != null) {
                dragStopTimer.cancel();
            }
            dragStopTimer = null;

            // find all drop zones and add classname
            for (Element dropZone : dropZones) {
                dropZone.addClassName(CUBA_FILEUPLOAD_DROPZONE_CLASS);
            }
        }
    }

    protected static void hideDropZones() {
        if (dragStopTimer != null) {
            dragStopTimer.cancel();
        }

        dragStopTimer = new Timer() {
            @Override
            public void run() {
                forceHideDropZones();
            }
        };
        dragStopTimer.schedule(300);
    }

    protected static void forceHideDropZones() {
        for (Element dropZone : dropZones) {
            dropZone.removeClassName(CUBA_FILEUPLOAD_DROPZONE_CLASS);
        }
        if (dragStopTimer != null) {
            dragStopTimer.cancel();
        }
        dragStopTimer = null;
    }

    protected native void setDropZone(Element fileInput, Element dropZoneElement) /*-{
        //noinspection JSUnresolvedFunction
        var upload = $wnd.jQuery(fileInput);

        upload.fileupload({
            dropZone: dropZoneElement
        });
    }-*/;

    protected native static boolean isDragEventContainsFiles(JavaScriptObject event) /*-{
        if (event && event.dataTransfer && event.dataTransfer.types) {
            for (var i = 0; i < event.dataTransfer.types.length; i++) {
                if (event.dataTransfer.types[i] == "Files") {
                    return true;
                }
            }
        }
        return false;
    }-*/;
}