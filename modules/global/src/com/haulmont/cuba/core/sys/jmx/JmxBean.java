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

package com.haulmont.cuba.core.sys.jmx;

import java.lang.annotation.*;

/**
 * Class-level annotation for automatic registration of the class instances
 * with a JMX server, according to the {@link #module} and {@link #alias} attributes.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxBean {

    /**
     * Module for the ObjectName of JMX bean. ObjectName is defined as: $wcn.$module:type=$alias,
     * where
     * $wcn - web context name is specified automatically from cuba.webContextName application property
     * $module - {@link JmxBean#module()} value
     * $alias - {@link JmxBean#alias()} value
     */
    String module() default "";

    /**
     * Alias for the ObjectName of JMX bean. ObjectName is defined as: $wcn.$module:type=$alias,
     * where
     * $wcn - web context name is specified automatically from cuba.webContextName application property
     * $module - module name
     * $alias - {@link JmxBean#alias()} value
     */
    String alias();
}
