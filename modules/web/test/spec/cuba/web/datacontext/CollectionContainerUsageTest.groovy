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

import com.haulmont.cuba.core.global.Sort
import com.haulmont.cuba.gui.components.Table
import com.haulmont.cuba.gui.components.data.table.ContainerTableItems
import com.haulmont.cuba.gui.model.CollectionChangeType
import com.haulmont.cuba.gui.model.CollectionContainer
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.gui.model.InstanceContainer
import com.haulmont.cuba.web.testmodel.datacontext.Foo
import spec.cuba.web.WebSpec

import java.util.function.Consumer

import static com.haulmont.cuba.gui.model.CollectionContainer.CollectionChangeEvent

class CollectionContainerUsageTest extends WebSpec {

    private CollectionContainer<Foo> container
    private Table<Foo> table

    void setup() {
        container = dataComponents.createCollectionContainer(Foo)

        table = uiComponents.create(Table)
        table.addColumn(new Table.Column(metadata.getClassNN(Foo).getPropertyPath('name')))
        table.setItems(new ContainerTableItems(this.container))
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

    def "sort items using Sorter"() {

        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')
        Foo foo3 = new Foo(name: 'foo3')

        container.items = [foo2, foo1, foo3]

        when:

        container.getSorter().sort(Sort.by(Sort.Order.asc('name')))

        then:

        container.items == [foo1, foo2, foo3]

        when:

        table.sort('name', Table.SortDirection.DESCENDING)

        then:

        container.items == [foo3, foo2, foo1]
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

        container.itemOrNull == null
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

        container.itemOrNull == null
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
        DataContext context = dataComponents.createDataContext()
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
        DataContext context = dataComponents.createDataContext()
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
        DataContext context = dataComponents.createDataContext()
        context.addPreCommitListener({e ->
            modified.addAll(e.removedInstances)
        })

        Foo foo1 = new Foo(name: 'foo1')
        entityStates.makeDetached(foo1)
        context.merge(foo1)
        container.items = [foo1]

        when:

        container.mutableItems.remove(foo1)
        context.remove(foo1)

        then:

        container.itemOrNull == null

        when:

        context.commit()

        then:

        modified.size() == 1
        modified.contains(foo1)
    }

    def "remove item in memory only"() {

        def modified = []
        DataContext context = dataComponents.createDataContext()
        context.addPreCommitListener({e ->
            modified.addAll(e.removedInstances)
        })

        Foo foo1 = new Foo(name: 'foo1')
        context.merge(foo1)
        container.items = [foo1]

        when:

        container.mutableItems.remove(foo1)

        then:

        container.itemOrNull == null

        when:

        context.commit()

        then:

        modified.size() == 0
    }

    def "remove single item with fired event"() {
        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')
        Foo foo3 = new Foo(name: 'foo3')

        def listener = Mock(Consumer)

        container.items = [foo1, foo2, foo3]
        container.addCollectionChangeListener(listener)

        when: "removing the first item"

        container.mutableItems.remove(foo1)

        then: "container fired event with item"

        1 * listener.accept(_) >> { List arguments ->
            CollectionChangeEvent event = arguments[0]
            assert event.changeType == CollectionChangeType.REMOVE_ITEMS
            assert event.changes == [foo1]
        }
    }

    def "remove multiple items with fired event"() {
        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')
        Foo foo3 = new Foo(name: 'foo3')

        def listener = Mock(Consumer)

        container.items = [foo1, foo2, foo3]
        container.addCollectionChangeListener(listener)

        when: "remove 2 items"

        container.mutableItems.removeAll([foo2, foo3])

        then: "container fired event with items"

        1 * listener.accept(_) >> { List arguments ->
            CollectionChangeEvent event = arguments[0]
            assert event.changeType == CollectionChangeType.REMOVE_ITEMS
            assert event.changes == [foo2, foo3]
        }
    }

    def "items added to mutableItems produce PropertyChangeEvent"() {
        Foo foo1 = new Foo(name: 'foo1')

        def listener = Mock(Consumer)

        container.mutableItems.add(foo1)
        container.addItemPropertyChangeListener(listener)

        when:
        foo1.name = '111'

        then:
        1 * listener.accept(_) >> { List arguments ->
            InstanceContainer.ItemPropertyChangeEvent event = arguments[0]
            assert event.item == foo1
            assert event.property == 'name'
            assert event.value == '111'
        }
    }

    def "PropertyChangedEvent is not lost after selecting another item"() {
        Foo foo1 = new Foo(name: 'foo1')
        Foo foo2 = new Foo(name: 'foo2')

        def listener = Mock(Consumer)

        container.items = [foo1, foo2]
        container.addItemPropertyChangeListener(listener)

        when:

        container.setItem(foo1)
        container.setItem(foo2)
        foo1.name = 'foo11'

        then:

        1 * listener.accept(_) >> { List arguments ->
            InstanceContainer.ItemPropertyChangeEvent event = arguments[0]
            assert event.item == foo1
            assert event.property == 'name'
            assert event.value == 'foo11'
        }
    }
}