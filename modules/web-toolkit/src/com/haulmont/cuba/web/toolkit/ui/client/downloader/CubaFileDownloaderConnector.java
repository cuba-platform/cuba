/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.downloader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaFileDownloader;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaFileDownloader.class)
public class CubaFileDownloaderConnector extends AbstractComponentConnector {

    public CubaFileDownloaderConnector() {
        registerRpc(CubaFileDownloaderClientRPC.class, new CubaFileDownloaderClientRPC() {
            @Override
            public void downloadFile(String resourceId) {
                downloadFileById(resourceId);
            }

            @Override
            public void viewDocument(String resourceId) {
                viewDocumentById(resourceId);
            }
        });
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(Hidden.class);
    }

    public void downloadFileById(String resourceId) {
        final String url = getResourceUrl(resourceId);
        if (url != null && !url.isEmpty()) {
            final IFrameElement iframe = Document.get().createIFrameElement();

            Style style = iframe.getStyle();
            style.setVisibility(Style.Visibility.HIDDEN);
            style.setHeight(0, Style.Unit.PX);
            style.setWidth(0, Style.Unit.PX);

            iframe.setFrameBorder(0);
            iframe.setTabIndex(-1);
            iframe.setSrc(url);
            RootPanel.getBodyElement().appendChild(iframe);

            Timer removeTimer = new Timer() {
                @Override
                public void run() {
                    iframe.removeFromParent();
                }
            };
            removeTimer.schedule(60 * 1000);
        }
    }

    public void viewDocumentById(String resourceId) {
        final String url = getResourceUrl(resourceId);
        if (url != null && !url.isEmpty()) {
            Window.open(url, "_blank", "");
        }
    }
}