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

package spec.cuba.web.datacontext

import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.entity.Entity
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.gui.screen.UiControllerUtils
import com.haulmont.cuba.web.testmodel.sales.Order
import com.haulmont.cuba.web.testmodel.sales.OrderLine
import com.haulmont.cuba.web.testmodel.sales.OrderLineParam
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.datacontext.screens.OrderScreen
import spock.lang.Unroll

@SuppressWarnings("GroovyAssignabilityCheck")
class CompositionScreensTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.datacontext.screens', 'com.haulmont.cuba.web.app.main'])
    }

    @Unroll
    def "create and immediate edit of the same nested instance"(boolean explicitParentDc) {

        showMainWindow()

        def orderScreen = screens.create(OrderScreen)
        def order = metadata.create(Order)
        orderScreen.order = order
        orderScreen.show()

        def orderScreenDc = UiControllerUtils.getScreenData(orderScreen).dataContext

        when: "create entity"

        def lineScreenForCreate = orderScreen.buildLineScreenForCreate(explicitParentDc)
        lineScreenForCreate.show()

        lineScreenForCreate.changeCommitAndClose(1)

        then:

        def order1 = orderScreenDc.find(Order, order.id)
        order1.orderLines.size() == 1
        def line1 = order1.orderLines[0]
        line1.order.is(order1)

        when: "edit same entity"

        def lineScreenForEdit = orderScreen.buildLineScreenForEdit(explicitParentDc)
        lineScreenForEdit.show()

        lineScreenForEdit.changeCommitAndClose(2)

        then:

        def order2 = orderScreenDc.find(Order, order.id)
        order2.is(order1)
        order2.orderLines.size() == 1
        def line2 = order2.orderLines[0]
        line2.is(line1)
        line2.order.is(order2)

        where:

        explicitParentDc << [true, false]
    }

    def "remove nested instance on 2nd level"() {

        showMainWindow()

        def orderScreen = screens.create(OrderScreen)

        def order = makeSaved(new Order(number: '1', orderLines: []))

        def orderLine = makeSaved(new OrderLine(quantity: 1, params: []))
        orderLine.order = order
        order.orderLines.add(orderLine)

        def lineParam = makeSaved(new OrderLineParam(name: 'p1'))
        lineParam.orderLine = orderLine
        orderLine.params.add(lineParam)

        orderScreen.order = order
        orderScreen.show()

        def orderScreenCtx = UiControllerUtils.getScreenData(orderScreen).dataContext

        when:

        def lineScreen = orderScreen.buildLineScreenForEdit(false)
        lineScreen.show()

        def lineScreenCtx = UiControllerUtils.getScreenData(lineScreen).dataContext

        then:

        lineScreenCtx.parent == orderScreenCtx
        lineScreen.paramsDc.items.contains(lineParam)

        when:

        def lineParam1 = lineScreenCtx.find(lineParam)
        lineScreen.paramsDc.getMutableItems().remove(lineParam1)
        lineScreenCtx.remove(lineParam1)
        lineScreenCtx.commit()

        then:

        orderScreenCtx.isRemoved(lineParam)
    }

    def "remove nested instance on 2nd level if the root entity did not have the full object graph"() {

        showMainWindow()

        def orderScreen = screens.create(OrderScreen)

        def order = makeSaved(new Order(number: '1', orderLines: []))

        def orderLine = makeSaved(new OrderLine(quantity: 1, params: []))
        orderLine.order = order
        order.orderLines.add(orderLine)

        orderScreen.order = order
        orderScreen.show()

        def orderScreenCtx = UiControllerUtils.getScreenData(orderScreen).dataContext

        when:

        def lineScreen = orderScreen.buildLineScreenForEdit(false)

        def lineParam = makeSaved(new OrderLineParam(name: 'p1'))
        lineScreen.getEditedEntity().params = []
        lineScreen.getEditedEntity().params.add(lineParam)

        lineScreen.show()

        def lineScreenCtx = UiControllerUtils.getScreenData(lineScreen).dataContext

        then:

        lineScreenCtx.parent == orderScreenCtx
        lineScreen.paramsDc.items.contains(lineParam)

        when:

        def lineParam1 = lineScreenCtx.find(lineParam)
        lineScreen.paramsDc.getMutableItems().remove(lineParam1)
        lineScreenCtx.remove(lineParam1)
        lineScreenCtx.commit()

        then:

        orderScreenCtx.isRemoved(lineParam)
    }

    private static <T extends Entity> T makeSaved(T entity) {
        def cc = new CommitContext().addInstanceToCommit(entity)
        def ds = TestServiceProxy.getDefault(DataService)
        return ds.commit(cc)[0] as T
    }
}