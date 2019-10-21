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
import com.haulmont.cuba.gui.screen.OpenMode
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.datagrid.screens.DataGridLoadColumnsByIncludeScreen

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class DataGridLoadColumnsByIncludeTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.datagrid.screens'])
    }

    def "load columns by includeAll"() {
        showMainWindow()

        def dataGridScreen = screens.create(DataGridLoadColumnsByIncludeScreen)
        dataGridScreen.show()

        when:
        def dataGrid = dataGridScreen.getWindow().getComponentNN("dataGridAll") as DataGrid
        def columnList = dataGrid.getColumns()

        then:
        columnList.size() == 4
    }

    def "load columns by includeAll with system properties"() {
        def screens = vaadinUi.screens
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridLoadColumnsByIncludeScreen)
        dataGridScreen.show()

        when:
        def dataGrid = dataGridScreen.getWindow().getComponentNN("dataGridSystem") as DataGrid
        def columnList = dataGrid.getColumns()

        then:
        columnList.size() == 12
    }

    def "exclude system and other properties"() {
        def screens = vaadinUi.screens
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def dataGridScreen = screens.create(DataGridLoadColumnsByIncludeScreen)
        dataGridScreen.show()

        when:
        def dataGrid = dataGridScreen.getWindow().getComponentNN("dataGridExclude") as DataGrid
        def columnList = dataGrid.getColumns()

        then:
        columnList.size() == 10

        dataGrid.getColumn("address") == null
        dataGrid.getColumn("createTs") == null
    }

    def "entity with embedded property"() {
        showMainWindow()

        def dataGridScreen = screens.create(DataGridLoadColumnsByIncludeScreen)
        dataGridScreen.show()

        when:
        def dataGrid = dataGridScreen.getWindow().getComponentNN("dataGridEmb") as DataGrid
        def columnList = dataGrid.getColumns()

        then:
        columnList.size() == 14

        dataGrid.getColumn("address.city") != null
        dataGrid.getColumn("address.zip") != null
        dataGrid.getColumn("address") != null
    }

    def "overriding columns"() {
        showMainWindow()

        def groupTableScreen = screens.create(DataGridLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def dataGrid = groupTableScreen.getWindow().getComponentNN("dataGridOverriding") as DataGrid
        def columnList = dataGrid.getColumns()

        then:
        columnList.size() == 12

        !dataGrid.getColumn("name").isSortable()
    }

    def "includeAll with non-persistent entity"() {
        showMainWindow()

        def dataGridTableScreen = screens.create(DataGridLoadColumnsByIncludeScreen)
        dataGridTableScreen.show()

        when:
        def dataGrid = dataGridTableScreen.getWindow().getComponentNN("dataGridNonPersist") as DataGrid
        def columnList = dataGrid.getColumns()

        then:
        columnList.size() == 5

        dataGrid.getColumn("isFragile") == null
    }

    def "load columns without view"() {
        showMainWindow()

        def dataGridTableScreen = screens.create(DataGridLoadColumnsByIncludeScreen)
        dataGridTableScreen.show()

        when:
        def dataGrid = dataGridTableScreen.getWindow().getComponentNN("dataGridWithoutView") as DataGrid
        def columnList = dataGrid.getColumns()

        then:
        columnList.size() == 12
    }
}
