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

package spec.cuba.web.screens

import com.haulmont.cuba.client.ClientUserSession
import com.haulmont.cuba.core.global.BeanLocator
import com.haulmont.cuba.core.global.GlobalConfig
import com.haulmont.cuba.core.global.UserSessionSource
import com.haulmont.cuba.gui.Screens
import com.haulmont.cuba.gui.config.WindowConfig
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.web.WebConfig
import com.vaadin.server.VaadinRequest
import spec.cuba.web.WebSpec

import java.util.function.Consumer

class ScreenTest extends WebSpec {
    private sessionSource

    def setup() {
        this.sessionSource = Mock(UserSessionSource)
        def session = Mock(ClientUserSession) {
            getLocale() >> Locale.ENGLISH
        }

        this.sessionSource.getUserSession() >> session
        session.isAuthenticated() >> false

        vaadinUi.userSessionSource = this.sessionSource
        vaadinUi.globalConfig = Mock(GlobalConfig)
        vaadinUi.webConfig = Mock(WebConfig)
        vaadinUi.beanLocator = Mock(BeanLocator)

        def vaadinRequest = Mock(VaadinRequest)
        vaadinUi.init(vaadinRequest)
    }

    def "Open login window"() {
        def windowConfig = cont.getBean(WindowConfig)
        def loginWindowInfo = windowConfig.getWindowInfo('loginWindow')

        Screens screens = cont.getBean(Screens.NAME, vaadinUi)

        def beforeShowListener = Mock(Consumer)
        def afterShowListener = Mock(Consumer)

        when:
        def loginWindow = screens.create(loginWindowInfo, OpenMode.ROOT)

        then:
        loginWindow != null

        when:
        loginWindow.addBeforeShowListener(beforeShowListener)
        loginWindow.addAfterShowListener(afterShowListener)
        screens.show(loginWindow)

        then:
        vaadinUi.topLevelWindow == loginWindow.window
        1 * beforeShowListener.accept(_)
        1 * afterShowListener.accept(_)
    }
}