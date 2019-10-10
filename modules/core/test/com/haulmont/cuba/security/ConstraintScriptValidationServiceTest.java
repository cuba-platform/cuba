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

package com.haulmont.cuba.security;

import com.haulmont.cuba.core.app.ConstraintScriptValidationService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class ConstraintScriptValidationServiceTest {

    @RegisterExtension
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private ConstraintScriptValidationService constraintScriptValidationServiceBean = AppBeans.get(ConstraintScriptValidationService.class);

    @Test
    public void testCompilationFailedException() {
        ConstraintScriptValidationService.ScriptValidationResult result =
                constraintScriptValidationServiceBean.evaluateConstraintScript(null, "import com.haulmont.cuba.core.Persistence_UNEXIST");
        Assertions.assertTrue(result.isCompilationFailedException());
    }

    @Test
    public void testNotCompilationFailedException() {
        ConstraintScriptValidationService.ScriptValidationResult result =
                constraintScriptValidationServiceBean.evaluateConstraintScript(null, "import com.haulmont.cuba.core.Persistence");
        Assertions.assertFalse(result.isCompilationFailedException());
    }
}
