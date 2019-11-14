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

package spec.cuba.web.screenfacet

import com.haulmont.cuba.gui.GuiDevelopmentException
import com.haulmont.cuba.gui.screen.FrameOwner
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.gui.components.WebButton
import com.haulmont.cuba.web.gui.components.WebScreenFacet
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.screenfacet.screens.ScreenFacetTestScreen
import spec.cuba.web.screenfacet.screens.ScreenToOpenWithFacet

@SuppressWarnings('GroovyAccessibility')
class ScreenFacetTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.screenfacet.screens'])
    }

    def 'ScreenFacet is loaded from XML'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(ScreenFacetTestScreen)

        when: 'Screen with ScreenFacet is opened'

        screenWithFacet.show()

        def screenIdFacet = screenWithFacet.screenIdFacet
        def screenClassFacet = screenWithFacet.screenClassFacet

        then: 'All ScreenFacet settings are correctly loaded'

        screenIdFacet.id == 'screenIdFacet'
        screenIdFacet.screenId == 'test_ScreenToOpenWithFacet'
        screenIdFacet.launchMode == OpenMode.NEW_TAB
        screenIdFacet.actionTarget == 'action'

        screenIdFacet.properties.find { p -> p.name == 'boolProp' }
        screenIdFacet.properties.find { p -> p.name == 'intProp' }
        screenIdFacet.properties.find { p -> p.name == 'doubleProp' }
        screenIdFacet.properties.find { p -> p.name == 'labelProp' }
        screenIdFacet.properties.find { p -> p.name == 'dcProp' }

        screenClassFacet.id == 'screenClassFacet'
        screenClassFacet.screenClass == ScreenToOpenWithFacet
        screenClassFacet.buttonTarget == 'button'
    }

    def 'ScreenFacet opens screen by screen id'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(ScreenFacetTestScreen)
        screenWithFacet.show()

        def screenFacet = screenWithFacet.screenIdFacet

        when: 'Facet shows a screen by screen id'

        def screen = screenFacet.show()

        then: 'Screen is correctly opened'

        screens.openedScreens.activeScreens.contains(screen)
    }

    def 'ScreenFacet opens screen by screen class'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(ScreenFacetTestScreen)
        screenWithFacet.show()

        def screenFacet = screenWithFacet.screenClassFacet

        when: 'Facet shows a screen by screen class'

        def screen = screenFacet.show()

        then: 'Screen is correctly opened'

        screens.openedScreens.activeScreens.contains(screen)
    }

    def 'ScreenFacet opens screen and injects props'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(ScreenFacetTestScreen)
        screenWithFacet.show()

        def screenFacet = screenWithFacet.screenIdFacet

        when: 'Screen properties are declared'

        def screen = screenFacet.show()

        then: 'Screen is opened and properties are correctly injected'

        screens.openedScreens.activeScreens.contains(screen)

        screen.boolProp
        screen.intProp == 42
        screen.doubleProp == 3.14159d

        screen.labelProp != null
        screen.dcProp != null
    }

    def 'ScreenFacet can be triggered via Action'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screen = screens.create(ScreenFacetTestScreen)
        screen.show()

        def screenFacet = screen.screenIdFacet as WebScreenFacet

        when: 'ScreenFacet target action performed'

        screen.action.actionPerform(screen.button)

        then: 'ScreenFacet screen is opened'

        screens.openedScreens.all.contains(screenFacet.screen)
    }

    def 'ScreenFacet can be triggered via Button'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screen = screens.create(ScreenFacetTestScreen)
        screen.show()

        def screenClassFacet = screen.screenClassFacet as WebScreenFacet

        when: 'Screen target button clicked'

        ((WebButton) screen.button).buttonClicked(null)

        then: 'Screen is opened'

        screens.openedScreens.all.contains(screenClassFacet.screen)
    }

    def 'ScreenFacet fires AfterShowEvent and AfterCloseEvent'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        mainWindow.show()

        def screenWithFacet = screens.create(ScreenFacetTestScreen)
        screenWithFacet.show()

        def screenFacet = screenWithFacet.screenIdFacet

        when: 'ScreenFacet screen is opened'

        def openedScreen = screenFacet.show()

        then: 'AfterShowEvent is fired'

        screenWithFacet.afterShowListenerTriggered

        when: 'Opened screen is closed'

        openedScreen.close(FrameOwner.WINDOW_CLOSE_ACTION)

        then: 'AfterCloseEvent is fired'

        screenWithFacet.afterCloseListenerTriggered
    }

    def 'ScreenFacet should be bound to frame'() {
        def screenFacet = new WebScreenFacet()

        when: 'Trying to show screen not bound to frame'

        screenFacet.show()

        then: 'Exception is thrown'

        thrown IllegalStateException

        when: 'Trying to setup declarative subscriptions'

        screenFacet.subscribe()

        then: 'Exception is thrown'

        thrown IllegalStateException
    }

    def 'ScreenFacet should have only one subscription'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screenWithFacet = screens.create(ScreenFacetTestScreen)
        screenWithFacet.show()

        def screenFacet = new WebScreenFacet()
        screenFacet.setOwner(screenWithFacet.getWindow())

        when: 'Both action and button are set as Screen target'

        screenFacet.setActionTarget('actionId')
        screenFacet.setButtonTarget('buttonId')
        screenFacet.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException
    }

    def 'ScreenFacet target should not be missing'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screenWithFacet = screens.create(ScreenFacetTestScreen)
        screenWithFacet.show()

        def screenFacet = new WebScreenFacet()
        screenFacet.setOwner(screenWithFacet.getWindow())

        when: 'Missing action is set to Screen'

        screenFacet.setActionTarget('missingAction')
        screenFacet.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException

        when: 'Missing button is set to Screen'

        screenFacet.setActionTarget(null)
        screenFacet.setButtonTarget('missingButton')
        screenFacet.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException
    }
}
