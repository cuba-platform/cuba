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
import com.haulmont.cuba.web.gui.components.WebDataGrid.AbstractRenderer;
import com.haulmont.cuba.web.widgets.renderers.CubaProgressBarRenderer;

/**
 * A renderer that represents a double values as a graphical progress bar.
 */
public class WebProgressBarRenderer extends AbstractRenderer<Double> implements DataGrid.ProgressBarRenderer {

    @Override
    protected CubaProgressBarRenderer createImplementation() {
        return new CubaProgressBarRenderer();
    }
}