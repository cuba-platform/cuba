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

package spec.cuba.web.components.slider

import com.haulmont.chile.core.annotations.JavaClass
import com.haulmont.chile.core.datatypes.Datatype
import com.haulmont.chile.core.datatypes.impl.BigDecimalDatatype
import com.haulmont.chile.core.datatypes.impl.DoubleDatatype
import com.haulmont.chile.core.datatypes.impl.IntegerDatatype
import com.haulmont.chile.core.datatypes.impl.LongDatatype
import com.haulmont.cuba.core.entity.ScheduledTask
import com.haulmont.cuba.gui.components.Slider
import com.haulmont.cuba.gui.screen.OpenMode
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.components.slider.screens.SliderScreen

@SuppressWarnings("GroovyAssignabilityCheck")
class SliderTest extends UiScreenSpec {

    void setup() {
        exportScreensPackages(['spec.cuba.web.components.slider.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def "Datatype is applied from the screen descriptor"(String id, Class<Datatype> datatypeClass) {
        showMainWindow()

        def sliderScreen = screens.create(SliderScreen)
        sliderScreen.show()

        when:

        def slider = (Slider) sliderScreen.getWindow().getComponentNN(id)

        then:

        slider.getDatatype().getClass() == datatypeClass
        slider.getValue().getClass() == datatypeClass.getAnnotation(JavaClass).value()

        where:

        id              | datatypeClass
        "sliderDefault" | DoubleDatatype
        "sliderDouble"  | DoubleDatatype
        "sliderInt"     | IntegerDatatype
        "sliderDecimal" | BigDecimalDatatype
        "sliderLong"    | LongDatatype
    }

    def "Value is propagated to ValueSource from Slider"() {
        showMainWindow()

        def sliderScreen = screens.create(SliderScreen)
        sliderScreen.show()

        def item = (ScheduledTask) sliderScreen.taskDc.getItem()
        def slider = (Slider) sliderScreen.getWindow().getComponentNN("sliderWithContainer")

        when: 'Value is set to Slider'
        slider.setValue(10)

        then: 'ValueSource is updated'
        item.getPeriod() == slider.getValue()
    }

    def "Value is propagated to Slider from ValueSource"() {
        showMainWindow()

        def sliderScreen = screens.create(SliderScreen)
        sliderScreen.show()

        def item = (ScheduledTask) sliderScreen.taskDc.getItem()
        def slider = (Slider) sliderScreen.getWindow().getComponentNN("sliderWithContainer")

        when: 'Value is set to ValueSource'
        item.setPeriod(10)

        then: 'Slider is updated'
        item.getPeriod() == slider.getValue()
    }
}
