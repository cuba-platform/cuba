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
import com.haulmont.cuba.security.app.RoleDefinitionBuilder
import com.haulmont.cuba.security.app.RoleDefinitionsJoiner
import com.haulmont.cuba.security.entity.Access
import com.haulmont.cuba.security.entity.EntityAttrAccess
import com.haulmont.cuba.security.entity.EntityOp
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class RoleDefinitionsJoinerTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    def "join default access when both roles have defaults"() {

        def role1 = RoleDefinitionBuilder.create()
            .withDefaultScreenAccess(Access.ALLOW)
            .withDefaultEntityCreateAccess(Access.ALLOW)
            .withDefaultEntityReadAccess(Access.ALLOW)
            .withDefaultEntityUpdateAccess(Access.ALLOW)
            .withDefaultEntityDeleteAccess(Access.ALLOW)
            .withDefaultEntityAttributeAccess(EntityAttrAccess.MODIFY)
            .withDefaultSpecificAccess(Access.ALLOW)
            .build()

        def role2 = RoleDefinitionBuilder.create()
            .withDefaultScreenAccess(Access.DENY)
            .withDefaultEntityCreateAccess(Access.DENY)
            .withDefaultEntityReadAccess(Access.DENY)
            .withDefaultEntityUpdateAccess(Access.DENY)
            .withDefaultEntityDeleteAccess(Access.DENY)
            .withDefaultEntityAttributeAccess(EntityAttrAccess.DENY)
            .withDefaultSpecificAccess(Access.DENY)
            .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.screenPermissions().defaultScreenAccess == Access.ALLOW
        joinedRole.entityPermissions().defaultEntityCreateAccess == Access.ALLOW
        joinedRole.entityPermissions().defaultEntityReadAccess == Access.ALLOW
        joinedRole.entityPermissions().defaultEntityUpdateAccess == Access.ALLOW
        joinedRole.entityPermissions().defaultEntityDeleteAccess == Access.ALLOW
        joinedRole.entityAttributePermissions().defaultEntityAttributeAccess == EntityAttrAccess.MODIFY
        joinedRole.specificPermissions().defaultSpecificAccess == Access.ALLOW
    }

    def "join default access when one role doesn't have defaults"() {

        def role1 = RoleDefinitionBuilder.create()
                .withDefaultScreenAccess(null)
                .withDefaultEntityCreateAccess(null)
                .withDefaultEntityReadAccess(null)
                .withDefaultEntityUpdateAccess(null)
                .withDefaultEntityDeleteAccess(null)
                .withDefaultEntityAttributeAccess(null)
                .withDefaultSpecificAccess(null)
                .build()

        def role2 = RoleDefinitionBuilder.create()
                .withDefaultScreenAccess(Access.DENY)
                .withDefaultEntityCreateAccess(Access.DENY)
                .withDefaultEntityReadAccess(Access.DENY)
                .withDefaultEntityUpdateAccess(Access.DENY)
                .withDefaultEntityDeleteAccess(Access.DENY)
                .withDefaultEntityAttributeAccess(EntityAttrAccess.DENY)
                .withDefaultSpecificAccess(Access.DENY)
                .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.screenPermissions().defaultScreenAccess == Access.DENY
        joinedRole.entityPermissions().defaultEntityCreateAccess == Access.DENY
        joinedRole.entityPermissions().defaultEntityReadAccess == Access.DENY
        joinedRole.entityPermissions().defaultEntityUpdateAccess == Access.DENY
        joinedRole.entityPermissions().defaultEntityDeleteAccess == Access.DENY
        joinedRole.entityAttributePermissions().defaultEntityAttributeAccess == EntityAttrAccess.DENY
        joinedRole.specificPermissions().defaultSpecificAccess == Access.DENY
    }


    def "join permissions when both roles define target"() {
        Metadata metadata = AppBeans.get(Metadata)
        def metaClass = metadata.getClass(User.class)

        def role1 = RoleDefinitionBuilder.create()
                .withScreenPermission('screen1', Access.DENY)
                .withEntityAccessPermission(metaClass, EntityOp.CREATE, Access.DENY)
                .withEntityAccessPermission(metaClass, EntityOp.READ, Access.DENY)
                .withEntityAccessPermission(metaClass, EntityOp.UPDATE, Access.DENY)
                .withEntityAccessPermission(metaClass, EntityOp.DELETE, Access.DENY)
                .withEntityAttrAccessPermission(metaClass, "login", EntityAttrAccess.DENY)
                .withSpecificPermission('spec1', Access.DENY)
                .withScreenElementPermission('alias', 'component', Access.DENY)
                .build()

        def role2 = RoleDefinitionBuilder.create()
                .withScreenPermission('screen1', Access.ALLOW)
                .withEntityAccessPermission(metaClass, EntityOp.CREATE, Access.ALLOW)
                .withEntityAccessPermission(metaClass, EntityOp.READ, Access.ALLOW)
                .withEntityAccessPermission(metaClass, EntityOp.UPDATE, Access.ALLOW)
                .withEntityAccessPermission(metaClass, EntityOp.DELETE, Access.ALLOW)
                .withEntityAttrAccessPermission(metaClass, "login", EntityAttrAccess.MODIFY)
                .withSpecificPermission('spec1', Access.ALLOW)
                .withScreenElementPermission('alias', 'component', Access.ALLOW)
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
        joinedRole.screenElementsPermissions().explicitPermissions['alias:component'] == Access.ALLOW.id
    }

    def "join permissions when one role has permission and the second role has defaults"() {
        Metadata metadata = AppBeans.get(Metadata)
        def metaClass = metadata.getClass(User.class)

        def role1 = RoleDefinitionBuilder.create()
                .withDefaultScreenAccess(Access.DENY)
                .withDefaultEntityCreateAccess(Access.DENY)
                .withDefaultEntityReadAccess(Access.DENY)
                .withDefaultEntityUpdateAccess(Access.DENY)
                .withDefaultEntityDeleteAccess(Access.DENY)
                .withDefaultEntityAttributeAccess(EntityAttrAccess.DENY)
                .withDefaultSpecificAccess(Access.DENY)
                .build()

        def role2 = RoleDefinitionBuilder.create()
                .withScreenPermission('screen1', Access.ALLOW)
                .withEntityAccessPermission(metaClass, EntityOp.CREATE, Access.ALLOW)
                .withEntityAccessPermission(metaClass, EntityOp.READ, Access.DENY)
                .withEntityAccessPermission(metaClass, EntityOp.UPDATE, Access.ALLOW)
                .withEntityAccessPermission(metaClass, EntityOp.DELETE, Access.ALLOW)
                .withEntityAttrAccessPermission(metaClass, "login", EntityAttrAccess. VIEW)
                .withSpecificPermission('spec1', Access.ALLOW)
                .withScreenElementPermission('alias', 'component', Access.ALLOW)
                .build()

        when:

        def joinedRole = RoleDefinitionsJoiner.join(role1, role2)

        then:

        joinedRole.screenPermissions().explicitPermissions['screen1'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:create'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:read'] == Access.DENY.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:update'] == Access.ALLOW.id
        joinedRole.entityPermissions().explicitPermissions['sec$User:delete'] == Access.ALLOW.id
        joinedRole.entityAttributePermissions().explicitPermissions['sec$User:login'] == EntityAttrAccess.VIEW.id
        joinedRole.specificPermissions().explicitPermissions['spec1'] == Access.ALLOW.id
        joinedRole.screenElementsPermissions().explicitPermissions['alias:component'] == Access.ALLOW.id
    }
}
