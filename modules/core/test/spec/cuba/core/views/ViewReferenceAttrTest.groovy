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

package spec.cuba.core.views

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.DevelopmentException
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class ViewReferenceAttrTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    def "Negative (String): PL-9999 Raise exception at the moment of view creation if not reference attribute has view"() {
        View wrongView = new View(User.class)
                .addProperty(
                    "name",
                    cont.metadata().viewRepository
                            .getView(User.class, "user.locale")
                )

        LoadContext<User> ctx = new LoadContext<>(User.class)
                .setView(wrongView)
        ctx.setQueryString("select u from sec\$User u")

        DataManager dataManager = AppBeans.get(DataManager.NAME)

        String exceptionMessage = ""

        when:
        try {
            dataManager.loadList(ctx)
        } catch (DevelopmentException e) {
            exceptionMessage = e.getMessage()
        }

        then:
        "Wrong Views mechanism usage found. View \"user.locale\" is set for property \"name\" of class \"sec\$User\", " +
                "but this property does not point to an Entity" == exceptionMessage
    }

    def "Negative (Boxed primitive): PL-9999 Raise exception at the moment of view creation if not reference attribute has view"() {
        View wrongView = new View(User.class)
                .addProperty(
                "active",
                cont.metadata().viewRepository
                        .getView(User.class, "user.locale")
        )

        LoadContext<User> ctx = new LoadContext<>(User.class)
                .setView(wrongView)
        ctx.setQueryString("select u from sec\$User u")

        DataManager dataManager = AppBeans.get(DataManager.NAME)

        String exceptionMessage = ""

        when:
        try {
            dataManager.loadList(ctx)
        } catch (DevelopmentException e) {
            exceptionMessage = e.getMessage()
        }

        then:
        "Wrong Views mechanism usage found. View \"user.locale\" is set for property \"active\" of class \"sec\$User\", " +
                "but this property does not point to an Entity" == exceptionMessage
    }

    def "Positive: PL-9999 Raise exception at the moment of view creation if not reference attribute has view"() {
        View correctView = new View(User.class)
                .addProperty("login")
                .addProperty(
                    "group",
                    new View(Group.class)
                            .addProperty("name")
                )

        LoadContext<User> ctx = LoadContext.create(User.class)
                .setView(correctView)
        ctx.setQueryString("select u from sec\$User u where u.login = 'admin'")

        DataManager dataManager = AppBeans.get(DataManager.NAME)
        User user

        when:
        user = dataManager.load(ctx)

        then:
        "Company" == user.group.name
    }
}