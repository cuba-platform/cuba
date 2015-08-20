/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VNotification;
import com.vaadin.shared.Position;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaFileUploadWidget extends FlowPanel {

    public static final String DEFAULT_CLASSNAME = "cuba-fileupload";

    protected Element inputElement;

    protected VButton submitButton;

    protected JQueryFileUploadOverlay fileUpload;
    protected CubaFileUploadProgressWindow progressWindow;

    protected String unableToUploadFileMessage;
    protected String progressWindowCaption;
    protected String cancelButtonCaption;

    protected int fileSizeLimit = -1;
    protected FilePermissionsHandler filePermissionsHandler;

    protected QueueUploadListener queueUploadListener;

    protected boolean enabled;

    public CubaFileUploadWidget() {
        submitButton = new VButton();
        submitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireNativeClick(inputElement);
            }
        });
        add(submitButton);
        submitButton.setTabIndex(-1);

        setStyleName(DEFAULT_CLASSNAME);

        if (inputElement != null) {
            getElement().removeChild(inputElement);
        }

        inputElement = Document.get().createFileInputElement();
        inputElement.setAttribute("name", "files[]");
        DOM.sinkEvents(inputElement, Event.ONFOCUS);

        getElement().appendChild(inputElement);

        fileUpload = new JQueryFileUploadOverlay(inputElement) {
            protected boolean canceled = false;

            @Override
            protected boolean isValidFile(String name, double size) {
                if (fileSizeLimit > 0 && size > fileSizeLimit) {
                    if (filePermissionsHandler != null) {
                        filePermissionsHandler.fileSizeLimitExceeded(name);
                    }
                    return false;
                }

                return true;
            }

            @Override
            protected void queueUploadStart() {
                progressWindow = new CubaFileUploadProgressWindow();
                progressWindow.setOwner(CubaFileUploadWidget.this);
                progressWindow.addStyleName(getStylePrimaryName() + "-progresswindow");

                progressWindow.setVaadinModality(true);
                progressWindow.setDraggable(true);
                progressWindow.setResizable(false);
                progressWindow.setClosable(true);

                progressWindow.setCaption(progressWindowCaption);
                progressWindow.setCancelButtonCaption(cancelButtonCaption);

                progressWindow.closeListener = new CubaFileUploadProgressWindow.CloseListener() {
                    @Override
                    public void onClose() {
                        canceled = true;
                        // null progress to prevent repeated hide() call inside cancelUploading
                        progressWindow = null;

                        cancelUploading();
                    }
                };

                progressWindow.setVisible(false);
                progressWindow.show();
                progressWindow.center();
                progressWindow.setVisible(true);

                canceled = false;
            }

            @Override
            protected void fileUploadStart(String fileName) {
                if (progressWindow != null) {
                    progressWindow.setCurrentFileName(fileName);
                }
            }

            @Override
            protected void uploadProgress(double loaded, double total) {
                if (progressWindow != null) {
                    float ratio = (float) (loaded / total);
                    progressWindow.setProgress(ratio);
                }
            }

            @Override
            protected void queueUploadStop() {
                if (progressWindow != null) {
                    progressWindow.hide();
                    progressWindow = null;
                }

                if (queueUploadListener != null) {
                    queueUploadListener.uploadFinished();
                }
            }

            @Override
            protected void uploadFailed(String textStatus, String errorThrown) {
                if (!canceled) {
                    if (unableToUploadFileMessage != null) {
                        // show notification without server round trip, server may be unreachable
                        VNotification notification = VNotification.createNotification(-1, CubaFileUploadWidget.this);
                        String message = "<h1>" + WidgetUtil.escapeHTML(unableToUploadFileMessage) + "</h1>";
                        notification.show(message, Position.MIDDLE_CENTER, "error");
                    }

                    canceled = true;
                    cancelUploading();
                }
            }
        };
    }

    private static native void fireNativeClick(Element element)
    /*-{
        element.click();
    }-*/;

    public void setMultiSelect(boolean multiple) {
        if (multiple) {
            inputElement.setAttribute("multiple", "");
        } else {
            inputElement.removeAttribute("multiple");
        }
    }

    public void setUploadUrl(String uploadUrl) {
        fileUpload.setUploadUrl(uploadUrl);
    }

    public void setAccept(String accept) {
        if (accept != null) {
            inputElement.setAttribute("accept", accept);
        } else {
            inputElement.removeAttribute("accept");
        }
    }

    public void disableUpload() {
        setEnabledForSubmitButton(false);
        // Cannot disable the fileupload while submitting or the file won't
        // be submitted at all
        inputElement.setAttribute("disabled", "disabled");
        enabled = false;
    }

    public void enableUpload() {
        setEnabledForSubmitButton(true);
        inputElement.removeAttribute("disabled");
        enabled = true;
    }

    protected void setEnabledForSubmitButton(boolean enabled) {
        submitButton.setEnabled(enabled);
        submitButton.setStyleName(StyleConstants.DISABLED, !enabled);
    }

    public interface FilePermissionsHandler {

        void fileSizeLimitExceeded(String filename);
    }

    public interface QueueUploadListener {

        void uploadFinished();
    }
}