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

package spec.cuba.global.events

import com.haulmont.bali.events.EventHub
import spec.cuba.global.events.eventtypes.ButtonClickEvent
import spec.cuba.global.events.eventtypes.InitEvent
import spec.cuba.global.events.eventtypes.TextChangeEvent
import spock.lang.Specification

import java.util.function.Consumer

@SuppressWarnings("GroovyPointlessBoolean")
class EventHubTest extends Specification {

    def "add and remove listener"() {
        def eventHub = new EventHub()

        when:

        def subscription = eventHub.subscribe(TextChangeEvent, { TextChangeEvent event ->
            // do nothing
        })

        then:

        eventHub.hasSubscriptions(TextChangeEvent) == true

        when:

        subscription.remove()

        then:

        eventHub.hasSubscriptions(TextChangeEvent) == false
    }

    def "add and publish listener"() {
        def eventHub = new EventHub()
        def tListener = Mock(Consumer)
        def bListener = Mock(Consumer)

        when:

        eventHub.subscribe(TextChangeEvent, tListener)
        eventHub.subscribe(ButtonClickEvent, bListener)
        eventHub.publish(TextChangeEvent, new TextChangeEvent(eventHub))

        then:

        1 * tListener.accept(_)
        0 * bListener.accept(_)
    }

    def "unsubscribe"() {
        def eventHub = new EventHub()
        def listener = Mock(Consumer)

        when:

        def subscription = eventHub.subscribe(TextChangeEvent, listener)
        eventHub.publish(TextChangeEvent, new TextChangeEvent(eventHub))

        then:

        1 * listener.accept(_)

        when:

        subscription.remove()
        eventHub.publish(TextChangeEvent, new TextChangeEvent(eventHub))

        then:

        0 * listener.accept(_)
    }

    def "repeated subscription"() {
        def eventHub = new EventHub()
        def listener = Mock(Consumer)

        when:

        def subscription1 = eventHub.subscribe(TextChangeEvent, listener)
        def subscription2 = eventHub.subscribe(TextChangeEvent, listener)
        eventHub.publish(TextChangeEvent, new TextChangeEvent(eventHub))

        then:

        1 * listener.accept(_)

        when:

        subscription1.remove()
        eventHub.publish(TextChangeEvent, new TextChangeEvent(eventHub))
        subscription2.remove()
        eventHub.publish(TextChangeEvent, new TextChangeEvent(eventHub))

        then:

        0 * listener.accept(_)
    }

    def "remove event listener when empty"() {
        def eventHub = new EventHub()
        def listener = Mock(Consumer)

        when:

        eventHub.unsubscribe(TextChangeEvent, listener)

        then:

        eventHub.hasSubscriptions(TextChangeEvent) == false
    }

    def "trigger once event listeners removed after invocation"() {
        def eventHub = new EventHub()
        def listener = Mock(Consumer)

        when:

        eventHub.subscribe(InitEvent, listener)
        eventHub.publish(InitEvent, new InitEvent(this))

        then:

        1 * listener.accept(_)

        when:

        eventHub.publish(InitEvent.class, new InitEvent(this))

        then:

        0 * listener.accept(_)
    }

    def "listeners are called in order of addition"() {
        def eventHub = new EventHub()
        def order = 0

        when:
        eventHub.subscribe(TextChangeEvent, { TextChangeEvent e ->
            if (order != 0) {
                throw new RuntimeException("Incorrect order ${order}")
            }
            order += 1
        })
        eventHub.subscribe(TextChangeEvent, { TextChangeEvent e ->
            if (order != 1) {
                throw new RuntimeException("Incorrect order ${order}")
            }
            order += 1
        })
        eventHub.subscribe(TextChangeEvent, { TextChangeEvent e ->
            if (order != 2) {
                throw new RuntimeException("Incorrect order ${order}")
            }
            order += 1
        })
        eventHub.subscribe(TextChangeEvent, { TextChangeEvent e ->
            if (order != 3) {
                throw new RuntimeException("Incorrect order ${order}")
            }
            order += 1
        })
        eventHub.subscribe(TextChangeEvent, { TextChangeEvent e ->
            if (order != 4) {
                throw new RuntimeException("Incorrect order ${order}")
            }
            order += 1
        })
        eventHub.publish(TextChangeEvent, new TextChangeEvent(this))

        then:
        order == 5
    }
}