/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.testmodel.sales.Order
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class BaseViewTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    def "base view"() {

        def view = cont.metadata().viewRepository.getView(Order, View.BASE)

        expect:

        view.containsProperty('number')
        view.containsProperty('date')
        view.containsProperty('amount')
        view.containsProperty('customer')
        !view.containsProperty('user')
    }
}
