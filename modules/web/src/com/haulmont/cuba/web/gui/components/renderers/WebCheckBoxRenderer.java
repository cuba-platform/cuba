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

package com.haulmont.cuba.web.gui.components.renderers;

import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.web.gui.components.WebDataGrid;
import com.haulmont.cuba.web.widgets.renderers.CubaCheckBoxRenderer;
import com.vaadin.v7.ui.renderers.Renderer;

/**
 * A renderer that represents a boolean values as a graphical check box icons.
 */
public class WebCheckBoxRenderer extends WebDataGrid.AbstractRenderer<Boolean> implements DataGrid.CheckBoxRenderer {

    @Override
    protected Renderer<Boolean> createImplementation() {
        return new CubaCheckBoxRenderer();
    }
}
