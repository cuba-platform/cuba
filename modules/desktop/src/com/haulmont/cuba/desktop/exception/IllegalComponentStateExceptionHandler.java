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

package com.haulmont.cuba.desktop.exception;

import javax.annotation.Nullable;
import java.awt.*;

/**
 */
public class IllegalComponentStateExceptionHandler extends AbstractExceptionHandler {

    public IllegalComponentStateExceptionHandler() {
        super(IllegalComponentStateException.class.getName());
    }

    @Override
    protected void doHandle(Thread thread, String className, String message, @Nullable Throwable throwable) {
        // Just swallow this exception because it usually occurs when user clicks on Language drop-down list
        // while the app is logging in and UI is freezed for some time.
    }
}
