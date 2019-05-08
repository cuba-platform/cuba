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

package spec.cuba.web.fragments

import com.haulmont.cuba.gui.components.Fragment
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.app.main.MainScreen
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.fragments.screens.FragmentWithParentListener
import spec.cuba.web.fragments.screens.ScreenWithFragmentListener

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class FragmentParentEventTest extends UiScreenSpec {

    def setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        exportScreensPackages(['spec.cuba.web.fragments.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def cleanup() {
        TestServiceProxy.clear()

        resetScreensConfig()
    }

    def "open fragment with subscribe on parent event"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create(MainScreen, OpenMode.ROOT)
        screens.show(mainWindow)

        when:

        def screen = screens.create(ScreenWithFragmentListener)
        screen.show()

        then:

        def fragment = screen.getWindow().getComponent(0) as Fragment
        fragment != null
        def controller = fragment.frameOwner as FragmentWithParentListener
        controller != null

        controller.showEvents == 1
    }
}