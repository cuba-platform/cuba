/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.haulmont.cuba.web.toolkit.ui.CubaFileUpload;
import com.haulmont.cuba.web.toolkit.ui.client.fileupload.CubaFileUploadServerRpc;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.VCaption;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaFileUpload.class)
public class CubaFileUploadConnector extends AbstractComponentConnector implements Paintable {

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