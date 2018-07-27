/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spec.cuba.web.datacontext

import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.global.Sort
import com.haulmont.cuba.gui.model.CollectionContainer
import com.haulmont.cuba.gui.model.CollectionLoader
import com.haulmont.cuba.web.testmodel.sales.Product
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.WebSpec

class SortingTest extends WebSpec {

    private CollectionContainer<Product> container
    private CollectionLoader loader

    @Override
    void setup() {
        container = dataContextFactory.createCollectionContainer(Product)
        loader = dataContextFactory.createCollectionLoader()
        loader.setContainer(container)
    }

    @Override
    void cleanup() {
        TestServiceProxy.clear()
    }

    def "sort in memory when all data is loaded"() {
        def products = [
                new Product(name: 'p1', price: 10),
                new Product(name: 'p2', price: 20)
        ]
        def dataService = Mock(DataService)
        TestServiceProxy.mock(DataService, dataService)

        when:

        loader.setQuery('select p from test$Product p')
        loader.setFirstResult(0)
        loader.setMaxResults(3)
        loader.load()

        then:

        1 * dataService.loadList(_) >> products
        container.items[0].name == 'p1'

        when:

        container.getSorter().sort(Sort.by(Sort.Direction.DESC, 'name'))

        then:

        0 * dataService.loadList(_) >> products
        container.items[0].name == 'p2'
    }

    def "sort on middleware when not all data is loaded"() {
        def products = [
                new Product(name: 'p1', price: 10),
                new Product(name: 'p2', price: 20),
                new Product(name: 'p3', price: 30)
        ]
        def dataService = Mock(DataService)
        TestServiceProxy.mock(DataService, dataService)

        when:

        loader.setQuery('select p from test$Product p')
        loader.setFirstResult(0)
        loader.setMaxResults(3)
        loader.load()

        then:

        1 * dataService.loadList(_) >> products
        container.items[0].name == 'p1'

        when:

        container.getSorter().sort(Sort.by(Sort.Direction.DESC, 'name'))

        then:

        1 * dataService.loadList(_) >> products.sort { it.name }.reverse()
        container.items[0].name == 'p3'
    }
}
