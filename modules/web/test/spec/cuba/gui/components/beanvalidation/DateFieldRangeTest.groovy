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
import com.haulmont.cuba.gui.components.DateField
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import spec.cuba.gui.components.beanvalidation.screens.DateValidationScreen
import spec.cuba.web.UiScreenSpec

import java.time.*

class DateFieldRangeTest extends UiScreenSpec {

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

    def "futureDateField and futureOrPresentDateField range test"() {

        given:
        showMainWindow()

        def dateValidationScreen = screens.create(DateValidationScreen)
        dateValidationScreen.show()

        def futureDateField = (DateField<Date>) dateValidationScreen.getWindow().getComponent("futureDateField")
        def futureOrPresentDateField = (DateField<Date>) dateValidationScreen.getWindow().getComponent("futureOrPresentDateField")

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        def rangeStart = dateTimeTransformations.transformFromZDT(zonedDateTime, futureDateField.getValueSource().getType())

        expect:
        futureDateField.getRangeStart().equals(rangeStart)
        futureOrPresentDateField.getRangeStart().equals(rangeStart)
        futureDateField.getRangeEnd() == null
        futureOrPresentDateField.getRangeEnd() == null
    }

    def "pastDateField and pastOrPresentDateField range test"() {
        given:
        showMainWindow()

        def dateValidationScreen = screens.create(DateValidationScreen)
        dateValidationScreen.show()

        def pastDateField = (DateField<Date>) dateValidationScreen.getWindow().getComponent("pastDateField")
        def pastOrPresentDateField = (DateField<Date>) dateValidationScreen.getWindow().getComponent("pastOrPresentDateField")

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        def rangeEnd = dateTimeTransformations.transformFromZDT(zonedDateTime, pastDateField.getValueSource().getType())

        expect:
        pastDateField.getRangeStart() == null
        pastOrPresentDateField.getRangeStart() == null
        pastDateField.getRangeEnd().equals(rangeEnd)
        pastOrPresentDateField.getRangeEnd().equals(rangeEnd)
    }

    def "specificFutureDateField range test"() {
        given:
        showMainWindow()

        def dateValidationScreen = screens.create(DateValidationScreen)
        dateValidationScreen.show()

        def specificFutureDateField = (DateField<Date>) dateValidationScreen.getWindow().getComponent("specificFutureDateField")

        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
        dateTime = dateTime.plusDays(1)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        def rangeStart = dateTimeTransformations.transformFromZDT(zonedDateTime, specificFutureDateField.getValueSource().getType())

        expect:
        specificFutureDateField.getRangeStart().equals(rangeStart)
        specificFutureDateField.getRangeEnd() == null
    }

}
