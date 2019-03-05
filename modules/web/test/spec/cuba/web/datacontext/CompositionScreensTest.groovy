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

package spec.cuba.web.datacontext

import com.haulmont.cuba.gui.config.WindowConfig
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.gui.screen.UiControllerUtils
import com.haulmont.cuba.gui.sys.UiControllersConfiguration
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import org.springframework.core.type.classreading.MetadataReaderFactory
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.datacontext.screens.OrderScreen
import spock.lang.Unroll

class CompositionScreensTest extends UiScreenSpec {

    @SuppressWarnings(["GroovyAssignabilityCheck", "GroovyAccessibility"])
    void setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        def windowConfig = cont.getBean(WindowConfig)

        def configuration = new UiControllersConfiguration()
        configuration.applicationContext = cont.getApplicationContext()
        configuration.metadataReaderFactory = cont.getBean(MetadataReaderFactory)
        configuration.basePackages = ['spec.cuba.web.datacontext.screens', 'com.haulmont.cuba.web.app.main']

        windowConfig.configurations = [configuration]
        windowConfig.initialized = false
    }

    @SuppressWarnings(["GroovyAccessibility"])
    def cleanup() {
        TestServiceProxy.clear()

        def windowConfig = cont.getBean(WindowConfig)
        windowConfig.configurations = []
        windowConfig.initialized = false
    }

    @Unroll
    def "create and immediate edit of the same nested instance"(boolean explicitParentDc) {

        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def orderScreen = screens.create(OrderScreen)
        def order = metadata.create(Order)
        orderScreen.order = order
        orderScreen.show()

        def orderScreenDc = UiControllerUtils.getScreenData(orderScreen).dataContext

        when: "create entity"

        def lineScreenForCreate = orderScreen.buildLineScreenForCreate(explicitParentDc)
        lineScreenForCreate.show()

        lineScreenForCreate.changeCommitAndClose(1)

        then:

        def order1 = orderScreenDc.find(Order, order.id)
        order1.orderLines.size() == 1
        def line1 = order1.orderLines[0]
        line1.order.is(order1)

        when: "edit same entity"

        def lineScreenForEdit = orderScreen.buildLineScreenForEdit(explicitParentDc)
        lineScreenForEdit.show()

        lineScreenForEdit.changeCommitAndClose(2)

        then:

        def order2 = orderScreenDc.find(Order, order.id)
        order2.is(order1)
        order2.orderLines.size() == 1
        def line2 = order2.orderLines[0]
        line2.is(line1)
        line2.order.is(order2)

        where:

        explicitParentDc << [true, false]
    }
}
