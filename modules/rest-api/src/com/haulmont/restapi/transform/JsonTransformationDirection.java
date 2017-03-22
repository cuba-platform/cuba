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

/**
 * Enum for JSON transformation direction
 */
public enum JsonTransformationDirection {
    /**
     * Transformation direction from the current state of the domain model to the state of specific version
     */
    TO_VERSION,

    /**
     * Transformation direction from some old state of the domain model to its current state
     */
    FROM_VERSION
}
