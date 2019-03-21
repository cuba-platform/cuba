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

package spec.cuba.web

import com.google.common.collect.HashMultimap
import com.haulmont.cuba.client.ClientUserSession
import com.haulmont.cuba.client.sys.cache.CachingStrategy
import com.haulmont.cuba.client.sys.cache.ClientCacheManager
import com.haulmont.cuba.client.sys.cache.DynamicAttributesCacheStrategy
import com.haulmont.cuba.client.testsupport.TestUserSessionSource
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCache
import com.haulmont.cuba.core.global.UserSessionSource
import com.haulmont.cuba.gui.config.WindowConfig
import com.haulmont.cuba.gui.sys.UiControllersConfiguration
import org.springframework.core.type.classreading.MetadataReaderFactory

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReadWriteLock

class UiScreenSpec extends WebSpec {

    ClientUserSession session

    void setup() {
        def userSessionSource = (TestUserSessionSource) cont.getBean(UserSessionSource.class)
        def testSession = new ClientUserSession(userSessionSource.createTestSession())

        this.session = Spy(testSession)
        this.sessionSource.getUserSession() >> session

        userSessionSource.setSession(session)

        session.isAuthenticated() >> true

        def clientCacheManager = cont.getBean(ClientCacheManager)
        def dynamicAttributesCacheStrategy = Mock(CachingStrategy)
        def dynamicAttributesCache =
                new DynamicAttributesCache(HashMultimap.create(), [:], new Date())
        def cacheLock = Mock(ReadWriteLock)
        cacheLock.writeLock() >> Mock(Lock)
        cacheLock.readLock() >> Mock(Lock)

        dynamicAttributesCacheStrategy.getObject() >> dynamicAttributesCache
        dynamicAttributesCacheStrategy.loadObject() >> dynamicAttributesCache
        dynamicAttributesCacheStrategy.lock() >> cacheLock

        clientCacheManager.addCachedObject(DynamicAttributesCacheStrategy.NAME, dynamicAttributesCacheStrategy)
    }

    @SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
    protected void exportScreensPackages(List<String> packages) {
        def windowConfig = cont.getBean(WindowConfig)

        def configuration = new UiControllersConfiguration()
        configuration.applicationContext = cont.getApplicationContext()
        configuration.metadataReaderFactory = cont.getBean(MetadataReaderFactory)
        configuration.basePackages = packages

        windowConfig.configurations = [configuration]
        windowConfig.initialized = false
    }

    @SuppressWarnings(["GroovyAccessibility"])
    protected void resetScreensConfig() {
        def windowConfig = cont.getBean(WindowConfig)
        windowConfig.configurations = []
        windowConfig.initialized = false
    }

    @SuppressWarnings("GroovyAccessibility")
    void cleanup() {
        def clientCacheManager = cont.getBean(ClientCacheManager)
        clientCacheManager.cache.remove(DynamicAttributesCacheStrategy.NAME)

        def userSessionSource = (TestUserSessionSource) cont.getBean(UserSessionSource.class)
        userSessionSource.setSession(null)
    }
}