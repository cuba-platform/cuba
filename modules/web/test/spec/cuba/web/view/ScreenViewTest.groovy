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

package spec.cuba.web.view


import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.security.entity.User
import spec.cuba.web.UiScreenSpec
import spec.cuba.web.view.screens.UserEditEmbeddedViewScreen

class ScreenViewTest extends UiScreenSpec {


    def setup() {
        exportScreensPackages(['spec.cuba.web.view.screens', 'com.haulmont.cuba.web.app.main'])
    }

    def "Embedded view initialized in instance container"() {
        def screens = vaadinUi.screens

        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        when: "show screen"

        def userEditScreen = screens.create(UserEditEmbeddedViewScreen)
        userEditScreen.setEntityToEdit(new User(login: 'admin'))
        userEditScreen.show()
        def view = userEditScreen.userDc.getView()

        then: "instance container contains embedded view"

        view != null
        view.name == ""
        view.getEntityClass() == User

        and: "view extends specified view"

        view.properties.find {it.name == "login"} != null

        and: "view has system properties"

        view.properties.find { it.name == "updateTs" } != null

        when:

        def groupViewProperty = view.properties.find { it.name == "group" }

        then: "view has inlined views"

        groupViewProperty != null
        groupViewProperty.view.properties.find { it.name == "name" } != null

        when:

        def userRolesViewProperty = view.properties.find { it.name == "userRoles" }

        then: "view has properties with deployed views"

        userRolesViewProperty != null
        userRolesViewProperty.view.name == "user.edit"
        userRolesViewProperty.view.properties.find { it.name == "role" } != null

    }

}
