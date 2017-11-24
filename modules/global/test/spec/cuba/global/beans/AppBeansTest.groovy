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

package spec.cuba.global.beans

import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Messages
import spock.lang.Specification

class AppBeansTest extends Specification {

    def "error message if context is not initialized"() {
        when:
        AppBeans.get(Messages.NAME)
        then:
        thrown(IllegalStateException)

        when:
        AppBeans.get(Messages.class)
        then:
        thrown(IllegalStateException)

        when:
        AppBeans.get(Messages.NAME, Messages.class)
        then:
        thrown(IllegalStateException)

        when:
        AppBeans.getPrototype(Messages.NAME)
        then:
        thrown(IllegalStateException)

        when:
        AppBeans.getAll(Messages.class)
        then:
        thrown(IllegalStateException)

        when:
        AppBeans.containsBean(Messages.NAME)
        then:
        thrown(IllegalStateException)
    }
}
