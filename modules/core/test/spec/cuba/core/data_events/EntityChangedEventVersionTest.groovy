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

package spec.cuba.core.data_events

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.entity.contracts.Id
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.EntitySet
import com.haulmont.cuba.security.app.EntityLog
import com.haulmont.cuba.testmodel.entitychangedevent.EceTestProduct
import com.haulmont.cuba.testmodel.entitychangedevent.TestProductChangeListener
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EntityChangedEventVersionTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    DataManager dataManager

    void setup() {
        dataManager = AppBeans.get(DataManager)
        AppBeans.get(EntityLog)
    }

    void cleanup() {
        def queryRunner = new QueryRunner(cont.persistence().dataSource)
        queryRunner.update('delete from TEST_ECE_STOCK')
        queryRunner.update('delete from TEST_ECE_PRODUCT')
        queryRunner.update('delete from TEST_ECE_LOG')
    }

    def "returned instance with latest version"() {

        def product = dataManager.create(EceTestProduct)

        when:

        def product1 = dataManager.commit(product)

        then:

        product1.version == dataManager.load(Id.of(product)).one().version
    }

    def "returned only given instance"() {

        def product = dataManager.create(EceTestProduct)

        when:

        EntitySet committed = dataManager.commit(new CommitContext(product))

        then:

        committed.size() == 1
        committed[0] == product
        committed[0].version == dataManager.load(Id.of(product)).one().version
    }

    def "suspended transaction"() {

        AppBeans.get(TestProductChangeListener).doLog = true

        def product = dataManager.create(EceTestProduct)

        when:

        def product1 = dataManager.commit(product)

        then:

        product1.version == dataManager.load(Id.of(product)).one().version

        cleanup:

        AppBeans.get(TestProductChangeListener).doLog = false
    }

}
