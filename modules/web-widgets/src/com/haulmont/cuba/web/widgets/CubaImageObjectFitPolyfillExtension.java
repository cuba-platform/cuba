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

package com.haulmont.cuba.web.widgets;

import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.ui.UI;

@WebJarResource("object-fit-images:ofi.min.js")
public class CubaImageObjectFitPolyfillExtension extends AbstractExtension {

    public void extend(CubaImage image) {
        super.extend(image);
    }

    public static CubaImageObjectFitPolyfillExtension get(UI ui) {
        CubaImageObjectFitPolyfillExtension extension = null;

        // Search singleton extension
        for (Extension uiExtension : ui.getExtensions()) {
            if (uiExtension instanceof CubaImageObjectFitPolyfillExtension) {
                extension = (CubaImageObjectFitPolyfillExtension) uiExtension;
                break;
            }
        }

        // Create new extension if not found
        if (extension == null) {
            extension = new CubaImageObjectFitPolyfillExtension();
            extension.extend(ui);
        }

        return extension;
    }
}