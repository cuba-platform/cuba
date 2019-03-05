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

package spec.cuba.web.masterdetail

import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.config.WindowConfig
import com.haulmont.cuba.gui.screen.MasterDetailScreen
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.gui.sys.UiControllersConfiguration
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import org.springframework.core.type.classreading.MetadataReaderFactory
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.masterdetail.screens.UserMasterDetail

import java.util.function.Consumer

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class MasterDetailInitEntityTest extends UiScreenSpec {

    void setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        def windowConfig = cont.getBean(WindowConfig)

        def configuration = new UiControllersConfiguration()
        configuration.applicationContext = cont.getApplicationContext()
        configuration.metadataReaderFactory = cont.getBean(MetadataReaderFactory)
        configuration.basePackages = ['spec.cuba.web.masterdetail.screens', 'com.haulmont.cuba.web.app.main']

        windowConfig.configurations = [configuration]
        windowConfig.initialized = false
    }

    def cleanup() {
        TestServiceProxy.clear()

        def windowConfig = cont.getBean(WindowConfig)
        windowConfig.configurations = []
        windowConfig.initialized = false
    }

    def "MasterDetailScreen fires InitEntityEvent on Create"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def initEntityListener = Mock(Consumer)

        def masterDetail = screens.create(UserMasterDetail)
        masterDetail.addInitEntityListener(initEntityListener)
        masterDetail.show()

        def table = masterDetail.getWindow().getComponentNN("table") as Table
        def createAction = table.getAction("create")

        when:
        createAction.actionPerform(table)
        def user = masterDetail.getEditedUser()

        then:
        user != null
        user.name == "New user"

        1 * initEntityListener.accept(_) >> { MasterDetailScreen.InitEntityEvent event ->
            assert event.getEntity() != null
        }
    }
}