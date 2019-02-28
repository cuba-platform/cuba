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

package spec.cuba.gui.sys

import com.haulmont.cuba.gui.sys.TestIdManager
import spock.lang.Specification

class TestIdManagerTest extends Specification {

    TestIdManager testIdManager

    void setup() {
        testIdManager = new TestIdManager()
    }

    void cleanup() {
        testIdManager.reset()
    }

    def "test normalize method"() {
        expect:
            testIdManager.normalize(input) == output
        where:
            input                             | output
            "écomponentId_g"                  | "_componentId_g"
            ".component#Id."                  | "_component_Id_"
            "componentId1"                    | "componentId1"
            "long`Id[with@34.symbol_length&1" | "long_Id_with_34_symbol_length_1"
            "id"                              | "id_id"
    }

    def "test getTestId method"() {
        when:
            def id = 'cuba_frame_componentId'
        then: "first 'get' should return normalized value"
            String firstTestId = testIdManager.getTestId(id)
            id == firstTestId

        when:
            def idWithDivider = 'cuba_frame_componentId&1'
        then: "second 'get' should return with number"
            String secondTestId = testIdManager.getTestId(id)
            idWithDivider == secondTestId

        when: "check collision when iterated id the same as new one"
            def testId1 = "cuba_test_id"
            def testId2 = "cuba_test_id&1"
            def testId3 = "cuba_test_id_1"
        then:
            testIdManager.getTestId(testId1)
            def norm1 = testIdManager.getTestId(testId1) // here we get "cuba_test_id&1"
            def norm2 = testIdManager.getTestId(testId2) // we should get "cuba_test_id_1"
            norm1 != norm2

            // we should get "cuba_test_id_1&1" as testId2 will be normalized to "cuba_test_id_1"
            def norm3 = testIdManager.getTestId(testId3)
            norm3 == "cuba_test_id_1&1"
    }
}