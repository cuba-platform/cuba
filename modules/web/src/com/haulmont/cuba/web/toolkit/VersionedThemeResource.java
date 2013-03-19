/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit;

import com.haulmont.cuba.web.App;
import com.vaadin.server.ThemeResource;
import com.vaadin.util.FileTypeResolver;

/**
 * @author artamonov
 * @version $Id$
 */
public class VersionedThemeResource extends ThemeResource {

    /**
     * Creates a resource.
     *
     * @param resourceId the Id of the resource.
     */
    public VersionedThemeResource(String resourceId) {
        super(resourceId);
    }

    @Override
    public String getResourceId() {
        return super.getResourceId() + "?v=" + App.getInstance().getWebResourceTimestamp();
    }

    @Override
    public String getMIMEType() {
        return FileTypeResolver.getMIMEType(super.getResourceId());
    }
}