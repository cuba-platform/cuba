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

package spec.cuba.gui.components.beanvalidation

import com.haulmont.cuba.core.global.DateTimeTransformations
import com.haulmont.cuba.gui.components.DatePicker
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import spec.cuba.gui.components.beanvalidation.screens.DateValidationScreen
import spec.cuba.web.UiScreenSpec

import java.time.*

class DatePickerRangeTest extends UiScreenSpec {

    DateTimeTransformations dateTimeTransformations

    void setup() {
        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })

        exportScreensPackages(['spec.cuba.gui.components.beanvalidation.screens'])

        dateTimeTransformations = cont.getBean(DateTimeTransformations.NAME)
    }

    def cleanup() {
        TestServiceProxy.clear()

        resetScreensConfig()
    }

    def "futureDatePicker and futureOrPresentDatePicker range test"() {
        given:
        showMainWindow()

        def dateValidationScreen = screens.create(DateValidationScreen)
        dateValidationScreen.show()

        def futureDatePicker = (DatePicker<Date>) dateValidationScreen.getWindow().getComponent("futureDatePicker")
        def futureOrPresentDatePicker = (DatePicker<Date>) dateValidationScreen.getWindow().getComponent("futureOrPresentDatePicker")

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        def rangeStart = dateTimeTransformations.transformFromZDT(zonedDateTime, futureDatePicker.getValueSource().getType())

        expect:
        futureDatePicker.getRangeStart().equals(rangeStart)
        futureOrPresentDatePicker.getRangeStart().equals(rangeStart)
        futureDatePicker.getRangeEnd() == null
        futureOrPresentDatePicker.getRangeEnd() == null
    }

    def "pastDatePicker and pasteOrPresentDatePicker range test"() {
        given:
        showMainWindow()

        def dateValidationScreen = screens.create(DateValidationScreen)
        dateValidationScreen.show()

        def pastDatePicker = (DatePicker<Date>) dateValidationScreen.getWindow().getComponent("pastDatePicker")
        def pastOrPresentDatePicker = (DatePicker<Date>) dateValidationScreen.getWindow().getComponent("pastOrPresentDatePicker")

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        def rangeEnd = dateTimeTransformations.transformFromZDT(zonedDateTime, pastDatePicker.getValueSource().getType())

        expect:
        pastDatePicker.getRangeStart() == null
        pastOrPresentDatePicker.getRangeStart() == null
        pastDatePicker.getRangeEnd().equals(rangeEnd)
        pastOrPresentDatePicker.getRangeEnd().equals(rangeEnd)
    }

    def "specificFutureDatePicker range test"() {
        given:
        showMainWindow()

        def dateValidationScreen = screens.create(DateValidationScreen)
        dateValidationScreen.show()

        def specificFutureDatePicker = (DatePicker<Date>) dateValidationScreen.getWindow().getComponent("specificFutureDatePicker")

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        dateTime = dateTime.plusDays(1)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        def rangeStart = dateTimeTransformations.transformFromZDT(zonedDateTime, specificFutureDatePicker.getValueSource().getType())

        expect:
        specificFutureDatePicker.getRangeStart().equals(rangeStart)
        specificFutureDatePicker.getRangeEnd() == null
    }
}
