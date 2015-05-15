/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;

import javax.activation.MimeType;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
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
    CommitRequest parseCommitRequest(String content, boolean commitDynamicAttributes);

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