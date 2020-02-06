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
import com.haulmont.cuba.core.global.UserSessionSource
import com.haulmont.cuba.security.role.BasicRoleDefinition
import com.haulmont.cuba.security.entity.EntityAttrAccess
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.global.UserSession
import com.haulmont.cuba.security.role.RoleDefinition
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class UserSessionPermissionsTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    UserSession userSession
    Metadata metadata

    def setup() {
        userSession = AppBeans.get(UserSessionSource).getUserSession()
        metadata = cont.metadata()
    }

    def "wildcard attribute permissions"() {
        RoleDefinition roleDefinition = BasicRoleDefinition.builder()
                .withEntityAttributePermission(User, '*', EntityAttrAccess.VIEW)
                .withEntityAttributePermission(User, 'firstName', EntityAttrAccess.MODIFY)
                .build()

        MetaClass userMetaClass = metadata.getClassNN(User.class)

        when:
        userSession.joinedRole = roleDefinition

        then:

        userSession.isEntityAttrPermitted(userMetaClass, "firstName", EntityAttrAccess.MODIFY)
        userSession.isEntityAttrPermitted(userMetaClass, "lastName", EntityAttrAccess.VIEW)
        !userSession.isEntityAttrPermitted(userMetaClass, "lastName", EntityAttrAccess.MODIFY)
    }
}
