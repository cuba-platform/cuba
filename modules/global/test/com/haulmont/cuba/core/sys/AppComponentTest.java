/*
 * Copyright (c) 2008-2016 Haulmont.
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

package com.haulmont.cuba.core.sys;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AppComponentTest {

    @Test
    public void testDependencies() throws Exception {
        AppComponent cuba = new AppComponent("cuba");
        AppComponent reports = new AppComponent("reports");
        AppComponent baseApp = new AppComponent("baseApp");
        AppComponent app = new AppComponent("app");
        AppComponent funcComp = new AppComponent("funcComp");

        reports.addDependency(cuba);
        baseApp.addDependency(reports);
        app.addDependency(baseApp);
        app.addDependency(funcComp);
        funcComp.addDependency(baseApp);

        assertTrue(app.dependsOn(cuba));

        List<AppComponent> components = new ArrayList<>();
        components.add(funcComp);
        components.add(app);
        components.add(baseApp);
        components.add(reports);
        components.add(cuba);

        Collections.sort(components);
        int i = 0;
        assertTrue(components.get(i++) == cuba);
        assertTrue(components.get(i++) == reports);
        assertTrue(components.get(i++) == baseApp);
        assertTrue(components.get(i++) == funcComp);
        assertTrue(components.get(i) == app);

        try {
            funcComp.addDependency(app);
            fail();
        } catch (Exception e) {
            // ok - circular dependency
        }
    }
}