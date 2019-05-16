/*
 * Copyright (c) 2008-2019 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package spec.cuba.web.clipboardtrigger

import com.haulmont.cuba.gui.components.ClipboardTrigger
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.app.main.MainScreen
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.clipboardtrigger.screens.ScreenWithClipboardTrigger

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class ClipboardTriggerFacetTest extends UiScreenSpec {

    def setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        exportScreensPackages(['spec.cuba.web.clipboardtrigger.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def cleanup() {
        TestServiceProxy.clear()

        resetScreensConfig()
    }

    def "open screen with ClipboardTrigger"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create(MainScreen, OpenMode.ROOT)
        screens.show(mainWindow)

        when:

        def screen = screens.create(ScreenWithClipboardTrigger)
        screen.show()

        then:

        screen.window.getFacet('copyTrigger') instanceof ClipboardTrigger
        screen.window.facets.count() == 1

        screen.copyTrigger != null
        screen.copyTrigger.button != null
        screen.copyTrigger.input != null
    }
}