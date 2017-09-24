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

package spec.cuba.core.datatypes

import com.haulmont.chile.core.datatypes.DatatypeRegistry
import com.haulmont.chile.core.datatypes.impl.*
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class DatatypeTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE;

    def "standard datatypes"() {

        def datatypes = AppBeans.get(DatatypeRegistry.class)

        expect:

        datatypes.get(String.class).class == StringDatatype
        datatypes.get(Boolean.class).class == BooleanDatatype
        datatypes.get(Integer.class).class == IntegerDatatype
        datatypes.get(Long.class).class == LongDatatype
        datatypes.get(Double.class).class == DoubleDatatype
        datatypes.get(BigDecimal.class).class == BigDecimalDatatype
        datatypes.get(java.util.Date.class).class == DateTimeDatatype
        datatypes.get(java.sql.Date.class).class == DateDatatype
        datatypes.get(java.sql.Time.class).class == TimeDatatype
        datatypes.get(UUID.class).class == UUIDDatatype
        datatypes.get(byte[].class).class == ByteArrayDatatype
    }

    def "not supported types"() {

        def datatypes = AppBeans.get(DatatypeRegistry.class)

        expect:

        datatypes.get(Float.class) == null
    }

}
