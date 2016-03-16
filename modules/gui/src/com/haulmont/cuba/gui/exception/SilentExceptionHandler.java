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

import com.haulmont.cuba.core.global.SilentException;
import com.haulmont.cuba.gui.WindowManager;
import org.springframework.core.Ordered;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;

/**
 * Handler that does nothing in respond to {@link SilentException}.
 *
 */
@Component("cuba_SilentExceptionHandler")
public class SilentExceptionHandler extends AbstractGenericExceptionHandler implements Ordered {

    public SilentExceptionHandler() {
        super(SilentException.class.getName());
    }

    @Override
    protected void doHandle(String className, String message, @Nullable Throwable throwable, WindowManager windowManager) {
        // do nothing
    }

    @Override
    public int getOrder() {
        return HIGHEST_PLATFORM_PRECEDENCE + 10;
    }
}
