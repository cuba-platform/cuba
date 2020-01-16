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

import com.haulmont.chile.core.model.MetaClass
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.security.app.RoleDefinitionBuilder
import com.haulmont.cuba.security.app.role.EffectiveEntityPermissionsBuilder
import com.haulmont.cuba.security.entity.Access
import com.haulmont.cuba.security.entity.EntityOp
import com.haulmont.cuba.security.entity.Role
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.role.PermissionsUtils
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EffectiveEntityPermissionsBuilderTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    Metadata metadata
    EffectiveEntityPermissionsBuilder effectiveEntityPermissionsBuilder

    def setup() {
        effectiveEntityPermissionsBuilder = AppBeans.get(EffectiveEntityPermissionsBuilder)
        metadata = cont.metadata()
    }

    def "build explicit role definition"() {
        MetaClass userMetaClass = metadata.getClassNN(User.class)
        MetaClass roleMetaClass = metadata.getClassNN(Role.class)
        def roleDefinition = RoleDefinitionBuilder.create()
                .withEntityAccessPermission(userMetaClass, EntityOp.CREATE, Access.ALLOW)
                .withEntityAccessPermission(userMetaClass, EntityOp.UPDATE, Access.ALLOW)
                .withDefaultEntityCreateAccess(Access.DENY)
                .withDefaultEntityReadAccess(Access.ALLOW)
                .build()

        when:

        def fullEntityPermissionsContainer = effectiveEntityPermissionsBuilder.buildEffectivePermissionContainer(roleDefinition.entityPermissions())
        def fullEntityPermissionsMap = fullEntityPermissionsContainer.explicitPermissions

        then:

        fullEntityPermissionsMap.size() > 0

        fullEntityPermissionsMap.get(PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.CREATE)) == 1
        fullEntityPermissionsMap.get(PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.READ)) == 1
        fullEntityPermissionsMap.get(PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.UPDATE)) == 1

        fullEntityPermissionsMap.get(PermissionsUtils.getEntityOperationTarget(roleMetaClass, EntityOp.UPDATE)) == 0
        fullEntityPermissionsMap.get(PermissionsUtils.getEntityOperationTarget(roleMetaClass, EntityOp.READ)) == 1
        fullEntityPermissionsMap.get(PermissionsUtils.getEntityOperationTarget(roleMetaClass, EntityOp.DELETE)) == 0
    }
}
