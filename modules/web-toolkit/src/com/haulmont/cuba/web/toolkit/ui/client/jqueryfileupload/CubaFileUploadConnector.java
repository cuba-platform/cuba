/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload;

import com.haulmont.cuba.web.toolkit.ui.CubaFileUpload;
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
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (stateChangeEvent.hasPropertyChanged("caption")
                || stateChangeEvent.hasPropertyChanged("captionAsHtml")) {
            VCaption.setCaptionText(getWidget().captionElement, getState());
        }

        if (stateChangeEvent.hasPropertyChanged("resources")) {
            if (getWidget().icon != null) {
                getWidget().buttonWrap.removeChild(getWidget().icon.getElement());
                getWidget().icon = null;
            }
            Icon icon = getIcon();
            if (icon != null) {
                getWidget().icon = icon;
                if (getState().iconAltText != null) {
                    icon.setAlternateText(getState().iconAltText);
                } else {
                    icon.setAlternateText("");
                }

                getWidget().buttonWrap.insertBefore(icon.getElement(),
                        getWidget().captionElement);
            }
        }

        if (stateChangeEvent.hasPropertyChanged("multiSelect")) {
            getWidget().setMultiSelect(getState().multiSelect);
        }

        if (stateChangeEvent.hasPropertyChanged("iconAltText")) {
            if (getWidget().icon != null) {
                Icon icon = getWidget().icon;
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