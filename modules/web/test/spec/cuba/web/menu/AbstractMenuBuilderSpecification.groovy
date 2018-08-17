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

package spec.cuba.web.menu

import com.haulmont.cuba.client.testsupport.TestUserSessionSource
import com.haulmont.cuba.core.config.Config
import com.haulmont.cuba.core.global.Configuration
import com.haulmont.cuba.core.global.MessageTools
import com.haulmont.cuba.core.global.Messages
import com.haulmont.cuba.gui.components.Window
import spock.lang.Specification

abstract class AbstractMenuBuilderSpecification extends Specification {

    def configuration = new Configuration() {
        @Override
        Config getConfig(Class configInterface) {
            return null
        }
    }
    def messageTools = new MessageTools(configuration) {
        @Override
        String loadString(String ref) {
            return ''
        }
    }

    def messages = Mock(Messages.class)
    def mainWindow = Mock(Window.class)
    def menuConfig = new TestMenuConfig()
    def userSession = new TestUserSessionSource().getUserSession()

    void setup() {
        mainWindow.getFrame() >> mainWindow
        menuConfig.messages = messages

        messages.getMainMessage(_ as String) >> ''
    }
}