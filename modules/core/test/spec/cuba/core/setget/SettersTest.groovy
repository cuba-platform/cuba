/*
 * Copyright (c) 2008-2020 Haulmont.
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

package spec.cuba.core.setget


import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.testmodel.setget.SettersEntity
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class SettersTest extends Specification {

    @Shared
    @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private Metadata metadata = cont.metadata()

    private SettersEntity settersEntity

    void setup() {
        settersEntity = metadata.create(SettersEntity.class)
    }

    def "Overloaded setter"() {
        when:
        String stringValue = "stringValue"
        settersEntity.setValue("stringField", stringValue)
        String afterValue = settersEntity.getValue("stringField")

        then:
        stringValue == afterValue

        when:
        Double doubleValue = 10D
        settersEntity.setValue("stringField", doubleValue)
        afterValue = settersEntity.getValue("stringField")

        then:
        doubleValue.toString() == afterValue

        when:
        settersEntity.setValue("stringField", true)

        then:
        thrown(IllegalArgumentException)
    }

    def "Static setter"() {
        when:
        Boolean booleanValue = false
        SettersEntity.setStaticFlag(booleanValue)
        Boolean afterValue = SettersEntity.getStaticFlag()

        then:
        booleanValue == afterValue

        when:
        booleanValue = true
        settersEntity.setValue("staticFlag", booleanValue)

        then:
        thrown(IllegalArgumentException)
    }
}
