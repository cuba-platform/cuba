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

import java.lang.annotation.*;
import java.lang.annotation.Target;

/**
 * Annotation for screen controllers which indicates that all data loaders should be triggered automatically
 * before showing the screen.
 * <p>
 * If you need to perform some actions after loading data but before the screen is shown, remove this annotation or
 * set its value to false and use {@code getScreenData().loadAll()} in a {@code BeforeShowEvent} listener.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LoadDataBeforeShow {

    /**
     * Set to false to disable automatic data loading for the screen.
     */
    boolean value() default true;
}