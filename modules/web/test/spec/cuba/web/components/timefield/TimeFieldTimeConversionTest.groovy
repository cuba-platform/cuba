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

package spec.cuba.web.components.timefield

import com.haulmont.cuba.web.widgets.CubaTimeFieldWrapper
import com.haulmont.cuba.web.widgets.CubaTimeFieldWrapper.AmPmLocalTime
import com.haulmont.cuba.web.widgets.client.timefield.AmPm
import spock.lang.Specification

import java.time.LocalTime

@SuppressWarnings("GroovyAccessibility")
class TimeFieldTimeConversionTest extends Specification {

    def '24h time conversion to 12h format'() {
        when: '00:00 24h to 12h'
        def time = convertTo12h(0)

        then: 'time is 12:00 AM'
        time.time.hour == 12 && time.amPm == AmPm.AM

        when: '6:00 24h to 12h'
        time = convertTo12h(6)

        then: 'time is 6:00 AM'
        time.time.hour == 6 && time.amPm == AmPm.AM

        when: '12:00 24h to 12h'
        time = convertTo12h(12)

        then: 'time is 12:00 PM'
        time.time.hour == 12 && time.amPm == AmPm.PM

        when: '18:00 24h to 12h'
        time = convertTo12h(18)

        then: 'time is 6:00 PM'
        time.time.hour == 6 && time.amPm == AmPm.PM
    }

    def '12h time conversion to 24h format'() {
        when: '12:00 AM to 24h'
        def time = convertTo24h(12, AmPm.AM)

        then: 'time is 00:00'
        time.hour == 0

        when: '06:00 AM to 24h'
        time = convertTo24h(6, AmPm.AM)

        then: 'time is 06:00'
        time.hour == 6

        when: '12:00 PM to 24h'
        time = convertTo24h(12, AmPm.PM)

        then: 'time is 12:00'
        time.hour == 12

        when: '06:00 PM to 24h'
        time = convertTo24h(6, AmPm.PM)

        then: 'time is 18:00'
        time.hour == 18
    }

    def convertTo12h(int hours24) {
        return new CubaTimeFieldWrapper()
                .convertTo12hFormat(timeOf(hours24))
    }

    def convertTo24h(int hours12, AmPm amPm) {
        return new CubaTimeFieldWrapper()
                .convertFrom12hFormat(new AmPmLocalTime(timeOf(hours12), amPm))
    }

    def timeOf(int hours) {
        return new LocalTime(hours, 0, 0, 0)
    }
}
