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

import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.data.value.CollectionContainerTableSource
import com.haulmont.cuba.gui.model.CollectionContainer
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.web.testmodel.datacontext.Foo
import spec.cuba.web.WebSpec

class CollectionContainerUsageTest extends WebSpec {

    private CollectionContainer<Foo> container
    private Table<Foo> table

    void setup() {
        container = dataContextFactory.createCollectionContainer(Foo)

        table = componentsFactory.createComponent(Table)
        table.addColumn(new Table.Column(metadata.getClassNN(Foo).getPropertyPath('name')))
        table.setTableSource(new CollectionContainerTableSource(this.container))
    }

    def "sort items"() {

        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')
        Foo foo3 = new Foo(name: 'foo3')

        when: "assigning a collection and selecting the first item"

        container.items = [foo1, foo2, foo3]
        table.setSelected(foo1)

        then: "container's current item is the first"

        container.items.indexOf(container.item) == 0

        when: "assigning a collection with reverse order"

        container.setItems(container.items.reverse())

        then: "container's current item is the last"

        container.items.indexOf(container.item) == 2
    }

    def "sort items in-place"() {

        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')
        Foo foo3 = new Foo(name: 'foo3')

        when: "assigning a collection and selecting the first item"

        container.items = [foo1, foo2, foo3]
        table.setSelected(foo1)

        then: "container's current item is the first"

        container.items.indexOf(container.item) == 0

        when: "sorting the collection in reverse order in-place"

        container.getMutableItems().sort { it.name }

        then: "container's current item is the first again"

        container.items.indexOf(container.item) == 0
    }

    def "filter items"() {

        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')
        Foo foo3 = new Foo(name: 'foo3')

        when: "assigning a collection and selecting the first item"

        container.items = [foo1, foo2, foo3]
        table.setSelected(foo1)

        then: "container's current item is the first"

        container.items.indexOf(container.item) == 0

        when: "assigning a collection with absent item"

        container.items = [foo2, foo3]

        then: "container has no current item"

        container.item == null
    }

    def "filter items in-place"() {

        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')
        Foo foo3 = new Foo(name: 'foo3')

        when: "assigning a collection and selecting the first item"

        container.items = [foo1, foo2, foo3]
        table.setSelected(foo1)

        then: "container's current item is the first"

        container.items.indexOf(container.item) == 0

        when: "removing the first item"

        container.mutableItems.remove(foo1)

        then: "container has no current item"

        container.item == null
    }

    def "table doesn't track container's item"() {

        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')

        container.items = [foo1, foo2]
        table.setSelected(foo1)

        when:

        container.item == foo2

        then:

        table.getSingleSelected() == foo1
    }

    def "add item in memory only"() {

        def modified = []
        DataContext context = dataContextFactory.createDataContext()
        context.addPreCommitListener({e ->
            modified.addAll(e.modifiedInstances)
        })

        Foo foo1 = new Foo(name: 'foo1')
        context.merge(foo1)
        container.items = [foo1]

        Foo foo2 = new Foo(name: 'foo2')

        when:

        container.mutableItems.add(foo2)
        table.setSelected(foo2)

        then:

        container.item == foo2

        when:

        context.commit()

        then:

        modified.size() == 1
        modified.contains(foo1)
    }

    def "add item and save it"() {

        def modified = []
        DataContext context = dataContextFactory.createDataContext()
        context.addPreCommitListener({e ->
            modified.addAll(e.modifiedInstances)
        })

        Foo foo1 = new Foo(name: 'foo1')
        context.merge(foo1)
        container.items = [foo1]

        Foo foo2 = new Foo(name: 'foo2')

        when:

        container.mutableItems.add(foo2)
        context.merge(foo2)
        table.setSelected(foo2)

        then:

        container.item == foo2

        when:

        context.commit()

        then:

        modified.size() == 2
        modified.containsAll(foo1, foo2)
    }

    def "remove item and save it"() {

        def modified = []
        DataContext context = dataContextFactory.createDataContext()
        context.addPreCommitListener({e ->
            modified.addAll(e.removedInstances)
        })

        Foo foo1 = new Foo(name: 'foo1')
        context.merge(foo1)
        container.items = [foo1]

        when:

        container.mutableItems.remove(foo1)
        context.remove(foo1)

        then:

        container.item == null

        when:

        context.commit()

        then:

        modified.size() == 1
        modified.contains(foo1)
    }

    def "remove item in memory only"() {

        def modified = []
        DataContext context = dataContextFactory.createDataContext()
        context.addPreCommitListener({e ->
            modified.addAll(e.removedInstances)
        })

        Foo foo1 = new Foo(name: 'foo1')
        context.merge(foo1)
        container.items = [foo1]

        when:

        container.mutableItems.remove(foo1)

        then:

        container.item == null

        when:

        context.commit()

        then:

        modified.size() == 0
    }

}
