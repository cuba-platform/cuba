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

package com.haulmont.cuba.core.sys.validation;

import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Path;
import javax.validation.TraversableResolver;
import java.lang.annotation.ElementType;

public class CubaValidationTraversableResolver implements TraversableResolver {

    private static final Logger log = LoggerFactory.getLogger(CubaValidationTraversableResolver.class);

    protected Metadata metadata;
    protected EntityStates entityStates;

    public CubaValidationTraversableResolver(Metadata metadata, EntityStates entityStates) {
        this.metadata = metadata;
        this.entityStates = entityStates;
    }

    @Override
    public final boolean isReachable(Object traversableObject,
                                     Path.Node traversableProperty,
                                     Class<?> rootBeanType,
                                     Path pathToTraversableObject,
                                     ElementType elementType) {
        log.trace("Calling isReachable on object {} with node name {}",
                traversableObject, traversableProperty.getName());

        if (traversableObject == null
                || metadata.getClass(traversableObject.getClass()) == null) {
            return true;
        }

        return entityStates.isLoaded(traversableObject, traversableProperty.getName());
    }

    @Override
    public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType,
                                Path pathToTraversableObject, ElementType elementType) {
        // return true as org.hibernate.validator.internal.engine.resolver.JPATraversableResolver does
        return true;
    }
}