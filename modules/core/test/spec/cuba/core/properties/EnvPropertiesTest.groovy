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

package spec.cuba.core.properties

import com.haulmont.cuba.core.sys.AppContext
import com.haulmont.cuba.testsupport.TestContainer
import org.apache.commons.lang3.SystemUtils
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class EnvPropertiesTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    def "env variables can be used as app properties"() {
        expect:

        AppContext.getProperty('TEST_ENV_PROP_1') == 'envValue1'

        if (SystemUtils.IS_OS_WINDOWS) {
            AppContext.getProperty('UserProfile') == System.getenv('UserProfile')

            // interpolation in com/haulmont/cuba/test-app.properties
            AppContext.getProperty('test.userProfile') == System.getenv('UserProfile')

        } else {
            AppContext.getProperty('HOME') == System.getenv('HOME')

            // interpolation in com/haulmont/cuba/test-app.properties
            AppContext.getProperty('test.userHome') == System.getenv('HOME')
        }
    }

    def "system property overrides env property"() {
        expect:
        AppContext.getProperty('TEST_ENV_PROP_2') == 'systemValue2'
    }

    def "app property does not override env property"() {
        expect:
        AppContext.getProperty('TEST_ENV_PROP_3') == 'envValue3'
    }

    def "env property with capitalized name overrides app property"() {
        expect:
        AppContext.getProperty('test.someProp4') == 'envValue4'
    }

    def "env property with capitalized name doesn't override app property if cuba.disableUppercaseEnvironmentProperties=true"() {

        AppContext.setProperty('cuba.disableUppercaseEnvironmentProperties', 'true')

        expect:
        AppContext.getProperty('test.someProp4') == 'appValue4'

        cleanup:
        AppContext.setProperty('cuba.disableUppercaseEnvironmentProperties', null)
    }
}
