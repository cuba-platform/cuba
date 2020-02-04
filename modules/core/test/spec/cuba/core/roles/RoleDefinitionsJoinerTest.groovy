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

package spec.cuba.core.roles

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.security.role.RoleDefinitionBuilder
import com.haulmont.cuba.security.role.RoleDefinitionsJoiner
import com.haulmont.cuba.security.entity.*
import com.haulmont.cuba.security.role.BasicRoleDefinition
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class RoleDefinitionsJoinerTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    def "join wildcard access when both roles have wildcards"() {

        def role1 = BasicRoleDefinition.builder()
                .withPermission(PermissionType.SCREEN, "*", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:create", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:read", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:update", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:delete", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_ATTR, "*:*", EntityAttrAccess.MODIFY.id)
                .withPermission(PermissionType.SPECIFIC, "*", Access.ALLOW.id)
                .build()

        def role2 = BasicRoleDefinition.builder()
                .withPermission(PermissionType.SCREEN, "*", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:create", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:read", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:update", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:delete", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_ATTR, "*:*", Access.DENY.id)
                .withPermission(PermissionType.SPECIFIC, "*", Access.DENY.id)
                .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.screenPermissions().explicitPermissions["*"] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions["*:create"] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions["*:read"] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions["*:update"] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions["*:delete"] == Access.ALLOW.id
        joinedRole.entityAttributePermissions().explicitPermissions["*:*"] == EntityAttrAccess.MODIFY.id
        joinedRole.specificPermissions().explicitPermissions["*"] == Access.ALLOW.id
    }

    def "join wildcard access when one role doesn't have wildcards"() {

        def role1 = BasicRoleDefinition.builder()
                .build()

        def role2 = BasicRoleDefinition.builder()
                .withPermission(PermissionType.SCREEN, "*", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:create", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:read", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:update", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_OP, "*:delete", Access.DENY.id)
                .withPermission(PermissionType.ENTITY_ATTR, "*:*", Access.DENY.id)
                .withPermission(PermissionType.SPECIFIC, "*", Access.DENY.id)
                .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.screenPermissions().explicitPermissions["*"] == Access.DENY.id
        joinedRole.entityPermissions().explicitPermissions["*:create"] == Access.DENY.id
        joinedRole.entityPermissions().explicitPermissions["*:read"] == Access.DENY.id
        joinedRole.entityPermissions().explicitPermissions["*:update"] == Access.DENY.id
        joinedRole.entityPermissions().explicitPermissions["*:delete"] == Access.DENY.id
        joinedRole.entityAttributePermissions().explicitPermissions["*:*"] == EntityAttrAccess.DENY.id
        joinedRole.specificPermissions().explicitPermissions["*"] == Access.DENY.id
    }


    def "join permissions when both roles define target"() {

        def role1 = BasicRoleDefinition.builder()
                .withScreenPermission('screen1', Access.DENY)
                .withEntityPermission('sec$User', EntityOp.CREATE, Access.DENY)
                .withEntityPermission('sec$User', EntityOp.READ, Access.DENY)
                .withEntityPermission('sec$User', EntityOp.UPDATE, Access.DENY)
                .withEntityPermission('sec$User', EntityOp.DELETE, Access.DENY)
                .withEntityAttributePermission('sec$User', "login", EntityAttrAccess.DENY)
                .withSpecificPermission('spec1', Access.DENY)
                .withScreenComponentPermission('alias', 'component', Access.DENY)
                .build()

        def role2 = BasicRoleDefinition.builder()
                .withScreenPermission('screen1', Access.ALLOW)
                .withEntityPermission('sec$User', EntityOp.CREATE, Access.ALLOW)
                .withEntityPermission('sec$User', EntityOp.READ, Access.ALLOW)
                .withEntityPermission('sec$User', EntityOp.UPDATE, Access.ALLOW)
                .withEntityPermission('sec$User', EntityOp.DELETE, Access.ALLOW)
                .withEntityAttributePermission('sec$User', "login", EntityAttrAccess.MODIFY)
                .withSpecificPermission('spec1', Access.ALLOW)
                .withScreenComponentPermission('alias', 'component', Access.ALLOW)
                .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.screenPermissions().explicitPermissions['screen1'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:create'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:read'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:update'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:delete'] == Access.ALLOW.id
        joinedRole.entityAttributePermissions().explicitPermissions['sec$User:login'] == EntityAttrAccess.MODIFY.id
        joinedRole.specificPermissions().explicitPermissions['spec1'] == Access.ALLOW.id
        joinedRole.screenComponentPermissions().explicitPermissions['alias:component'] == Access.ALLOW.id
    }

    def "join permissions when one role has permission and the second role has wildcards"() {
        def role1 = BasicRoleDefinition.builder()
                .withPermission(PermissionType.SCREEN, "*", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:create", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:read", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:update", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_OP, "*:delete", Access.ALLOW.id)
                .withPermission(PermissionType.ENTITY_ATTR, "*:*", EntityAttrAccess.MODIFY.id)
                .withPermission(PermissionType.SPECIFIC, "*", Access.ALLOW.id)
                .build()

        def role2 = BasicRoleDefinition.builder()
                .withPermission(PermissionType.SCREEN, 'screen1', Access.DENY.getId())
                .withPermission(PermissionType.ENTITY_OP, 'sec$User:create', Access.DENY.getId())
                .withPermission(PermissionType.ENTITY_OP, 'sec$User:update', Access.DENY.getId())
                .withPermission(PermissionType.ENTITY_OP, 'sec$User:delete', Access.DENY.getId())
                .withPermission(PermissionType.ENTITY_ATTR, 'sec$User:login', EntityAttrAccess.DENY.getId())
                .withPermission(PermissionType.SPECIFIC, 'spec1', Access.DENY.getId())
                .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.screenPermissions().explicitPermissions['screen1'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:create'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:update'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:delete'] == Access.ALLOW.id
        joinedRole.entityAttributePermissions().explicitPermissions['sec$User:login'] == EntityAttrAccess.MODIFY.id
        joinedRole.entityAttributePermissions().explicitPermissions['*:*'] == EntityAttrAccess.MODIFY.id
        joinedRole.specificPermissions().explicitPermissions['spec1'] == Access.ALLOW.id
    }

    def "join attribute wildcard for entity and global attribute wildcard"() {
        def role1 = BasicRoleDefinition.builder()
                .withPermission(PermissionType.ENTITY_ATTR, "my_Entity1:*", EntityAttrAccess.MODIFY.id)
                .withPermission(PermissionType.ENTITY_ATTR, "my_Entity2:*", EntityAttrAccess.DENY.id)
                .build()

        def role2 = BasicRoleDefinition.builder()
                .withPermission(PermissionType.ENTITY_ATTR, '*:*', EntityAttrAccess.VIEW.getId())
                .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.entityAttributePermissions().explicitPermissions['my_Entity1:*'] == EntityAttrAccess.MODIFY.id
        joinedRole.entityAttributePermissions().explicitPermissions['my_Entity2:*'] == EntityAttrAccess.VIEW.id
        joinedRole.entityAttributePermissions().explicitPermissions['*:*'] == EntityAttrAccess.VIEW.id
    }
}
