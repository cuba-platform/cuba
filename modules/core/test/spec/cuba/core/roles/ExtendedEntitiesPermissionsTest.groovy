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

import com.haulmont.chile.core.model.MetaClass
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.Security
import com.haulmont.cuba.core.global.UserSessionSource
import com.haulmont.cuba.security.entity.Access
import com.haulmont.cuba.security.entity.EntityAttrAccess
import com.haulmont.cuba.security.entity.EntityOp
import com.haulmont.cuba.security.global.UserSession
import com.haulmont.cuba.security.role.BasicRoleDefinition
import com.haulmont.cuba.security.role.RoleDefinition
import com.haulmont.cuba.testmodel.roles.ExtRoleTestEntity
import com.haulmont.cuba.testmodel.roles.RoleTestEntity
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class ExtendedEntitiesPermissionsTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    UserSession userSession
    Metadata metadata
    Security security
    RoleDefinition defaultRole

    def setup() {
        userSession = AppBeans.get(UserSessionSource).getUserSession()
        defaultRole = userSession.joinedRole
        metadata = cont.metadata()
        security = AppBeans.get(Security)
    }

    def cleanup() {
        userSession.joinedRole = defaultRole
    }

    def "RoleDefinition builder should use original MetaClass name as permission target"() {

        when:
        RoleDefinition roleDefinition = BasicRoleDefinition.builder()
                .withEntityPermission(ExtRoleTestEntity.class, EntityOp.CREATE, Access.ALLOW)
                .withEntityAttributePermission(ExtRoleTestEntity.class, "name", EntityAttrAccess.VIEW)
                .withEntityAttributePermission(ExtRoleTestEntity.class, "newAttr", EntityAttrAccess.VIEW)
                .build()

        then:

        roleDefinition.entityPermissions().explicitPermissions['test_RoleTestEntity:create'] == Access.ALLOW.getId()
        roleDefinition.entityPermissions().explicitPermissions['test_ExtRoleTestEntity:create'] == null
        roleDefinition.entityAttributePermissions().explicitPermissions['test_RoleTestEntity:name'] == EntityAttrAccess.VIEW.getId()
        roleDefinition.entityAttributePermissions().explicitPermissions['test_RoleTestEntity:newAttr'] == EntityAttrAccess.VIEW.getId()
    }

    def "Extended entity permission in Security"() {

        RoleDefinition roleDefinition = BasicRoleDefinition.builder()
                .withEntityPermission(ExtRoleTestEntity.class, EntityOp.CREATE, Access.ALLOW)
                .withEntityAttributePermission(ExtRoleTestEntity.class, "name" , EntityAttrAccess.VIEW)
                .withEntityAttributePermission(ExtRoleTestEntity.class, "newAttr" , EntityAttrAccess.VIEW)
                .build()

        MetaClass roleTestEntityMetaClass = metadata.getClassNN(RoleTestEntity.class)

        when:
        userSession.joinedRole = roleDefinition

        then:
        security.isEntityOpPermitted(roleTestEntityMetaClass, EntityOp.CREATE)
        security.isEntityOpPermitted(RoleTestEntity.class, EntityOp.CREATE)
        security.isEntityOpPermitted(ExtRoleTestEntity.class, EntityOp.CREATE)
        security.isEntityAttrPermitted(roleTestEntityMetaClass, 'name', EntityAttrAccess.VIEW)
        security.isEntityAttrPermitted(roleTestEntityMetaClass, 'newAttr', EntityAttrAccess.VIEW)
        security.isEntityAttrPermitted(RoleTestEntity.class, 'name', EntityAttrAccess.VIEW)
        security.isEntityAttrPermitted(RoleTestEntity, 'newAttr', EntityAttrAccess.VIEW)
        security.isEntityAttrPermitted(ExtRoleTestEntity.class, 'name', EntityAttrAccess.VIEW)
        security.isEntityAttrPermitted(ExtRoleTestEntity, 'newAttr', EntityAttrAccess.VIEW)
    }
}
