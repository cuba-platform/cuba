/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

/**
 * A resource that is located in classpath with the given <code>path</code>.
 * <p>
 * For obtaining resources the {@link com.haulmont.cuba.core.global.Resources} infrastructure interface is using.
 * <p>
 * For example if your resource is located in the web module and has the following path: "com/company/app/web/images/image.png",
 * ClassPathResource's path should be: "/com/company/app/web/images/image.png".
 */
public interface ClasspathResource extends Resource, ResourceView.HasMimeType, ResourceView.HasStreamSettings {
    ClasspathResource setPath(String path);

    String getPath();
}
