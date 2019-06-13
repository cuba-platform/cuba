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
import com.haulmont.cuba.web.app.main.MainScreen
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.fragments.screens.FragmentWithEvent
import spec.cuba.web.fragments.screens.ScreenWithListener

import java.util.function.Consumer

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class FragmentEventsTest extends UiScreenSpec {

    def setup() {
        exportScreensPackages(['spec.cuba.web.fragments.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def "open screen with event listener on fragment controller"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create(MainScreen, OpenMode.ROOT)
        screens.show(mainWindow)

        def handler = Mock(Consumer)

        when:
        def screen = screens.create(ScreenWithListener)
        screen.handler = handler
        screen.show()

        then:
        screen.fragmentWithEvent != null

        def fragment = screen.getWindow().getComponent(0) as Fragment
        fragment != null
        def controller = fragment.frameOwner as FragmentWithEvent
        controller != null

        when:
        controller.hello()

        then:
        1 * handler.accept(_)
    }
}