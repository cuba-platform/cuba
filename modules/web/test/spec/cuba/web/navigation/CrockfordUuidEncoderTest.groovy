/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package spec.cuba.web.navigation

import com.haulmont.cuba.web.sys.navigation.CrockfordUuidEncoder
import spock.lang.Specification

class CrockfordUuidEncoderTest extends Specification {

    def 'Positive case'() {
        def uuid = UUID.fromString('0ab2d259-de93-455c-9719-1583dc11a39d')

        when: 'uuid is serialized and deserialized'
        def encoded = CrockfordUuidEncoder.encode(uuid)
        def decoded = CrockfordUuidEncoder.decode(encoded)

        then: 'deserialized uuid should be equal to initial value'
        uuid == decoded
    }

    def 'Negative cases'() {
        def s = 'so-eRand0m$tring'

        when: 'null is passed for encoding'
        CrockfordUuidEncoder.encode(null)

        then: 'fail'
        thrown IllegalArgumentException

        when: 'null is passed for decoding'
        CrockfordUuidEncoder.decode(null)

        then: 'fail'
        thrown IllegalArgumentException

        when: 'bad string is passed for decoding'
        CrockfordUuidEncoder.decode(s)

        then: 'fail'
        thrown NumberFormatException
    }

    def 'Case does not matter for decoding'() {
        def uuid = UUID.fromString('79c08841-8063-4f85-86d0-25b3410a857c')
        def encoded = CrockfordUuidEncoder.encode(uuid)

        when: 'lower case or upper case chars are used'
        def decodedLc = CrockfordUuidEncoder.decode(encoded.toLowerCase())
        def decodedUc = CrockfordUuidEncoder.decode(encoded.toUpperCase())

        then: 'result should be the same'
        uuid == decodedLc && uuid == decodedUc
    }
}
