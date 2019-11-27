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

package spec.cuba.web.components.calendar

import com.haulmont.chile.core.annotations.JavaClass
import com.haulmont.chile.core.datatypes.Datatype
import com.haulmont.chile.core.datatypes.impl.*
import com.haulmont.cuba.gui.components.Calendar
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.calendar.screens.CalendarScreen

@SuppressWarnings("GroovyAssignabilityCheck")
class CalendarTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.calendar.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def "Datatype is applied from the screen descriptor"(String id, Class<Datatype> datatypeClass) {
        showMainWindow()

        def calendarScreen = screens.create(CalendarScreen)
        calendarScreen.show()

        when:

        def calendar = calendarScreen.getWindow().getComponentNN(id) as Calendar

        then:

        noExceptionThrown()
        calendar.getDatatype().getClass() == datatypeClass
        calendar.getStartDate().getClass() == datatypeClass.getAnnotation(JavaClass).value()
        calendar.getEndDate().getClass() == datatypeClass.getAnnotation(JavaClass).value()

        where:

        id                       | datatypeClass
        "calendarDefault"        | DateTimeDatatype
        "calendarDate"           | DateDatatype
        "calendarDateTime"       | DateTimeDatatype
        "calendarLocalDate"      | LocalDateDatatype
        "calendarLocalDateTime"  | LocalDateTimeDatatype
        "calendarOffsetDateTime" | OffsetDateTimeDatatype
    }

    def "StartDateProperty and EndDateProperty values are propagated to Calendar from ValueSource"() {
        showMainWindow()

        def calendarScreen = screens.create(CalendarScreen)
        calendarScreen.show()

        def item = calendarScreen.tasksDc.getItems().get(0)
        def calendar = calendarScreen.getWindow().getComponentNN("calendarWithContainer") as Calendar

        when: 'StartDateProperty and EndDateProperty values are set to ValueSource'
        // StartDateProperty
        item.setStartDate(new Date())
        // EndDateProperty
        item.setLastStartTime(new Date())

        def events = calendar.getEventProvider().getEvents()

        then: 'Calendar contains events'
        !events.isEmpty()

        and: 'Calendar StartDateProperty is updated'
        item.getStartDate() == events.get(0).getStart()

        and: 'Calendar EndDateProperty is updated'
        item.getLastStartTime() == events.get(0).getEnd()
    }
}
