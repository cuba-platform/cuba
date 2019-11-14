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

package spec.cuba.web.messagedialog

import com.haulmont.cuba.gui.Dialogs
import com.haulmont.cuba.gui.GuiDevelopmentException
import com.haulmont.cuba.gui.components.ContentMode
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.gui.components.WebButton
import com.haulmont.cuba.web.gui.components.WebMessageDialogFacet
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.messagedialog.screens.MessageDialogScreen

@SuppressWarnings('GroovyAccessibility')
class MessageDialogFacetTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.messagedialog.screens'])
    }

    def 'MessageDialog attributes are correctly loaded'() {
        def screens = vaadinUi.screens

        def mainScreen = screens.create('mainWindow', OpenMode.ROOT)
        mainScreen.show()

        when: 'MessageDialog is configured in XML'

        def screenWithDialog = screens.create(MessageDialogScreen)
        def messageDialog = screenWithDialog.messageDialog

        then: 'Attribute values are propagated to MessageDialog facet'

        messageDialog.id == 'messageDialog'
        messageDialog.caption == 'MessageDialog Facet'
        messageDialog.message == 'MessageDialog Test'
        messageDialog.type == Dialogs.MessageType.WARNING
        messageDialog.contentMode == ContentMode.HTML
        messageDialog.height == 200
        messageDialog.width == 350
        messageDialog.styleName == 'msg-dialog-style'
        messageDialog.modal
        messageDialog.maximized
        messageDialog.closeOnClickOutside

        when: 'MessageDialog is shown'

        messageDialog.show()

        then: 'UI has this dialog window'

        vaadinUi.windows.any { window ->
            window.caption == 'MessageDialog Facet'
        }
    }

    def 'Declarative MessageDialog subscription on Action'() {
        def screens = vaadinUi.screens

        def mainScreen = screens.create('mainWindow', OpenMode.ROOT)
        mainScreen.show()

        def screen = screens.create(MessageDialogScreen)
        screen.show()

        when: 'Dialog target action performed'

        screen.dialogAction.actionPerform(screen.dialogButton)

        then: 'Dialog is shown'

        vaadinUi.windows.find { w -> w.caption == 'Dialog Action Subscription' }
    }

    def 'Declarative MessageDialog subscription on Button'() {
        def screens = vaadinUi.screens

        def mainScreen = screens.create('mainWindow', OpenMode.ROOT)
        mainScreen.show()

        def screen = screens.create(MessageDialogScreen)

        when: 'Dialog target button is clicked'

        ((WebButton) screen.dialogButton).buttonClicked(null)

        then: 'Dialog is shown'

        vaadinUi.windows.find { w -> w.caption == 'Dialog Button Subscription' }
    }

    def 'MessageDialog should be bound to frame'() {
        def dialog = new WebMessageDialogFacet()

        when: 'Trying to show Dialog not bound to frame'

        dialog.show()

        then: 'Exception is thrown'

        thrown IllegalStateException

        when: 'Trying to setup declarative subscription without frame'

        dialog.subscribe()

        then: 'Exception is thrown'

        thrown IllegalStateException
    }

    def 'MessageDialog should have single subscription'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(MessageDialogScreen)

        def dialog = new WebMessageDialogFacet()
        dialog.setOwner(screen.getWindow())
        dialog.setActionTarget('actionId')
        dialog.setButtonTarget('buttonId')

        when: 'Both action and button are set as Dialog target'

        dialog.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException
    }

    def 'MessageDialog target should not be missing'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(MessageDialogScreen)

        def dialog = new WebMessageDialogFacet()
        dialog.setOwner(screen.getWindow())

        when: 'Missing action is set as target'

        dialog.setActionTarget('missingAction')
        dialog.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException

        when: 'Missing button is set as target'

        dialog.setActionTarget(null)
        dialog.setButtonTarget('missingButton')
        dialog.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException
    }
}
