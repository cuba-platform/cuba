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

package spec.cuba.core.deletepolicy

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.testmodel.deletepolicy.DeletePolicy_OneToOne_First
import com.haulmont.cuba.testmodel.deletepolicy.DeletePolicy_OneToOne_Second
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DeletePolicy_Unlink_OneToOneTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Persistence persistence = cont.persistence()
    private Metadata metadata = cont.metadata()
    private DataManager dataManager

    private DeletePolicy_OneToOne_First first
    private DeletePolicy_OneToOne_Second second

    void setup() {
        persistence.runInTransaction({ em ->
            first = metadata.create(DeletePolicy_OneToOne_First)
            first.firstFld = 'first fld'
            em.persist(first)

            second = metadata.create(DeletePolicy_OneToOne_Second)
            second.secondFld = 'second fld'
            second.setFirst(first)
            em.persist(second)
        })
        dataManager = AppBeans.get(DataManager.class)
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_DELETE_POLICY_ONE_TO_ONE_SECOND')
        runner.update('delete from TEST_DELETE_POLICY_ONE_TO_ONE_FIRST')
    }

    def "unlink @OneToOny property if it isn't owning side"() {
        setup:
        View secondView_1 = new View(DeletePolicy_OneToOne_Second.class)
                .addProperty("secondFld")
        View firstView_1 = new View(DeletePolicy_OneToOne_First.class)
                .addProperty("firstFld")
                .addProperty("second", secondView_1)

        View firstView_2 = new View(DeletePolicy_OneToOne_First.class)
                .addProperty("firstFld")
        View secondView_2 = new View(DeletePolicy_OneToOne_Second.class)
                .addProperty("secondFld")
                .addProperty("first", firstView_2)

        when:

        DeletePolicy_OneToOne_First entityFirst = dataManager.load(
                new LoadContext<DeletePolicy_OneToOne_First>(DeletePolicy_OneToOne_First.class)
                        .setView(firstView_1)
                        .setId(first.id))

        then:

        entityFirst.second != null

        when:

        dataManager.remove(entityFirst)
        DeletePolicy_OneToOne_Second entitySecond = dataManager.load(
                new LoadContext<DeletePolicy_OneToOne_Second>(DeletePolicy_OneToOne_Second.class)
                        .setView(secondView_2)
                        .setId(second.id))

        then:

        entitySecond.first == null
    }

    def "unlink @OneToOny property if it is owning side and is loaded"() {
        setup:
        View firstView = new View(DeletePolicy_OneToOne_First.class)
                .addProperty("firstFld")
        View secondView_2 = new View(DeletePolicy_OneToOne_Second.class)
                .addProperty("secondFld")
                .addProperty("first", firstView)

        when:

        DeletePolicy_OneToOne_Second entitySecond = dataManager.load(
                new LoadContext<DeletePolicy_OneToOne_Second>(DeletePolicy_OneToOne_Second.class)
                        .setView(secondView_2)
                        .setId(second.id))

        then:

        entitySecond.first != null

        when:

        dataManager.remove(entitySecond)
        entitySecond = dataManager.load(
                new LoadContext<DeletePolicy_OneToOne_Second>(DeletePolicy_OneToOne_Second.class)
                        .setView(secondView_2)
                        .setId(second.id).setSoftDeletion(false))

        then:

        entitySecond.first == null
    }

    def "unlink @OneToOny property if it is owning side and isn't loaded"() {
        setup:
        View firstView = new View(DeletePolicy_OneToOne_First.class)
                .addProperty("firstFld")
        View secondView_2 = new View(DeletePolicy_OneToOne_Second.class)
                .addProperty("secondFld")
                .addProperty("first", firstView)

        when:

        DeletePolicy_OneToOne_Second entitySecond = dataManager.load(
                new LoadContext<DeletePolicy_OneToOne_Second>(DeletePolicy_OneToOne_Second.class)
                        .setView(secondView_2)
                        .setId(second.id))

        then:

        entitySecond.first != null

        when:

        dataManager.remove(dataManager.load(new LoadContext<DeletePolicy_OneToOne_Second>(DeletePolicy_OneToOne_Second.class)
                .setView(View.LOCAL)
                .setId(second.id)))
        entitySecond = dataManager.load(
                new LoadContext<DeletePolicy_OneToOne_Second>(DeletePolicy_OneToOne_Second.class)
                        .setView(secondView_2)
                        .setId(second.id).setSoftDeletion(false))

        then:

        entitySecond.first == null
    }


}
