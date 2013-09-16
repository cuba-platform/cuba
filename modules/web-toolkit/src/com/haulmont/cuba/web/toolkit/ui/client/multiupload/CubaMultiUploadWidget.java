/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FormPanel;
import com.haulmont.cuba.web.toolkit.ui.client.Properties;
import com.vaadin.client.VConsole;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMultiUploadWidget extends FormPanel {

    public static final String CLASSNAME = "cuba-multiupload";

    private static final boolean DEBUG_MODE = true;

    private static boolean scriptInjected = false;

    protected BootstrapFailureHandler bootstrapFailureHandler;

    protected NotificationHandler notificationHandler;

    protected String jsessionId = "";

    protected String fileTypes = "*.*";
    protected String fileTypesDescription = "";
    protected double fileSizeLimit = 10.0;
    protected double queueUploadLimit = 100.0;
    protected int queueSizeLimit = 50;

    protected String buttonCaption = "Upload";
    protected Integer buttonWidth = 90;
    protected Integer buttonHeight = 25;

    protected String buttonImageUri;
    protected String targetUrl;
    protected String resourcesVersion = "debug";
    protected String baseResourcesUri = "";

    protected String themeName;

    private String uploadId;
    private String swfUri;

    private String jsIncludeUri;

    protected Element uploadButton = DOM.createDiv();
    protected Element themeDiv = DOM.createDiv();
    protected Element progressDiv = DOM.createDiv();

    public CubaMultiUploadWidget() {
        setStyleName(CLASSNAME);
        DOM.appendChild(getElement(), uploadButton);

        progressDiv.setClassName(CLASSNAME + "-progress");

        initProgressWindow();
        attachProgressWindow();
    }

    private void attachProgressWindow() {
        Element parentDoc = (Element) getElement().getOwnerDocument().getElementsByTagName("body").getItem(0);
        themeDiv = DOM.createDiv();

        DOM.appendChild(themeDiv, progressDiv);
        DOM.appendChild(parentDoc, themeDiv);
    }

    private void initProgressWindow() {
        progressDiv.getStyle().setVisibility(Style.Visibility.HIDDEN);
        progressDiv.getStyle().setDisplay(Style.Display.NONE);

        NodeList<Node> nodes = progressDiv.getChildNodes();
        if (nodes != null) {
            for (int i = 0; i < nodes.getLength(); i++) {
                progressDiv.removeChild(nodes.getItem(i));
            }
        }
    }

    public void initComponent(String uploadId) {
        this.jsIncludeUri = baseResourcesUri + "swfupload.min.js?v=" + resourcesVersion;
        this.swfUri = baseResourcesUri + "swfupload.swf?v=" + resourcesVersion;
        this.uploadId = uploadId;

        this.uploadButton.setId("upload_button_" + uploadId);
        this.progressDiv.setId("upload_progress_" + uploadId);

        this.themeDiv.setClassName(themeName);

        VConsole.log("Required js: " + jsIncludeUri);
        if (!scriptInjected) {
            ScriptInjector.fromUrl(jsIncludeUri).setCallback(new Callback<Void, Exception>() {
                @Override
                public void onFailure(Exception reason) {
                    VConsole.log("Unable to inject js: " + jsIncludeUri);

                    if (bootstrapFailureHandler != null)
                        bootstrapFailureHandler.resourceLoadFailed();

                    setDisabled();
                }

                @Override
                public void onSuccess(Void result) {
                    VConsole.log("Successfully injected js: " + jsIncludeUri);

                    checkAndInitialize();
                }
            }).setWindow(getWindow()).setRemoveTag(false).inject();

            scriptInjected = true;
        } else {
            checkAndInitialize();
        }
    }

    private void setDisabled() {
        setStyleName(CLASSNAME + "-disabled");
    }

    private native JavaScriptObject getWindow() /*-{
        return $wnd;
    }-*/;

    private void checkAndInitialize() {
        SwfUploadAPI.onReady(new Runnable() {
            @Override
            public void run() {
                initializeSwfUpload();
            }
        });
    }

    public void initializeSwfUpload() {
        Options opts = Options.create();

        opts.set("flash_url", swfUri);
        opts.set("upload_url", targetUrl + ";jsessionid=" + jsessionId);

        opts.set("file_size_limit", fileSizeLimit + " MB");
        opts.set("file_types", fileTypes);
        opts.set("file_types_description", fileTypesDescription);
        opts.set("file_upload_limit", queueUploadLimit);
        opts.set("file_queue_limit", (double)queueSizeLimit);

        // Set custom settings
        Options customOpts = Options.create();
        customOpts.set("progressTarget", progressDiv.getId());
        opts.set("custom_settings", customOpts);

        // Set debug mode
        opts.set("debug", DEBUG_MODE);

        opts.set("button_image_url", buttonImageUri);
        opts.set("button_width", String.valueOf(buttonWidth));
        opts.set("button_height", String.valueOf(buttonHeight));

        opts.set("button_placeholder_id", uploadButton.getId());
        // todo artamonov make configurable paddings, text and style
        opts.set("button_text_left_padding", "22");
        opts.set("button_text_top_padding", "4");
        opts.set("button_text", "<span class=\"swfupload\">" + buttonCaption + "</span>");
        opts.set("button_text_style",
                ".swfupload {font-size: 12px; font-family: verdana,Tahoma,sans-serif;}");

        setDefaultOptions(opts);

        /*
         *   SWF Handlers list
         *
         *   file_queued_handler           : fileQueued
         *   file_queue_error_handler      : fileQueueError
         *   file_dialog_complete_handler  : fileDialogComplete
         *   upload_start_handler          : uploadStart
         *   upload_progress_handler       : uploadProgress
         *   upload_error_handler          : uploadError
         *   upload_success_handler        : uploadSuccess
         *   upload_complete_handler       : uploadComplete
         *   queue_complete_handler        : queueComplete
         */

        opts.bindJsObject("file_dialog_complete_handler", "cuba_multiupload_fileDialogComplete");
        opts.bindJsObject("upload_start_handler", "cuba_multiupload_uploadStart");
        opts.bindJsObject("upload_progress_handler", "cuba_multiupload_uploadProgress");
        opts.bindJsObject("upload_complete_handler", "cuba_multiupload_uploadComplete");
        opts.bindJsObject("upload_success_handler", "cuba_multiupload_uploadSuccess");

        setDefaultHandlers(opts);

        showUploadButton("upload_" + uploadId, opts);

        VConsole.log("Inject swf resource: " + swfUri);
        VConsole.log("Upload target: " + targetUrl);
    }

    public void queueUploadComplete() {
        initProgressWindow();

        if (notificationHandler != null)
            notificationHandler.queueUploadComplete();
    }

    public void errorNotify(String file, String message, int errorCode) {
        if (notificationHandler != null)
            notificationHandler.error(file, message, errorCode);
    }

    public void swfLoaded() {
        VConsole.log("Found required flash player");
    }

    public void swfLoadFailed() {
        VConsole.log("Unable to initialize flash player");

        if (bootstrapFailureHandler != null)
            bootstrapFailureHandler.flashNotInstalled();

        setDisabled();
    }

    private native void showUploadButton(String varName, Options opts) /*-{
        $wnd[varName] = $wnd.swfUploadHelper.create(opts);
    }-*/;

    private native void setDefaultOptions(Options opts) /*-{
        opts['minimum_flash_version'] = '9.0.28';
        opts['button_cursor'] = $wnd.SWFUpload.CURSOR.HAND;
        opts['button_window_mode'] = $wnd.SWFUpload.WINDOW_MODE.TRANSPARENT;
    }-*/;

    private native void setDefaultHandlers(Options opts) /*-{
        var swfu = this;

        opts['swfupload_pre_load_handler'] = function() {
            swfu.@com.haulmont.cuba.web.toolkit.ui.client.multiupload.CubaMultiUploadWidget::swfLoaded()();
        };
        opts['swfupload_load_failed_handler'] = function() {
            swfu.@com.haulmont.cuba.web.toolkit.ui.client.multiupload.CubaMultiUploadWidget::swfLoadFailed()();
        };
        opts['upload_error_handler'] = function (file, errorCode, message) {
            swfu.@com.haulmont.cuba.web.toolkit.ui.client.multiupload.CubaMultiUploadWidget::errorNotify(Ljava/lang/String;Ljava/lang/String;I)(file.name, message, errorCode);
        };
        opts['file_queue_error_handler'] = function (file, errorCode, message) {
            swfu.@com.haulmont.cuba.web.toolkit.ui.client.multiupload.CubaMultiUploadWidget::errorNotify(Ljava/lang/String;Ljava/lang/String;I)(file.name, message, errorCode);
        };
        opts['queue_complete_handler'] = function (numFilesUploaded) {
            swfu.@com.haulmont.cuba.web.toolkit.ui.client.multiupload.CubaMultiUploadWidget::queueUploadComplete()();
        };
    }-*/;

    public static class Options extends Properties {
        public static Options create() {
            return JavaScriptObject.createObject().cast();
        }

        protected Options() {
        }

        public final native void bindJsObject(String propertyName, String objectName) /*-{
            this[propertyName] = $wnd[objectName];
        }-*/;
    }

    public interface BootstrapFailureHandler {
        void resourceLoadFailed();

        void flashNotInstalled();
    }

    public interface NotificationHandler {
        void error(String fileName, String message, int code);

        void queueUploadComplete();
    }
}