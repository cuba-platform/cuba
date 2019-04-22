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

package com.haulmont.cuba.core.app.serialization;

/**
 * An object that configures how to serialize the entity to JSON. Objects of this type are used by methods of the {@link
 * EntitySerializationAPI}. This object may be also used during the deserialization.
 */
public enum EntitySerializationOption {

    /**
     * If an entity occurs in object graph several times, then the second and next occurrences will be replaced just
     * with a JSON object with the single field - entity identified.
     */
    COMPACT_REPEATED_ENTITIES,

    /**
     * Specifies that fields with null values should be serialized. The default behavior is not to serialize nulls.
     */
    SERIALIZE_NULLS,

    /**
     * Specifies that entity instance name will be serialized. The default behavior is not to serialize instance name.
     */
    SERIALIZE_INSTANCE_NAME,

    /**
     * Specifies that read-only non-persistent properties (getter methods annotated with @MetaProperty) should NOT be serialized.
     */
    DO_NOT_SERIALIZE_RO_NON_PERSISTENT_PROPERTIES,

    /**
     * Specifies that JSON must be pretty printed
     */
    PRETTY_PRINT
}
