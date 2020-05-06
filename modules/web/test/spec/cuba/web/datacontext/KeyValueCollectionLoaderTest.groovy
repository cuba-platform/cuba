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

package spec.cuba.web.datacontext

import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.entity.KeyValueEntity
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.ValueLoadContext
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader
import com.haulmont.cuba.gui.model.KeyValueContainer
import com.haulmont.cuba.gui.model.KeyValueInstanceLoader
import com.haulmont.cuba.web.container.CubaTestContainer
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer
import java.util.function.Function

@SuppressWarnings("GroovyAssignabilityCheck")
class KeyValueCollectionLoaderTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = CubaTestContainer.Common.INSTANCE

    private Metadata metadata
    private DataManager dataManager
    private DataComponents factory

    void setup() {
        metadata = cont.getBean(Metadata)
        dataManager = cont.getBean(DataManager)
        factory = cont.getBean(DataComponents)
    }

    void cleanup() {
        TestServiceProxy.clear()
    }

    def "successful load"() {
        KeyValueCollectionLoader loader = factory.createKeyValueCollectionLoader()
        KeyValueCollectionContainer container = factory.createKeyValueCollectionContainer()

        Consumer preLoadListener = Mock()
        loader.addPreLoadListener(preLoadListener)

        Consumer postLoadListener = Mock()
        loader.addPostLoadListener(postLoadListener)

        def kv = new KeyValueEntity()

        TestServiceProxy.mock(DataService, Mock(DataService) {
            loadValues(_) >> [kv]
        })

        when:

        loader.setContainer(container)
        loader.setQuery('select bla-bla')
        loader.load()

        then:

        container.getItems() == [kv]

        1 * preLoadListener.accept(_)
        1 * postLoadListener.accept(_)
    }

    def "fail if query is null and loader is null"() {
        KeyValueCollectionLoader loader = factory.createKeyValueCollectionLoader()
        KeyValueCollectionContainer container = factory.createKeyValueCollectionContainer()

        when:
        loader.setContainer(container)
        loader.load()

        then:
        IllegalStateException exception = thrown()
    }

    def "proceed if query is null and loader is not null"() {
        KeyValueCollectionLoader loader = factory.createKeyValueCollectionLoader()
        KeyValueCollectionContainer container = factory.createKeyValueCollectionContainer()

        def kv = new KeyValueEntity()

        when:

        loader.setContainer(container)
        loader.setLoadDelegate(new Function<ValueLoadContext, List<KeyValueEntity>>() {
            @Override
            List<KeyValueEntity> apply(ValueLoadContext valueLoadContext) {
                return [kv].asList()
            }
        })
        loader.load()

        then:

        container.getItems().size() == 1
        container.getItems().contains(kv)
    }

    def "prevent load by PreLoadEvent"() {
        KeyValueCollectionLoader loader = factory.createKeyValueCollectionLoader()
        KeyValueCollectionContainer container = factory.createKeyValueCollectionContainer()

        Consumer preLoadListener = { KeyValueCollectionLoader.PreLoadEvent e -> e.preventLoad() }
        loader.addPreLoadListener(preLoadListener)

        Consumer postLoadListener = Mock()
        loader.addPostLoadListener(postLoadListener)

        when:

        loader.setContainer(container)
        loader.setQuery('select bla-bla')
        loader.load()

        then:

        container.getItems() == []

        0 * postLoadListener.accept(_)
    }
}
