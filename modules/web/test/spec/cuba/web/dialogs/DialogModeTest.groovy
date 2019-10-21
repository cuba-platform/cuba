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

package spec.cuba.web.dialogs

import com.haulmont.cuba.gui.components.DialogWindow
import com.haulmont.cuba.gui.screen.OpenMode
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.dialogs.screens.DialogAutoSizeTestScreen
import spec.cuba.web.dialogs.screens.DialogSpecifiedSizeTestScreen

@SuppressWarnings("GroovyAssignabilityCheck")
class DialogModeTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.dialogs.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def 'DialogMode supports AUTO width and height'() {
        showMainWindow()

        def screen = screens.create(DialogAutoSizeTestScreen)
        screen.show()

        when:
        def dialogWindow = screen.getWindow() as DialogWindow
        def dialogWidth = dialogWindow.getDialogWidth()
        def dialogHeight = dialogWindow.getDialogHeight()

        then:
        dialogWidth == -1
        dialogHeight == -1
    }

    def 'DialogMode supports specified width and height'() {
        showMainWindow()

        def screen = screens.create(DialogSpecifiedSizeTestScreen)
        screen.show()

        when:
        def dialogWindow = screen.getWindow() as DialogWindow
        def dialogWidth = dialogWindow.getDialogWidth()
        def dialogHeight = dialogWindow.getDialogHeight()

        then:
        dialogWidth == 600
        dialogHeight == 400
    }
}
