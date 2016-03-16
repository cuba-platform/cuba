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

package com.haulmont.cuba.gui.exception;

import com.haulmont.cuba.gui.WindowManager;

/**
 * Interface to be implemented by exception handlers defined on GUI level.
 *
 */
public interface GenericExceptionHandler {

    /**
     * Defines the highest precedence for {@link org.springframework.core.Ordered} platform handlers.
     */
    int HIGHEST_PLATFORM_PRECEDENCE = 100;

    /**
     * Defines the lowest precedence for {@link org.springframework.core.Ordered} platform handlers.
     */
    int LOWEST_PLATFORM_PRECEDENCE = 1000;

    /**
     * Handle an exception. Implementation class should either handle the exception and return true, or return false
     * to delegate execution to the next handler in the chain of responsibility.
     * @param exception     exception instance
     * @param windowManager WindowManager instance
     * @return              true if the exception has been succesfully handled, false if not
     */
    boolean handle(Throwable exception, WindowManager windowManager);
}
