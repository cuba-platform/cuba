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

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated {@link UiController} is linked to an XML template.
 * The annotated class must be a direct or indirect subclass of either {@link Screen} or {@link ScreenFragment}.
 * This annotation is inherited by subclasses.
 *
 * @see Screen
 * @see ScreenFragment
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface UiDescriptor {

    /**
     * Path to the XML descriptor. If the value contains a file name only, it is assumed that the file is located
     * in the package of the controller class.
     */
    @AliasFor("path")
    String value() default "";

    /**
     * Path to the XML descriptor. If the value contains a file name only, it is assumed that the file is located
     * in the package of the controller class.
     */
    @AliasFor("value")
    String path() default "";
}