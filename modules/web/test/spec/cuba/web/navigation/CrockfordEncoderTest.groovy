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

import com.haulmont.cuba.web.sys.navigation.CrockfordEncoder
import spock.lang.Specification

import java.util.regex.Pattern

class CrockfordEncoderTest extends Specification {

    protected static final Pattern ACCEPTABLE_CHARS_REGEX = Pattern.compile('^[a-zA-Z0-9]*$')

    def "Random UUID serialization"() {
        def uuid = UUID.randomUUID()
        def noHyphensUuid = uuid.toString()
                .replaceAll("-", "")

        def bi = new BigInteger(noHyphensUuid, 16)

        when: "uuid is serialized and deserialized"
        def encoded = CrockfordEncoder.encode(bi)
        def decoded = CrockfordEncoder.decode(encoded)
                .toString(16)

        then: "deserialized uuid should be equal to initial value"
        noHyphensUuid == decoded
    }

    def "Only acceptable characters are used"() {
        def uuid = UUID.randomUUID()
        def noHyphensUuid = uuid.toString()
                .replaceAll("-", "")

        def bi = new BigInteger(noHyphensUuid, 16)

        when: "we encode correct value"
        def encoded = CrockfordEncoder.encode(bi)

        then: "only acceptable characters should be used"
        ACCEPTABLE_CHARS_REGEX.matcher(encoded)
                .matches()
    }

    def "Case does not matter"() {
        def uuid = UUID.randomUUID()
        def noHyphensUuid = uuid.toString()
                .replaceAll("-", "")

        def biLc = new BigInteger(noHyphensUuid.toLowerCase(), 16)
        def biUc = new BigInteger(noHyphensUuid.toUpperCase(), 16)

        when: "lower case or upper case chars are used"
        def encodedLc = CrockfordEncoder.encode(biLc)
        def decodedLc = CrockfordEncoder.decode(encodedLc)
                .toString(16)

        def encodedUc = CrockfordEncoder.encode(biUc)
        def decodedUc = CrockfordEncoder.decode(encodedUc)
                .toString(16)

        then: "result should be the same"
        noHyphensUuid == decodedLc && noHyphensUuid == decodedUc
    }

    def "Excluded characters are not accepted"() {
        def s = 'so-eRand0m$tring'


        when: "bad string is passed for decoding"
        CrockfordEncoder.decode(s)

        then: "fail"
        thrown NumberFormatException
    }
}
