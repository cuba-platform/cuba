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
 *
 */

package com.haulmont.cuba.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.activation.MimeType;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 */
public interface Convertor {
    MimeType getMimeType();

    String getType();

    /**
     * Converts an entity to string representation of search result
     * in a format of current {@code convertor} (xml, json, etc.)
     */
    String process(Entity entity, MetaClass metaclass, View view) throws Exception;

    /**
     * Converts an collection of entities to string representation of search result
     * in a format of current {@code convertor} (xml, json, etc.)
     */
    String process(List<Entity> entities, MetaClass metaClass, View view) throws Exception;

    /**
     * Converts an collection of entities to string representation of commit result
     * in a format of current {@code convertor} (xml, json, etc.)
     */
    String process(Set<Entity> entities) throws Exception;

    /**
     * Converts an object (simple datatype, entity or collection of entities) to string representation
     * of method invocation result in a format of current {@code convertor} (xml, json, etc.)
     */
    String processServiceMethodResult(Object result, Class resultType) throws Exception;

    /**
     * Converts a string representation of {@code CommitContext} to {@code CommitRequest}
     */
    CommitRequest parseCommitRequest(String content);

    /**
     * Converts a string representation of service invocation request to {@code ServiceRequest} object
     */
    ServiceRequest parseServiceRequest(String content) throws Exception;

    /**
     * Converts a string representation of entity to {@code Entity} object
     */
    Entity parseEntity(String content);

    /**
     * Converts a string representation of entities collection to collection of entities
     * @param content
     * @return
     */
    Collection<? extends Entity> parseEntitiesCollection(String content, Class<? extends Collection> collectionClass);

    /**
     * @return list of rest api versions this convertor can work with
     */
    List<Integer> getApiVersions();
}