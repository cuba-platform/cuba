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

package spec.cuba.web.components.webcomponentshelper

import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.gui.components.WebComponentsHelper
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.webcomponentshelper.screen.WchTestScreen

class WebComponentsHelperTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.webcomponentshelper.screen'])
    }

    def 'Find action in frame'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(WchTestScreen)
        screen.show()

        def frame = screen.getWindow()

        when: 'Finding screen action'


        def screenAction = WebComponentsHelper.findAction(frame, 'screenAction')

        then: 'Action found'

        screenAction

        when: 'Finding Table action'

        def tableAction = WebComponentsHelper.findAction(frame, 'table.createAction')

        then: 'Action found'

        tableAction

        when: 'Finding LookupPickerField action'

        def lpfAction = WebComponentsHelper.findAction(frame, 'lookupPickerField.lpfAction')

        then: 'Action found'

        lpfAction


    }
}
