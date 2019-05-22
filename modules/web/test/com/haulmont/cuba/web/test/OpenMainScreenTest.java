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

package com.haulmont.cuba.web.test;

import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.security.app.UserManagementService;
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.container.CubaTestContainer;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import com.haulmont.cuba.web.testsupport.proxy.TestServiceProxy;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OpenMainScreenTest {

    @Mocked
    public UserManagementService userManagementService;

    @Rule
    public TestUiEnvironment environment =
            new TestUiEnvironment(CubaTestContainer.Common.INSTANCE)
                    .withScreenPackages("com.haulmont.cuba.web.app.main")
                    .withUserLogin("admin");

    @Before
    public void before() {
        new Expectations() {
            {
                userManagementService.getSubstitutedUsers((UUID) any); result = Collections.emptyList(); minTimes = 0;
            }
        };

        TestServiceProxy.mock(UserManagementService.class, userManagementService);
    }

    @Test
    public void openMainScreen() {
        Screen screen = environment.getScreens()
                .create(MainScreen.class, OpenMode.ROOT)
                .show();

        assertNotNull(screen);
        assertTrue(screen instanceof MainScreen);
    }
}