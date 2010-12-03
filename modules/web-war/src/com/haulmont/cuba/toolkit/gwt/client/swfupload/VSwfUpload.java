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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FormPanel;
import com.haulmont.cuba.toolkit.gwt.client.Properties;
import com.haulmont.cuba.toolkit.gwt.client.ResourcesLoader;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class
        VSwfUpload extends FormPanel implements Paintable {
    private String paintableId;
    private ApplicationConnection client;
    private Timer t;

    private Element uploadButton = DOM.createSpan();

    public VSwfUpload() {
        DOM.appendChild(getElement(), uploadButton);
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

    public void updateFromUIDL(UIDL uidl, final ApplicationConnection client) {
        paintableId = uidl.getId();

        this.client = client;

        injectJs();

        if (client.updateComponent(this, uidl, false)) {
            return;
        }
        String actionString = "#";
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
        final String width = getValueFromUIDL(uidl, "width", "90px");
        final String height = getValueFromUIDL(uidl, "height", "25px");

        final String controlPid = paintableId.toString();

        uploadButton.setId(paintableId + "_upload");

        SwfUploadAPI.onReady(new Runnable() {
            public void run() {
                initPrototype();
                String uri = client.getAppUri();

                Options opts = Options.create();
                // Flash resource url
                opts.set("flash_url", uri + (uri.endsWith("/") ? "" : "/") + "VAADIN/resources/flash/" + "swfupload.swf");
                // Resource url
                opts.set("upload_url", ";jsessionid=" + client.getConfiguration().getSessionId()
                        + "?pid=" + controlPid.toString() + "&multiupload=true");

                // Set file parameters
                opts.set("file_size_limit", fileSizeLimit);
                opts.set("file_types", fileTypes);
                opts.set("file_types_description", fileTypesDescription);
                opts.set("file_upload_limit", fileUploadLimit);
                opts.set("file_queue_limit", fileQueueLimit);

                // Set debug mode
                opts.set("debug", false);
                uri = client.getThemeUri();
                // Appearance properties
                opts.set("button_image_url", uri + (uri.endsWith("/") ? "" : "/") +
                        "images/selectfiles.png");
                opts.set("button_width", width);
                opts.set("button_height", height);
                opts.set("button_placeholder_id", uploadButton.getId());
                opts.set("button_text_left_padding", "22");
                opts.set("button_text_top_padding", "4");
                opts.set("button_text", "<span class=\"swfupload\">" + caption + "</span>");
                opts.set("button_text_style",
                        ".swfupload {font-size: 12px; font-family: verdana,Tahoma,sans-serif; }");

                // Add event handlers
                applyJsObjectByName(opts, "file_dialog_complete_handler", "fileDialogComplete");

                // Add error handler
                String notifierId = "UploadErrorHandler_" + paintableId;
                addErrorHandler(notifierId);
                applyJsObjectByName(opts, "upload_error_handler", notifierId);
                applyJsObjectByName(opts, "file_queue_error_handler", notifierId);

                // Add complete handler                
                String refresherId = "MultiUploadRefresher_" + paintableId;
                addRefresher(refresherId);
                applyJsObjectByName(opts, "queue_complete_handler", refresherId);

                showUpload("varUpload_" + paintableId, opts);
            }
        });
    }

    protected void injectJs() {
        if (!ResourcesLoader.injectJs(null, client.getAppUri(), "/js/swfupload.js")) {
            ResourcesLoader.injectJs(null, client.getAppUri(), "/js/swfupload.queue.js");
            ResourcesLoader.injectJs(null, client.getAppUri(), "/js/swfupload.handlers.js");
        }
    }

    public void refreshServerSide() {
        client.updateVariable(paintableId, "queueUploadComplete", 1, true);
    }

    public void errorNotify(String file, String message, int errorCode) {
        client.updateVariable(paintableId, "uploadError", new String[]{file, message, String.valueOf(errorCode)}, true);
    }

    private native void addErrorHandler(String optionName)/*-{
        var swfu = this;
        $wnd[optionName] = function uploadError(file, errorCode, message){
            swfu.@com.haulmont.cuba.toolkit.gwt.client.swfupload.VSwfUpload::errorNotify(Ljava/lang/String;Ljava/lang/String;I)(file.name,message,errorCode);
        }; 
    }-*/;

    private native void addRefresher(String optionName)/*-{
        var swfu = this;
        $wnd[optionName] = function(numFilesUploaded){
            swfu.@com.haulmont.cuba.toolkit.gwt.client.swfupload.VSwfUpload::refreshServerSide()();
        };
    }-*/;

    private static native void applyJsObjectByName(Options opts, String propertyName, String objectName) /*-{
        opts[propertyName] = $wnd[objectName];
    }-*/;

    private static native void initPrototype() /*-{
        $wnd.initSWFUploadPrototype();
    }-*/;

    private static native void showUpload(String varName, Options opts) /*-{
        $wnd[varName] = $wnd.swfUploadHelper.create(opts);
    }-*/;

    public static class Options extends Properties {

        public static Options create() {
            return JavaScriptObject.createObject().cast();
        }

        protected Options() {
        }
    }
}
