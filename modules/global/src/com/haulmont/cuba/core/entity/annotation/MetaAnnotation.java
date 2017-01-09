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
 */

package com.haulmont.cuba.core.entity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an annotation as being a meta-annotation for entity classes and attributes.
 *
 * <p>Meta-annotations are automatically processed by metadata loader and are available via
 * {@code MetaClass.getAnnotations()} or {@code MetaProperty.getAnnotations()}. They can be also added or overridden in
 * {@code metadata.xml}.
 *
 * <p>If a meta-annotation is defined on a class, it's propagated down to subclasses until it has the
 * {@code propagateToSubclasses} attribute set to false. See for example {@link SystemLevel} annotation.
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaAnnotation {
}
