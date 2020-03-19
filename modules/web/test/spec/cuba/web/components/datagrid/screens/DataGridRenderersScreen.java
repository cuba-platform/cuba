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

package spec.cuba.web.components.datagrid.screens;

import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.screen.LoadDataBeforeShow;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.web.testmodel.sample.RendererEntity;

import javax.inject.Inject;

@UiController
@UiDescriptor("datagrid-renderers-screen.xml")
@LoadDataBeforeShow
public class DataGridRenderersScreen extends Screen {
    @Inject
    protected DataGrid<RendererEntity> renderersDataGrid;

    @Subscribe
    public void onInit(InitEvent event) {
        DataGrid.ButtonRenderer<RendererEntity> buttonRenderer = renderersDataGrid.createRenderer(DataGrid.ButtonRenderer.class);
        buttonRenderer.setNullRepresentation("buttonRenderer");
        renderersDataGrid.getColumn("button").setRenderer(buttonRenderer);

        DataGrid.ClickableTextRenderer<RendererEntity> clickableTextRenderer = renderersDataGrid.createRenderer(DataGrid.ClickableTextRenderer.class);
        clickableTextRenderer.setNullRepresentation("clickableTextRenderer");
        renderersDataGrid.getColumn("clickableText").setRenderer(clickableTextRenderer);

        DataGrid.ImageRenderer<RendererEntity> imageRenderer = renderersDataGrid.createRenderer(DataGrid.ImageRenderer.class);
        renderersDataGrid.getColumn("image").setRenderer(imageRenderer);
    }
}
