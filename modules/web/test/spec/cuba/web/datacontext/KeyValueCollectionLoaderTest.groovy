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
import com.haulmont.cuba.gui.model.CollectionContainer
import com.haulmont.cuba.gui.model.CollectionLoader
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader
import com.haulmont.cuba.web.testmodel.datacontext.Foo
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

import java.util.function.Consumer

import static com.haulmont.cuba.client.testsupport.TestSupport.reserialize

class KeyValueCollectionLoaderTest extends Specification {

    @Shared @ClassRule
    public TestContainer cont = TestContainer.Common.INSTANCE

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
