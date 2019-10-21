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

package spec.cuba.web.components.datepicker

import com.haulmont.chile.core.annotations.JavaClass
import com.haulmont.chile.core.datatypes.Datatype
import com.haulmont.chile.core.datatypes.impl.*
import com.haulmont.cuba.gui.components.DatePicker
import com.haulmont.cuba.gui.screen.OpenMode
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.datepicker.screens.DatePickerDatatypeScreen

class DatePickerDatatypeTest extends UiScreenSpec {

    @SuppressWarnings(["GroovyAssignabilityCheck", "GroovyAccessibility"])
    void setup() {
        exportScreensPackages(['spec.cuba.web.components.datepicker.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def "datatype is applied from the screen descriptor"(String id, Class<Datatype> datatypeClass) {
        showMainWindow()

        def datatypesScreen = screens.create(DatePickerDatatypeScreen)
        datatypesScreen.show()

        when:

        DatePicker datePicker = (DatePicker) datatypesScreen.getWindow().getComponentNN(id)

        then:

        datePicker.getDatatype().getClass() == datatypeClass
        datePicker.getValue().getClass() == datatypeClass.getAnnotation(JavaClass).value()

        where:

        id                     | datatypeClass
        "datePicker"           | DateDatatype
        "dateTimePicker"       | DateTimeDatatype
        "localDatePicker"      | LocalDateDatatype
        "localDateTimePicker"  | LocalDateTimeDatatype
        "offsetDateTimePicker" | OffsetDateTimeDatatype
    }
}