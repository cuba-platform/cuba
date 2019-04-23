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


import com.haulmont.cuba.client.testsupport.TestSupport
import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.sys.persistence.CubaEntityFetchGroup
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.gui.model.impl.NoopDataContext
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserRole
import com.haulmont.cuba.web.testmodel.sales.*
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import org.eclipse.persistence.internal.queries.EntityFetchGroup
import org.eclipse.persistence.queries.FetchGroupTracker
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DataContextTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataComponents factory
    private EntityStates entityStates
    private Metadata metadata

    void setup() {
        factory = cont.getBean(DataComponents)
        metadata = cont.getBean(Metadata)
        entityStates = cont.getBean(EntityStates)
    }

    void cleanup() {
        TestServiceProxy.clear()
    }

    def "merge equal instances"() throws Exception {
        DataContext context = factory.createDataContext()

        when: "merging instance first time"

        User user1 = new User(login: 'u1', name: 'User 1')

        User mergedUser1 = context.merge(user1)

        User entityInContext = context.find(User, user1.getId())

        then: "returned another instance"

        entityInContext == mergedUser1
        entityInContext.is(mergedUser1)
        !entityInContext.is(user1)

        when: "merging another instance with the same id"

        User user11 = new User(id: user1.id, login: 'u11', name: 'User 11')

        User mergedUser11 = context.merge(user11)

        then: "returned instance which was already in context"

        mergedUser11.is(mergedUser1)
        !mergedUser11.is(user11)
    }

    def "merge graph"() throws Exception {
        DataContext context = factory.createDataContext()

        when: "merging graph containing different instances with same ids"

        // an object being merged
        User user1 = new User(login: 'u1', name: 'User 1', userRoles: [])
        makeDetached(user1, ['login', 'name', 'userRoles'])
        Role role1 = new Role(name: 'Role1')
        makeDetached(role1, ['name'])
        UserRole user1Role1 = new UserRole(user: user1, role: role1)
        makeDetached(user1Role1, ['user', 'role'])
        user1.userRoles.add(user1Role1)

        // somewhere in the object graph another object with the same id
        User user11 = new User(id: user1.id, login: 'u11', name: 'User 11')
        makeDetached(user11, ['login', 'name'])
        UserRole user11Role1 = new UserRole(user: user11, role: role1)
        makeDetached(user11Role1, ['user', 'role'])
        user1.getUserRoles().add(user11Role1)

        //  user1
        //      user1Role1
        //          role1
        //      user11Role1
        //          user11 (=user1)
        //          role1

        User mergedUser1 = context.merge(user1)

        then: "context contains first merged instance"

        !mergedUser1.is(user1)
        context.find(User, user1.id).is(mergedUser1)

        and: "merged instance has local attributes of the second object"

        mergedUser1.login == "u11"
        mergedUser1.name == "User 11"

        and:

        context.find(UserRole, user1Role1.id) == user1Role1
        !context.find(UserRole, user1Role1.id).is(user1Role1)

        context.find(UserRole, user11Role1.id) == user11Role1
        !context.find(UserRole, user11Role1.id).is(user11Role1)

        context.find(Role, role1.id) == role1
        !context.find(Role, role1.id).is(role1)

        and: "second object in the graph is now the same instance"

        mergedUser1.is(mergedUser1.userRoles[1].user)

        and: "context is not changed on merge"

        !context.hasChanges()
    }

    def "merge equal instances when first one has null collection"() throws Exception {
        DataContext context = factory.createDataContext()

        when: "merging two equal instances"

        User user1 = new User()
        user1.login = "u1"
        user1.name = "User 1"

        Role role1 = new Role()
        role1.name = "Role 1"

        User user11 = new User()
        user11.login = "u11"
        user11.name = "User 11"
        user11.id = user1.id

        UserRole user11Role1 = new UserRole()
        user11Role1.user = user11
        user11Role1.role = role1
        user11.userRoles = new ArrayList<>()
        user11.userRoles.add(user11Role1)

        //  user1
        //  user11 (=user1)
        //      user11Role1
        //          role1

        User mergedUser1 = context.merge(user1)

        User mergedUser11 = context.merge(user11)

        then: "context contains first instance"

        mergedUser1 == user1
        mergedUser11.is(mergedUser1)
        context.find(User, user1.id).is(mergedUser1)

        and: "merged instance has local attributes of the second object"

        mergedUser1.login == "u11"
        mergedUser1.name == "User 11"

        and: "merged instance has collection of the second object"

        mergedUser1.userRoles != null
        mergedUser1.userRoles[0].user.is(mergedUser1)

        and:

        context.find(UserRole, user11Role1.id) == user11Role1
        context.find(Role, role1.id) == role1
    }

    def "merge new"() throws Exception {
        DataContext context = factory.createDataContext()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> Collections.emptySet()
        })

        when: "merging and committing graph of new instances"

        User user1 = new User()
        user1.login = "u1"
        user1.name = "User 1"
        user1.userRoles = new ArrayList<>()

        Role role1 = new Role()
        role1.name = "Role 1"

        Role role2 = new Role()
        role1.name = "Role 2"

        UserRole user1Role1 = new UserRole()
        user1Role1.user = user1
        user1Role1.role = role1

        user1.userRoles.add(user1Role1)

        UserRole user1Role2 = new UserRole()
        user1Role2.user = user1
        user1Role2.role = role2

        user1.userRoles.add(user1Role2)

        context.merge(user1)

        Collection modified = []
        Collection removed = []

        context.addPreCommitListener({ e ->
            modified.addAll(e.modifiedInstances)
            removed.addAll(e.removedInstances)
        })

        context.commit()

        then: "all merged instances are committed"

        modified.size() == 5
        modified.contains(user1)
        modified.contains(role1)
        modified.contains(role2)
        modified.contains(user1Role1)
        modified.contains(user1Role2)

        and:

        removed.isEmpty()
    }

    def "merge and modify"() throws Exception {

        DataContext context = factory.createDataContext()

        when: "merge graph then modify and remove some instances"

        User user1 = new User(login: 'u1', name: 'User 1', userRoles: [])
        makeDetached(user1)

        Role role1 = new Role(name: 'Role 1')
        makeDetached(role1)

        Role role2 = new Role(name: 'Role 2')
        makeDetached(role2)

        UserRole user1Role1 = new UserRole(user: user1, role: role1)
        makeDetached(user1Role1)
        user1.userRoles.add(user1Role1)

        UserRole user1Role2 = new UserRole(user: user1, role: role2)
        makeDetached(user1Role2)
        user1.userRoles.add(user1Role2)

        context.merge(user1)

        context.find(Role, role1.id).name = 'Role 1 modified'

        user1.userRoles.remove(user1Role2)
        context.remove(user1Role2)

        Collection modified = []
        Collection removed = []

        context.addPreCommitListener({ e->
            modified.addAll(e.modifiedInstances)
            removed.addAll(e.removedInstances)
        })

        def committed = context.commit()

        then:

        modified.size() == 2
        modified.contains(role1)
        modified.contains(user1)

        removed.size() == 1
        removed.contains(user1Role2)

        committed.containsAll([role1, user1])
        !committed.contains(user1Role2)
    }

    def "remove"() {
        DataContext context = factory.createDataContext()

        User user1 = new User(login: 'u1', name: 'User 1')
        makeDetached(user1)
        context.merge(user1)

        when:

        context.remove(user1)

        then:

        context.find(User, user1.id) == null

        when:

        def removed = []
        context.addPreCommitListener { e -> removed.addAll(e.removedInstances) }
        context.commit()

        then:

        removed.contains(user1)
    }

    def "evict"() {
        DataContext context = factory.createDataContext()

        User user1 = new User(login: 'u1', name: 'User 1')
        makeDetached(user1)
        context.merge(user1)

        when:

        context.evict(user1)

        then:

        context.find(User, user1.id) == null

        when:

        def removed = []
        context.addPreCommitListener { e -> removed.addAll(e.removedInstances) }
        context.commit()

        then:

        removed.isEmpty()
    }

    def "commit returns different reference"() {
        DataContext context = factory.createDataContext()

        Product product1 = new Product(name: "p1", price: 100)
        Product product2 = new Product(name: "p2", price: 200)
        OrderLine line = new OrderLine(quantity: 10, product: product1)
        makeDetached(product1, product2, line)
        def line1 = context.merge(line)

        Collection committed = []
        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> { CommitContext cc ->
                committed.addAll(cc.commitInstances)
                def entities = TestServiceProxy.getDefault(DataService).commit(cc)
                entities.find { it == line }.product = TestSupport.reserialize(product2)
                entities
            }
        })

        when:

        line1.quantity = 20
        context.commit()

        then:

        def line2 = context.find(OrderLine, line.id)
        line2.quantity == 20
        line2.product == product2

    }

    def "merged objects always have observable collections"() {

        def dataContext = factory.createDataContext()

        Order order1 = makeSaved(new Order(number: "111", orderLines: []))
        OrderLine orderLine11 = makeSaved(new OrderLine(quantity: 10))
        orderLine11.order = order1
        order1.orderLines.add(orderLine11)

        OrderLine orderLine12 = makeSaved(new OrderLine(quantity: 20))
        orderLine12.order = order1
        order1.orderLines.add(orderLine12)

        Order order2 = makeSaved(new Order(id: order1.id, number: "222", orderLines: []))
        OrderLine orderLine2 = makeSaved(new OrderLine(id: orderLine11.id, order: order2, quantity: 10))
        order2.orderLines.add(orderLine2)

        when:

        Order order1_1 = dataContext.merge(order1)

        then:

        order1_1.orderLines instanceof com.haulmont.cuba.gui.model.impl.ObservableList

        when:

        Order order2_1 = dataContext.merge(order2)

        then:

        order2_1.is(order1_1)
        order2_1.orderLines instanceof com.haulmont.cuba.gui.model.impl.ObservableList

        when:

        order2_1.orderLines.add(dataContext.merge(orderLine12))

        then:

        order2_1.orderLines[1].order.is(order2_1)
        dataContext.isModified(order2_1)
    }

    def "removed object is removed from collections too"() {

        def dataContext = factory.createDataContext()

        Order order1 = makeSaved(new Order(number: "111", orderLines: []))
        OrderLine orderLine11 = makeSaved(new OrderLine(quantity: 10))
        orderLine11.order = order1
        order1.orderLines.add(orderLine11)

        OrderLine orderLine12 = makeSaved(new OrderLine(quantity: 20))
        orderLine12.order = order1
        order1.orderLines.add(orderLine12)

        Order order1_1 = dataContext.merge(order1)
        OrderLine orderLine12_1 = order1_1.orderLines[1]

        when:

        dataContext.remove(orderLine12_1)

        then:

        order1_1.orderLines.size() == 1
        !order1_1.orderLines.contains(orderLine12_1)
    }

    def "system fields are preserved on merge"() {

        def dataContext = factory.createDataContext()

        Order order1 = makeSaved(new Order(number: "111"))
        ((FetchGroupTracker) order1)._persistence_setFetchGroup(new CubaEntityFetchGroup(['id', 'version', 'number']))

        Order order2 = makeSaved(new Order(id: order1.id, number: "111", orderLines: []))
        ((FetchGroupTracker) order2)._persistence_setFetchGroup(new CubaEntityFetchGroup(['id', 'version', 'number', 'orderLines']))
        OrderLine orderLine21 = makeSaved(new OrderLine(quantity: 10, order: order2))
        order2.orderLines.add(orderLine21)

        when:

        Order order1_1 = dataContext.merge(order1)
        Order order2_1 = dataContext.merge(order2)

        then:

        order2_1 == order1
        order2_1.orderLines.size() == 1
        ((FetchGroupTracker) order2_1)._persistence_getFetchGroup().attributeNames.containsAll(['id', 'version', 'number', 'orderLines'])

    }

    def "commit delegate"() {

        Order order1 = makeSaved(new Order(number: "111"))

        def dataContext = factory.createDataContext()
        dataContext.setCommitDelegate { CommitContext cc ->
            [makeSaved(new Order(id: order1.id, number: 'committed through delegate'))].toSet()
        }

        def order2 = dataContext.merge(order1)

        when:

        order2.number = '222'
        dataContext.commit()

        then:

        dataContext.find(Order, order1.id).number == 'committed through delegate'
    }

    def "read-only context"() {
        def dataContext = new NoopDataContext()
        def order1 = new Order(number: "111")

        when:

        def order = dataContext.merge(order1)

        then:

        order.is(order1)
        !dataContext.hasChanges()
        !dataContext.isModified(order1)
    }

    def "change of embedded entity makes host entity changed"() {
        Address address1 = new Address(city: "Samara", zip: "111")
        Customer customer1 = new Customer(name: "cust1", address: address1)
        makeDetached(customer1)

        def dataContext = factory.createDataContext()

        when:

        Customer customer = dataContext.merge(customer1)

        then:

        !dataContext.hasChanges()

        when:

        customer.address.zip = '222'

        then:

        dataContext.isModified(customer.address)
        dataContext.isModified(customer)
    }

    def "evicted embedded entity has no PropertyChangeListener"() {
        Address address1 = new Address(city: "Samara", zip: "111")
        Customer customer1 = new Customer(name: "cust1", address: address1)
        makeDetached(customer1)

        def dataContext = factory.createDataContext()

        when:

        Customer customer = dataContext.merge(customer1)

        then:

        !customer.address.__propertyChangeListeners.isEmpty()

        when:

        dataContext.evict(customer)

        then:

        customer.address.__propertyChangeListeners.isEmpty()
    }

    def "removed embedded entity has no PropertyChangeListener"() {
        Address address1 = new Address(city: "Samara", zip: "111")
        Customer customer1 = new Customer(name: "cust1", address: address1)
        makeDetached(customer1)

        def dataContext = factory.createDataContext()

        when:

        Customer customer = dataContext.merge(customer1)

        then:

        !customer.address.__propertyChangeListeners.isEmpty()

        when:

        dataContext.remove(customer)

        then:

        customer.address.__propertyChangeListeners.isEmpty()
    }

    def "create entity"() {

        def dataContext = factory.createDataContext()

        when:

        Customer customer = dataContext.create(Customer)

        then:

        dataContext.hasChanges()
        dataContext.isModified(customer)
        dataContext.find(Customer, customer.id).is(customer)
    }

    def "remove of newly created entity does not commit it"() {
        DataContext context = factory.createDataContext()

        Order order = new Order(number: '111')
        context.merge(order)

        when:

        context.remove(order)

        then:

        context.find(Order, order.id) == null

        when:

        def removed = []
        context.addPreCommitListener { e -> removed.addAll(e.removedInstances) }
        context.commit()

        then:

        !removed.contains(order)
    }

    private void makeDetached(def entity) {
        entityStates.makeDetached(entity)
    }

    private void makeDetached(def entity, List<String> attributes) {
        entityStates.makeDetached(entity)
        ((FetchGroupTracker) entity)._persistence_setFetchGroup(
                new EntityFetchGroup(['id', 'version', 'deleteTs'] + attributes))

    }

    private void makeDetached(Entity... entities) {
        for (Entity entity : entities) {
            entityStates.makeDetached(entity)
        }
    }

    private static <T> T makeSaved(T entity) {
        TestServiceProxy.getDefault(DataService).commit(new CommitContext().addInstanceToCommit(entity))[0] as T
    }
}
