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

package spec.cuba.web.navigation

import com.haulmont.cuba.gui.navigation.NavigationState
import com.haulmont.cuba.web.sys.navigation.UrlTools
import spock.lang.Specification

class UrlToolsTest extends Specification {

    def 'Empty state is returned in case of failure parsing'() {
        when: 'Null fragment is passed'
        def state = UrlTools.parseState(null)

        then:
        NavigationState.EMPTY == state

        when: 'Empty fragment is passed'
        state = UrlTools.parseState('')

        then:
        NavigationState.EMPTY == state

        when: 'Bad format fragment is passed'
        state = UrlTools.parseState('#!root/42?qwerty=asd')

        then:
        NavigationState.EMPTY == state
    }

    def 'Root route may contain letters, digits and dashes'() {
        def lettersRoute = 'root'
        def lettersAndDigitsRoute = 'root42'
        def lettersAndDashesRoute = 'dashed-route'

        when: 'Letters are used in root route'
        def state = UrlTools.parseState(lettersRoute)

        then: 'State is correctly parsed'
        lettersRoute == state.getRoot()

        when: 'Letters and digits are used in root route'
        state = UrlTools.parseState(lettersAndDigitsRoute)

        then: 'State is correctly parsed'
        lettersAndDigitsRoute == state.getRoot()

        when: 'Letters and dashed are used in root route'
        state = UrlTools.parseState(lettersAndDashesRoute)

        then: 'State is correctly parsed'
        lettersAndDashesRoute == state.getRoot()
    }

    def 'Nested screen route considering URL state mark is parsed'() {
        def nestedScreenRoute = 'main/users'
        def twoNestedScreensRoute = 'main/users/roles'

        def nestedScreenStateMarkRoute = 'main/0/users'
        def twoNestedScreensRouteStateMarkRoute = 'main/0/users/roles'

        when: 'Simple nested screen route is passed'
        def state = UrlTools.parseState(nestedScreenRoute)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getNestedRoute() == 'users'

        when: 'Two nested screens route is passed'
        state = UrlTools.parseState(twoNestedScreensRoute)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getNestedRoute() == 'users/roles'

        when: 'Nested screen route with state mark is passed'
        state = UrlTools.parseState(nestedScreenStateMarkRoute)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getStateMark() == '0'
        state.getNestedRoute() == 'users'

        when: 'Two nested screens with state mark route is passed'
        state = UrlTools.parseState(twoNestedScreensRouteStateMarkRoute)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getStateMark() == '0'
        state.getNestedRoute() == 'users/roles'
    }

    def 'Params route considering URL state mark is parsed'() {
        def params = 'p1=v1&p2=v2&p3=v3'

        def rootRouteWithParams = "login?redirectTo=users&${params}"
        def nestedRouteWithParams = "main/users?${params}"
        def twoNestedRoutesWithParams = "main/users/roles?${params}"

        def nestedRouteStateMarkParams = "main/0/users?${params}"
        def twoNestedRoutesStateMarkParams = "main/0/users/roles?${params}"

        when: 'Root route with params is passed'
        def state = UrlTools.parseState(rootRouteWithParams)

        then: 'State is correctly parsed'
        state.getRoot() == 'login'
        state.getParamsString() == "redirectTo=users&${params}"

        when: 'Nested route with params is passed'
        state = UrlTools.parseState(nestedRouteWithParams)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getNestedRoute() == 'users'
        state.getParamsString() == params

        when: 'Two nested routes with params are passed'
        state = UrlTools.parseState(twoNestedRoutesWithParams)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getNestedRoute() == 'users/roles'
        state.getParamsString() == params

        when: 'Nested route with state mark and params are passed'
        state = UrlTools.parseState(nestedRouteStateMarkParams)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getStateMark() == '0'
        state.getNestedRoute() == 'users'
        state.getParamsString() == params

        when: 'Two nested routes with state mark and params are passed'
        state = UrlTools.parseState(twoNestedRoutesStateMarkParams)

        then: 'State is correctly parsed'
        state.getRoot() == 'main'
        state.getStateMark() == '0'
        state.getNestedRoute() == 'users/roles'
        state.getParamsString() == params
    }
}
