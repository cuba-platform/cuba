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

package com.haulmont.restapi.swagger;

import io.swagger.models.Swagger;

/**
 * This bean generates Swagger documentation according to the 2.0 specification.
 * <p>
 * Generated documentation includes operations with entities, predefined REST queries and exposed services.
 */
public interface SwaggerGenerator {

    String NAME = "cuba_SwaggerGenerator";

    /**
     * @return a {@code Swagger} object that can be transformed to JSON or YAML version of documentation
     */
    Swagger generateSwagger();
}
