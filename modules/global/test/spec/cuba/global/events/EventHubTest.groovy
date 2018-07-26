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
import spec.cuba.global.events.eventtypes.TextChangeEvent
import spock.lang.Specification

@SuppressWarnings("GroovyPointlessBoolean")
class EventHubTest extends Specification {

    def "add and remove listener"() {
        def eventHub = new EventHub()

        when:
        def subscription = eventHub.subscribe(TextChangeEvent.class, { TextChangeEvent event ->
            // do nothing
        })

        then:
        eventHub.hasSubscriptions(TextChangeEvent.class) == true

        when:
        subscription.remove()

        then:
        eventHub.hasSubscriptions(TextChangeEvent.class) == false
    }

    def "add and publish listener"() {
        def eventHub = new EventHub()

        when:
        def tCount = 0
        def bCount = 0

        eventHub.subscribe(TextChangeEvent.class, { TextChangeEvent event ->
            tCount++
        })
        eventHub.subscribe(ButtonClickEvent.class, { ButtonClickEvent event ->
            bCount++
        })
        eventHub.publish(TextChangeEvent.class, new TextChangeEvent(eventHub))

        then:
        tCount == 1
        bCount == 0
    }

    def "unsubscribe"() {
        def eventHub = new EventHub()

        when:
        def count = 0
        def subscription = eventHub.subscribe(TextChangeEvent.class, { TextChangeEvent event ->
            count++
        })
        eventHub.publish(TextChangeEvent.class, new TextChangeEvent(eventHub))

        then:
        count == 1

        when:
        subscription.remove()
        eventHub.publish(TextChangeEvent.class, new TextChangeEvent(eventHub))

        then:
        count == 1
    }

    def "repeated subscription"() {
        def eventHub = new EventHub()

        when:
        def count = 0
        def listener = { TextChangeEvent event ->
            count++
        }

        def subscription1 = eventHub.subscribe(TextChangeEvent.class, listener)
        def subscription2 = eventHub.subscribe(TextChangeEvent.class, listener)
        eventHub.publish(TextChangeEvent.class, new TextChangeEvent(eventHub))

        then:
        count == 1

        when:
        subscription1.remove()
        eventHub.publish(TextChangeEvent.class, new TextChangeEvent(eventHub))
        subscription2.remove()
        eventHub.publish(TextChangeEvent.class, new TextChangeEvent(eventHub))

        then:
        count == 1
    }

    def "remove event listener when empty"() {
        def eventHub = new EventHub()

        when:
        def listener = { TextChangeEvent event ->
            count++
        }
        eventHub.unsubscribe(TextChangeEvent.class, listener)

        then:
        eventHub.hasSubscriptions(TextChangeEvent.class) == false
    }
}