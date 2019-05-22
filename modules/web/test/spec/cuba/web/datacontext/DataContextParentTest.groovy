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

import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserRole
import com.haulmont.cuba.web.container.CubaTestContainer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import org.eclipse.persistence.internal.queries.EntityFetchGroup
import org.eclipse.persistence.queries.FetchGroupTracker
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
class DataContextParentTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = CubaTestContainer.Common.INSTANCE

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

    def "child context creation and merging instance from parent"() throws Exception {

        DataContext context = factory.createDataContext()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> Collections.emptySet()
        })

        when: "merge instance into parent context"

        User user1 = makeSaved(new User(login: 'u1', name: 'User 1', userRoles: []))
        Role role1 = makeSaved(new Role(name: 'Role 1'))
        UserRole user1Role1 = makeSaved(new UserRole(user: user1, role: role1))
        user1.userRoles.add(user1Role1)

        def parentUser = context.merge(user1)
        def parentRole = context.find(Role, role1.id)
        def parentUserRole = context.find(UserRole, user1Role1.id)

        then:

        !context.hasChanges()

        when: "create child context"

        DataContext childContext = factory.createDataContext()
        childContext.setParent(context)

        then: "child context is clean and empty"

        !context.hasChanges()
        !childContext.hasChanges()
        childContext.getAll().isEmpty()

        when: "merge instance from parent to child context"

        childContext.merge(parentUser)

        def childUser = childContext.find(User, user1.id)
        def childRole = childContext.find(Role, role1.id)
        def childUserRole = childContext.find(UserRole, user1Role1.id)

        then: "child context has correct graph of different instances"

        childUser == parentUser
        !childUser.is(parentUser)
        childUser.userRoles[0] == childUserRole

        childRole == parentRole
        !childRole.is(parentRole)

        childUserRole == parentUserRole
        !childUserRole.is(parentUserRole)

        childUserRole.user?.is(childUser)
        childUserRole.role?.is(childRole)
    }

    def "modification of collection in child context"() throws Exception {

        DataContext ctx1 = factory.createDataContext()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> Collections.emptySet()
        })

        when: "merge instance into parent and child contexts"

        User user1_ctx1 = ctx1.merge(new User(login: 'u1', name: 'User1'))

        DataContext ctx2 = factory.createDataContext()

        ctx2.setParent(ctx1)

        ctx2.merge(user1_ctx1)

        then: "it exists in child context too, but as a different instance"

        User user1_ctx2 = ctx2.find(User, user1_ctx1.id)
        user1_ctx2 != null
        !user1_ctx2.is(user1_ctx1)

        when: "add detail instance to collection of the master object in child context and commit it"

        UserRole ur1_ctx2 = ctx2.merge(new UserRole(user: user1_ctx2))

        user1_ctx2.userRoles = []
        user1_ctx2.userRoles.add(ur1_ctx2)

        def modified = []
        ctx2.addPreCommitListener({ e ->
            modified.addAll(e.modifiedInstances)
        })

        ctx2.commit()

        then: "child context commits both detail and master instances to parent context"

        modified.size() == 2
        modified.contains(user1_ctx2)
        modified.contains(ur1_ctx2)

        user1_ctx1.userRoles != null
        user1_ctx1.userRoles.size() == 1

        UserRole ur1_ctx1 = ctx1.find(UserRole, ur1_ctx2.id)
        user1_ctx1.userRoles[0].is(ur1_ctx1)

        when: "committing parent context"

        modified.clear()

        ctx1.addPreCommitListener({ e ->
            modified.addAll(e.modifiedInstances)
        })

        ctx1.commit()

        then: "parent context commits both detail and master instances"

        modified.size() == 2
        modified.contains(user1_ctx1)
        modified.contains(ur1_ctx1)
    }

    def "creating new instances in child context"() throws Exception {

        DataContext ctx1 = factory.createDataContext()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> Collections.emptySet()
        })

        when: "merge instance into parent context"

        User user1_ctx1 = ctx1.merge(new User(login: 'u1', name: 'User 1'))

        DataContext ctx2 = factory.createDataContext()
        ctx2.setParent(ctx1)

        ctx2.merge(user1_ctx1)

        then:

        User user1_ctx2 = ctx2.find(User, user1_ctx1.id)
        user1_ctx2 != null
        !user1_ctx2.is(user1_ctx1)
        isNew(user1_ctx2)

        when: "create new instances in child context and commit"

        UserRole ur1_ctx2 = ctx2.merge(new UserRole(user: user1_ctx2))

        Role r1_ctx2 = ctx2.merge(new Role(name: 'r1'))
        ur1_ctx2.role = r1_ctx2

        user1_ctx2.userRoles = [ur1_ctx2]

        ctx2.commit()

        then: "new instances are in parent context"

        User user1 = ctx1.find(User, user1_ctx1.id)
        user1.is(user1_ctx1)

        UserRole ur1 = ctx1.find(UserRole, ur1_ctx2.id)
        ur1.user.is(user1_ctx1)

        Role r1 = ctx1.find(Role, r1_ctx2.id)
        ur1.role.is(r1)

        user1.userRoles[0].is(ur1)
    }

    def "commit to parent"() {

        DataContext ctx1 = factory.createDataContext()

        def order = new Order(number: 1, orderLines: [], version: 1)
        makeDetached(order, ['number', 'orderLines'])

        def line = new OrderLine(quantity: 1, order: order)
        order.orderLines.add(line)

        when:

        def order1 = ctx1.merge(order)

        DataContext ctx2 = factory.createDataContext()
        ctx2.setParent(ctx1)

        def line1 = ctx1.find(OrderLine, line.id)

        ctx2.merge(line1)

        then:

        def order2 = ctx2.find(Order, order.id)
        def line2 = ctx2.find(OrderLine, line.id)
        order2.orderLines[0].is(line2)
        line2.order.is(order2)

        when:

        line2.quantity = 2
        ctx2.commit()

        then:

        order1.is(ctx1.find(Order, order.id))
        line1.is(ctx1.find(OrderLine, line.id))
        line1.quantity == 2
        order1.orderLines[0].is(line1)

        and:

        order2.is(ctx2.find(Order, order.id))
        line2.is(ctx2.find(OrderLine, line.id))
        order2.orderLines[0].is(line2)
        line2.order.is(order2)
    }

    private void makeDetached(def entity, List<String> attributes) {
        entityStates.makeDetached(entity)
        ((FetchGroupTracker) entity)._persistence_setFetchGroup(
                new EntityFetchGroup(['id', 'version', 'deleteTs'] + attributes))

    }

    boolean isNew(def entity) {
        BaseEntityInternalAccess.isNew(entity)
    }

    private static <T> T makeSaved(T entity) {
        TestServiceProxy.getDefault(DataService).commit(new CommitContext().addInstanceToCommit(entity))[0] as T
    }
}
