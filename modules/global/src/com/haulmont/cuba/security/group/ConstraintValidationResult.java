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

package com.haulmont.cuba.security.group;

import java.io.Serializable;

public class ConstraintValidationResult implements Serializable {
    private static final long serialVersionUID = -2567588357873216655L;

    protected boolean isCompilationFailedException = false;
    protected String stacktrace;
    protected String errorMessage;

    public boolean isCompilationFailedException() {
        return isCompilationFailedException;
    }

    public void setCompilationFailedException(boolean compilationFailedException) {
        isCompilationFailedException = compilationFailedException;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
