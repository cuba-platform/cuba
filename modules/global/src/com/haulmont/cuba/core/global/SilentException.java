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
package com.haulmont.cuba.core.global;

/**
 * Exception that is used to interrupt an execution flow without any messages to the user.
 *
 */
@Logging(Logging.Type.NONE)
public class SilentException extends RuntimeException {

    private static final long serialVersionUID = 6598108074890603763L;

    public SilentException() {
        super();
    }

    public SilentException(String message) {
        super(message);
    }

    public SilentException(String message, Throwable cause) {
        super(message, cause);
    }

    public SilentException(Throwable cause) {
        super(cause);
    }

    protected SilentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
