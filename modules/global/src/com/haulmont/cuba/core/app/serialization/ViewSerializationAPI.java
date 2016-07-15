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

import com.haulmont.cuba.core.global.View;

/**
 * Class that is used for serialization and deserialization of views to JSON.
 */
public interface ViewSerializationAPI {

    String NAME = "cuba_ViewSerialization";

    /**
     * Deserializes a JSON object to view. The method automatically identifies whether the view was serialized with the
     * {@link ViewSerializationOption#COMPACT_FORMAT} option.
     *
     * @param json    JSON objects that represents the view
     * @return a view
     */
    View fromJson(String json);

    /**
     * Serializes a view to JSON object
     *
     * @param view    a view
     * @param options options specifying how a JSON object graph should be serialized
     * @return a string that represents a JSON object
     */
    String toJson(View view, ViewSerializationOption... options);
}
