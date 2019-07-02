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

package spec.cuba.web.dataloadcoordinator

import com.haulmont.cuba.gui.components.DataLoadCoordinator
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.app.main.MainScreen
import com.haulmont.cuba.web.testmodel.petclinic.Address
import com.haulmont.cuba.web.testmodel.petclinic.Owner
import com.haulmont.cuba.web.testmodel.petclinic.OwnerCategory
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.dataloadcoordinator.screens.*
import spock.lang.Unroll

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class DataLoadCoordinatorFacetTest extends UiScreenSpec {

    def setup() {
        exportScreensPackages(['spec.cuba.web.dataloadcoordinator.screens', 'com.haulmont.cuba.web.app.main'])
    }

    @Unroll
    def "manual configuration"() {
        def screens = vaadinUi.screens
        screens.create(MainScreen, OpenMode.ROOT).show()

        when: "show screen"

        def screen = screens.create(screenClass)
        screen.show()

        then: "facet is created and injected"

        screen.window.getFacet('dlc') instanceof DataLoadCoordinator
        screen.dlc != null

        and: "master loader is triggered once"

        screen.events.size() == 1
        screen.events[0].loader == 'ownersDl'
        screen.events[0].loadContext.query.parameters.isEmpty()

        when: "master item is selected"

        screen.events.clear()
        screen.ownersDc.setItem(screen.ownersDc.getItems()[0])

        then: "slave loader is triggered"

        screen.events.size() == 1
        screen.events[0].loader == 'petsDl'
        screen.events[0].loadContext.query.parameters.size() == 1
        screen.events[0].loadContext.query.parameters['owner'] == screen.ownersDc.getItem()

        when: "entity filter field value is set"

        screen.events.clear()
        def category = new OwnerCategory()
        screen.categoryFilterField.setValue(category)

        then: "master loader is triggered, its item is cleared so slave is triggered too"

        screen.events.size() == 2
        screen.events[0].loader == 'ownersDl'
        screen.events[0].loadContext.query.parameters['category'] == category
        screen.events[1].loader == 'petsDl'

        when: "string filter field is set"

        screen.events.clear()
        screen.nameFilterField.setValue("o")

        then: "slave is triggered and parameter is wrapped for case-insensitive like"

        screen.events.size() == 1
        screen.events[0].loader == 'ownersDl'
        screen.events[0].loadContext.query.parameters['name'] == '(?i)%o%'

        where:

        screenClass << [DlcManualScreen, DlcManualNoParamScreen, DlcManualWithLoadDataBeforeShowScreen]
    }

    def "auto configuration"() {
        def screens = vaadinUi.screens
        screens.create(MainScreen, OpenMode.ROOT).show()

        when: "show screen"

        def screen = screens.create(DlcAutoScreen)
        screen.show()

        then: "facet is created and injected"

        screen.window.getFacet('dlc') instanceof DataLoadCoordinator
        screen.dlc != null

        and: "master loader is triggered once"

        screen.events.size() == 1
        screen.events[0].loader == 'ownersDl'
        screen.events[0].loadContext.query.parameters.isEmpty()

        when: "master item is selected"

        screen.events.clear()
        screen.ownersDc.setItem(screen.ownersDc.getItems()[0])

        then: "slave loader is triggered"

        screen.events.size() == 1
        screen.events[0].loader == 'petsDl'
        screen.events[0].loadContext.query.parameters.size() == 1
        screen.events[0].loadContext.query.parameters['container_ownersDc'] == screen.ownersDc.getItem()

        when: "entity filter field value is set"

        screen.events.clear()
        def category = new OwnerCategory()
        screen.categoryFilterField.setValue(category)

        then: "master loader is triggered, its item is cleared so slave is triggered too"

        screen.events.size() == 2
        screen.events[0].loader == 'ownersDl'
        screen.events[0].loadContext.query.parameters['component_categoryFilterField'] == category
        screen.events[1].loader == 'petsDl'

        when: "string filter field is set"

        screen.events.clear()
        screen.nameFilterField.setValue("o")

        then: "slave is triggered and parameter is wrapped for case-insensitive like"

        screen.events.size() == 1
        screen.events[0].loader == 'ownersDl'
        screen.events[0].loadContext.query.parameters['component_nameFilterField'] == '(?i)%o%'
    }

    def "auto configuration of screen with fragment"() {
        def screens = vaadinUi.screens
        screens.create(MainScreen, OpenMode.ROOT).show()

        when: "show screen"

        def screen = screens.create(DlcAutoWithFragmentScreen)
        screen.setEntityToEdit(new Owner(name: 'Joe', address: new Address(postcode: '123')))
        screen.show()

        then: "fragment's facet is created and injected"

        def screenFragment = screen.addressFragment
        screenFragment.getFragment().getFacet('addressDlc') != null
        screenFragment.addressDlc != null

        and: "master loader is triggered once"

        screenFragment.events.size() == 1
        screenFragment.events[0].loader == 'countriesDl'
        screenFragment.events[0].loadContext.query.parameters.isEmpty()

        when: "master item is selected"

        screenFragment.events.clear()
        screenFragment.countriesDc.setItem(screenFragment.countriesDc.getItems()[0])

        then: "slave loader is triggered"

        screenFragment.events.size() == 1
        screenFragment.events[0].loader == 'citiesDl'
        screenFragment.events[0].loadContext.query.parameters.size() == 1
        screenFragment.events[0].loadContext.query.parameters['container_countriesDc'] == screenFragment.countriesDc.getItem()
    }

}