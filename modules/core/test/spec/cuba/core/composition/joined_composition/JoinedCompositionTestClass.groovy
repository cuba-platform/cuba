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
package spec.cuba.core.composition.joined_composition

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.testmodel.selfinherited.ChildEntity
import com.haulmont.cuba.testmodel.selfinherited.ChildEntityDetail
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class JoinedCompositionTestClass extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = new TestContainer()
            .setAppPropertiesFiles(Arrays.asList("cuba-app.properties", "test-app.properties", "cuba-test-app.properties", "/spec/cuba/core/composition/test-composition-app.properties"))

    private Persistence persistence = cont.persistence()
    private Metadata metadata = cont.metadata()

    private dataManager

    void setup() {
        dataManager = AppBeans.get(DataManager)
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_CHILD_ENTITY_DETAIL')
        runner.update('delete from TEST_ROOT_ENTITY_DETAIL')
        runner.update('delete from TEST_CHILD_ENTITY')
        runner.update('delete from TEST_ROOT_ENTITY')
    }

    def "store master-detail"() {
        when:
        persistence.runInTransaction({ em ->
            ChildEntity childEntity = metadata.create(ChildEntity)
            childEntity.name = 'name'
            childEntity.description = 'description'
            em.persist(childEntity)

            ChildEntityDetail childEntityDetail = metadata.create(ChildEntityDetail)
            childEntityDetail.childEntity = childEntity
            childEntityDetail.info = 'info'
            em.persist(childEntityDetail)
        })

        then:
        noExceptionThrown()
    }
}
