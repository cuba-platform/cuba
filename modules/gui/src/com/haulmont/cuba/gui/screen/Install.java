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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for declarative handler methods in UI controllers.
 * <br>
 * Example:
 * <pre>
 *    &#64;Install(to = "label", subject = "formatter")
 *    protected String formatValue(Integer value) {
 *        // the method used as Label formatter
 *        return "1.0";
 *    }
 * </pre>
 *
 * @see Screen
 * @see ScreenFragment
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@java.lang.annotation.Target(ElementType.METHOD)
public @interface Install {
    /**
     * @return type of target
     */
    Target target() default Target.COMPONENT;

    /**
     * @return type of functional interface, can be used instead of {@link #subject()}
     */
    Class type() default Object.class;

    /**
     * @return property name that will be set using annotated method
     */
    String subject() default "";

    /**
     * @return id or path to target object
     */
    String to() default "";

    /**
     * Declares whether the annotated dependency is required.
     * <p>Defaults to {@code true}.
     */
    boolean required() default true;
}