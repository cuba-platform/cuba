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

package spec.cuba.core.data_manager

import com.haulmont.cuba.core.entity.KeyValueEntity
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.FluentLoaderTestAccess
import com.haulmont.cuba.core.global.FluentValueLoader
import com.haulmont.cuba.core.global.Stores
import com.haulmont.cuba.core.global.TemporalValue
import com.haulmont.cuba.core.global.ValueLoadContext
import com.haulmont.cuba.testmodel.sales.Customer
import com.haulmont.cuba.testmodel.sales.Status
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.TemporalType

class FluentValueLoaderTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private DataManager dataManager
    private customer
    private UUID customerId

    void setup() {
        dataManager = AppBeans.get(DataManager)
        customer = cont.metadata().create(Customer)
        customer.name = 'Smith'
        customer.status = Status.OK
        customerId = customer.id
        dataManager.commit(customer)
    }

    void cleanup() {
        cont.deleteRecord(customer)
    }

    def "usage examples"() {

        List<KeyValueEntity> list
        KeyValueEntity one
        Optional<KeyValueEntity> optional

        // Loading multiple values wrapped in KeyValueEntity

        when:

        list = dataManager.loadValues('select c.name from test$Customer c')
                .property('custName')
                .list()

        one = dataManager.loadValues('select c.name from test$Customer c')
                .property('custName')
                .one()

        optional = dataManager.loadValues('select c.name from test$Customer c')
                .property('custName')
                .optional()

        then:

        list[0].getValue('custName') == 'Smith'
        one.getValue('custName') == 'Smith'
        optional.get().getValue('custName') == 'Smith'

        when:

        list = dataManager.loadValues('select c.name, c.status from test$Customer c where c.name = :n')
                .properties('custName', 'custStatus')
                .parameter('n', 'Smith')
                .list()

        then:

        list[0].getValue('custName') == 'Smith'
        list[0].getValue('custStatus') == Status.OK.getId()

        // Loading a single value

        when:

        List<String> names = dataManager.loadValue('select c.name from test$Customer c', String).list()

        String name = dataManager.loadValue('select c.name from test$Customer c where c.id = :id', String)
                .parameter('id', customerId)
                .one()

        Optional<String> optName = dataManager.loadValue('select c.name from test$Customer c where c.id = :id', String)
                .parameter('id', customerId)
                .optional()

        then:

        names[0] == 'Smith'
        name == 'Smith'
        optName.get() == 'Smith'

        when:

        Long count = dataManager.loadValue('select count(c) from test$Customer c', Long).one()

        then:

        count == 1

        // The provided number value type can be different from the real type returned by the query. In the example
        // below, 'count(c)' is of Long type, but it is returned as Integer

        when:

        Object intCount = dataManager.loadValue('select count(c) from test$Customer c', Integer).one()

        then:

        intCount instanceof Integer
        intCount == 1
    }

    def "test ValueLoadContext"() {
        def loader
        ValueLoadContext loadContext

        when:

        loader = dataManager.loadValues('select c.name from test$Customer c')
                .property('name')
        loadContext = FluentLoaderTestAccess.createLoadContext(loader)

        then:

        loadContext.query.queryString == 'select c.name from test$Customer c'
        loadContext.storeName == Stores.MAIN
        loadContext.softDeletion
        loadContext.properties == ['name']

        when:

        loader = dataManager.loadValues('select c.name from test$Customer c where name = :n')
                .parameter('n', 'Smith')
        loadContext = FluentLoaderTestAccess.createLoadContext(loader)

        then:

        loadContext.query.parameters == ['n': 'Smith']
        loadContext.query.noConversionParams == null

        when:

        loader = dataManager.loadValues('select c.name from test$Customer c where name = :n')
                .parameter('n', 'Smith', false)
        loadContext = FluentLoaderTestAccess.createLoadContext(loader)

        then:

        loadContext.query.parameters == ['n': 'Smith']
        loadContext.query.noConversionParams[0] == 'n'

        when:

        loader = dataManager.loadValues('select.name c from test$Customer c where createTs >= :c')
                .parameter('c', new Date(), TemporalType.DATE)
        loadContext = FluentLoaderTestAccess.createLoadContext(loader)

        then:

        loadContext.query.parameters['c'] instanceof TemporalValue

        when:

        loader = dataManager.loadValues('select c.name from test$Customer c').softDeletion(false)
        loadContext = FluentLoaderTestAccess.createLoadContext(loader)

        then:

        !loadContext.softDeletion

        when:

        loader = dataManager.loadValues('select c.name from test$Customer c')
                .firstResult(10)
                .maxResults(100)
        loadContext = FluentLoaderTestAccess.createLoadContext(loader)

        then:

        loadContext.query.firstResult == 10
        loadContext.query.maxResults == 100
    }

    def "test casting number values"() {

        when:

        FluentValueLoader<Integer> intLoader = dataManager.loadValue('select xyz', Integer)

        then:

        FluentLoaderTestAccess.castValue(intLoader, Long.valueOf(10)).equals(10)
        FluentLoaderTestAccess.castValue(intLoader, Short.valueOf((short) 10)).equals(10)
        FluentLoaderTestAccess.castValue(intLoader, Double.valueOf(10.123)).equals(10)
        FluentLoaderTestAccess.castValue(intLoader, Float.valueOf(10.123)).equals(10)
        FluentLoaderTestAccess.castValue(intLoader, BigDecimal.valueOf(10.123)).equals(10)
        FluentLoaderTestAccess.castValue(intLoader, BigInteger.valueOf(10)).equals(10)

        when:

        FluentValueLoader<Long> longLoader = dataManager.loadValue('select xyz', Long)

        then:

        FluentLoaderTestAccess.castValue(longLoader, Integer.valueOf(10)).equals(10L)
        FluentLoaderTestAccess.castValue(longLoader, Short.valueOf((short) 10)).equals(10L)
        FluentLoaderTestAccess.castValue(longLoader, Double.valueOf(10.123)).equals(10L)
        FluentLoaderTestAccess.castValue(longLoader, Float.valueOf(10.123)).equals(10L)
        FluentLoaderTestAccess.castValue(longLoader, BigDecimal.valueOf(10.123)).equals(10L)
        FluentLoaderTestAccess.castValue(longLoader, BigInteger.valueOf(10)).equals(10L)

        when:

        FluentValueLoader<BigDecimal> decimalLoader = dataManager.loadValue('select xyz', BigDecimal)

        then:

        FluentLoaderTestAccess.castValue(decimalLoader, 10).equals(BigDecimal.valueOf(10.0))
        FluentLoaderTestAccess.castValue(decimalLoader, 10L).equals(BigDecimal.valueOf(10.0))
        FluentLoaderTestAccess.castValue(decimalLoader, (short) 10).equals(BigDecimal.valueOf(10.0))
        FluentLoaderTestAccess.castValue(decimalLoader, 10.123).equals(BigDecimal.valueOf(10.123))
        FluentLoaderTestAccess.castValue(decimalLoader, 10.123F) instanceof BigDecimal
        FluentLoaderTestAccess.castValue(decimalLoader, BigInteger.valueOf(10)).equals(BigDecimal.valueOf(10.0))

        when:

        FluentValueLoader<Double> doubleLoader = dataManager.loadValue('select xyz', Double)

        then:

        FluentLoaderTestAccess.castValue(doubleLoader, 10) instanceof Double
        FluentLoaderTestAccess.castValue(doubleLoader, 10L) instanceof Double
        FluentLoaderTestAccess.castValue(doubleLoader, (short) 10) instanceof Double
        FluentLoaderTestAccess.castValue(doubleLoader, 10.123F) instanceof Double
        FluentLoaderTestAccess.castValue(doubleLoader, BigDecimal.valueOf(10.123)) instanceof Double
        FluentLoaderTestAccess.castValue(doubleLoader, BigInteger.valueOf(10)) instanceof Double
    }

}
