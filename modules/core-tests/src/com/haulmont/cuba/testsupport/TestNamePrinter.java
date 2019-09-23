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
 *
 */

package com.haulmont.cuba.testsupport;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestNamePrinter extends TestWatcher implements BeforeEachCallback, AfterEachCallback {

    @Override
    protected void starting(Description description) {
        System.out.println(">>>\n>>> Starting " + description.getMethodName() + "\n>>>");
    }

    @Override
    protected void finished(Description description) {
        System.out.println(">>>>\n>>> Finished " + description.getMethodName() + "\n>>>");
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        System.out.println(">>>\n>>> Finished " + extensionContext.getDisplayName() + "\n>>>");
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.out.println(">>>\n>>> Starting " + extensionContext.getDisplayName() + "\n>>>");
    }
}