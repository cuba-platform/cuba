/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.upload;

import com.haulmont.cuba.web.toolkit.ui.CubaUpload;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.upload.UploadConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaUpload.class)
public class CubaUploadConnector extends UploadConnector {

    @Override
    public CubaUploadState getState() {
        return (CubaUploadState) super.getState();
    }

    @Override
    public CubaUploadWidget getWidget() {
        return (CubaUploadWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("accept")) {
            getWidget().setAccept(getState().accept);
        }
    }
}