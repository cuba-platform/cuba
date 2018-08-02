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

package spec.cuba.core.embedded

import com.haulmont.bali.db.QueryRunner
import com.haulmont.cuba.core.EntityManager
import com.haulmont.cuba.core.Persistence
import com.haulmont.cuba.core.Transaction
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.testmodel.embedded.AddressEmbedded
import com.haulmont.cuba.testmodel.embedded.AddressEmbeddedContainer
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EmbeddedTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataManager dataManager
    private Persistence persistence
    private Metadata metadata

    protected UUID container1Id, container2Id

    void setup() {
        dataManager = AppBeans.get(DataManager)
        persistence = cont.persistence()
        metadata = cont.metadata()

        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                AddressEmbeddedContainer container1 = metadata.create(AddressEmbeddedContainer.class)
                container1.name = "TestContainer#1"
                container1Id = container1.id

                em.persist(container1)

                AddressEmbeddedContainer container2 = metadata.create(AddressEmbeddedContainer.class)
                container2.name = "TestContainer#2"

                AddressEmbedded addressEmbedded = metadata.create(AddressEmbedded)
                addressEmbedded.country = "Country#2"
                container2.address = addressEmbedded

                container2Id = container2.id

                em.persist(container2)
            }
        })
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_ADDRESS_EMBEDDED_CONTAINER')
    }

    def "save embedded after value was null"() {
        def view = new View(AddressEmbeddedContainer, false)
                .addProperty("name")
                .addProperty("address", new View(AddressEmbedded.class).addProperty("country"))
                .setLoadPartialEntities(true)
        def container = dataManager.load(AddressEmbeddedContainer).view(view).id(container1Id).one()

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when:

        container.name = "TestContainer#1_1"
        AddressEmbedded addressEmbedded = metadata.create(AddressEmbedded)
        addressEmbedded.country = "Country#1"
        container.address = addressEmbedded
        dataManager.commit(container)

        container = dataManager.load(AddressEmbeddedContainer).view(view).id(container1Id).one()

        then:

        container != null
        container.name == "TestContainer#1_1"
        container.address != null
        container.address.country == "Country#1"
    }

    def "save embedded after value was not null"() {
        def view = new View(AddressEmbeddedContainer, false)
                .addProperty("name")
                .addProperty("address", new View(AddressEmbedded.class).addProperty("country").addProperty("index"))
                .setLoadPartialEntities(true)
        def container = dataManager.load(AddressEmbeddedContainer).view(view).id(container2Id).one()

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when:

        container.name = "TestContainer#2_1"
        container.address.index = 10
        dataManager.commit(container)

        container = dataManager.load(AddressEmbeddedContainer).view(view).id(container2Id).one()

        then:

        container != null
        container.name == "TestContainer#2_1"
        container.address != null
        container.address.index == 10
    }


    def "save embedded with null values"() {
        def view = new View(AddressEmbeddedContainer, false)
                .addProperty("name")
                .addProperty("address", new View(AddressEmbedded.class).addProperty("country").addProperty("index"))
                .setLoadPartialEntities(true)
        def container = dataManager.load(AddressEmbeddedContainer).view(view).id(container2Id).one()

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        when:

        container.name = "TestContainer#2_2"
        container.address.country = null
        dataManager.commit(container)

        container = dataManager.load(AddressEmbeddedContainer).view(view).id(container2Id).one()

        then:

        container != null
        container.name == "TestContainer#2_2"
        container.address == null
    }
}
