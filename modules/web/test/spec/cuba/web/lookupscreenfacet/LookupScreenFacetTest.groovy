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

package spec.cuba.web.lookupscreenfacet

import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.entity.User
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.lookupscreenfacet.screens.LookupScreenFacetTestScreen

@SuppressWarnings('GroovyAccessibility')
class LookupScreenFacetTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.lookupscreenfacet.screens'])
    }

    def 'LookupScreenFacet is loaded from XML'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(LookupScreenFacetTestScreen)

        when: 'Screen with LookupScreenFacet is opened'

        screenWithFacet.show()

        def lookupScreenFacet = screenWithFacet.lookupScreen
        def tableLookupScreenFacet = screenWithFacet.tableLookupScreen
        def fieldLookupScreenFacet = screenWithFacet.fieldLookupScreen

        then: 'All LookupScreenFacet settings are correctly loaded'

        lookupScreenFacet.id == 'lookupScreen'
        lookupScreenFacet.entityClass == User
        lookupScreenFacet.launchMode == OpenMode.DIALOG
        lookupScreenFacet.actionTarget == 'action'
        lookupScreenFacet.pickerField == screenWithFacet.pickerField
        lookupScreenFacet.container == screenWithFacet.userDc
        lookupScreenFacet.listComponent == screenWithFacet.usersTable

        tableLookupScreenFacet.id == 'tableLookupScreen'
        tableLookupScreenFacet.listComponent == screenWithFacet.usersTable
        tableLookupScreenFacet.buttonTarget == 'button'

        fieldLookupScreenFacet.id == 'fieldLookupScreen'
        fieldLookupScreenFacet.pickerField == screenWithFacet.pickerField
    }

    def 'LookupScreenFacet opens lookup by entity class'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(LookupScreenFacetTestScreen)
        screenWithFacet.show()

        def lookupScreenFacet = screenWithFacet.lookupScreen

        when: 'LookupScreenFacet opens lookup by entity class'

        def userBrowser = lookupScreenFacet.show()

        then: 'Lookup is correctly opened'

        screens.openedScreens.all.contains(userBrowser)
    }

    def 'LookupScreenFacet opens lookup by list component'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(LookupScreenFacetTestScreen)
        screenWithFacet.show()

        def tableLookupScreenFacet = screenWithFacet.tableLookupScreen

        when: 'LookupScreenFacet opens lookup by list component'

        def userBrowser = tableLookupScreenFacet.show()

        then: 'Lookup is correctly opened'

        screens.openedScreens.all.contains(userBrowser)
    }

    def 'LookupScreenFacet opens lookup by PickerField'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(LookupScreenFacetTestScreen)
        screenWithFacet.show()

        def fieldLookupScreenFacet = screenWithFacet.fieldLookupScreen

        when: 'LookupScreenFacet opens lookup by list component'

        def userBrowser = fieldLookupScreenFacet.show()

        then: 'Lookup is correctly opened'

        screens.openedScreens.all.contains(userBrowser)
    }

    def 'Delegates are correctly installed into LookupScreenFacet'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(LookupScreenFacetTestScreen)

        when: 'Screen with LookupScreenFacet is opened'

        screenWithFacet.show()

        def lookupScreenFacet = screenWithFacet.lookupScreen

        then: 'Delegates are installed'

        lookupScreenFacet.selectHandler
        lookupScreenFacet.selectValidator
        lookupScreenFacet.transformation
    }
}
