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

package spec.cuba.web.components.grouptable

import com.haulmont.cuba.gui.components.GroupTable
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.grouptable.screens.GroupTableLoadColumnsByIncludeScreen

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class GroupTableLoadColumnsByIncludeTest extends UiScreenSpec {

    void setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        exportScreensPackages(['spec.cuba.web.components.grouptable.screens'])
    }

    def cleanup() {
        TestServiceProxy.clear()

        resetScreensConfig()
    }

    def "load column by includeAll"() {
        def screens = vaadinUi.screens
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def groupTableScreen = screens.create(GroupTableLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def groupTable = groupTableScreen.getWindow().getComponentNN("groupTableAll") as GroupTable
        def columnList = groupTable.getColumns()

        then:
        columnList.size() == 4
    }

    def "load column by includeAll and includeSystem"() {
        def screens = vaadinUi.screens
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def groupTableScreen = screens.create(GroupTableLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def groupTable = groupTableScreen.getWindow().getComponentNN("groupTableSystem") as GroupTable
        def columnList = groupTable.getColumns()

        then:
        columnList.size() == 12
    }

    def "exclude system and other properties"() {
        def screens = vaadinUi.screens
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def groupTableScreen = screens.create(GroupTableLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def groupTable = groupTableScreen.getWindow().getComponentNN("groupTableExclude") as GroupTable
        def columnList = groupTable.getColumns()

        then:
        columnList.size() == 10

        groupTable.getColumn("address") == null
        groupTable.getColumn("createTs") == null
    }

    def "load columns by view"() {
        def screens = vaadinUi.screens
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def groupTableScreen = screens.create(GroupTableLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def groupTable = groupTableScreen.getWindow().getComponentNN("groupTableView") as GroupTable
        def columnList = groupTable.getColumns()

        then:
        columnList.size() == 4
    }

    def "entity with embedded property"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def groupTableScreen = screens.create(GroupTableLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def groupTable = groupTableScreen.getWindow().getComponentNN("groupTableEmb") as GroupTable
        def columnList = groupTable.getColumns()

        then:
        columnList.size() == 6

        groupTable.getColumn("address.city") != null
        groupTable.getColumn("address.zip") != null
        groupTable.getColumn("address") != null
    }

    def "grouping and overriding columns"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def groupTableScreen = screens.create(GroupTableLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def groupTable = groupTableScreen.getWindow().getComponentNN("groupTableGrouping") as GroupTable
        def columnList = groupTable.getColumns()

        then:
        columnList.size() == 4

        groupTable.getColumn("address").isGroupAllowed()
        !groupTable.getColumn("name").isSortable()
    }

    def "group table with non persistent entity"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def groupTableScreen = screens.create(GroupTableLoadColumnsByIncludeScreen)
        groupTableScreen.show()

        when:
        def groupTable = groupTableScreen.getWindow().getComponentNN("groupTableNonPersist") as GroupTable
        def columnList = groupTable.getColumns()

        then:
        columnList.size() == 4

        groupTable.getColumn("isFragile") == null
    }
}
