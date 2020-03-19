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

package spec.cuba.web.components.datagrid

import com.haulmont.cuba.gui.components.DataGrid
import com.haulmont.cuba.web.gui.components.renderers.*
import com.haulmont.cuba.web.testmodel.sample.RendererEntity
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.datagrid.screens.DataGridRenderersScreen

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class DataGridRenderersTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.datagrid.screens'])
    }

    def "Renderer is applied for DataGrid column"(String id, Class<DataGrid.Renderer> rendererClass) {
        showMainWindow()

        def dataGridScreen = screens.create(DataGridRenderersScreen)
        dataGridScreen.show()

        when: "Screen is loaded"

        DataGrid<RendererEntity> dataGrid = dataGridScreen.getWindow().getComponent("renderersDataGrid")

        then: "Renderer is applied for DataGrid column"

        dataGrid.getColumn(id).getRenderer().getClass() == rendererClass

        where:

        id              | rendererClass
        "button"        | WebButtonRenderer
        "checkBox"      | WebCheckBoxRenderer
        "clickableText" | WebClickableTextRenderer
        "component"     | WebComponentRenderer
        "date"          | WebDateRenderer
        "icon"          | WebIconRenderer
        "image"         | WebImageRenderer
        "html"          | WebHtmlRenderer
        "localDate"     | WebLocalDateRenderer
        "localDateTime" | WebLocalDateTimeRenderer
        "number"        | WebNumberRenderer
        "progressBar"   | WebProgressBarRenderer
        "text"          | WebTextRenderer
    }

    def "NullRepresentation renderer parameter is applied for DataGrid column"(String id, String nullRepresentation) {
        showMainWindow()

        def dataGridScreen = screens.create(DataGridRenderersScreen)
        dataGridScreen.show()

        when: "Screen is loaded"

        DataGrid<RendererEntity> dataGrid = dataGridScreen.getWindow().getComponent("renderersDataGrid")
        def renderer = dataGrid.getColumn(id).getRenderer()

        then: "NullRepresentation renderer parameter is applied for DataGrid column"

        renderer instanceof DataGrid.HasNullRepresentation
        ((DataGrid.HasNullRepresentation) renderer).getNullRepresentation() == nullRepresentation

        where:

        id              | nullRepresentation
        "button"        | "buttonRenderer"
        "clickableText" | "clickableTextRenderer"
        "date"          | "dateRenderer"
        "html"          | "htmlRenderer"
        "localDate"     | "localDateRenderer"
        "localDateTime" | "localDateTimeRenderer"
        "number"        | "numberRenderer"
        "text"          | "textRenderer"
    }

    def "FormatString renderer parameter is applied for DataGrid column"(String id, String formatString) {
        showMainWindow()

        def dataGridScreen = screens.create(DataGridRenderersScreen)
        dataGridScreen.show()

        when: "Screen is loaded"

        DataGrid<RendererEntity> dataGrid = dataGridScreen.getWindow().getComponent("renderersDataGrid")
        def renderer = dataGrid.getColumn(id).getRenderer()

        then: "FormatString renderer parameter is applied for DataGrid column"

        renderer instanceof DataGrid.HasFormatString
        ((DataGrid.HasFormatString) renderer).getFormatString() == formatString

        where:

        id       | formatString
        "date"   | "yyyy-MM-dd HH:mm:ss"
        "number" | "%f"
    }

    def "FormatPattern renderer parameter is applied for DataGrid column"(String id, String formatPattern) {
        showMainWindow()

        def dataGridScreen = screens.create(DataGridRenderersScreen)
        dataGridScreen.show()

        when: "Screen is loaded"

        DataGrid<RendererEntity> dataGrid = dataGridScreen.getWindow().getComponent("renderersDataGrid")
        def renderer = dataGrid.getColumn(id).getRenderer()

        then: "FormatPattern renderer parameter is applied for DataGrid column"

        renderer instanceof DataGrid.HasDateTimeFormatter
        ((DataGrid.HasDateTimeFormatter) renderer).getFormatPattern() == formatPattern

        where:

        id              | formatPattern
        "localDate"     | "dd/MM/YYYY"
        "localDateTime" | "dd/MM/YYYY HH:mm:ss"
    }
}
