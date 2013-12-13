/*
 * A High-Level Framework for Application Configuration
 *
 * Copyright 2007 Merlin Hughes / Learning Objects, Inc.
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

package com.haulmont.cuba.core.config;

import java.lang.annotation.*;

/**
 * DEPRECATED. Always specify full property name in {@link Property} annotation.
 * <p/>
 * Annotation specifying the prefix for all configuration fields in an interface.
 *
 * @author Merlin Hughes
 * @version $Id$
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Prefix
{
    /**
     * The prefix.
     */
    public String value();
}
