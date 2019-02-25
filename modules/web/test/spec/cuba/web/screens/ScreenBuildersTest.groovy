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

package spec.cuba.web.screens

import com.google.common.collect.Iterables
import com.haulmont.cuba.core.app.DataService
import com.haulmont.cuba.core.global.CommitContext
import com.haulmont.cuba.core.global.LoadContext
import com.haulmont.cuba.gui.ScreenBuilders
import com.haulmont.cuba.gui.app.security.user.browse.UserBrowser
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor
import com.haulmont.cuba.gui.components.HasValue
import com.haulmont.cuba.gui.data.CollectionDatasource
import com.haulmont.cuba.gui.screen.LookupScreen
import com.haulmont.cuba.gui.screen.OpenMode
import com.haulmont.cuba.gui.util.OperationResult
import com.haulmont.cuba.security.app.UserManagementService
import com.haulmont.cuba.security.entity.Group
import com.haulmont.cuba.security.entity.User
import com.haulmont.cuba.web.testsupport.TestServiceProxy
import spec.cuba.web.UiScreenSpec

import java.util.function.Consumer
import java.util.function.Function

@SuppressWarnings("GroovyAccessibility")
class ScreenBuildersTest extends UiScreenSpec {

    def setup() {
        def group = new Group(name: 'Company')
        def users = [
                new User(login: 'admin', loginLowerCase: 'admin', group: group, active: true, name: 'Administrator'),
                new User(login: 'anonymous', loginLowerCase: 'anonymous', group: group, active: true, name: 'Anonymous')
        ]

        TestServiceProxy.mock(DataService, Mock(DataService) {
            loadList(_) >> { LoadContext lc ->
                if (lc.entityMetaClass == 'sec$User') {
                    return users
                }

                return []
            }
            commit(_) >> { CommitContext cc ->
                return cc.getCommitInstances()
            }
        })

        TestServiceProxy.mock(UserManagementService, Mock(UserManagementService) {
            getSubstitutedUsers(_) >> Collections.emptyList()
        })
    }

    def cleanup() {
        TestServiceProxy.clear()
    }

    def "build and show UserEditor with ScreenBuilders"() {
        def screens = vaadinUi.screens

        def screenBuilders = cont.getBean(ScreenBuilders.class)

        def afterCloseListener = Mock(Consumer)
        def transformation = Spy(new Function() {
            @Override
            Object apply(Object o) {
                return o
            }
        })

        def field = Mock(HasValue)

        when:
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        then:
        vaadinUi.topLevelWindow == mainWindow.window

        when:
        def user = new User()
        user.setLogin("test")
        user.setGroup(new Group(name: 'Company'))

        UserEditor editor = (UserEditor) screenBuilders.editor(User.class, mainWindow)
            .newEntity(user)
            .withTransformation(transformation)
            .withField(field)
            .show()

        editor.passwField.setValue("1")
        editor.confirmPasswField.setValue("1")

        editor.addAfterCloseListener(afterCloseListener)
        def result = editor.closeWithCommit()

        then:
        result.status == OperationResult.Status.SUCCESS

        1 * transformation.apply(_) >> { User selectedUser ->
            assert selectedUser != null
        }
        1 * afterCloseListener.accept(_)
        1 * field.setValue(_)
    }

    def "build and show UserBrowser with ScreenBuilders"() {
        def screens = vaadinUi.screens

        def screenBuilders = cont.getBean(ScreenBuilders.class)

        def afterCloseListener = Mock(Consumer)
        def transformation = Spy(new Function() {
            @Override
            Object apply(Object o) {
                return o
            }
        })

        def field = Mock(HasValue)

        when:
        def mainWindow = screens.create("mainWindow", OpenMode.ROOT)
        screens.show(mainWindow)

        then:
        vaadinUi.topLevelWindow == mainWindow.window

        when:
        UserBrowser lookup = (UserBrowser) screenBuilders.lookup(User.class, mainWindow)
                .withScreenId('sec$User.browse')
                .withTransformation(transformation)
                .withField(field)
                .show()

        lookup.addAfterCloseListener(afterCloseListener)

        def usersDs = lookup.dsContext.get('usersDs') as CollectionDatasource
        def user = Iterables.getLast(usersDs.getItems()) as User
        lookup.usersTable.setSelected(user)

        def selectAction = lookup.getActionNN(LookupScreen.LOOKUP_SELECT_ACTION_ID)
        selectAction.actionPerform(lookup.usersTable)

        then:
        !screens.getOpenedScreens().all.contains(lookup)

        1 * transformation.apply(_)
        1 * afterCloseListener.accept(_)
        1 * field.setValue(_)
    }
}