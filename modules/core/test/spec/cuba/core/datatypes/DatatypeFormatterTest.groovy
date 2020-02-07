/*
 * Copyright (c) 2008-2020 Haulmont.
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

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.DatatypeFormatter
import com.haulmont.cuba.core.global.UserSessionSource
import com.haulmont.cuba.security.global.UserSession
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

class DatatypeFormatterTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    DatatypeFormatter datatypeFormatter
    Locale savedLocale
    UserSessionSource userSessionSource

    def setup() {
        datatypeFormatter = AppBeans.get(DatatypeFormatter)
        userSessionSource = AppBeans.get(UserSessionSource)

        UserSession userSession = userSessionSource.userSession
        savedLocale = userSession.locale
        userSession.locale = Locale.ENGLISH
    }

    def cleanup() {
        userSessionSource.userSession.locale = savedLocale
    }

    def 'format LocalDate'() {
        def localDate = LocalDate.of(2020, 02, 23)

        when:
        def formatted = datatypeFormatter.formatLocalDate(localDate)

        then:
        formatted == '23/02/2020'
    }

    def 'parse LocalDate'() {
        String str = '23/02/2020'

        when:

        LocalDate localDate = datatypeFormatter.parseLocalDate(str)

        then:

        localDate == LocalDate.of(2020, 02, 23)
    }


    def 'format LocalDateTime'() {
        def localDateTime = LocalDateTime.of(2020, 02, 23, 14, 56)

        when:
        def formatted = datatypeFormatter.formatLocalDateTime(localDateTime)

        then:
        formatted == '23/02/2020 14:56'
    }

    def 'parse LocalDateTime'() {
        String str = '23/02/2020 14:56'

        when:

        LocalDateTime localDateTime = datatypeFormatter.parseLocalDateTime(str)

        then:

        localDateTime == LocalDateTime.of(2020, 02, 23, 14, 56)
    }

    def 'format LocalTime'() {
        def localTime = LocalTime.of(14, 56)

        when:
        def formatted = datatypeFormatter.formatLocalTime(localTime)

        then:
        formatted == '14:56'
    }

    def 'parse LocalTime'() {
        String str = '14:56'

        when:

        LocalTime localTime = datatypeFormatter.parseLocalTime(str)

        then:

        localTime == LocalTime.of(14, 56)
    }

    def 'format OffsetDateTime'() {
        LocalDateTime localDateTime = LocalDateTime.of(2020, 02, 23, 14, 56)
        ZoneOffset offset = ZoneOffset.of("+02:00")
        OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, offset)

        when:
        def formatted = datatypeFormatter.formatOffsetDateTime(offsetDateTime)

        then:
        formatted == '23/02/2020 14:56 +0200'
    }

    def 'parse OffsetDateTime'() {
        String str = '23/02/2020 14:56 +0200'

        when:

        OffsetDateTime offsetDateTime = datatypeFormatter.parseOffsetDateTime(str)

        then:

        LocalDateTime localDateTime = LocalDateTime.of(2020, 02, 23, 14, 56)
        ZoneOffset offset = ZoneOffset.of("+02:00")

        offsetDateTime == OffsetDateTime.of(localDateTime, offset)
    }

    def 'format OffsetTime'() {
        LocalTime localTime = LocalTime.of(14, 56)
        ZoneOffset offset = ZoneOffset.of("+02:00")
        OffsetTime offsetTime = OffsetTime.of(localTime, offset)

        when:
        def formatted = datatypeFormatter.formatOffsetTime(offsetTime)

        then:

        formatted == '14:56 +0200'
    }

    def 'parse OffsetTime'() {
        String str = '14:56 +0200'

        when:

        OffsetTime offsetTime = datatypeFormatter.parseOffsetTime(str)

        then:

        LocalTime localTime = LocalTime.of(14, 56)
        ZoneOffset offset = ZoneOffset.of("+02:00")

        offsetTime == OffsetTime.of(localTime, offset)
    }

}
