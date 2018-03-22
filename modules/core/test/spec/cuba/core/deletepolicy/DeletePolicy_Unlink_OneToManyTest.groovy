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
import com.haulmont.cuba.testmodel.deletepolicy.DeletePolicy_OneToMany_First
import com.haulmont.cuba.testmodel.deletepolicy.DeletePolicy_Root
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DeletePolicy_Unlink_OneToManyTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Persistence persistence = cont.persistence()
    private Metadata metadata = cont.metadata()
    private DataManager dataManager

    private DeletePolicy_Root root
    private DeletePolicy_OneToMany_First first1, first2

    void setup() {
        persistence.runInTransaction({ em ->
            root = metadata.create(DeletePolicy_Root)
            root.rootFld = 'root fld'
            em.persist(root)

            first1 = metadata.create(DeletePolicy_OneToMany_First)
            first1.firstFld = 'first fld #1'
            first1.setRoot(root)
            em.persist(first1)

            first2 = metadata.create(DeletePolicy_OneToMany_First)
            first2.firstFld = 'first fld #2'
            first2.setRoot(root)
            em.persist(first2)

        })
        dataManager = AppBeans.get(DataManager.class)
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_DELETE_POLICY_ONE_TO_MANY_FIRST')
        runner.update('delete from TEST_DELETE_POLICY_ROOT')
    }

    def "unlink @OneToMany property if it isn't owning side and is loaded"() {
        setup:

        View rootView_2 = new View(DeletePolicy_Root.class)
                .addProperty("rootFld")
        View firstView_1 = new View(DeletePolicy_OneToMany_First.class)
                .addProperty("firstFld")
                .addProperty("root", rootView_2)
        View rootView_1 = new View(DeletePolicy_Root.class)
                .addProperty("rootFld")
                .addProperty("onetomany", firstView_1)

        View firstView_2 = new View(DeletePolicy_OneToMany_First.class)
                .addProperty("firstFld")
                .addProperty("root", rootView_2)

        when:

        DeletePolicy_Root entityRoot = dataManager.load(
                new LoadContext<DeletePolicy_Root>(DeletePolicy_Root.class)
                        .setView(rootView_1)
                        .setId(root.id))

        then:

        entityRoot.onetomany != null
        entityRoot.onetomany.size() == 2

        when:

        dataManager.remove(entityRoot)
        entityRoot = dataManager.load(
                new LoadContext<DeletePolicy_Root>(DeletePolicy_Root.class)
                        .setView(rootView_1)
                        .setId(root.id).setSoftDeletion(false))

        DeletePolicy_OneToMany_First entityFirst1 = dataManager.load(
                new LoadContext<DeletePolicy_OneToMany_First>(DeletePolicy_OneToMany_First.class)
                        .setView(firstView_2)
                        .setId(first1.id))

        DeletePolicy_OneToMany_First entityFirst2 = dataManager.load(
                new LoadContext<DeletePolicy_OneToMany_First>(DeletePolicy_OneToMany_First.class)
                        .setView(firstView_2)
                        .setId(first2.id))

        then:

        entityRoot.onetomany != null
        entityRoot.onetomany.size() == 0
        entityFirst1 != null
        entityFirst1.root == null
        entityFirst2 != null
        entityFirst2.root == null
    }

    def "unlink @OneToMany property if it isn't owning side and isn't loaded"() {
        setup:

        View rootView_2 = new View(DeletePolicy_Root.class)
                .addProperty("rootFld")
        View firstView_1 = new View(DeletePolicy_OneToMany_First.class)
                .addProperty("firstFld")
                .addProperty("root", rootView_2)
        View rootView_1 = new View(DeletePolicy_Root.class)
                .addProperty("rootFld")
                .addProperty("onetomany", firstView_1)

        View firstView_2 = new View(DeletePolicy_OneToMany_First.class)
                .addProperty("firstFld")
                .addProperty("root", rootView_2)

        when:

        DeletePolicy_Root entityRoot = dataManager.load(
                new LoadContext<DeletePolicy_Root>(DeletePolicy_Root.class)
                        .setView(rootView_1)
                        .setId(root.id))

        then:

        entityRoot.onetomany != null
        entityRoot.onetomany.size() == 2

        when:

        dataManager.remove(dataManager.load(new LoadContext<DeletePolicy_Root>(DeletePolicy_Root.class)
                .setView(View.LOCAL)
                .setId(root.id)))
        entityRoot = dataManager.load(
                new LoadContext<DeletePolicy_Root>(DeletePolicy_Root.class)
                        .setView(rootView_1)
                        .setId(root.id).setSoftDeletion(false))

        DeletePolicy_OneToMany_First entityFirst1 = dataManager.load(
                new LoadContext<DeletePolicy_OneToMany_First>(DeletePolicy_OneToMany_First.class)
                        .setView(firstView_2)
                        .setId(first1.id))

        DeletePolicy_OneToMany_First entityFirst2 = dataManager.load(
                new LoadContext<DeletePolicy_OneToMany_First>(DeletePolicy_OneToMany_First.class)
                        .setView(firstView_2)
                        .setId(first2.id))

        then:

        entityRoot.onetomany != null
        entityRoot.onetomany.size() == 0
        entityFirst1 != null
        entityFirst1.root == null
        entityFirst2 != null
        entityFirst2.root == null
    }
}
