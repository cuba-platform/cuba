/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import com.haulmont.cuba.web.toolkit.ui.CubaMultiUpload;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaMultiUpload.class)
public class CubaMultiUploadConnector extends AbstractComponentConnector implements Paintable {

    protected boolean initialized = false;

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    public CubaMultiUploadWidget getWidget() {
        return (CubaMultiUploadWidget) super.getWidget();
    }

    @Override
    public CubaMultiUploadState getState() {
        return (CubaMultiUploadState) super.getState();
    }

    @Override
    protected void init() {
        super.init();

        getWidget().bootstrapFailureHandler = new CubaMultiUploadWidget.BootstrapFailureHandler() {
            @Override
            public void resourceLoadFailed() {
                getRpcProxy(CubaMultiUploadServerRpc.class).resourceLoadingFailed();
            }

            @Override
            public void flashNotInstalled() {
                getRpcProxy(CubaMultiUploadServerRpc.class).flashNotInstalled();
            }
        };
        getWidget().notificationHandler = new CubaMultiUploadWidget.NotificationHandler() {
            @Override
            public void error(String fileName, String message, int code) {
                getRpcProxy(CubaMultiUploadServerRpc.class).uploadError(fileName, message, code);
            }

            @Override
            public void queueUploadComplete() {
                getRpcProxy(CubaMultiUploadServerRpc.class).queueUploadCompleted();
            }
        };
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (!isRealUpdate(uidl)) {
            return;
        }

        if (!initialized) {
            initialized = true;

            String appVersion = getConnection().getConfiguration().getApplicationVersion();

            CubaMultiUploadWidget widget = getWidget();

            widget.themeName = getConnection().getUIConnector().getActiveTheme();

            widget.resourcesVersion = appVersion;
            widget.buttonImageUri = getResourceUrl(CubaMultiUploadState.BUTTON_IMAGE_KEY);
            widget.bootstrapJsUrl = getResourceUrl(CubaMultiUploadState.SWFUPLOAD_BOOTSTRAP_JS_KEY);
            widget.flashUrl = getResourceUrl(CubaMultiUploadState.SWFUPLOAD_FLASH_KEY);

            widget.targetUrl = client.translateVaadinUri(uidl.getStringVariable("action"));

            CubaMultiUploadState state = getState();

            widget.buttonCaption = state.buttonCaption;
            widget.buttonHeight = state.buttonHeight;
            widget.buttonWidth = state.buttonWidth;
            widget.buttonStyles = state.buttonStyles;
            widget.buttonDisabledStyles = state.buttonDisabledStyles;

            widget.buttonTextLeft = state.buttonTextLeft;
            widget.buttonTextTop = state.buttonTextTop;

            widget.queueSizeLimit = state.queueSizeLimit;
            widget.fileSizeLimit = state.fileSizeLimit;
            widget.queueUploadLimit = state.queueUploadLimit;

            widget.fileTypes = state.fileTypes;
            widget.fileTypesDescription = state.fileTypesDescription;
            widget.jsessionId = state.jsessionId;
            widget.buttonEnabled = state.buttonEnabled;

            widget.initComponent(getConnectorId());
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("buttonEnabled")) {
            getWidget().setButtonEnabled(getState().buttonEnabled);
        }
    }
}