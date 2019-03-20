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

package spec.cuba.core.data_manager

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.validation.EntityValidationException
import com.haulmont.cuba.testmodel.beanvalidation.EmbeddedValidatedEntity
import com.haulmont.cuba.testmodel.beanvalidation.ValidatedEntity
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityValidationTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Persistence persistence = cont.persistence()
    private DataManager dataManager

    void setup() {
        dataManager = AppBeans.get(DataManager)
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_VALIDATED_ENTITY')
    }

    def "ALWAYS_VALIDATE test"() {
        when:
        def context = new CommitContext()
        context.setValidationType(CommitContext.ValidationType.ALWAYS_VALIDATE)
        def validatedEntity = new ValidatedEntity(name: "1")
        context.addInstanceToCommit(validatedEntity)
        dataManager.commit(context)

        then:
        thrown(EntityValidationException)
    }

    def "NEVER_VALIDATE test"() {
        when:
        def context = new CommitContext()
        context.setValidationType(CommitContext.ValidationType.NEVER_VALIDATE)
        def validatedEntity = new ValidatedEntity(name: "1")
        context.addInstanceToCommit(validatedEntity)
        def committedEntity = dataManager.commit(context).iterator().next()

        then:
        committedEntity == validatedEntity
    }

    def "Default validation test"() {
        when:
        def validatedEntity = new ValidatedEntity(name: "1")
        dataManager.commit(validatedEntity)

        then:
        thrown(EntityValidationException)
    }

    def "Entity update test"() {
        when:
        def validatedEntity = new ValidatedEntity(name: "11111")
        def committedEntity = dataManager.commit(validatedEntity)

        then:
        committedEntity == validatedEntity

        when:
        committedEntity.setName("1")
        dataManager.commit(committedEntity)

        then:
        thrown(EntityValidationException)
    }

    def "Entity remove test"() {
        when:
        def context = new CommitContext()
        context.setValidationType(CommitContext.ValidationType.NEVER_VALIDATE)
        def validatedEntity = new ValidatedEntity(name: "1")
        context.addInstanceToCommit(validatedEntity)
        def committedEntity = dataManager.commit(context).iterator().next()

        then:
        committedEntity == validatedEntity

        when:
        context = new CommitContext()
        context.setValidationType(CommitContext.ValidationType.ALWAYS_VALIDATE)
        context.addInstanceToRemove(committedEntity)
        dataManager.commit(context)

        then: "ok"
    }

    def "Embedded entity test"() {
        when:
        def validatedEntity = new ValidatedEntity(name: "11111")
        validatedEntity.setEmbeddedValidatedEntity(new EmbeddedValidatedEntity(name: "1"))
        dataManager.commit(validatedEntity)

        then:
        thrown(EntityValidationException)
    }
}
