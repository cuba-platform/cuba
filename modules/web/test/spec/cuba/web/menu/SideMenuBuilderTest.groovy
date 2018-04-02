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

package spec.cuba.web.menu

import com.haulmont.cuba.core.global.MessageTools
import com.haulmont.cuba.gui.components.mainwindow.SideMenu
import com.haulmont.cuba.gui.config.MenuConfig
import com.haulmont.cuba.gui.config.MenuItem
import com.haulmont.cuba.security.global.UserSession
import com.haulmont.cuba.web.gui.components.mainwindow.WebSideMenu
import com.haulmont.cuba.web.sys.SideMenuBuilder

import java.util.function.Consumer

class SideMenuBuilderTest extends AbstractMenuBuilderSpecification {

    def builder = new TestSideMenuBuilder()

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

    static class TestSideMenuBuilder extends SideMenuBuilder {

        static final noActionMenuCommand = new Consumer<SideMenu.MenuItem>() {
            @Override
            void accept(SideMenu.MenuItem menuItem) {
            }
        }

        void setSession(UserSession userSession) {
            this.@session = userSession
        }

        void setMenuConfig(MenuConfig menuConfig) {
            this.@menuConfig = menuConfig
        }

        void setMessageTools(MessageTools messageTools) {
            this.@messageTools = messageTools
        }

        @Override
        void build(SideMenu menu, List<MenuItem> rootItems) {
            super.build(menu, rootItems)
        }

        @Override
        protected Consumer<SideMenu.MenuItem> createMenuCommandExecutor(MenuItem item) {
            return noActionMenuCommand
        }
    }
}