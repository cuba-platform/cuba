/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.multiupload;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaMultiUploadServerRpc extends ServerRpc {

    void resourceLoadingFailed();

    void flashNotInstalled();

    void queueUploadCompleted();

    void uploadError(String fileName, String message, int code);
}