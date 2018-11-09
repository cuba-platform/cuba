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

import com.haulmont.cuba.client.ClientUserSession
import com.haulmont.cuba.core.app.ConfigStorageService
import com.haulmont.cuba.core.app.PersistenceManagerService
import com.haulmont.cuba.core.global.*
import com.haulmont.cuba.gui.UiComponents
import com.haulmont.cuba.gui.events.sys.UiEventsMulticaster
import com.haulmont.cuba.gui.model.DataComponents
import com.haulmont.cuba.gui.theme.ThemeConstants
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory
import com.haulmont.cuba.web.*
import com.haulmont.cuba.web.sys.AppCookies
import com.haulmont.cuba.web.testsupport.TestContainer
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import com.vaadin.server.Page
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinSession
import com.vaadin.server.WebBrowser
import com.vaadin.shared.ui.ui.PageState
import com.vaadin.ui.ConnectorTracker
import com.vaadin.ui.UI
import org.junit.ClassRule
import spock.lang.Shared
import spock.lang.Specification

class WebSpec extends Specification {

    @Shared @ClassRule
    TestContainer cont = TestContainer.Common.INSTANCE

    Metadata metadata
    MetadataTools metadataTools
    ViewRepository viewRepository
    EntityStates entityStates
    DataManager dataManager
    DataComponents dataComponents
    ComponentsFactory componentsFactory
    UiComponents uiComponents

    UserSessionSource sessionSource

    AppUI vaadinUi

    @SuppressWarnings("GroovyAccessibility")
    void setup() {
        metadata = cont.getBean(Metadata)
        metadataTools = cont.getBean(MetadataTools)
        viewRepository = cont.getBean(ViewRepository)
        entityStates = cont.getBean(EntityStates)
        dataManager = cont.getBean(DataManager)
        dataComponents = cont.getBean(DataComponents)
        componentsFactory = cont.getBean(ComponentsFactory)
        uiComponents = cont.getBean(UiComponents)

        sessionSource = Mock(UserSessionSource)

        // all the rest is required for web components

        TestServiceProxy.mock(PersistenceManagerService, Mock(PersistenceManagerService) {
            isNullsLastSorting() >> false
        })

        TestServiceProxy.mock(ConfigStorageService, Mock(ConfigStorageService) {
            getDbProperties() >> [:]
        })

        App app = new TestApp()
        app.cookies = new AppCookies()

        def connection = Mock(Connection)
        app.connection = connection
        app.events = Mock(Events)

        def webBrowser = new WebBrowser()

        VaadinSession vaadinSession = Mock() {
            hasLock() >> true
            getAttribute(App) >> app
            getAttribute(App.NAME) >> app
            getAttribute(Connection) >> connection
            getAttribute(Connection.NAME) >> connection
            getLocale() >> Locale.ENGLISH
            getBrowser() >> webBrowser
        }
        VaadinSession.setCurrent(vaadinSession)

        ConnectorTracker vaadinConnectorTracker = Mock() {
            isWritingResponse() >> false
        }

        vaadinUi = Spy(AppUI)
        vaadinUi.app = app
        vaadinUi.messages = cont.getBean(Messages)
        vaadinUi.getConnectorTracker() >> vaadinConnectorTracker

        vaadinUi.globalConfig = Mock(GlobalConfig)
        vaadinUi.webConfig = Mock(WebConfig)
        vaadinUi.beanLocator = Mock(BeanLocator)

        def page = new Page(vaadinUi, new PageState())
        def vaadinRequest = Mock(VaadinRequest) {
            getParameter("v-loc") >> "http://localhost:8080/app"
            getParameter("v-cw") >> "1280"
            getParameter("v-ch") >> "1080"
            getParameter("v-wn") >> "1"
        }
        page.init(vaadinRequest)

        vaadinUi.getPage() >> page
        vaadinUi.getSession() >> vaadinSession
        vaadinUi.uiEventsMulticaster = Mock(UiEventsMulticaster)

        vaadinUi.applicationContext = cont.getApplicationContext()

        def session = Mock(ClientUserSession) {
            getLocale() >> Locale.ENGLISH
        }

        this.sessionSource.getUserSession() >> session
        session.isAuthenticated() >> false

        vaadinUi.userSessionSource = this.sessionSource

        UI.setCurrent(vaadinUi)

        vaadinUi.init(vaadinRequest)
    }

    void cleanup() {
        TestServiceProxy.clear()
    }

    static class TestApp extends DefaultApp {
        TestApp() {
            this.themeConstants = new ThemeConstants([:])
        }
    }
}