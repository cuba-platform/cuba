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


import com.haulmont.cuba.core.sys.SpringProfileSpecificNameResolver
import spock.lang.Specification

class SpringProfileSpecificNameResolverTest extends Specification {

    def "no profiles"() {
        def resolver = new SpringProfileSpecificNameResolver((String) null)

        expect:
        resolver.getDerivedNames('app.properties') == ['app.properties']
    }

    def "one profile"() {
        def resolver = new SpringProfileSpecificNameResolver("dev")

        expect:
        resolver.getDerivedNames('app.properties') == ['app.properties','dev-app.properties']

        resolver.getDerivedNames('classpath:app.properties') ==
                ['classpath:app.properties','classpath:dev-app.properties']

        resolver.getDerivedNames('classpath:com/company/demo/app.properties') ==
                ['classpath:com/company/demo/app.properties','classpath:com/company/demo/dev-app.properties']

        resolver.getDerivedNames('file:\\conf\\app-core\\local.app.properties') ==
                ['file:\\conf\\app-core\\local.app.properties','file:/conf/app-core/dev-local.app.properties']
    }

    def "multiple profiles"() {
        def resolver = new SpringProfileSpecificNameResolver("dev,foo")

        expect:
        resolver.getDerivedNames('app.properties') == ['app.properties','dev-app.properties','foo-app.properties']

        resolver.getDerivedNames('classpath:app.properties') ==
                ['classpath:app.properties','classpath:dev-app.properties','classpath:foo-app.properties']

        resolver.getDerivedNames('classpath:com/company/demo/app.properties') ==
                ['classpath:com/company/demo/app.properties','classpath:com/company/demo/dev-app.properties','classpath:com/company/demo/foo-app.properties']

        resolver.getDerivedNames('file:\\conf\\app-core\\local.app.properties') ==
                ['file:\\conf\\app-core\\local.app.properties','file:/conf/app-core/dev-local.app.properties','file:/conf/app-core/foo-local.app.properties']
    }
}
