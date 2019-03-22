/*
 * Copyright (c) 2008-2018 Haulmont.
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

package spec.cuba.web.screens

import com.google.common.collect.ImmutableMap
import com.haulmont.cuba.core.global.BeanLocator
import com.haulmont.cuba.core.global.Events
import com.haulmont.cuba.core.global.Messages
import com.haulmont.cuba.gui.Dialogs
import com.haulmont.cuba.gui.Notifications
import com.haulmont.cuba.gui.Screens
import com.haulmont.cuba.gui.components.*
import com.haulmont.cuba.gui.components.actions.BaseAction
import com.haulmont.cuba.gui.components.sys.WindowImplementation
import com.haulmont.cuba.gui.screen.FrameOwner
import com.haulmont.cuba.gui.screen.MessageBundle
import com.haulmont.cuba.gui.screen.Screen
import com.haulmont.cuba.gui.screen.ScreenContext
import com.haulmont.cuba.gui.screen.impl.MessageBundleImpl
import com.haulmont.cuba.gui.sys.UiControllerDependencyInjector
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector
import com.haulmont.cuba.security.entity.User
import spec.cuba.web.screens.injection.*
import spock.lang.Specification

@SuppressWarnings("GroovyAccessibility")
class UiControllerDependencyInjectorTest extends Specification {

    def "Injector supports fields"() {
        def screen = new ScreenInjectToFields()

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(Window)
        def screenContext = Mock(ScreenContext)

        def usersTable = Mock(Table)
        def button = Mock(Button)
        def textField = Mock(TextField)

        def commitAction = Mock(Action)
        def createAction = Mock(Action)

        def notifications = Mock(Notifications)
        def screens = Mock(Screens)
        def dialogs = Mock(Dialogs)
        def messages = Mock(Messages)

        screenContext.getDialogs() >> dialogs
        screenContext.getNotifications() >> notifications
        screenContext.getScreens() >> screens

        window.getComponent("usersTable") >> usersTable
        window.getComponent("fieldGroup.name") >> textField
        window.getComponent("button") >> button
        window.getAction(_) >> commitAction
        window.getFrameOwner() >> screen

        usersTable.getAction(_) >> createAction

        screen.window = window
        screen.screenContext = screenContext

        beanLocator.getAll(Messages) >> ImmutableMap.of("messages", messages)
        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)
        beanLocator.getPrototype(MessageBundle.NAME) >> new MessageBundleImpl()

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:

        injector.inject()

        then:

        screen.button == button
        screen.messages == messages

        screen.commitAction == commitAction
        screen.textField == textField
        screen.createAction == createAction

        screen.notifications == notifications
        screen.screens == screens
        screen.dialogs == dialogs

        screen.logger != null
        screen.messageBundle != null
    }

    def "Injector supports setters"() {
        def screen = new ScreenInjectToSetters()

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(Window)

        def button = Mock(Button)
        def messages = Mock(Messages)

        window.getComponent("button") >> button

        screen.window = window

        beanLocator.getAll(Messages) >> ImmutableMap.of("messages", messages)
        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:

        injector.inject()

        then:

        screen.button == button
        screen.messages == messages
    }

    def "Injector supports UiEvent listeners"() {
        def screen = new ScreenBindEventListener()

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(TestWindow)
        def events = Mock(Events)

        screen.window = window

        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)
        beanLocator.get(Events.NAME) >> events

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:

        injector.inject()

        then:

        screen.uiEventListeners.size() == 2
    }

    def "Injector supports @Subscribe listeners"() {
        def screen = new ScreenBindSubscribe()

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(TestWindow)

        def btn1 = Mock(Button)
        def textField1 = Mock(TextField)
        def split1 = Mock(SplitPanel)
        def commitAction = new BaseAction("commit")

        window.getComponent("btn1") >> btn1
        window.getComponent("textField1") >> textField1
        window.getComponent("split1") >> split1

        window.getSubPart("commit") >> commitAction

        screen.window = window

        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:

        injector.inject()

        then:

        1 * btn1.addClickListener(_)
        1 * textField1.addValueChangeListener(_)
        1 * split1.addSplitPositionChangeListener(_)

        commitAction.eventHub.hasSubscriptions(Action.ActionPerformedEvent)
        screen.eventHub.hasSubscriptions(Screen.InitEvent)
    }

    def "Injector supports @Install methods"() {
        def screen = new ScreenBindInstall()

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(TestWindow)

        def label1 = Mock(Label)
        def groupTable = Mock(Table)
        def tree = Mock(Tree)
        def textField1 = Mock(TextField)

        def usersTable = Mock(Table)
        def genColumn = new Table.Column<User>("genColumn")
        genColumn.setOwner(usersTable)
        usersTable.getSubPart("genColumn") >> genColumn

        def dataGrid = Mock(DataGrid)
        DataGrid.Column dataGridNameColumn = Mock(DataGrid.Column)
        dataGrid.getSubPart("name") >> dataGridNameColumn

        window.getComponent("label1") >> label1
        window.getComponent("usersTable") >> usersTable
        window.getComponent("groupTable") >> groupTable
        window.getComponent("tree") >> tree
        window.getComponent("textField1") >> textField1
        window.getComponent("dataGrid") >> dataGrid

        screen.window = window

        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:

        injector.inject()

        then:

        1 * label1.setFormatter(_)
        1 * usersTable.addStyleProvider(_)
        1 * groupTable.addStyleProvider(_)
        1 * tree.setIconProvider(_)
        1 * textField1.addValidator(_)
        1 * dataGrid.setCellDescriptionProvider({
            it != null && it.getDescription(null, null) == 'OK'
        })
        1 * usersTable.addGeneratedColumn("genColumn", _)

        1 * dataGridNameColumn.setEditFieldGenerator(_)
        1 * dataGridNameColumn.setStyleProvider(_)
        1 * dataGridNameColumn.setDescriptionProvider(_)
    }

    def "Injector supports non-required dependencies"() {
        def screen = new ScreenOptionalDependencies()

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(TestWindow)

        screen.window = window

        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:

        injector.inject()

        then:

        screen.textField == null
    }

    def "Screen fires private event handlers"() {
        def screen = new ParentScreen()

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(TestWindow)

        screen.window = window

        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:
        injector.inject()

        then:
        screen.eventHub != null
        screen.eventHub.hasSubscriptions(Screen.InitEvent.class)

        when:
        def event = new Screen.InitEvent(screen, Screen.NO_OPTIONS)
        screen.eventHub.publish(Screen.InitEvent.class, event)

        then:
        screen.fired == 1
    }

    def "Screen fires private event handlers from parent screen"() {
        def screen = new ChildScreen() // this screen inherits private event handler from parent class

        def injector = new UiControllerDependencyInjector(screen, FrameOwner.NO_OPTIONS)
        def inspector = new UiControllerReflectionInspector()
        def beanLocator = Mock(BeanLocator)
        def window = Mock(TestWindow)

        screen.window = window

        beanLocator.getAll(BeanLocator) >> ImmutableMap.of("beanLocator", beanLocator)

        injector.reflectionInspector = inspector
        injector.beanLocator = beanLocator

        when:
        injector.inject()

        then:
        screen.eventHub != null
        screen.eventHub.hasSubscriptions(Screen.InitEvent.class)

        when:
        def event = new Screen.InitEvent(screen, Screen.NO_OPTIONS)
        screen.eventHub.publish(Screen.InitEvent.class, event)

        then:
        screen.fired == 1
    }

    private interface TestWindow extends Window, WindowImplementation {

    }
}