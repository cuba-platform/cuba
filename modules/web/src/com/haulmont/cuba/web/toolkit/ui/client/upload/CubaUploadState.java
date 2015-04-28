/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.upload;

import com.vaadin.shared.AbstractComponentState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaUploadState extends AbstractComponentState {

    // permitted mime types, comma separated
    public String accept = null;
}