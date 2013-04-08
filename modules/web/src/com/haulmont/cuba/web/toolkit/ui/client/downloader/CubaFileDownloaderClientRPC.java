/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.downloader;

import com.vaadin.shared.communication.ClientRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaFileDownloaderClientRPC extends ClientRpc {

    void downloadFile(String resourceId);

    void viewDocument(String resourceId);
}