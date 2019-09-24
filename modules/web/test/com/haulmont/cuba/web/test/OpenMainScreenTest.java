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
import com.haulmont.cuba.web.app.main.MainScreen;
import com.haulmont.cuba.web.container.CubaTestContainer;
import com.haulmont.cuba.web.testsupport.TestUiEnvironment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sample test with {@link TestUiEnvironment}.
 */
public class OpenMainScreenTest {
    @RegisterExtension
    public TestUiEnvironment environment =
            new TestUiEnvironment(CubaTestContainer.Common.INSTANCE) // use cuba shared test container
                    .withScreenPackages("com.haulmont.cuba.web.app.main") // replaces default screen packages
                    .withUserLogin("admin"); // changes user login in the session

    @Test
    public void openMainScreen() {
        Screen screen = environment.getScreens()
                .create(MainScreen.class, OpenMode.ROOT)
                .show();

        assertNotNull(screen);
        assertTrue(screen instanceof MainScreen);
    }
}