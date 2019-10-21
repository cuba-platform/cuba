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

    UiControllerReflectionInspector inspector

    def setup() {

        inspector = new UiControllerReflectionInspector()

    }

    def "Get annotated @Inject elements"() {

        when:

        List<UiControllerReflectionInspector.InjectElement> elements = inspector.getAnnotatedInjectElements(ScreenWithInjection)

        then:

        elements.size() == 6
        isField(elements, 'label')
        isField(elements, 'nameField')
        isField(elements, 'scripting')
        isField(elements, 'resources')

        isMethod(elements, 'setMessages')
        isMethod(elements, 'setBeanLocator')

        then: "all elements are accessible"

        accessibleElementsEqualTo(elements, 6)

    }


    def "Get annotated @EventListener methods"() {

        when:

        List<Method> methods = inspector.getAnnotatedListenerMethods(ScreenWithEventListeners)

        then:

        methods.size() == 2
        isAvailable(methods, 'handleSomeEvent')
        isAvailable(methods, 'handlePrivateEvent')

        when:

        List<Method> childMethods = inspector.getAnnotatedListenerMethods(ScreenWithParentEventListeners)

        then: "Subclass can override event handlers without explicit @EventListener annotation"

        methodsAreAvailableAndAccessible(childMethods, ['handleSomeEvent', 'handlePrivateEvent', 'handleAdditionalEvent'])

    }

    def "Get annotated @Subscribe methods"() {

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithSubscribe).collect({ it.method })

        then:

        methodsAreAvailableAndAccessible(methods, ['onClick', 'onShow', 'onAfterShow', 'init'])

        when:

        def childMethods = inspector.getAnnotatedSubscribeMethods(ScreenWithParentSubscribe).collect({ it.method })

        then: "Subclass can override event handlers without explicit @Subscribe annotation"

        methodsAreAvailableAndAccessible(childMethods, ['onClick', 'onShow', 'onAfterShow', 'init', 'onClick2'])

    }

    def "Inspector sorts @Subscribe methods by @Order"() {

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

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithMixin).collect({ it.method })

        then:

        methodsAreAvailableAndAccessible(methods, ['onClick', 'onShow', 'onAfterShow', 'init', 'onInitMixin'])
    }

    def "Get annotated @Install methods"() {

        when:

        def methods = inspector.getAnnotatedInstallMethods(ScreenWithInstall).collect({ it.method })

        then:

        methodsAreAvailableAndAccessible(methods, ['format', 'runnableMethod', 'getCellStyleName', 'getData', 'consumeEvent'])
    }

    def "Get addListener methods"() {

        given:

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

        given:

        def screen = new ScreenWithSubscribe()
        def screen2 = new ScreenWithSubscribe()

        def button = Mock(Button)

        when:

        def methods = inspector.getAnnotatedSubscribeMethods(ScreenWithSubscribe)

        then:

        methods.size() == 4

        when:

        def lambdaFactory = inspector.getConsumerMethodFactory(ScreenWithSubscribe, methods[0], Button.ClickEvent)

        then:

        lambdaFactory != null

        when:

        def consumer = lambdaFactory.invokeWithArguments(screen)

        then:

        consumer instanceof Consumer

        when: "Lambda invoked"

        ((Consumer) consumer).accept(new Button.ClickEvent(button))

        then: "Listener calls method from screen"

        screen.buttonClicks == 1

        when: "Try to create second lambda for the same screen class"

        def consumer2 = lambdaFactory.invokeWithArguments(screen2)

        then: "Classes of lambdas are the same"

        consumer.class.is(consumer2.class)
    }

    def "Get property setters"() {

        when: "screen is inspected for setters"

        def setters = inspector.getPropertySetters(ScreenWithProperties)

        then: "public methods with single argument should be returned"

        methodsAreAvailable(setters, ['setIntProperty', 'setStringProperty', 'setTableProperty'])

        when: "subclass is inspected"

        def childSetters = inspector.getPropertySetters(ScreenWithParentProperties)

        then: "the same methods list should be returned"

        methodsAreAvailable(childSetters, ['setIntProperty', 'setStringProperty', 'setTableProperty'])

    }


    boolean methodsAreAvailableAndAccessible(List<Method> methods, List<String> methodNames) {

        methodNames.each {
            assert isAvailable(methods, it)
            assert isAccessible(methods, it)
        }

        assert methodNames.size() == methods.size()
        true
    }


    boolean methodsAreAvailable(List<Method> methods, List<String> methodNames) {

        methodNames.each {
            assert isAvailable(methods, it)
        }

        assert methodNames.size() == methods.size()
        true
    }



    private boolean isAvailable(List<Method> methods, String methodName) {
        methods.find({ it.name == methodName }) != null
    }

    private boolean isAccessible(List<Method> methods, String methodName) {
        methods.find({ it.name == methodName }).isAccessible()
    }

    boolean accessibleElementsEqualTo(List<UiControllerReflectionInspector.InjectElement> elements, int expectedAmount) {
        elements.findAll({ it.element.isAccessible() }).size() == expectedAmount
    }

    private boolean isMethod(List<UiControllerReflectionInspector.InjectElement> elements, elementName) {
        findElementByName(elements, elementName).element.class == Method
    }

    private UiControllerReflectionInspector.InjectElement findElementByName(List<UiControllerReflectionInspector.InjectElement> elements, elementName) {
        elements.find({ it.element.name == elementName })
    }

    private boolean isField(List<UiControllerReflectionInspector.InjectElement> elements, elementName) {
        findElementByName(elements, elementName).element.class == Field
    }
}