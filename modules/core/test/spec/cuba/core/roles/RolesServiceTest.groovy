/*
 * Copyright (c) 2008-2020 Haulmont.
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

package spec.cuba.core.roles

import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.role.BasicRoleDefinition
import com.haulmont.cuba.security.app.role.PredefinedRoleDefinitionRepository
import com.haulmont.cuba.security.entity.Access
import com.haulmont.cuba.security.entity.EntityAttrAccess
import com.haulmont.cuba.security.entity.EntityOp
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.Permission
import com.haulmont.cuba.security.entity.PermissionType
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserRole
import com.haulmont.cuba.security.role.RoleDefinition
import com.haulmont.cuba.security.role.RoleTransformationOption
import com.haulmont.cuba.security.role.RolesService
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class RolesServiceTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    RolesService rolesService
    PredefinedRoleDefinitionRepository predefinedRoleDefinitionRepository
    Persistence persistence
    Metadata metadata
    DataManager dataManager

    def setup() {
        dataManager = AppBeans.get(DataManager)
        persistence = cont.persistence()
        metadata = cont.metadata()
        predefinedRoleDefinitionRepository = AppBeans.get(PredefinedRoleDefinitionRepository)
        rolesService = AppBeans.get(RolesService)
    }

    def cleanup() {
        predefinedRoleDefinitionRepository.reset()
    }

    def "getDefaultRoles"() {
        given:

        RoleDefinition defaultPredefinedRole = BasicRoleDefinition.builder()
                .withName('defaultPredefinedRole')
                .withIsDefault(true)
                .build()
        RoleDefinition nonDefaultPredefinedRole = BasicRoleDefinition.builder()
                .withName('nonDefaultPredefinedRole')
                .withIsDefault(false)
                .build()
        predefinedRoleDefinitionRepository.registerRoleDefinition(defaultPredefinedRole)
        predefinedRoleDefinitionRepository.registerRoleDefinition(nonDefaultPredefinedRole)

        Role defaultDatabaseRole = metadata.create(Role.class)
        defaultDatabaseRole.name = 'defaultDatabaseRole'
        defaultDatabaseRole.defaultRole = true

        Role nonDefaultDatabaseRole = metadata.create(Role.class)
        nonDefaultDatabaseRole.name = 'nonDefaultDatabaseRole'
        nonDefaultDatabaseRole.defaultRole = false

        def createdEntities = [defaultDatabaseRole, nonDefaultDatabaseRole]
        persistEntities(createdEntities)

        when:

        List<Role> roles = rolesService.getDefaultRoles()

        then:

        roles.size() == 2
        roles.find { it.name == 'defaultPredefinedRole' } != null
        roles.find { it.name == 'defaultDatabaseRole' } != null

        cleanup:

        deleteEntities(createdEntities)
    }

    def "getPermissions"() {
        given:

        def predefinedRoleName = 'predefinedRole'
        RoleDefinition predefinedRole = BasicRoleDefinition.builder()
                .withName(predefinedRoleName)
                .withEntityPermission(User.class, EntityOp.CREATE, Access.ALLOW)
                .withEntityPermission(User.class, EntityOp.READ, Access.DENY)
                .withEntityAttributePermission(User.class, 'password', EntityAttrAccess.DENY)
                .withIsDefault(true)
                .build()

        predefinedRoleDefinitionRepository.registerRoleDefinition(predefinedRole)

        when:

        def entityPermissions = rolesService.getPermissions(predefinedRoleName, PermissionType.ENTITY_OP)

        then:

        entityPermissions.size() == 2
        entityPermissions.find { it.target == 'sec$User:create' }.value == Access.ALLOW.id
        entityPermissions.find { it.target == 'sec$User:read' }.value == Access.DENY.id

        when:

        def entityAttrPermissions = rolesService.getPermissions(predefinedRoleName, PermissionType.ENTITY_ATTR)

        then:

        entityAttrPermissions.size() == 1
        entityAttrPermissions.find { it.target == 'sec$User:password' }.value == EntityAttrAccess.DENY.id
    }

    def "getRoleDefinitionsForUser"() {
        given:

        def predefinedRoleName = 'predefinedRole'
        RoleDefinition predefinedRole = BasicRoleDefinition.builder()
                .withName(predefinedRoleName)
                .withEntityPermission(User.class, EntityOp.CREATE, Access.ALLOW)
                .withEntityPermission(User.class, EntityOp.READ, Access.DENY)
                .withEntityAttributePermission(User.class, 'password', EntityAttrAccess.DENY)
                .withIsDefault(false)
                .build()

        predefinedRoleDefinitionRepository.registerRoleDefinition(predefinedRole)

        def newEntities = []

        Group group1 = metadata.create(Group)
        group1.name = 'group1'
        newEntities << group1

        User user1 = metadata.create(User)
        user1.login = 'user1'
        user1.group = group1
        newEntities << user1

        Role databaseRole = metadata.create(Role.class)
        databaseRole.name = 'databaseRole'
        databaseRole.defaultRole = false
        newEntities << databaseRole

        Permission permission1 = metadata.create(Permission)
        permission1.role = databaseRole
        permission1.type = PermissionType.ENTITY_OP
        permission1.target = 'sec$User:delete'
        permission1.value = Access.DENY.id
        newEntities << permission1

        UserRole userRole1 = metadata.create(UserRole)
        userRole1.role = databaseRole
        userRole1.user = user1
        newEntities << userRole1

        UserRole userRole2 = metadata.create(UserRole)
        userRole2.roleName = predefinedRoleName
        userRole2.user = user1
        newEntities << userRole2

        persistEntities(newEntities)

        when:

        User user1Minimal = dataManager.reload(user1, View.MINIMAL)

        Collection<RoleDefinition> roleDefinitions = rolesService.getRoleDefinitionsForUser(user1Minimal)

        then:

        roleDefinitions.size() == 2

        cleanup:

        deleteEntities(newEntities)
    }

    def "transformToRole"() {
        RoleDefinition predefinedRole = BasicRoleDefinition.builder()
                .withName("role1")
                .withEntityPermission(User.class, EntityOp.CREATE, Access.ALLOW)
                .withEntityAttributePermission(User.class, 'password', EntityAttrAccess.DENY)
                .withIsDefault(true)
                .build()

        when:

        Role role = rolesService.transformToRole(predefinedRole)

        then:

        role.name == 'role1'
        role.defaultRole
        role.permissions.size() == 2

        def permission1 = role.permissions.find { it.type == PermissionType.ENTITY_OP }
        permission1.target == 'sec$User:create'
        permission1.value == 1

        def permission2 = role.permissions.find { it.type == PermissionType.ENTITY_ATTR }
        permission2.target == 'sec$User:password'
        permission2.value == 0

        when:

        role = rolesService.transformToRole(predefinedRole,
                RoleTransformationOption.DO_NOT_INCLUDE_PERMISSIONS)

        then:

        role.name == 'role1'
        role.defaultRole
        role.permissions == null

    }

    def deleteEntities(List<Entity> entities) {
        deleteRecordsOfType(entities, Permission.class)
        deleteRecordsOfType(entities, UserRole.class)
        deleteRecordsOfType(entities, Role.class)
        deleteRecordsOfType(entities, User.class)
        deleteRecordsOfType(entities, Group.class)
    }

    def deleteRecordsOfType(Collection<Entity> entities, Class<? extends Entity> clazz) {
        entities.findAll { clazz.isAssignableFrom(it.class) }
                .forEach { cont.deleteRecord(it) }
    }

    def persistEntities(Collection<Entity> entities) {
        cont.persistence().runInTransaction { em ->
            entities.forEach { em.persist(it) }
        }
    }
}
