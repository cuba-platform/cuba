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

package spec.cuba.web.components.optionsgroup

import com.haulmont.cuba.gui.components.OptionsGroup
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testmodel.sales.Product
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.optionsgroup.screens.OptionsGroupTestScreen

import java.util.function.Consumer

class OptionsGroupTest extends UiScreenSpec {

    @SuppressWarnings(['GroovyAssignabilityCheck', 'GroovyAccessibility'])
    void setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        exportScreensPackages(['spec.cuba.web.components.optionsgroup.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def 'List value is propagated to ValueSource from multiselect OptionsGroup'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsGroupTestScreen)
        screen.show()

        def optionsGroup = screen.optionsGroup as OptionsGroup<List<OrderLine>, OrderLine>
        def orderLine = screen.allOrderLinesDc.getItems().get(0)

        def orderLinesDc = screen.orderLinesDc

        when: 'List value is set to OptionsGroup'
        optionsGroup.setValue([orderLine])

        then: 'ValueSource is updated'
        orderLinesDc.items.size() == 1 && orderLinesDc.items.contains(orderLine)
    }

    def 'List value is propagated to multiselect OptionsGroup from ValueSource'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsGroupTestScreen)
        screen.show()

        def optionsGroup = screen.optionsGroup as OptionsGroup<List<OrderLine>, OrderLine>
        def orderLine = screen.allOrderLinesDc.getItems().get(0)

        when: 'List value is set to ValueSource'
        screen.orderLinesDc.mutableItems.add(orderLine)

        then: 'OptionsGroup is updated'
        optionsGroup.value.size() == 1 && optionsGroup.value.contains(orderLine)
    }

    def 'Set value is propagated to ValueSource from multiselect OptionsGroup'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsGroupTestScreen)
        screen.show()

        def optionsGroup = screen.setOptionsGroup as OptionsGroup<Set<Product>, Product>
        def product = screen.allProductsDc.items.get(0)
        def catalog = screen.catalogDc.item

        when: 'Set value is set to OptionsGroup'
        optionsGroup.setValue(Collections.singleton(product))

        then: 'ValueSource is updated'
        catalog.products.size() == 1 && catalog.products.contains(product)
    }

    def 'Value is propagated to ValueSource from single select OptionsGroup'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsGroupTestScreen)
        screen.show()

        def optionsGroup = screen.singleOptionGroup
        def product = screen.allProductsDc.items.get(0)

        when: 'A value is set to single select OptionsGroup'
        optionsGroup.setValue(product)

        then: 'Property container is updated'
        screen.productDc.item == product
    }

    def 'Value is propagated to single select OptionsGroup from ValueSource'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsGroupTestScreen)
        screen.show()

        def singleOptionGroup = screen.singleOptionGroup
        def product = screen.allProductsDc.items.get(0)

        when: 'A value is set to property container'
        screen.orderLineDc.item.product = product

        then: 'Single select OptionsGroup is updated'
        singleOptionGroup.value == product
    }

    def 'ValueChangeEvent is fired exactly once for OptionsGroup'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def testScreen = screens.create(OptionsGroupTestScreen)
        testScreen.show()

        def optionsGroup = testScreen.optionsGroup as OptionsGroup<List<OrderLine>, OrderLine>
        def singleOptionsGroup = testScreen.singleOptionGroup as OptionsGroup<Product, Product>

        def valueChangeListener = Mock(Consumer)
        def singleValueChangeListener = Mock(Consumer)

        optionsGroup.addValueChangeListener(valueChangeListener)
        singleOptionsGroup.addValueChangeListener(singleValueChangeListener)

        def order = testScreen.orderDc.item
        def orderLine = testScreen.orderLineDc.item

        def olOption = testScreen.allOrderLinesDc.items.get(0)
        def secondOlOption = testScreen.allOrderLinesDc.items.get(1)

        def productOption = testScreen.allProductsDc.items.get(0)

        when: 'A value is set to OptionsGroup'
        optionsGroup.setValue([olOption])
        singleOptionsGroup.setValue(productOption)

        then: 'ValueChangeEvent is fired once'
        1 * valueChangeListener.accept(_)
        1 * singleValueChangeListener.accept(_)

        when: 'ValueSource is changed'
        testScreen.orderLinesDc.mutableItems.add(secondOlOption)

        then: 'ValueChangeEvent is fired once'
        1 * valueChangeListener.accept(_)

        when: 'Entity property value is set to null'
        order.orderLines = null
        orderLine.product = null

        then: 'ValueChangeEvent is fired once'
        1 * valueChangeListener.accept(_)
        1 * singleValueChangeListener.accept(_)
    }
}
