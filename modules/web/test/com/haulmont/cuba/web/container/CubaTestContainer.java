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

package com.haulmont.cuba.web.container;

import com.haulmont.cuba.web.testsupport.TestContainer;

import java.util.Arrays;

public class CubaTestContainer extends TestContainer {

    public static class Common extends CubaTestContainer {

        public static final CubaTestContainer INSTANCE = new CubaTestContainer();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void before() throws Throwable {
            if (!initialized) {
                super.before();
                initialized = true;
            }
            setupContext();
        }

        @Override
        public void after() {
            cleanupContext();
            // never stops - do not call super
        }
    }

    public CubaTestContainer() {
        setSpringConfig("com/haulmont/cuba/web/testsupport/test-web-spring.xml com/haulmont/cuba/test-web-spring.xml");
        setAppPropertiesFiles(Arrays.asList(
                "com/haulmont/cuba/web-app.properties",
                "com/haulmont/cuba/web/testsupport/test-web-app.properties",
                "com/haulmont/cuba/test-web-app.properties"
        ));
    }
}