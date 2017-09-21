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

package spec.datatypes

import com.haulmont.chile.core.datatypes.impl.StringDatatype
import com.haulmont.cuba.core.sys.DatatypeRegistryImpl
import spock.lang.Specification

class DatatypeRegistryTest extends Specification {

    def "add non-default datatype"() {

        def registry = new DatatypeRegistryImpl()

        def stringDatatype = new StringDatatype()

        def myStringDatatype = new StringDatatype() {
            @Override
            String getName() {
                return 'my_string'
            }
            @Override
            String toString() {
                return getName()
            }
        }

        when:

        registry.register(stringDatatype, true)
        registry.register(myStringDatatype, false)

        then:

        registry.get(String.class).is(stringDatatype)
        registry.get(StringDatatype.NAME).is(stringDatatype)
        registry.get('my_string').is(myStringDatatype)
    }

    def "override default datatype"() {

        def registry = new DatatypeRegistryImpl()

        def stringDatatype = new StringDatatype()

        def myStringDatatype = new StringDatatype() {
            @Override
            String toString() {
                return 'my_datatype'
            }
        }

        when:

        registry.register(stringDatatype, true)
        registry.register(myStringDatatype, true)

        then:

        registry.get(String.class).is(myStringDatatype)
        registry.get(StringDatatype.NAME).is(myStringDatatype)
    }
}
