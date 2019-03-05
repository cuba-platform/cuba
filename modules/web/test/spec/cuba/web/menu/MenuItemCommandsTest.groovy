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

package spec.cuba.web.menu

import com.haulmont.bali.util.Dom4j
import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor
import com.haulmont.cuba.gui.config.MenuItem
import com.haulmont.cuba.gui.config.MenuItemCommands
import com.haulmont.cuba.gui.config.WindowConfig
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.gui.sys.UiControllersConfiguration
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.menu.commandtargets.TestWebBean
import org.springframework.core.type.classreading.MetadataReaderFactory
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.menu.commandtargets.PropertiesInjectionTestScreen
import spec.cuba.web.menu.commandtargets.TestMenuItemConsumer
import spec.cuba.web.menu.commandtargets.TestMenuItemRunnable
import spec.cuba.web.menu.commandtargets.TestRunnable

class MenuItemCommandsTest extends UiScreenSpec {

    @SuppressWarnings(['GroovyAssignabilityCheck', 'GroovyAccessibility'])
    void setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        def configuration = new UiControllersConfiguration()
        configuration.applicationContext = cont.getApplicationContext()
        configuration.metadataReaderFactory = cont.getBean(MetadataReaderFactory)
        configuration.basePackages = ['spec.cuba.web.menu.commandtargets', 'com.haulmont.cuba.web.app.main']

        def windowConfig = cont.getBean(WindowConfig)
        windowConfig.configurations = [configuration]
        windowConfig.initialized = false

        TestMenuItemConsumer.launched.set(false)
        TestMenuItemConsumer.beanLocatorSet.set(false)

        TestMenuItemRunnable.launched.set(false)
        TestMenuItemRunnable.beanLocatorSet.set(false)

