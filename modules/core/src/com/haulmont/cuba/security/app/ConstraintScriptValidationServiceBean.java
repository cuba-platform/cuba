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

package com.haulmont.cuba.security.app;

import com.haulmont.cuba.core.app.ConstraintScriptValidationService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Security;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(ConstraintScriptValidationService.NAME)
public class ConstraintScriptValidationServiceBean implements ConstraintScriptValidationService {
    @Inject
    Security security;

    @Override
    public ScriptValidationResult evaluateConstraintScript(Entity entity, String groovyScript) {
        ScriptValidationResult result = new ScriptValidationResult(false);
        try {
            security.evaluateConstraintScript(entity, groovyScript);
        } catch (CompilationFailedException e) {
            result.setCompilationFailedException(true);
            result.setStacktrace(ExceptionUtils.getStackTrace(e));
            result.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            // ignore
        }
        return result;
    }
}
