/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fileupload;

import com.vaadin.shared.communication.ClientRpc;

/**
 * @author artamonov
 */
public interface CubaFileUploadClientRpc extends ClientRpc {

    void continueUploading();
}