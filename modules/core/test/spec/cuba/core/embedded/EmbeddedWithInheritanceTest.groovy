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
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testmodel.embeddedwithinheritance.Person
import com.haulmont.cuba.testmodel.embeddedwithinheritance.VerificationInfo
import com.haulmont.cuba.testsupport.TestContainer
import com.haulmont.cuba.testsupport.TestSupport
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EmbeddedWithInheritanceTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataManager dataManager
    private Persistence persistence
    private Metadata metadata

    protected UUID personId, userId

    void setup() {
        dataManager = AppBeans.get(DataManager)
        persistence = cont.persistence()
        metadata = cont.metadata()

        persistence.createTransaction().execute(new Transaction.Runnable() {
            @Override
            public void run(EntityManager em) {
                Person person = metadata.create(Person)
                personId = person.id

                User user = metadata.create(User)
                userId = user.id
                user.login = 'test'
                user.loginLowerCase = 'test'
                user.group = (Group) em.find(Group.class, TestSupport.COMPANY_GROUP_ID)

                em.persist(user)

                VerificationInfo verificationInfo = metadata.create(VerificationInfo)
                verificationInfo.user = user
                verificationInfo.date = new Date()

                person.name = "Person#1"
                person.verificationInfo = verificationInfo

                em.persist(person)
            }
        })
    }

    void cleanup() {
        def runner = new QueryRunner(persistence.dataSource)
        runner.update('delete from TEST_EMBEDDED_PERSON')
        cont.deleteRecord('SEC_USER', userId)
    }

    def "load person with embedded"() {
        when:
        def view = new View(Person, false)
                .addProperty("name")
                .addProperty("verificationInfo",
                        new View(VerificationInfo)
                                .addProperty("date")
                                .addProperty("user", new View(User).addProperty("login"))

                )
                .setLoadPartialEntities(true)

        Person person = dataManager.load(Person).view(view).id(personId).one()

        then:

        person != null
        person.verificationInfo.user != null
        person.verificationInfo.user.login == 'test'
    }
}
