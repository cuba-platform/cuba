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

package com.haulmont.cuba.web.widgets.renderers;

import com.vaadin.v7.ui.Grid;
import elemental.json.JsonValue;
import org.apache.commons.lang.BooleanUtils;

public class CubaCheckBoxRenderer extends Grid.AbstractRenderer<Boolean> {

    public CubaCheckBoxRenderer() {
        super(Boolean.class, null);
    }

    @Override
    public JsonValue encode(Boolean value) {
        return super.encode(BooleanUtils.isTrue(value));
    }
}