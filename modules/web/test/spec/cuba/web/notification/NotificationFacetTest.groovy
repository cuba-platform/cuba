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

package spec.cuba.web.notification

import com.haulmont.cuba.gui.GuiDevelopmentException
import com.haulmont.cuba.gui.Notifications
import com.haulmont.cuba.gui.components.ContentMode
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.gui.components.WebButton
import com.haulmont.cuba.web.gui.components.WebNotificationFacet
import com.vaadin.server.Extension
import com.vaadin.ui.Notification
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.notification.screens.ScreenWithNotification

@SuppressWarnings('GroovyAccessibility')
class NotificationFacetTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.notification.screens'])
    }

    def 'Notification attributes are applied'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(ScreenWithNotification)

        when: 'Notification is configured in XML'
        screen.show()

        def notification = screen.testNotification

        then: 'Attribute values are propagated to notification facet'

        notification.id == 'testNotification'
        notification.type == Notifications.NotificationType.HUMANIZED
        notification.caption == 'Notification Facet Test'
        notification.description == 'Description from XML'
        notification.contentMode == ContentMode.HTML
        notification.delay == 3000
        notification.position == Notifications.Position.TOP_CENTER
        notification.styleName == 'notification-facet-style'
    }

    def 'Notification Install and Subscribe handlers'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(ScreenWithNotification)
        screen.show()

        def notification = screen.testNotification

        when: 'Notification is shown'

        notification.show()

        then: 'Caption and description providers are triggered'

        screen.testNotification.captionProvider != null
        screen.testNotification.descriptionProvider != null

        screen.captionProvided
        screen.descriptionProvided

        when: 'All notifications are closed'

        closeAllNotifications()

        then: 'CloseEvent is fired'

        screen.closeEvtFired
    }

    def 'Declarative Notification Action subscription'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(ScreenWithNotification)

        when: 'Notification target action is performed'

        screen.notificationAction.actionPerform(screen.notificationButton)

        then: 'Notification is shown'

        vaadinUi.getExtensions().find { ext ->
            ext instanceof Notification &&
                    ((Notification) ext).caption == 'Notification Action subscription'
        }
    }

    def 'Declarative Notification Button subscription'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(ScreenWithNotification)

        when: 'Notification target button is clicked'

        ((WebButton) screen.notificationButton)
                .buttonClicked(null)

        then: 'Notification is shown'

        vaadinUi.getExtensions().find { ext ->
            ext instanceof Notification &&
                    ((Notification) ext).caption == 'Notification Button subscription'
        }

    }

    def 'Notification should be bound to frame'() {
        def notification = new WebNotificationFacet()

        when: 'Trying to show Notification not bound to frame'

        notification.show()

        then: 'Exception is thrown'

        thrown IllegalStateException

        when: 'Trying to setup declarative subscription without frame'

        notification.subscribe()

        then: 'Exception is thrown'

        thrown IllegalStateException
    }

    def 'Notification should have single subscription'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(ScreenWithNotification)

        def notification = new WebNotificationFacet()

        notification.setOwner(screen.getWindow())
        notification.setActionTarget('actionId')
        notification.setButtonTarget('buttonId')

        when: 'Both action and button are set as Notification subscription target'

        notification.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException
    }

    def 'Notification target should not be missing'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(ScreenWithNotification)

        def notification = new WebNotificationFacet()

        notification.setOwner(screen.getWindow())
        notification.setActionTarget('missingAction')

        when: 'Notification is bound to missing action'

        notification.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException

        when: 'Notification is bound to missing button'

        notification.setActionTarget(null)
        notification.setButtonTarget('missingButton')
        notification.subscribe()

        then: 'Exception is thrown'

        thrown GuiDevelopmentException
    }

    protected void closeAllNotifications() {
        def notifications = []
        for (Extension ext : vaadinUi.getExtensions()) {
            if (ext instanceof Notification) {
                notifications.push(ext as Notification)
            }
        }
        for (Notification ntf : notifications) {
            ntf.close()
        }
    }
}
