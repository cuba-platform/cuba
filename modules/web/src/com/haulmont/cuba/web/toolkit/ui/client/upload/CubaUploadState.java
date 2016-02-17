/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.upload;

import com.vaadin.shared.annotations.NoLayout;
import com.vaadin.shared.ui.upload.UploadState;

/**
 * @author artamonov
 */
public class CubaUploadState extends UploadState {

    // permitted mime types, comma separated
    @NoLayout
    public String accept = null;
}