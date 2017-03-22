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

package com.haulmont.restapi.transform;

import com.haulmont.restapi.config.RestJsonTransformations;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Standard implementation of the {@link EntityJsonTransformer}. Instances of this class are created and registered
 * automatically by the {@link RestJsonTransformations} class.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StandardEntityJsonTransformer extends AbstractEntityJsonTransformer {
    public StandardEntityJsonTransformer(String fromEntityName, String toEntityName, String version, JsonTransformationDirection direction) {
        super(fromEntityName, toEntityName, version, direction);
    }
}
