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

package spec.cuba.web.datacontext

import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.web.container.CubaTestContainer
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testmodel.sales.Product
import com.haulmont.cuba.web.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

class PropertyContainerTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = CubaTestContainer.Common.INSTANCE

    private Metadata metadata
    private DataManager dataManager
    private DataComponents factory

    void setup() {
        metadata = cont.getBean(Metadata)
        dataManager = cont.getBean(DataManager)
        factory = cont.getBean(DataComponents)
    }

    def 'setItem for nested container leads to parent container PropertyChangedEvent'() {
        def masterContainer = factory.createInstanceContainer(OrderLine)
        def nestedContainer = factory.createInstanceContainer(Product, masterContainer, 'product')

        def masterPropertyChangeListener = Mock(Consumer)
        masterContainer.addItemPropertyChangeListener(masterPropertyChangeListener)

        def nestedItemChangeListener = Mock(Consumer)
        nestedContainer.addItemChangeListener(nestedItemChangeListener)

        def masterEntity = metadata.create(OrderLine)
        def product = metadata.create(Product)

        when: 'An entity is set to master container'
        masterContainer.setItem(masterEntity)

        then: 'The entity is accessible through nested container'
        nestedContainer.master.item == masterEntity

        when: 'An entity is set to nested container'
        nestedContainer.item = product

        then: 'Nested Container item change listener triggered, master property change triggered, item property changed'
        1 * nestedItemChangeListener.accept(_)
        1 * masterPropertyChangeListener.accept(_)
        masterEntity.product == product
    }
}
