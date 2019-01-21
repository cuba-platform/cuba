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

package com.haulmont.cuba.web.sys.navigation;

import com.haulmont.bali.util.URLEncodeUtils;
import com.haulmont.cuba.web.sys.WebUrlRouting;

import javax.annotation.Nonnull;
import java.util.UUID;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * This class is intended for serializing entity ids to be used as URL param.
 * <p>
 * String, Integer and Long ids are serialized as-is.
 * <p>
 * UUID ids are serialized using Crockford Base32 encoding.
 *
 * @see CrockfordUuidEncoder
 * @see WebUrlRouting
 * @see UrlChangeHandler
 */
public final class UrlIdSerializer {

    private UrlIdSerializer() {
    }

    /**
     * Serializes the given {@code id} to string representation.
     * <p>
     * String, Integer, Long and UUID ids are only supported.
     *
     * @param id id to serialize
     * @return serialized string representation of id
     * @throws IllegalArgumentException if null id is passed or it has an unsupported type
     */
    @Nonnull
    public static String serializeId(Object id) {
        checkNotNullArgument(id, "Id cannot be null");

        String serialized;
        Class<?> idClass = id.getClass();

        if (String.class == idClass
                || Integer.class == idClass
                || Long.class == idClass) {
            serialized = URLEncodeUtils.encodeUtf8(id.toString());

        } else if (UUID.class == idClass) {
            serialized = CrockfordUuidEncoder.encode(((UUID) id));
        } else {
            throw new IllegalArgumentException(
                    String.format("Unable to serialize id '%s' of type '%s'", id, idClass));
        }

        return serialized;
    }

    /**
     * Deserializes the given {@code serializedId} as an id with {@code idClass} type.
     * <p>
     * String, Integer, Long and UUID ids are only supported.
     *
     * @param idClass      id type
     * @param serializedId serialized id
     * @return deserialized id
     * @throws IllegalArgumentException if null id and/or type are passed or the given id type is not supported
     */
    @Nonnull
    public static Object deserializeId(Class idClass, String serializedId) {
        checkNotNullArgument(idClass, "Unable to deserialize id without its type");
        checkNotNullArgument(serializedId, "Unable to deserialize null id");

        Object deserialized;
        String decoded = URLEncodeUtils.decodeUtf8(serializedId);

        try {
            if (String.class == idClass) {
                deserialized = decoded;

            } else if (Integer.class == idClass) {
                deserialized = Integer.valueOf(decoded);

            } else if (Long.class == idClass) {
                deserialized = Long.valueOf(decoded);

            } else if (UUID.class == idClass) {
                deserialized = CrockfordUuidEncoder.decode(serializedId);
            } else {
                throw new IllegalArgumentException(
                        String.format("Unable to deserialize id '%s' of type '%s'", serializedId, idClass));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    String.format("An error occurred while deserializing id: '%s' of type '%s'",
                            serializedId, idClass));
        }

        return deserialized;
    }
}
