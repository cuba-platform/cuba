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

package spec.cuba.global.datatypes

import com.haulmont.chile.core.datatypes.impl.BigDecimalDatatype
import com.haulmont.chile.core.datatypes.impl.BooleanDatatype
import com.haulmont.chile.core.datatypes.impl.ByteArrayDatatype
import com.haulmont.chile.core.datatypes.impl.DateDatatype
import com.haulmont.chile.core.datatypes.impl.DateTimeDatatype
import com.haulmont.chile.core.datatypes.impl.DoubleDatatype
import com.haulmont.chile.core.datatypes.impl.IntegerDatatype
import com.haulmont.chile.core.datatypes.impl.LocalDateDatatype
import com.haulmont.chile.core.datatypes.impl.LocalDateTimeDatatype
import com.haulmont.chile.core.datatypes.impl.LocalTimeDatatype
import com.haulmont.chile.core.datatypes.impl.LongDatatype
import com.haulmont.chile.core.datatypes.impl.OffsetDateTimeDatatype
import com.haulmont.chile.core.datatypes.impl.OffsetTimeDatatype
import com.haulmont.chile.core.datatypes.impl.StringDatatype
import com.haulmont.chile.core.datatypes.impl.TimeDatatype
import com.haulmont.chile.core.datatypes.impl.UUIDDatatype
import org.dom4j.Element
import spock.lang.Specification

import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime;

/**
 *
 */
class DatatypeTest extends Specification {

    def "java type specified by annotation"() {

        Element element = Mock()

        def bigDecimalDatatype = new BigDecimalDatatype(element)
        def booleanDatatype = new BooleanDatatype()
        def byteArrayDatatype = new ByteArrayDatatype()
        def dateDatatype = new DateDatatype(element)
        def dateTimeDatatype = new DateTimeDatatype(element)
        def doubleDatatype = new DoubleDatatype(element)
        def integerDatatype = new IntegerDatatype(element)
        def longDatatype = new LongDatatype(element)
        def stringDatatype = new StringDatatype()
        def timeDatatype = new TimeDatatype(element)
        def uuidDatatype = new UUIDDatatype()
        def localDateDatatype = new LocalDateDatatype(element)
        def localDateTimeDatatype = new LocalDateTimeDatatype(element)
        def localTimeDatatype = new LocalTimeDatatype(element)
        def offsetDateTimeDatatype = new OffsetDateTimeDatatype(element)
        def offsetTimeDatatype = new OffsetTimeDatatype(element)

        expect:

        bigDecimalDatatype.getJavaClass() == BigDecimal
        booleanDatatype.getJavaClass() == Boolean
        byteArrayDatatype.getJavaClass() == byte[]
        dateDatatype.getJavaClass() == java.sql.Date
        dateTimeDatatype.getJavaClass() == Date
        doubleDatatype.getJavaClass() == Double
        integerDatatype.getJavaClass() == Integer
        longDatatype.getJavaClass() == Long
        stringDatatype.getJavaClass() == String
        timeDatatype.getJavaClass() == Time
        uuidDatatype.getJavaClass() == UUID
        localDateDatatype.getJavaClass() == LocalDate
        localDateTimeDatatype.getJavaClass() == LocalDateTime
        localTimeDatatype.getJavaClass() == LocalTime
        offsetDateTimeDatatype.getJavaClass() == OffsetDateTime
        offsetTimeDatatype.getJavaClass() == OffsetTime
    }

    def "deprecated method still works"() {
        def stringDatatype = new StringDatatype()

        expect:
        stringDatatype.getName() == 'string'
    }
}
