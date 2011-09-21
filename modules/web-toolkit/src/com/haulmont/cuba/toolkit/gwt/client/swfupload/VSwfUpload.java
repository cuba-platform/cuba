/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 18.10.2010 19:11:44
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.swfupload;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FormPanel;
import com.haulmont.cuba.toolkit.gwt.client.Properties;
import com.haulmont.cuba.toolkit.gwt.client.ResourcesLoader;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

@SuppressWarnings("unused")
public class VSwfUpload
        extends FormPanel
        implements Paintable {

    private static final boolean DEBUG_MODE = false;

    public static final String CLASSNAME = "v-multiupload";

    private boolean initialized = false;

    private String paintableId;
    private ApplicationConnection client;

    private Element uploadButton = DOM.createDiv();
    private Element progressDiv = DOM.createDiv();

    public VSwfUpload() {
        setStyleName(CLASSNAME);
        DOM.appendChild(getElement(), uploadButton);

        progressDiv.setClassName(CLASSNAME + "-progress");
        initProgressWindow();
        Element parentDoc = (Element) getElement().getOwnerDocument().getElementsByTagName("body").getItem(0);
        DOM.appendChild(parentDoc, progressDiv);
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

    private static String getValueFromUIDL(UIDL uidl, String attribute, String defaultValue) {
        if (uidl.hasAttribute(attribute))
            return uidl.getStringAttribute(attribute);
        else
            return defaultValue;
    }

    private static Double getValueFromUIDL(UIDL uidl, String attribute, Double defaultValue) {
        if (uidl.hasAttribute(attribute))
            return uidl.getDoubleAttribute(attribute);
        else
            return defaultValue;
    }

    public void updateFromUIDL(final UIDL uidl, final ApplicationConnection client) {
        paintableId = uidl.getId();

        uploadButton.setId(paintableId + "_upload");
        progressDiv.setId(paintableId + "_progress");

        this.client = client;

        if (client.updateComponent(this, uidl, false)) {
            return;
        }

        if (initialized)
            return;

        injectJs();

        final String actionString;
        if (uidl.hasAttribute("action")) {
            String action = uidl.getStringAttribute("action");
            actionString = "".equals(action) ? "#" : action;
        } else {
            actionString = client.getAppUri();
        }
        setAction(actionString);

        final String fileTypes = getValueFromUIDL(uidl, "fileTypes", "*.*");
        final String fileTypesDescription = getValueFromUIDL(uidl, "fileTypesDescription", "All Files");
        final String fileSizeLimit = getValueFromUIDL(uidl, "fileSizeLimit", "10 MB");
        final Double fileUploadLimit = getValueFromUIDL(uidl, "fileUploadLimit", 100.0);
        final Double fileQueueLimit = getValueFromUIDL(uidl, "fileQueueLimit", 0.0);

        final String caption = getValueFromUIDL(uidl, "caption", "Upload");
        final String width = getValueFromUIDL(uidl, "width", "90").replace("px", "");
        final String height = getValueFromUIDL(uidl, "height", "25").replace("px", "");

        final String controlPid = paintableId;

        SwfUploadAPI.onReady(new Runnable() {
            public void run() {
                initSwfUploadObjects();
                String appUri = client.getAppUri();
                appUri = appUri + (appUri.endsWith("/") ? "" : "/");

                Options opts = Options.create();
                // Flash resource url
                opts.set("flash_url", appUri + "VAADIN/resources/flash/" + "swfupload.swf");
                // Resource url
                opts.set("upload_url", appUri + ";jsessionid=" + client.getConfiguration().getSessionId()
                        + "?pid=" + controlPid + "&multiupload=true");

                // Set file parameters
                opts.set("file_size_limit", fileSizeLimit);
                opts.set("file_types", fileTypes);
                opts.set("file_types_description", fileTypesDescription);
                opts.set("file_upload_limit", fileUploadLimit);
                opts.set("file_queue_limit", fileQueueLimit);

                // Set custom settings
                Options customOpts = Options.create();
                customOpts.set("progressTarget", progressDiv.getId());
                opts.set("custom_settings", customOpts);

                // Set debug mode
                opts.set("debug", DEBUG_MODE);
                String themeUri = client.getThemeUri();
                themeUri = themeUri + (themeUri.endsWith("/") ? "" : "/");
                // Appearance properties
                String imageUrl = themeUri + "multiupload/images/button.png";
                opts.set("button_image_url", imageUrl);

                opts.set("button_width", width);
                opts.set("button_height", height);

                opts.set("button_placeholder_id", uploadButton.getId());
                opts.set("button_text_left_padding", "22");
                opts.set("button_text_top_padding", "4");
                opts.set("button_text", "<span class=\"swfupload\">" + caption + "</span>");
                opts.set("button_text_style",
                        ".swfupload {font-size: 12px; font-family: verdana,Tahoma,sans-serif; }");

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

                // Add event handlers
                applyJsObjectByName(opts, "file_dialog_complete_handler", "fileDialogComplete");
                applyJsObjectByName(opts, "upload_start_handler", "uploadStart");
                applyJsObjectByName(opts, "upload_progress_handler", "uploadProgress");
                applyJsObjectByName(opts, "upload_complete_handler", "uploadComplete");
                applyJsObjectByName(opts, "upload_success_handler", "uploadSuccess");

                // Add error handler
                addErrorHandler(opts, "upload_error_handler");
                addErrorHandler(opts, "file_queue_error_handler");

                // Add complete handler
                addRefresher(opts, "queue_complete_handler");

                showUpload("varUpload_" + paintableId, opts);
            }
        });

        initialized = true;
    }

    protected void injectJs() {
        ResourcesLoader.injectJs(null, client.getAppUri(), "/js/swfupload.js");
    }

    public void refreshServerSide() {
        initProgressWindow();

        client.updateVariable(paintableId, "queueUploadComplete", 1, true);
    }

    public void errorNotify(String file, String message, int errorCode) {
        initProgressWindow();

        client.updateVariable(paintableId, "uploadError", new String[]{file, message, String.valueOf(errorCode)}, true);
    }

    private native void addErrorHandler(Options opts, String optionName)/*-{
        var swfu = this;
        opts[optionName] = function(file, errorCode, message) {
            swfu.@com.haulmont.cuba.toolkit.gwt.client.swfupload.VSwfUpload::errorNotify(Ljava/lang/String;Ljava/lang/String;I)(file.name, message, errorCode);
        };
    }-*/;

    private native void addRefresher(Options opts, String optionName)/*-{
        var swfu = this;
        opts[optionName] = function(numFilesUploaded) {
            swfu.@com.haulmont.cuba.toolkit.gwt.client.swfupload.VSwfUpload::refreshServerSide()();
        };
    }-*/;

    private static native void applyJsObjectByName(Options opts, String propertyName, String objectName) /*-{
        opts[propertyName] = $wnd[objectName];
    }-*/;

    private static native void initSwfUploadObjects() /*-{
        $wnd.initSWFUploadPrototype();
    }-*/;

    private static native void showUpload(String varName, Options opts) /*-{
        $wnd[varName] = $wnd.swfUploadHelper.create(opts);
    }-*/;

    private static native Node appendProgressWindow(Node node)/*-{
        document.getElementsByTagName("body")[0].appendChild(node);
    }-*/;

    public static class Options extends Properties {

        public static Options create() {
            return JavaScriptObject.createObject().cast();
        }

        protected Options() {
        }
    }
}