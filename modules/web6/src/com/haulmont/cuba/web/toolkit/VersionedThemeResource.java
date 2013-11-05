/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit;

import com.haulmont.cuba.web.App;
import com.vaadin.service.FileTypeResolver;
import com.vaadin.terminal.ThemeResource;

/**
 * Web resource with versioning support
 *
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