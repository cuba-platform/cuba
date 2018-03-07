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

package com.haulmont.cuba.web.widgets.client.upload;

import com.haulmont.cuba.web.widgets.CubaUpload;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.upload.UploadConnector;
import com.vaadin.shared.ui.Connect;

@Deprecated
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
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("accept")) {
            getWidget().setAccept(getState().accept);
        }

        if (stateChangeEvent.hasPropertyChanged("resources")) {
            if (getWidget().submitButton.icon != null) {
                getWidget().submitButton.wrapper.removeChild(getWidget().submitButton.icon.getElement());
                getWidget().submitButton.icon = null;
            }
            Icon icon = getIcon();
            if (icon != null) {
                getWidget().submitButton.icon = icon;

                getWidget().submitButton.wrapper.insertBefore(icon.getElement(),
                        getWidget().submitButton.captionElement);
            }
        }
    }
}