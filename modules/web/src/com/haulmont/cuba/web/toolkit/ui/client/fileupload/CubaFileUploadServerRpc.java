/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fileupload;

import com.vaadin.shared.communication.ServerRpc;

/**
 * @author artamonov
 * @version $Id$
 */
public interface CubaFileUploadServerRpc extends ServerRpc {

    void fileSizeLimitExceeded(String fileName);

    void queueUploadFinished();
}