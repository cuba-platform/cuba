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

package com.haulmont.cuba.gui.export;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Resources;

import java.io.InputStream;

/**
 * DataProvider for application resources
 *
 */
public class ResourceDataProvider implements ExportDataProvider {

    private String resourcePath;

    public ResourceDataProvider(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public InputStream provide() {
        Resources resources = AppBeans.get(Resources.NAME);
        return resources.getResourceAsStream(resourcePath);
    }
}