/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.renderers;

import com.vaadin.client.communication.JsonDecoder;
import com.vaadin.client.connectors.grid.AbstractGridRendererConnector;
import com.vaadin.client.metadata.TypeDataStore;
import com.vaadin.client.ui.Icon;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;
import elemental.json.JsonValue;

@Connect(com.haulmont.cuba.web.widgets.renderers.CubaIconRenderer.class)
public class CubaIconRendererConnector extends AbstractGridRendererConnector<Icon> {

    @Override
    public CubaIconRenderer getRenderer() {
        return (CubaIconRenderer) super.getRenderer();
    }

    @Override
    public Icon decode(JsonValue value) {
        URLReference reference = (URLReference) JsonDecoder.decodeValue(
                TypeDataStore.getType(URLReference.class), value, null,
                getConnection());

        return reference != null
                ? getConnection().getIcon(reference.getURL())
                : null;
    }
}
