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

package spec.cuba.global.persistence

import com.haulmont.cuba.core.sys.persistence.CubaEntityFetchGroup
import com.haulmont.cuba.core.sys.persistence.FetchGroupUtils
import spock.lang.Specification

class FetchGroupUtilsTest extends Specification {

    def first = new CubaEntityFetchGroup(['a', 'b.b1'])
    def second = new CubaEntityFetchGroup(['a', 'c', 'b.b2', 'b.b3', 'b.b2.b21'])

    def "flat attributes"() {
        expect:

        first.getGroup('b').attributeNames == ['b1'].toSet()
        second.getGroup('b').attributeNames == ['b2', 'b3'].toSet()
        second.getGroup('b').getGroup('b2').attributeNames == ['b21'].toSet()

        FetchGroupUtils.getFetchGroupAttributes(first) == ['a', 'b.b1'].toSet()
        FetchGroupUtils.getFetchGroupAttributes(second) == ['a', 'c', 'b.b2.b21', 'b.b3'].toSet()
    }

    def "merge"() {
        when:

        def result = FetchGroupUtils.mergeFetchGroups(first, second)

        then:

        result.attributeNames == ['a', 'b', 'c'].toSet()
        result.getGroup('b').attributeNames == ['b1', 'b2', 'b3'].toSet()
        result.getGroup('b').getGroup('b2').attributeNames == ['b21'].toSet()
    }

    def "merge with first null"() {
        when:

        def result = FetchGroupUtils.mergeFetchGroups(null, second)

        then:

        result.attributeNames == ['a', 'b', 'c'].toSet()
        result.getGroup('b').attributeNames == ['b2', 'b3'].toSet()
        result.getGroup('b').getGroup('b2').attributeNames == ['b21'].toSet()
    }

    def "merge with second null"() {
        when:

        def result = FetchGroupUtils.mergeFetchGroups(first, null)

        then:

        result.attributeNames == ['a', 'b'].toSet()
        result.getGroup('b').attributeNames == ['b1'].toSet()
    }

    def "merge both null"() {
        when:

        def result = FetchGroupUtils.mergeFetchGroups(null, null)

        then:

        result.attributeNames.isEmpty()
    }
}
