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

package spec.cuba.web.screens

import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector
import spec.cuba.web.screens.samples.ScreenWithEventListeners
import spec.cuba.web.screens.samples.ScreenWithInjection
import spec.cuba.web.screens.samples.ScreenWithParentEventListeners
import spec.cuba.web.screens.samples.ScreenWithParentSubscribe
import spec.cuba.web.screens.samples.ScreenWithSubscribe
import spock.lang.Specification

import java.lang.reflect.Field
import java.lang.reflect.Method

class UiControllerReflectionInspectorTest extends Specification {
    def "Get annotated @Inject elements"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def elements = inspector.getAnnotatedInjectElements(ScreenWithInjection)

        then:

        elements.size() == 5

        elements.find({ it.element.name == 'label' }).element.class == Field
        elements.find({ it.element.name == 'nameField' }).element.class == Field
        elements.find({ it.element.name == 'scripting' }).element.class == Field

        elements.find({ it.element.name == 'setMessages' }).element.class == Method
        elements.find({ it.element.name == 'setBeanLocator' }).element.class == Method

        then: "@Autowired is not supported by screen injector"

        elements.find({ it.element.name == 'resources' }) == null

        then: "all elements are accessible"

        elements.findAll({ it.element.isAccessible() }).size() == 5
    }

    def "Get annotated @EventListener methods"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedListenerMethods(ScreenWithEventListeners)

        then:

        methods.size() == 2
        methods.find({ it.name == 'handleSomeEvent'}) != null
        methods.find({ it.name == 'handlePrivateEvent'}) != null

        when:

        def childMethods = inspector.getAnnotatedListenerMethods(ScreenWithParentEventListeners)

        then: "Subclass can override event handlers without explicit @EventListener annotation"

        childMethods.size() == 3
        childMethods.find({ it.name == 'handleSomeEvent'}) != null
        childMethods.find({ it.name == 'handlePrivateEvent'}) != null
        childMethods.find({ it.name == 'handleAdditionalEvent'}) != null

        then: "all methods are accessible"

        childMethods.findAll({ it.isAccessible() }).size() == 3
    }

    def "Get annotated @Subscribe methods"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithSubscribe)

        then:

        methods.size() == 4
        methods.find({ it.name == 'onClick'}) != null
        methods.find({ it.name == 'onShow'}) != null
        methods.find({ it.name == 'onAfterShow'}) != null
        methods.find({ it.name == 'init'}) != null

        when:

        def childMethods = inspector.getAnnotatedSubscribeMethods(ScreenWithParentSubscribe)

        then: "Subclass can override event handlers without explicit @Subscribe annotation"

        childMethods.size() == 5
        childMethods.find({ it.name == 'onClick'}) != null
        childMethods.find({ it.name == 'onShow'}) != null
        childMethods.find({ it.name == 'onAfterShow'}) != null
        childMethods.find({ it.name == 'init'}) != null

        childMethods.find({ it.name == 'onClick2'}) != null

        then: "all methods are accessible"

        childMethods.findAll({ it.isAccessible() }).size() == 5
    }
}