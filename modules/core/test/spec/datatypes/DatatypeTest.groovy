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

    @SuppressWarnings("UnnecessaryQualifiedReference")
    def "standard datatypes"() {

        def datatypes = AppBeans.get(DatatypeRegistry.class)

        expect:

        datatypes.get(String.class).name == StringDatatype.NAME
        datatypes.get(Boolean.class).name == BooleanDatatype.NAME
        datatypes.get(Integer.class).name == IntegerDatatype.NAME
        datatypes.get(Long.class).name == LongDatatype.NAME
        datatypes.get(Double.class).name == DoubleDatatype.NAME
        datatypes.get(BigDecimal.class).name == BigDecimalDatatype.NAME
        datatypes.get(java.util.Date.class).name == DateTimeDatatype.NAME
        datatypes.get(java.sql.Date.class).name == DateDatatype.NAME
        datatypes.get(java.sql.Time.class).name == TimeDatatype.NAME
        datatypes.get(UUID.class).name == UUIDDatatype.NAME
        datatypes.get(byte[].class).name == ByteArrayDatatype.NAME
    }

    def "not supported types"() {

        def datatypes = AppBeans.get(DatatypeRegistry.class)

        expect:

        datatypes.get(Float.class) == null
    }

}
