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

package spec.cuba.core.metadata_tools

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.MetadataTools
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.security.entity.UserSessionEntity
import com.haulmont.cuba.testmodel.not_persistent.NotPersistentStringIdEntity
import com.haulmont.cuba.testmodel.primary_keys.StringKeyEntity
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class MetadataToolsTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private MetadataTools metadataTools

    void setup() {
        metadataTools = AppBeans.get(MetadataTools)
    }

    def "primary key name for persistent entities"() {

        when:

        def primaryKeyName = metadataTools.getPrimaryKeyName(cont.metadata().getClassNN(User))

        then:

        primaryKeyName == 'id'

        when:

        primaryKeyName = metadataTools.getPrimaryKeyName(cont.metadata().getClassNN(StringKeyEntity))

        then:

        primaryKeyName == 'code'
    }

    def "primary key name for non-persistent entity"() {

        def primaryKeyName

        when:

        primaryKeyName = metadataTools.getPrimaryKeyName(cont.metadata().getClassNN(UserSessionEntity))

        then:

        primaryKeyName == 'id'

        when:

        primaryKeyName = metadataTools.getPrimaryKeyName(cont.metadata().getClassNN(NotPersistentStringIdEntity))

        then:

        primaryKeyName == 'identifier'
    }
}
