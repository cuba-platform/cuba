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
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.EntityStates
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.gui.model.DataContextFactory
import com.haulmont.cuba.gui.model.impl.DataContextAccessor
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserRole
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DataContextTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataContextFactory factory
    private EntityStates entityStates
    private Metadata metadata

    void setup() {
        factory = cont.getBean(DataContextFactory)
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

        then: "returned the same instance"

        entityInContext.is(mergedUser1)
        entityInContext.is(user1)

        when: "merging another instance with the same id"

        User user11 = new User(id: user1.id, login: 'u11', name: 'User 11')

        User mergedUser11 = context.merge(user11)

        then: "returned instance which was already in context"

        mergedUser11.is(mergedUser1)
        mergedUser11.is(user1)
        !mergedUser11.is(user11)
    }

    def "merge graph"() throws Exception {
        DataContext context = factory.createDataContext()

        when: "merging graph containing different instances with same ids"

        // an object being merged
        User user1 = new User(login: 'u1', name: 'User 1', userRoles: [])
        makeDetached(user1)
        Role role1 = new Role(name: 'Role1')
        makeDetached(role1)
        UserRole user1Role1 = new UserRole(user: user1, role: role1)
        makeDetached(user1Role1)
        user1.userRoles.add(user1Role1)

        // somewhere in the object graph another object with the same id
        User user11 = new User(id: user1.id, login: 'u11', name: 'User 11')
        makeDetached(user11)
        UserRole user11Role1 = new UserRole(user: user11, role: role1)
        makeDetached(user11Role1)
        user1.getUserRoles().add(user11Role1)

        //  user1
        //      user1Role1
        //          role1
        //      user11Role1
        //          user11 (=user1)
        //          role1

        User mergedUser1 = context.merge(user1)

        then: "context contains first merged instance"

        mergedUser1.is(user1)
        context.find(User, user1.id).is(mergedUser1)

        and: "merged instance has local attributes of the second object"

        mergedUser1.login == "u11"
        mergedUser1.name == "User 11"

        and:

        context.find(UserRole, user1Role1.id).is(user1Role1)
        context.find(UserRole, user11Role1.id).is(user11Role1)
        context.find(Role, role1.id).is(role1)

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

        mergedUser1.is(user1)
        mergedUser11.is(mergedUser1)
        context.find(User, user1.id).is(mergedUser1)

        and: "merged instance has local attributes of the second object"

        mergedUser1.login == "u11"
        mergedUser1.name == "User 11"

        and: "merged instance has collection of the second object"

        mergedUser1.userRoles != null
        mergedUser1.userRoles[0].user.is(mergedUser1)

        and:

        context.find(UserRole, user11Role1.id).is(user11Role1)
        context.find(Role, role1.id).is(role1)
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

        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> Collections.emptySet()
        })

        when: "merge graph then modify and remove some instances"

        User user1 = createDetached(User)
        user1.login = "u1"
        user1.name = "User 1"
        user1.userRoles = new ArrayList<>()

        Role role1 = createDetached(Role)
        role1.name = "Role 1"

        Role role2 = createDetached(Role)
        role1.name = "Role 2"

        UserRole user1Role1 = createDetached(UserRole)
        user1Role1.user = user1
        user1Role1.role = role1

        user1.userRoles.add(user1Role1)

        UserRole user1Role2 = createDetached(UserRole)
        user1Role2.user = user1
        user1Role2.role = role2

        user1.userRoles.add(user1Role2)

        context.merge(user1)

        role1.name = "Role 1 modified"

        user1.userRoles.remove(user1Role2)
        context.remove(user1Role2)

        Collection modified = []
        Collection removed = []

        context.addPreCommitListener({ e->
            modified.addAll(e.modifiedInstances)
            removed.addAll(e.removedInstances)
        })

        context.commit()

        then:

        modified.size() == 2
        modified.contains(role1)
        modified.contains(user1)

        removed.size() == 1
        removed.contains(user1Role2)
    }

    def "copy state"() throws Exception {

        DataContext context = factory.createDataContext()

        User src, dst

        when: "(1) src.new > dst.new : copy all non-null"

        src = new User()
        src.setLogin("u-src")

        dst = new User()
        dst.id = src.id
        dst.login = "u-dst"
        dst.name = "Dest User"
        dst.userRoles = new ArrayList<>()

        DataContextAccessor.copyState(context, src, dst)

        then:

        entityStates.isNew(dst)
        dst.getVersion() == null
        dst.login == "u-src"
        dst.name == "Dest User"
        dst.userRoles != null

        when: "(2) src.new -> dst.det : do nothing"

        src = new User()
        src.login = "u-src"

        dst = new User()
        dst.id = src.id
        dst.version = 1
        dst.login = "u-dst"
        dst.name = "Dest User"
        dst.userRoles = new ArrayList<>()
        entityStates.makeDetached(dst)

        DataContextAccessor.copyState(context, src, dst)

        then:

        entityStates.isDetached(dst)
        dst.getVersion() != null
        dst.login == "u-dst"
        dst.name == "Dest User"
        dst.userRoles != null

        when: "(3) src.det -> dst.new : copy all loaded, make detached"

        src = new User()
        src.version = 1
        src.login = "u-src"
        entityStates.makeDetached(src)

        dst = new User()
        dst.id = src.id
        dst.login = "u-dst"
        dst.name = "Dest User"
        dst.userRoles = new ArrayList<>()

        DataContextAccessor.copyState(context, src, dst)

        then:

        entityStates.isDetached(dst)
        dst.version == 1
        dst.login == "u-src"
        dst.name == null
        dst.userRoles != null

        when: "(4) src.det -> dst.det : if src.version >= dst.version, copy all loaded"

        src = new User()
        src.version = 2
        src.login = "u-src"
        entityStates.makeDetached(src)

        dst = new User()
        dst.id = src.id
        dst.version = 1
        dst.login = "u-dst"
        dst.name = "Dest User"
        dst.userRoles = new ArrayList<>()
        entityStates.makeDetached(dst)

        DataContextAccessor.copyState(context, src, dst)

        then:

        entityStates.isDetached(dst)
        dst.version == 2
        dst.login == "u-src"
        dst.name == null
        dst.userRoles != null

        when: "(4) src.det -> dst.det : if src.version < dst.version, do nothing"

        src = new User()
        src.version = 1
        src.login = "u-src"
        entityStates.makeDetached(src)

        dst = new User()
        dst.id = src.id
        dst.version = 2
        dst.login = "u-dst"
        dst.name = "Dest User"
        dst.userRoles = new ArrayList<>()
        entityStates.makeDetached(dst)

        DataContextAccessor.copyState(context, src, dst)

        then:

        entityStates.isDetached(dst)
        dst.version == 2
        dst.login == "u-dst"
        dst.name == "Dest User"
        dst.userRoles != null
    }

    def "child context has correct object graph"() throws Exception {

        DataContext context = factory.createDataContext()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> Collections.emptySet()
        })

        when: "merge instance into parent context"

        User user1 = makeSaved(new User(login: 'u1', name: 'User 1', userRoles: []))
        Role role1 = makeSaved(new Role(name: 'Role 1'))
        UserRole user1Role1 = makeSaved(new UserRole(user: user1, role: role1))
        user1.userRoles.add(user1Role1)

        context.merge(user1)

        then:

        !context.hasChanges()

        when:

        DataContext childContext = factory.createDataContext()
        childContext.setParent(context)

        def childUser = childContext.find(User, user1.id)
        def childRole = childContext.find(Role, role1.id)
        def childUserRole = childContext.find(UserRole, user1Role1.id)

        then:

        childUser == user1
        !childUser.is(user1)
        childUser.userRoles[0] == childUserRole

        childRole == role1
        !childRole.is(role1)

        childUserRole == user1Role1
        !childUserRole.is(user1Role1)

        childUserRole.user?.is(childUser)
        childUserRole.role?.is(childRole)

        !context.hasChanges()
        !childContext.hasChanges()
    }

    def "parent context"() throws Exception {

        DataContext context = factory.createDataContext()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            commit(_) >> Collections.emptySet()
        })

        when: "merge instance into parent context"

        User user1 = new User()
        user1.login = "u1"
        user1.name = "User 1"
        user1.userRoles = new ArrayList<>()

        context.merge(user1)

        DataContext childContext = factory.createDataContext()

        childContext.setParent(context)

        then: "it exists in child context too, but as a different instance"

        User user1InChild = childContext.find(User, user1.id)
        user1InChild != null
        user1InChild.userRoles != null
        !user1InChild.is(user1)

        when: "add detail instance to collection of the master object in child context and commit it"

        UserRole user1Role1 = new UserRole()
        user1Role1.user = user1InChild
        user1InChild.userRoles.add(user1Role1)

        childContext.merge(user1Role1)

        def modified = []
        childContext.addPreCommitListener({ e ->
            modified.addAll(e.modifiedInstances)
        })

        childContext.commit()

        then: "child context commits both detail and master instances to parent context"

        modified.size() == 2
        modified.contains(user1InChild)
        modified.contains(user1Role1)

        user1.userRoles != null
        user1.userRoles.size() == 1

        when: "committing parent context"

        modified.clear()

        context.addPreCommitListener({ e ->
            modified.addAll(e.modifiedInstances)
        })

        context.commit()

        then: "parent context commits both detail and master instances"

        modified.size() == 2
        modified.contains(user1)
        modified.contains(user1Role1)
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

    private <T> T createDetached(Class<T> entityClass) {
        def entity = metadata.create(entityClass)
        entityStates.makeDetached(entity)
        entity
    }

    private void makeDetached(def entity) {
        entityStates.makeDetached(entity)
    }

    private static <T> T makeSaved(T entity) {
        TestServiceProxy.getDefault(DataService).commit(new CommitContext().addInstanceToCommit(entity))[0] as T
    }
}
