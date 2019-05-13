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

package spec.cuba.web.components.optionslist

import com.haulmont.cuba.gui.components.OptionsList
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testmodel.sales.Product
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.optionslist.screens.OptionsListTestScreen

import java.util.function.Consumer

class OptionsListTest extends UiScreenSpec {

    @SuppressWarnings(['GroovyAssignabilityCheck', 'GroovyAccessibility'])
    void setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        exportScreensPackages(['spec.cuba.web.components.optionslist.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def 'List value is propagated to ValueSource from multiselect OptionsList'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsListTestScreen)
        screen.show()

        def optionsList = screen.optionsList as OptionsList<List<OrderLine>, OrderLine>
        def orderLine = screen.allOrderLinesDc.getItems().get(0)
        def orderLinesDc = screen.orderLinesDc

        when: 'List value is set to OptionsList'
        optionsList.setValue([orderLine])

        then: 'ValueSource is updated'
        orderLinesDc.items.size() == 1 && orderLinesDc.items.contains(orderLine)
    }

    def 'List value is propagated to multiselect OptionsList from ValueSource'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsListTestScreen)
        screen.show()

        def optionsList = screen.optionsList as OptionsList<List<OrderLine>, OrderLine>
        def orderLine = screen.allOrderLinesDc.getItems().get(0)

        when: 'List value is set to ValueSource'
        screen.orderLinesDc.mutableItems.add(orderLine)

        then: 'OptionsList is updated'
        optionsList.value.size() == 1 && optionsList.value.contains(orderLine)
    }

    def 'Set value is propagated to ValueSource from multiselect OptionsList'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsListTestScreen)
        screen.show()

        def optionsList = screen.setOptionsList as OptionsList<Set<Product>, Product>
        def product = screen.allProductsDc.items.get(0)
        def catalog = screen.catalogDc.item

        when: 'Set value is set to OptionsList'
        optionsList.setValue(Collections.singleton(product))

        then: 'ValueSource is updated'
        catalog.products.size() == 1 && catalog.products.contains(product)
    }

    def 'Value is propagated to ValueSource from single select OptionsList'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsListTestScreen)
        screen.show()

        def optionsList = screen.singleOptionsList
        def product = screen.allProductsDc.items.get(0)

        when: 'A value is set to single select OptionsList'
        optionsList.setValue(product)

        then: 'Property container is updated'
        screen.productDc.item == product
    }

    def 'Value is propagated to single select OptionsList from ValueSource'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsListTestScreen)
        screen.show()

        def singleOptionsList = screen.singleOptionsList
        def product = screen.allProductsDc.items.get(0)

        when: 'A value is set to property container'
        screen.orderLineDc.item.product = product

        then: 'Single select OptionsList is updated'
        singleOptionsList.value == product
    }

    def 'ValueChangeEvent is fired exactly once for OptionsList'() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create('mainWindow', OpenMode.ROOT)
        screens.show(mainWindow)

        def screen = screens.create(OptionsListTestScreen)
        screen.show()

        def optionsList = screen.optionsList as OptionsList<List<OrderLine>, OrderLine>
        def requiredOptionsList = screen.requiredOptionsList as OptionsList<List<OrderLine>, OrderLine>
        def singleOptionsList = screen.singleOptionsList as OptionsList<Product, Product>

        def valueChangeListener = Mock(Consumer)
        def requiredValueChangeListener = Mock(Consumer)
        def singleValueChangeListener = Mock(Consumer)

        optionsList.addValueChangeListener(valueChangeListener)
        requiredOptionsList.addValueChangeListener(requiredValueChangeListener)
        singleOptionsList.addValueChangeListener(singleValueChangeListener)

        def order = screen.orderDc.item
        def orderLine = screen.orderLineDc.item

        def olOption = screen.allOrderLinesDc.items.get(0)
        def secondOlOption = screen.allOrderLinesDc.items.get(1)

        def productOption = screen.allProductsDc.items.get(0)

        when: 'A value is set to OptionsList'
        optionsList.setValue([olOption])
        singleOptionsList.setValue(productOption)

        then: 'ValueChangeEvent is fired once'
        1 * valueChangeListener.accept(_)
        1 * requiredValueChangeListener.accept(_)
        1 * singleValueChangeListener.accept(_)

        when: 'ValueSource is changed'
        screen.orderLinesDc.mutableItems.add(secondOlOption)

        then: 'ValueChangeEvent is fired once'
        1 * valueChangeListener.accept(_)
        1 * requiredValueChangeListener.accept(_)

        when: 'Entity property value is set to null'
        order.orderLines = null
        orderLine.product = null

        then: 'ValueChangeEvent is fired once'
        1 * valueChangeListener.accept(_)
        1 * requiredValueChangeListener.accept(_)
        1 * singleValueChangeListener.accept(_)
    }
}
