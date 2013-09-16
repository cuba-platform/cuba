/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import com.google.gwt.core.client.GWT;
import com.haulmont.cuba.web.toolkit.ui.CubaMultiUpload;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(value = CubaMultiUpload.class, loadStyle = Connect.LoadStyle.LAZY)
public class CubaMultiUploadConnector extends AbstractComponentConnector implements Paintable {

    public static final String BASE_RESOURCES_PATH = "/../../resources/swfupload/";

    private boolean initialized = false;

    @Override
    public CubaMultiUploadWidget getWidget() {
        return (CubaMultiUploadWidget) super.getWidget();
    }

    @Override
    public CubaMultiUploadState getState() {
        return (CubaMultiUploadState) super.getState();
    }

    @Override
    protected CubaMultiUploadWidget createWidget() {
        CubaMultiUploadWidget uploadWidget = GWT.create(CubaMultiUploadWidget.class);
        uploadWidget.bootstrapFailureHandler = new CubaMultiUploadWidget.BootstrapFailureHandler() {
            @Override
            public void resourceLoadFailed() {
                getRpcProxy(CubaMultiUploadServerRpc.class).resourceLoadingFailed();
            }

            @Override
            public void flashNotInstalled() {
                getRpcProxy(CubaMultiUploadServerRpc.class).flashNotInstalled();
            }
        };
        uploadWidget.notificationHandler = new CubaMultiUploadWidget.NotificationHandler() {
            @Override
            public void error(String fileName, String message, int code) {
                getRpcProxy(CubaMultiUploadServerRpc.class).uploadError(fileName, message, code);
            }

            @Override
            public void queueUploadComplete() {
                getRpcProxy(CubaMultiUploadServerRpc.class).queueUploadCompleted();
            }
        };
        return uploadWidget;
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (!isRealUpdate(uidl)) {
            return;
        }

        if (!initialized) {
            initialized = true;

            String themeUri = getConnection().getThemeUri();
            String appVersion = getConnection().getConfiguration().getApplicationVersion();

            CubaMultiUploadWidget widget = getWidget();

            widget.themeName = getConnection().getConfiguration().getThemeName();
            widget.baseResourcesUri = themeUri + BASE_RESOURCES_PATH;
            widget.resourcesVersion = appVersion;
            widget.buttonImageUri = getResourceUrl(CubaMultiUploadState.BUTTON_IMAGE_KEY);

            widget.targetUrl = client.translateVaadinUri(uidl.getStringVariable("action"));

            CubaMultiUploadState state = getState();

            widget.buttonCaption = state.buttonCaption;
            widget.buttonHeight = state.buttonHeight;
            widget.buttonWidth = state.buttonWidth;

            widget.queueSizeLimit = state.queueSizeLimit;
            widget.fileSizeLimit = state.fileSizeLimit;
            widget.queueUploadLimit = state.queueUploadLimit;

            widget.fileTypes = state.fileTypes;
            widget.fileTypesDescription = state.fileTypesDescription;
            widget.jsessionId = state.jsessionId;

            widget.initComponent(getConnectorId());
        }
    }
}