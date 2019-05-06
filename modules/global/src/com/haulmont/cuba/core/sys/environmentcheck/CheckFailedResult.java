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

package com.haulmont.cuba.core.sys.environmentcheck;

import javax.annotation.Nullable;

/**
 * Class for result of environment sanity check fail.
 */
public class CheckFailedResult {
    protected String message;
    protected Exception exception;

    public CheckFailedResult (String message, @Nullable Exception exception){
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public Exception getException() {
        return exception;
    }
}
