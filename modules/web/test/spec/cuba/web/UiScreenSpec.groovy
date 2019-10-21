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
import com.haulmont.cuba.client.sys.cache.ClientCacheManager
import com.haulmont.cuba.client.sys.cache.DynamicAttributesCacheStrategy
import com.haulmont.cuba.client.testsupport.TestUserSessionSource
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCache
import com.haulmont.cuba.core.global.UserSessionSource
import com.haulmont.cuba.gui.Screens
import com.haulmont.cuba.gui.config.WindowConfig
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.gui.screen.Screen
import com.haulmont.cuba.gui.sys.UiControllersConfiguration
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy
import com.haulmont.cuba.web.testsupport.ui.TestCachingStrategy

@SuppressWarnings(["GroovyAccessibility", "GroovyAssignabilityCheck"])
class UiScreenSpec extends WebSpec {

    void setup() {
        def clientCacheManager = cont.getBean(ClientCacheManager)
        def dynamicAttributesCacheStrategy = new TestCachingStrategy()
        def dynamicAttributesCache =
                new DynamicAttributesCache(HashMultimap.create(), [:], new Date())

        dynamicAttributesCacheStrategy.setData(dynamicAttributesCache)

        clientCacheManager.addCachedObject(DynamicAttributesCacheStrategy.NAME, dynamicAttributesCacheStrategy)

        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })
    }

    protected void exportScreensPackages(List<String> packages) {
        def windowConfig = cont.getBean(WindowConfig)

        def configuration = new UiControllersConfiguration()
        def injector = cont.getApplicationContext().getAutowireCapableBeanFactory()
        injector.autowireBean(configuration)
        configuration.basePackages = packages

        windowConfig.configurations = [configuration]
        windowConfig.initialized = false
    }

    protected void resetScreensConfig() {
        def windowConfig = cont.getBean(WindowConfig)
        windowConfig.configurations = []
        windowConfig.initialized = false
    }

    void cleanup() {
        TestServiceProxy.clear()

        resetScreensConfig()

        def clientCacheManager = cont.getBean(ClientCacheManager)
        clientCacheManager.cache.remove(DynamicAttributesCacheStrategy.NAME)

        def userSessionSource = (TestUserSessionSource) cont.getBean(UserSessionSource.class)
        userSessionSource.setSession(null)
    }

    protected Screens getScreens() {
        vaadinUi.screens
    }

    protected Screen showMainWindow() {
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)
        mainWindow
    }
}