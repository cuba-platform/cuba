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

package spec.cuba.global.instance_utils

import com.haulmont.chile.core.model.utils.InstanceUtils
import spock.lang.Specification

class InstanceUtilsTest extends Specification {

    def "parseValuePath accepts null input"() {
        when:
        String[] path = InstanceUtils.parseValuePath(null)

        then:
        noExceptionThrown()
        path.length == 0
    }

    def "formatValuePath accepts null input"() {
        when:
        String path = InstanceUtils.formatValuePath(null)

        then:
        noExceptionThrown()
        path == ''
    }

    def "getValueEx accepts null input"() {
        when:
        Object value = InstanceUtils.getValueEx(null, 'foo')

        then:
        noExceptionThrown()
        value == null

        when:
        value = InstanceUtils.getValueEx(null, null as String)

        then:
        noExceptionThrown()
        value == null

        when:
        value = InstanceUtils.getValueEx(null, ['foo'] as String[])

        then:
        noExceptionThrown()
        value == null

        when:
        value = InstanceUtils.getValueEx(null, null as String[])

        then:
        noExceptionThrown()
        value == null
    }
}
