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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VNotification;
import com.vaadin.shared.Position;
import freemarker.template.utility.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 */
public class CubaFileUploadWidget extends FlowPanel implements Focusable {

    public static final String DEFAULT_CLASSNAME = "cuba-fileupload";

    protected VButton submitButton;

    protected JQueryFileUploadOverlay fileUpload;
    protected CubaFileUploadProgressWindow progressWindow;

    protected String unableToUploadFileMessage;
    protected String progressWindowCaption;
    protected String cancelButtonCaption;

    protected long fileSizeLimit = -1;
    protected Set<String> permittedExtensions;
    protected FilePermissionsHandler filePermissionsHandler;

    protected QueueUploadListener queueUploadListener;
    protected FileUploadedListener fileUploadedListener;

    protected boolean enabled;

    public CubaFileUploadWidget() {
        submitButton = new VButton();
        submitButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                fireNativeClick(getFileInputElement());
            }
        });
        add(submitButton);
        submitButton.setTabIndex(-1);

        setStyleName(DEFAULT_CLASSNAME);

        Element inputElement = Document.get().createFileInputElement();
        inputElement.setAttribute("name", "files[]");
        inputElement.setAttribute("title", " ");
        listenToFocusEvents(inputElement);

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

                if (hasInvalidExtension(name)) {
                    if (filePermissionsHandler != null) {
                        filePermissionsHandler.fileExtensionNotAllowed(name);
                    }
                    return false;
                }

                return true;
            }

            protected boolean hasInvalidExtension(String name) {
                if (permittedExtensions != null && !permittedExtensions.isEmpty()) {
                    if (name.lastIndexOf(".") > 0) {
                        String fileExtension = name.substring(name.lastIndexOf("."), name.length());
                        return !permittedExtensions.contains(fileExtension.toLowerCase());
                    }
                    return true;
                }
                return false;
            }

            @Override
            protected void queueUploadStart() {
                // listen to events of new input element
                listenToFocusEvents(getFileInputElement());

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

                        if (queueUploadListener != null) {
                            queueUploadListener.uploadFinished();
                        }
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
            protected void fileUploadSucceed(String fileName) {
                if (fileUploadedListener != null) {
                    fileUploadedListener.fileUploaded(fileName);
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

                getFileInputElement().focus();

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
                        String fileName = "";
                        if (progressWindow != null) {
                            fileName = progressWindow.getCurrentFileName();
                        }

                        String message = "<h1>" + WidgetUtil.escapeHTML(unableToUploadFileMessage.replace("%s", fileName)) + "</h1>";
                        notification.show(message, Position.MIDDLE_CENTER, "error");
                    }

                    canceled = true;
                    cancelUploading();
                }
            }
        };
    }

    protected void listenToFocusEvents(Element inputElement) {
        DOM.sinkEvents(inputElement, Event.ONFOCUS | Event.ONBLUR);
    }

    // Due to jquery file upload behavior we need to get input element from DOM
    protected Element getFileInputElement() {
        return getElement().getElementsByTagName("input").getItem(0);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        int type = DOM.eventGetType(event);
        if (getFileInputElement().isOrHasChild(Element.as(event.getEventTarget()))) {
            if (type == Event.ONFOCUS) {
                submitButton.addStyleDependentName("focus");
            } else if (type == Event.ONBLUR) {
                submitButton.removeStyleDependentName("focus");
            }
        }
    }

    protected static native void fireNativeClick(Element element)
    /*-{
        element.click();
    }-*/;

    public void setMultiSelect(boolean multiple) {
        if (multiple) {
            getFileInputElement().setAttribute("multiple", "");
        } else {
            getFileInputElement().removeAttribute("multiple");
        }
    }

    public void setUploadUrl(String uploadUrl) {
        fileUpload.setUploadUrl(uploadUrl);
    }

    public void setAccept(String accept) {
        if (accept != null) {
            getFileInputElement().setAttribute("accept", accept);
        } else {
            getFileInputElement().removeAttribute("accept");
        }
    }

    public void disableUpload() {
        setEnabledForSubmitButton(false);
        // Cannot disable the fileupload while submitting or the file won't
        // be submitted at all
        getFileInputElement().setAttribute("disabled", "disabled");
        enabled = false;
    }

    public void enableUpload() {
        setEnabledForSubmitButton(true);
        getFileInputElement().removeAttribute("disabled");
        enabled = true;
    }

    protected void setEnabledForSubmitButton(boolean enabled) {
        submitButton.setEnabled(enabled);
        submitButton.setStyleName(StyleConstants.DISABLED, !enabled);
    }

    public VButton getSubmitButton() {
        return submitButton;
    }

    public void cancelAllUploads() {
        fileUpload.cancelUploading();
    }

    public void continueUploading() {
        fileUpload.continueUploading();
    }

    @Override
    public int getTabIndex() {
        return getFileInputElement().getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
    }

    @Override
    public void setFocus(boolean focused) {
        if (focused) {
            getFileInputElement().focus();
        } else {
            getFileInputElement().blur();
        }
    }

    @Override
    public void setTabIndex(int index) {
        getFileInputElement().setTabIndex(index);
    }

    public interface FilePermissionsHandler {
        void fileSizeLimitExceeded(String filename);
        void fileExtensionNotAllowed(String filename);
    }

    public interface QueueUploadListener {
        void uploadFinished();
    }

    public interface FileUploadedListener {
        void fileUploaded(String fileName);
    }
}