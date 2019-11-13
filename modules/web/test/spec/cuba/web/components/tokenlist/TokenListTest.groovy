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

package spec.cuba.web.components.tokenlist

import com.google.common.collect.Lists
import com.haulmont.cuba.gui.components.TokenList
import com.haulmont.cuba.gui.components.data.options.ContainerOptions
import com.haulmont.cuba.gui.components.data.options.ListEntityOptions
import com.haulmont.cuba.gui.components.data.options.MapEntityOptions
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.entity.Constraint
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.tokenlist.screens.TokenListScreen

class TokenListTest extends UiScreenSpec {

    protected Constraint constraint1
    protected Constraint constraint2

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.tokenlist.screens', 'com.haulmont.cuba.web.app.main'])

        constraint1 = metadata.create(Constraint.class)
        constraint2 = metadata.create(Constraint.class)
    }

    def "List options are set to TokenList optionsContainer using setOptionsList"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def tokenListScreen = screens.create(TokenListScreen)
        tokenListScreen.show()

        def tokenList = tokenListScreen.tokenList
        def list = Lists.newArrayList(constraint1, constraint2)

        when: 'List options are set to TokenList using setOptionsList'
        tokenList.setOptionsList(list)

        then: 'No exception must be thrown'
        noExceptionThrown()

        and: 'Options class must be ListEntityOptions'
        tokenList.getOptions().getClass() == ListEntityOptions

        and: 'Items of options must be equal to List specified by the setOptionsList'
        ((ListEntityOptions) tokenList.getOptions()).getItemsCollection() == list
    }

    def "Map options are set to TokenList optionsContainer using setOptionsMap"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def tokenListScreen = screens.create(TokenListScreen)
        tokenListScreen.show()

        def tokenList = tokenListScreen.tokenList

        def map = new HashMap<String, Constraint>()
        map.put("Constraint 1", constraint1)
        map.put("Constraint 2", constraint2)

        when: 'Map options are set to TokenList using setOptionsMap'
        tokenList.setOptionsMap(map)

        then: 'No exception must be thrown'
        noExceptionThrown()

        and: 'Options class must be MapEntityOptions'
        tokenList.getOptions().getClass() == MapEntityOptions

        and: 'Items of options must be equal to Map specified by the setOptionsMap'
        ((MapEntityOptions) tokenList.getOptions()).getItemsCollection() == map
    }

    def "ContainerOptions are set to TokenList optionsContainer using setOptions"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        def tokenListScreen = screens.create(TokenListScreen)
        tokenListScreen.show()

        def tokenList = tokenListScreen.tokenList

        def container = dataComponents.createCollectionContainer(Constraint.class)
        container.setItems(Lists.newArrayList(constraint1, constraint2))
        def options = new ContainerOptions(container)

        when: 'ContainerOptions are set to TokenList using setOptions'
        tokenList.setOptions(options)

        then: 'No exception must be thrown'
        noExceptionThrown()

        and: 'Options class must be ContainerOptions'
        tokenList.getOptions().getClass() == ContainerOptions

        then: 'Options must be equal to initial options'
        tokenList.getOptions() == options

        and: 'Container of options must be equal to CollectionContainer'
        ((ContainerOptions) tokenList.getOptions()).getContainer() == container
    }
}
