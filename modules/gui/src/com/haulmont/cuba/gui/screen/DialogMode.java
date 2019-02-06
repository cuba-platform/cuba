/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.gui.components.DialogWindow;
import com.haulmont.cuba.gui.components.DialogWindow.WindowMode;

import java.lang.annotation.*;
import java.lang.annotation.Target;

/**
 * Specifies parameters of {@link DialogWindow} if the window is opened as {@link OpenMode#DIALOG}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DialogMode {
    String width() default "";
    String height() default "";

    WindowMode windowMode() default WindowMode.NORMAL;

    boolean modal() default true;
    boolean closeable() default true;
    boolean closeOnClickOutside() default false;
    boolean resizable() default false;

    boolean forceDialog() default false;
}