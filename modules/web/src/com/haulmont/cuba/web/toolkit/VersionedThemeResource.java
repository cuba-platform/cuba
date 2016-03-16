/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit;

import com.haulmont.cuba.web.App;
import com.vaadin.server.ThemeResource;
import com.vaadin.util.FileTypeResolver;

/**
 * Web resource with versioning support
 *
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