        TestRunnable.launched.set(false)
        TestRunnable.beanLocatorSet.set(false)
    }

    @SuppressWarnings('GroovyAccessibility')
    def cleanup() {
        TestServiceProxy.clear()

        def windowConfig = cont.getBean(WindowConfig)
        windowConfig.configurations = []
        windowConfig.initialized = false
    }

    @SuppressWarnings(['GroovyAccessibility'])
    def 'Create and run Screen command'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def menuCommands = cont.getBean(MenuItemCommands)

        def user = new User()
        user.setId(UUID.fromString('60885987-1b61-4247-94c7-dff348347f93'))

        def dataService = Mock(DataService)
        dataService.load(_ as LoadContext) >> user

        menuCommands.dataService = dataService

        when: 'Screen menu item command with params and properties is running'
        def screenCmd = menuCommands.create(mainWindow, createScreenMenuItem()) as MenuItemCommands.ScreenCommand

        screenCmd.run()

        then: 'All params are loaded, all properties are injected into UI Controller'
        screenCmd.getDescription() == 'Opening window: "cuba_PropertiesInjectionTestScreen"'

        screenCmd.params.get('testAppPropParam') == 'cuba'
        screenCmd.params.get('testBooleanParam') == Boolean.FALSE
        screenCmd.params.get('testStringParam') == 'Hello World!'

        screenCmd.controllerProperties.find { it.name == 'testIntProperty' && it.value == '42' }
        screenCmd.controllerProperties.find { it.name == 'testStringProperty' && it.value == 'Hello World!' }
        screenCmd.controllerProperties.find { it.name == 'entityToEdit' }

        PropertiesInjectionTestScreen testScreen = screens.getOpenedScreens().getActiveScreens().stream()
                .filter({ it instanceof PropertiesInjectionTestScreen })
                .findFirst()
                .orElseThrow({
            throw new IllegalStateException('PropertiesInjectionTestScreen should be in opened screens')}) as PropertiesInjectionTestScreen

        testScreen.testIntProperty == 42
        testScreen.testStringProperty == 'Hello World!'
        testScreen.entityToEdit == user
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Create and run Editor Screen command'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def menuCommands = cont.getBean(MenuItemCommands)

        def user = new User()
        user.setId(UUID.fromString('60885987-1b61-4247-94c7-dff348347f93'))

        def dataService = Mock(DataService)
        dataService.load(_ as LoadContext) >> user

        menuCommands.dataService = dataService

        when: 'Editor screen command is running'
        menuCommands.create(mainWindow, createEditorMenuItem())
                .run()

        then: 'The "entityToEdit" property is injected'
        def userEditor = vaadinUi.screens.getOpenedScreens()
                .getActiveScreens()
                .stream()
                .filter({ it instanceof UserEditor })
                .findFirst()
                .orElseThrow({
            throw new IllegalStateException('UserEditor should be in active screens')
        }) as UserEditor

        userEditor.getEditedEntity() == user
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Create and run Bean command'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def menuCommands = cont.getBean(MenuItemCommands)

        when: 'Bean command menu item is running'
        def beanCommand = menuCommands.create(mainWindow, createBeanMenuItem())
        beanCommand.run()

        then: 'Corresponding bean method is invoked'
        beanCommand.getDescription() == 'Calling bean method: cuba_TestWebBean#testMethod'

        cont.getBean(TestWebBean).testMethodInvoked.get()
    }

    @SuppressWarnings('GroovyAccessibility')
    def 'Create and run Runnable command'() {
        when: 'Runnable class menu item is running'
        def runnableCmd = cont.getBean(MenuItemCommands)
                .create(null, createRunnableMenuItem())
        runnableCmd.run()

        then: 'Corresponding Runnable instance is launched'
        runnableCmd.getDescription() == 'Running "spec.cuba.web.menu.commandtargets.TestRunnable"'

        TestRunnable.launched.get()
        TestRunnable.beanLocatorSet.get()
    }

    def 'Create and run MenuItemRunnable command'() {
        when: 'MenuItemRunnable menu item is running'
        def menuItemRunnableCmd = cont.getBean(MenuItemCommands)
                .create(null, createMenuItemRunnable())
        menuItemRunnableCmd.run()

        then: 'Corresponding instance is launched'
        menuItemRunnableCmd.getDescription() == 'Running "spec.cuba.web.menu.commandtargets.TestMenuItemRunnable"'

        TestMenuItemRunnable.launched.get()
        TestMenuItemRunnable.beanLocatorSet.get()
    }

    def 'Create and run Consumer command'() {
        when: 'Menu item params consumer is running'
        def consumerCmd = cont.getBean(MenuItemCommands)
                .create(null, createConsumerMenuItem())
        consumerCmd.run()

        then: 'Corresponding instance is launched'
        consumerCmd.getDescription() == 'Running "spec.cuba.web.menu.commandtargets.TestMenuItemConsumer"'

        TestMenuItemConsumer.launched.get()
        TestMenuItemConsumer.beanLocatorSet.get()
    }

    MenuItem createScreenMenuItem() {
        def menuItem = new MenuItem('testScreenItem')
        menuItem.setScreen('cuba_PropertiesInjectionTestScreen')

        def itemDescriptor = Dom4j.readDocument('''
<item screen="cuba_PropertiesInjectionTestScreen"  openType="NEW_TAB" resizable="true">
    <param name="testAppPropParam" value="${cuba.webContextName}"/>
    <param name="testBooleanParam" value="false"/>
    <param name="testStringParam"  value="Hello World!"/>
    <param name="testEntityParam" value="sec$User-60885987-1b61-4247-94c7-dff348347f93"/>
    
    <properties>
        <property name="testIntProperty" value="42"/>
        <property name="testStringProperty" value="Hello World!"/>
        <property name="entityToEdit" entityClass="com.haulmont.cuba.security.entity.User" 
                entityId="60885987-1b61-4247-94c7-dff348347f93" entityView="user.browse"/> 
    </properties>
</item>
''').rootElement

        menuItem.setDescriptor(itemDescriptor)
        menuItem
    }

    MenuItem createEditorMenuItem() {
        def menuItem = new MenuItem('testEditorItem')
        menuItem.setScreen('sec$User.edit')

        def itemDescriptor = Dom4j.readDocument('''
<item screen="sec$User.edit">
    <properties>
        <property name="entityToEdit"
                  entityClass="com.haulmont.cuba.security.entity.User"
                  entityId="60885987-1b61-4247-94c7-dff348347f93"/>
    </properties>
</item>
''').rootElement

        menuItem.setDescriptor(itemDescriptor)
        menuItem
    }

    MenuItem createBeanMenuItem() {
        def menuItem = new MenuItem('testBeanItem')
        menuItem.setBean(TestWebBean.NAME)
        menuItem.setBeanMethod('testMethod')
        menuItem
    }

    MenuItem createRunnableMenuItem() {
        def menuItem = new MenuItem('testBeanItem')
        menuItem.setRunnableClass(TestRunnable.class.getName())
        menuItem
    }

    MenuItem createMenuItemRunnable() {
        def menuItem = new MenuItem('testBeanItem')
        menuItem.setRunnableClass(TestMenuItemRunnable.class.getName())
        menuItem
    }

    MenuItem createConsumerMenuItem() {
        def menuItem = new MenuItem('testConsumerItem')
        menuItem.setRunnableClass(TestMenuItemConsumer.class.getName())
        menuItem
    }
}
