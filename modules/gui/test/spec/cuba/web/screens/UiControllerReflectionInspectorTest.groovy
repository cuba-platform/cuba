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

import com.haulmont.cuba.gui.components.Button
import com.haulmont.cuba.gui.components.TextInputField
import com.haulmont.cuba.gui.sys.UiControllerReflectionInspector
import spec.cuba.web.screens.inspection.*
import spock.lang.Specification

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.function.Consumer

class UiControllerReflectionInspectorTest extends Specification {
    def "Get annotated @Inject elements"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def elements = inspector.getAnnotatedInjectElements(ScreenWithInjection)

        then:

        elements.size() == 6

        elements.find({ it.element.name == 'label' }).element.class == Field
        elements.find({ it.element.name == 'nameField' }).element.class == Field
        elements.find({ it.element.name == 'scripting' }).element.class == Field
        elements.find({ it.element.name == 'resources' }).element.class == Field

        elements.find({ it.element.name == 'setMessages' }).element.class == Method
        elements.find({ it.element.name == 'setBeanLocator' }).element.class == Method

        then: "all elements are accessible"

        elements.findAll({ it.element.isAccessible() }).size() == 6
    }

    def "Get annotated @EventListener methods"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedListenerMethods(ScreenWithEventListeners)

        then:

        methods.size() == 2
        methods.find({ it.name == 'handleSomeEvent' }) != null
        methods.find({ it.name == 'handlePrivateEvent' }) != null

        when:

        def childMethods = inspector.getAnnotatedListenerMethods(ScreenWithParentEventListeners)

        then: "Subclass can override event handlers without explicit @EventListener annotation"

        childMethods.size() == 3
        childMethods.find({ it.name == 'handleSomeEvent' }) != null
        childMethods.find({ it.name == 'handlePrivateEvent' }) != null
        childMethods.find({ it.name == 'handleAdditionalEvent' }) != null

        then: "all methods are accessible"

        childMethods.findAll({ it.isAccessible() }).size() == 3
    }

    def "Get annotated @Subscribe methods"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithSubscribe).collect({ it.method })

        then:

        methods.size() == 4
        methods.find({ it.name == 'onClick' }) != null
        methods.find({ it.name == 'onShow' }) != null
        methods.find({ it.name == 'onAfterShow' }) != null
        methods.find({ it.name == 'init' }) != null

        when:

        def childMethods = inspector.getAnnotatedSubscribeMethods(ScreenWithParentSubscribe).collect({ it.method })

        then: "Subclass can override event handlers without explicit @Subscribe annotation"

        childMethods.size() == 5
        childMethods.find({ it.name == 'onClick' }) != null
        childMethods.find({ it.name == 'onShow' }) != null
        childMethods.find({ it.name == 'onAfterShow' }) != null
        childMethods.find({ it.name == 'init' }) != null

        childMethods.find({ it.name == 'onClick2' }) != null

        then: "all methods are accessible"

        childMethods.findAll({ it.isAccessible() }).size() == 5
    }

    def "Inspector sorts @Subscribe methods by @Order"() {

        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithOrderedSubscribe).collect({ it.method })

        then:

        methods.size() == 6
        methods[0].name == 'onClick1'
        methods[1].name == 'onClick2'
        methods[2].name == 'onClick3'
        methods[3].name == 'onClick4'
        methods[4].name == 'onAfterInit'
        methods[5].name == 'onInit'
    }

    def "Inspector sorts @Subscribe methods by @Order with parent"() {

        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithParentOrdered).collect({ it.method })

        then:

        methods.size() == 9
        methods[0].name == 'onClick6'
        methods[1].name == 'onClick1'
        methods[2].name == 'onClick2'
        methods[3].name == 'onClick7'
        methods[4].name == 'onClick3'
        methods[5].name == 'onClick4'
        methods[6].name == 'onAfterInit' // parent declared method first
        methods[7].name == 'childAfterInit' // child declared method after
        methods[8].name == 'onInit'
    }

    def "Inspector finds @Subscribe default methods in mixin interfaces"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithMixin).collect({ it.method })

        then:

        methods.size() == 5
        methods.find({ it.name == 'onClick' }) != null
        methods.find({ it.name == 'onShow' }) != null
        methods.find({ it.name == 'onAfterShow' }) != null
        methods.find({ it.name == 'init' }) != null
        methods.find({ it.name == 'onInitMixin' }) != null
    }

    def "Get annotated @Install methods"() {
        def inspector = new UiControllerReflectionInspector()

        when:

        def methods = inspector.getAnnotatedInstallMethods(ScreenWithInstall).collect({ it.method })

        then:

        methods.size() == 4
        methods.find({ it.name == 'format' }) != null
        methods.find({ it.name == 'getCellStyleName' }) != null
        methods.find({ it.name == 'getData' }) != null
        methods.find({ it.name == 'consumeEvent' }) != null
        methods.find({ it.name == 'ignoredMethod' }) == null
    }

    def "Get addListener methods"() {
        def inspector = new UiControllerReflectionInspector()
        def textField = new TestTextField()
        def listener = Mock(Consumer)

        when:

        def methodHandle = inspector.getAddListenerMethod(TestTextField, TextInputField.TextChangeEvent)

        then:

        methodHandle != null

        when:

        methodHandle.invokeWithArguments(textField, listener)
        methodHandle.invokeWithArguments(textField, listener)

        then:

        textField.listener == listener
    }

    def "Get lambda factory for @Subscribe method"() {
        def screen = new ScreenWithSubscribe()
        def screen2 = new ScreenWithSubscribe()

        def inspector = new UiControllerReflectionInspector()
        def button = Mock(Button)

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithSubscribe)

        then:

        methods.size() == 4

        when:

        def lambdaFactory = inspector.getConsumerMethodFactory(ScreenWithSubscribe, methods[0].methodHandle, Button.ClickEvent)

        then:

        lambdaFactory != null

        when:

        def consumer = lambdaFactory.invokeWithArguments(screen)

        then:

        consumer instanceof Consumer

        when: "Lambda invoked"

        ((Consumer)consumer).accept(new Button.ClickEvent(button))

        then: "Listener calls method from screen"

        screen.buttonClicks == 1

        when: "Try to create second lambda for the same screen class"

        def consumer2 = lambdaFactory.invokeWithArguments(screen2)

        then: "Classes of lambdas are the same"

        consumer.class.is(consumer2.class)
    }

    def "Get property setters"() {
        def inspector = new UiControllerReflectionInspector()

        when: "screen is inspected for setters"

        def setters = inspector.getPropertySetters(ScreenWithProperties)

        then: "public methods with single argument should be returned"

        setters.size() == 3

        setters.find({ it.name == 'setIntProperty' }) != null
        setters.find({ it.name == 'setStringProperty' }) != null
        setters.find({ it.name == 'setTableProperty' }) != null

        when: "subclass is inspected"

        def childSetters = inspector.getPropertySetters(ScreenWithParentProperties)

        then: "the same methods list should be returned"

        childSetters.size() == 3

        childSetters.find({ it.name == 'setIntProperty' }) != null
        childSetters.find({ it.name == 'setStringProperty' }) != null
        childSetters.find({ it.name == 'setTableProperty' }) != null
    }
}