/*
 * Copyright (c) 2008-2018 Haulmont.
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

package spec.cuba.global.localization

import com.haulmont.cuba.core.sys.AvailableLocalesFactory
import spock.lang.Specification

class AvailableLocalesFactoryTest extends Specification {

    def "locales are in correct order"() {
        def factory = new AvailableLocalesFactory()

        when:

        def object = factory.build("English|en;Russian|ru")

        then:

        object instanceof LinkedHashMap
        object['English'] == Locale.forLanguageTag('en')
        object['Russian'] == Locale.forLanguageTag('ru')
    }
}
