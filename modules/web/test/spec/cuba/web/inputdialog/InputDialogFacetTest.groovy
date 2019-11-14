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

package spec.cuba.web.inputdialog

import com.haulmont.cuba.gui.screen.OpenMode
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.inputdialog.screens.InputDialogScreen

import java.util.stream.Collectors

class InputDialogFacetTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.inputdialog.screens', 'com.haulmont.cuba.gui.app.core.inputdialog'])
    }

    def 'InputDialog parameters are initialized'() {
        def screens = vaadinUi.screens

        def mainScreen = screens.create('mainWindow', OpenMode.ROOT)
        mainScreen.show()

        def inputDialogScreen = screens.create(InputDialogScreen)
        inputDialogScreen.show()

        when: 'InputDialog is shown'

        def inputDialog = inputDialogScreen.inputDialog.show()

        then: 'All parameters are present'

        def paramIds = inputDialog.getParameters()
                .stream()
                .map({ param -> param.id })
                .collect(Collectors.toList())

        paramIds.contains('booleanParam')
        paramIds.contains('intParam')
        paramIds.contains('stringParam')
        paramIds.contains('decimalParam')
        paramIds.contains('enumParam')
        paramIds.contains('entityParam')
    }

    def 'InputDialog parameter default values are propagated'() {
        def screens = vaadinUi.screens

        def mainScreen = screens.create('mainWindow', OpenMode.ROOT)
        mainScreen.show()

        def inputDialogScreen = screens.create(InputDialogScreen)
        inputDialogScreen.show()

        when: 'InputDialog is shown'

        def inputDialogFacet = inputDialogScreen.inputDialog
        def inputDialog = inputDialogFacet.show()

        then:

        !inputDialog.getValue('booleanParam')
        inputDialog.getValue('intParam') == 42
        inputDialog.getValue('stringParam') == 'Hello world!'
        inputDialog.getValue('decimalParam') == 1234567890
    }

    def 'InputDialog custom actions are propagated'() {
        def screens = vaadinUi.screens

        def mainScreen = screens.create('mainWindow', OpenMode.ROOT)
        mainScreen.show()

        def inputDialogScreen = screens.create(InputDialogScreen)
        inputDialogScreen.show()

        when: 'Custom actions declared for InputDialog'

        def inputDialog = inputDialogScreen.inputDialogCustomActions
        inputDialog.show()

        def actions = inputDialog.actions

        then: 'Actions are propagated into InputDialog'

        actions.find { action -> action.id == 'ok' } != null
        actions.find { action -> action.id == 'cancel' } != null
    }
}
