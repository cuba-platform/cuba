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

package spec.cuba.core.extended_entities

import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaClass
import com.haulmont.cuba.core.app.keyvalue.KeyValueMetaProperty
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.ExtendedEntities
import com.haulmont.cuba.testsupport.TestContainer
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class ExtendedEntitiesTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

    private ExtendedEntities extendedEntities

    void setup() {
        extendedEntities = AppBeans.get(ExtendedEntities)
    }

    def "KeyValueEntity cannot be extended so always return the same meta-class"() {

        def metaClass = new KeyValueMetaClass()
        metaClass.addProperty(new KeyValueMetaProperty(metaClass, 'foo', String.class))
        metaClass.addProperty(new KeyValueMetaProperty(metaClass, 'bar', String.class))

        expect:

        extendedEntities.getEffectiveMetaClass(metaClass) == metaClass
        extendedEntities.getOriginalOrThisMetaClass(metaClass) == metaClass
        extendedEntities.getOriginalMetaClass(metaClass) == null
    }
}
