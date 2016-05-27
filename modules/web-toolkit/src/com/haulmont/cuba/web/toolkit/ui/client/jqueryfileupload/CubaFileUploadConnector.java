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

import com.haulmont.cuba.web.toolkit.ui.CubaFileUpload;
import com.haulmont.cuba.web.toolkit.ui.client.fileupload.CubaFileUploadClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.fileupload.CubaFileUploadServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.fileupload.CubaFileUploadState;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.VCaption;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.shared.ui.Connect;

@Connect(CubaFileUpload.class)
public class CubaFileUploadConnector extends AbstractComponentConnector implements Paintable {

    public CubaFileUploadConnector() {
        registerRpc(CubaFileUploadClientRpc.class, new CubaFileUploadClientRpc() {
            @Override
            public void continueUploading() {
                // check if attached
                if (getWidget().isAttached()) {
                    getWidget().continueUploading();
                }
            }
        });
    }

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    public CubaFileUploadWidget getWidget() {
        return (CubaFileUploadWidget) super.getWidget();
    }

    @Override
    public CubaFileUploadState getState() {
        return (CubaFileUploadState) super.getState();
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        getWidget().cancelAllUploads();
    }

    @Override
    protected void init() {
        super.init();

        getWidget().filePermissionsHandler = new CubaFileUploadWidget.FilePermissionsHandler() {
            @Override
            public void fileSizeLimitExceeded(String filename) {
                getRpcProxy(CubaFileUploadServerRpc.class).fileSizeLimitExceeded(filename);
            }

            @Override
            public void fileExtensionNotAllowed(String filename) {
                getRpcProxy(CubaFileUploadServerRpc.class).fileExtensionNotAllowed(filename);
            }
        };

        getWidget().queueUploadListener = new CubaFileUploadWidget.QueueUploadListener() {
            @Override
            public void uploadFinished() {
                // send events to server only if widget is still attached to UI
                if (getWidget().isAttached()) {
                    getRpcProxy(CubaFileUploadServerRpc.class).queueUploadFinished();
                }
            }
        };

        getWidget().fileUploadedListener = new CubaFileUploadWidget.FileUploadedListener() {
            @Override
            public void fileUploaded(String fileName) {
                // send events to server only if widget is still attached to UI
                if (getWidget().isAttached()) {
                    getRpcProxy(CubaFileUploadServerRpc.class).fileUploaded(fileName);
                }
            }
        };
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("caption")
                || stateChangeEvent.hasPropertyChanged("captionAsHtml")) {
            VCaption.setCaptionText(getWidget().submitButton.captionElement, getState());

            if ("".equals(getState().caption) || getState().caption == null) {
                getWidget().submitButton.addStyleDependentName("empty-caption");
            } else {
                getWidget().submitButton.removeStyleDependentName("empty-caption");
            }
        }

        if (stateChangeEvent.hasPropertyChanged("resources")) {
            if (getWidget().submitButton.icon != null) {
                getWidget().submitButton.wrapper.removeChild(getWidget().submitButton.icon.getElement());
                getWidget().submitButton.icon = null;
            }
            Icon icon = getIcon();
            if (icon != null) {
                getWidget().submitButton.icon = icon;
                if (getState().iconAltText != null) {
                    icon.setAlternateText(getState().iconAltText);
                } else {
                    icon.setAlternateText("");
                }

                getWidget().submitButton.wrapper.insertBefore(icon.getElement(),
                        getWidget().submitButton.captionElement);
            }
        }

        if (stateChangeEvent.hasPropertyChanged("multiSelect")) {
            getWidget().setMultiSelect(getState().multiSelect);
        }

        if (stateChangeEvent.hasPropertyChanged("iconAltText")) {
            if (getWidget().submitButton.icon != null) {
                Icon icon = getWidget().submitButton.icon;
                if (getState().iconAltText != null) {
                    icon.setAlternateText(getState().iconAltText);
                } else {
                    icon.setAlternateText("");
                }
            }
        }

        if (stateChangeEvent.hasPropertyChanged("progressWindowCaption")) {
            getWidget().progressWindowCaption = getState().progressWindowCaption;
        }

        if (stateChangeEvent.hasPropertyChanged("cancelButtonCaption")) {
            getWidget().cancelButtonCaption = getState().cancelButtonCaption;
        }

        if (stateChangeEvent.hasPropertyChanged("unableToUploadFileMessage")) {
            getWidget().unableToUploadFileMessage = getState().unableToUploadFileMessage;
        }

        if (stateChangeEvent.hasPropertyChanged("accept")) {
            getWidget().setAccept(getState().accept);
        }

        if (stateChangeEvent.hasPropertyChanged("fileSizeLimit")) {
            getWidget().fileSizeLimit = getState().fileSizeLimit;
        }

        if (stateChangeEvent.hasPropertyChanged("permittedExtensions")) {
            getWidget().permittedExtensions = getState().permittedExtensions;
        }

        if (!isEnabled() || isReadOnly()) {
            getWidget().disableUpload();
        } else {
            getWidget().enableUpload();
        }
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (!isRealUpdate(uidl)) {
            return;
        }

        final String uploadUrl = client.translateVaadinUri(uidl
                .getStringVariable("uploadUrl"));

        getWidget().setUploadUrl(uploadUrl);
    }
}