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

package com.haulmont.cuba.web.sys

import com.haulmont.cuba.gui.components.mainwindow.AppMenu
import com.haulmont.cuba.gui.config.MenuItem
import com.haulmont.cuba.web.gui.components.mainwindow.WebSideMenu

import java.util.function.Consumer

class SideMenuBuilderTest extends AbstractMenuBuilderSpecification {

    def noActionMenuCommand = new Consumer<AppMenu.MenuItem>() {
        @Override
        void accept(AppMenu.MenuItem menuItem) {
        }
    }

    def builder = new SideMenuBuilder() {
        @Override
        protected Consumer<AppMenu.MenuItem> createMenuCommandExecutor(MenuItem item) {
            return noActionMenuCommand
        }
    }

    @Override
    void setup() {
        builder.session = userSession
        builder.menuConfig = menuConfig
        builder.messageTools = messageTools
    }

    def "sidemenu builder removes extra empty submenus if they are present in MenuConfig"() {
        given: "menu loads XML with extra empty submenu"

        def menu = new WebSideMenu()
        menu.frame = mainWindow

        menuConfig.loadTestMenu('''
<menu-config xmlns="http://schemas.haulmont.com/cuba/menu.xsd">
    <menu id="MAIN">
        <menu id="A"
              description="F">
            <item id="A1"
                  screen="sec$User.browse"/>
        </menu>

        <menu id="X">

        </menu>

        <menu id="B">
            <item id="B1"
                  screen="sec$User.browse"/>
        </menu>
    </menu>
</menu-config>
'''.trim())

        when: "we build UI menu structure"
        builder.build(menu, menuConfig.rootItems)

        then: "menu should not contain extra empty submenu"
        menu.menuItems.size() == 1
        menu.menuItems[0].children.size() == 2
    }

    def "sidemenu builder does not create separator elements"() {
        given: "menu loads XML with extra empty submenu"

        def menu = new WebSideMenu()
        menu.frame = mainWindow

        menuConfig.loadTestMenu('''
<menu-config xmlns="http://schemas.haulmont.com/cuba/menu.xsd">
    <menu id="MAIN">
        <menu id="A"
              description="F">
            <item id="A1"
                  screen="sec$User.browse"/>
        </menu>

        <separator/>

        <menu id="B">
            <item id="B1"
                  screen="sec$User.browse"/>
        </menu>
    </menu>

    <separator/>
    
    <item id="B2"
          screen="sec$User.browse"/>
</menu-config>
'''.trim())

        when: "we build UI menu structure"
        builder.build(menu, menuConfig.rootItems)

        then: "menu should not contain separators"
        menu.menuItems.size() == 2
        menu.menuItems

        menu.menuItems[0].children.size() == 2
    }
}