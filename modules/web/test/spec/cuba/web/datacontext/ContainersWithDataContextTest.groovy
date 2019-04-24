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

package spec.cuba.web.datacontext

import com.haulmont.cuba.gui.model.CollectionPropertyContainer
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.gui.model.DataContext
import com.haulmont.cuba.gui.model.InstanceContainer
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class ContainersWithDataContextTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataComponents factory

    void setup() {
        factory = cont.getBean(DataComponents)
    }

    def "entity added to property container are not merged into context"() {

        def context = factory.createDataContext()
        def orderDc = factory.createInstanceContainer(Order)
        def linesDc = factory.createCollectionContainer(OrderLine, orderDc, 'orderLines')

        def order1 = new Order(number: '1', orderLines: [])
        def line1 = new OrderLine(order: order1, quantity: 1)
        order1.orderLines.add(line1)

        orderDc.setItem(context.merge(order1))

        def line2 = new OrderLine(order: order1, quantity: 2)

        when:

        linesDc.getMutableItems().add(line2)

        then:

        !context.contains(line2)
    }
}
