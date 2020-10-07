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

package spec.cuba.gui.components

import com.haulmont.bali.datastruct.Pair
import com.haulmont.cuba.web.gui.components.WebDateField
import spock.lang.Specification

class DateFieldDateTimePatternTest extends Specification {
    def "Unsupported field DayOfWeek in a search filter#3019"() {
        expect:
        new WebDateField().findTimePosition(pattern) == result

        where:
        pattern << ["yyyy-MM-dd HH:mm EE", "HH:mm EE yyyy-MM-dd", "yyyy-MM-dd ssmmHH EE", "yyyy-MM-dd HHmmss", "ss HH yyyy-MM-dd"]
        result << [new Pair<>(11, 15), new Pair<>(0, 4), new Pair<>(11, 16), new Pair<>(11, 16), new Pair<>(0, 4)]

    }
}
