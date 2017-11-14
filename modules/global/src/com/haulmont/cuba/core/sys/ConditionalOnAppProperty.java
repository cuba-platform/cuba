/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.sys;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Indicates that a component is only eligible for registration when specified application property has value equal to
 * specified.
 * <br>
 * The annotation can be used on Spring Framework beans. You can use one or many annotations of this type on single
 * Java class.
 * <br>
 * <p><strong>NOTE</strong>: Inheritance of {@code @ConditionalOnAppProperty} annotations is not supported;
 * any conditions from superclasses or from overridden methods will not be considered.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConditionalOnAppProperties.class)
@Conditional(OnConfigPropertyCondition.class)
public @interface ConditionalOnAppProperty {
    String property();

    String value();

    String defaultValue() default "";
}