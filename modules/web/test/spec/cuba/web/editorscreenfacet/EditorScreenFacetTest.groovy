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

package spec.cuba.web.editorscreenfacet

import com.haulmont.cuba.gui.builders.EditMode
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.entity.User
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.editorscreenfacet.screens.EditorScreenFacetTestScreen

class EditorScreenFacetTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.editorscreenfacet.screens'])
    }

    def 'EditorScreenFacet is loaded from XML'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(EditorScreenFacetTestScreen)

        when: 'Screen with EditorScreenFacet is opened'

        screenWithFacet.show()

        def editorScreenFacet = screenWithFacet.editorScreenFacet

        then: 'All EditorScreenFacet settings are correctly loaded'

        editorScreenFacet.id == 'editorScreenFacet'
        editorScreenFacet.launchMode == OpenMode.DIALOG
        editorScreenFacet.entityClass == User
        editorScreenFacet.actionTarget == 'action'
        editorScreenFacet.pickerField == screenWithFacet.userField
        editorScreenFacet.listComponent == screenWithFacet.usersTable
        editorScreenFacet.editMode == EditMode.EDIT
        editorScreenFacet.addFirst
    }

    def 'EditorScreenFacet opens editor by entity class'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(EditorScreenFacetTestScreen)
        screenWithFacet.show()

        def editorScreenFacet = screenWithFacet.editorScreenFacet

        when: 'EditorScreenFacet opens editor by entity class'

        def userEditor = editorScreenFacet.show()

        then: 'Editor is correctly opened'

        screens.openedScreens.all.contains(userEditor)
    }

    def 'EditorScreenFacet opens editor by list component'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(EditorScreenFacetTestScreen)
        screenWithFacet.show()

        def tableScreenFacet = screenWithFacet.tableScreenFacet

        when: 'EditorScreenFacet opens editor by entity class'

        def userEditor = tableScreenFacet.show()

        then: 'Editor is correctly opened'

        screens.openedScreens.all.contains(userEditor)
    }

    def 'EditorScreenFacet opens editor by PickerField'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(EditorScreenFacetTestScreen)
        screenWithFacet.show()

        def fieldScreenFacet = screenWithFacet.fieldScreenFacet

        when: 'EditorScreenFacet opens editor by entity class'

        def userEditor = fieldScreenFacet.show()

        then: 'Editor is correctly opened'

        screens.openedScreens.all.contains(userEditor)
    }

    def 'EditorScreenFacet opens editor by entity provider'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(EditorScreenFacetTestScreen)
        screenWithFacet.show()

        def providerEditorFacet = screenWithFacet.editorEntityProvider

        when: 'EditorScreenFacet opens editor by entity class'

        def userEditor = providerEditorFacet.show()

        then: 'Editor is correctly opened'

        screens.openedScreens.all.contains(userEditor)

    }

    def 'Delegates are correctly installed into EditorScreenFacet'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(EditorScreenFacetTestScreen)

        when: 'Screen with EditorScreenFacet is opened'

        screenWithFacet.show()

        def editorScreenFacet = screenWithFacet.editorScreenFacet

        then: 'Delegates are installed'

        editorScreenFacet.entityProvider
        editorScreenFacet.initializer
    }
}
