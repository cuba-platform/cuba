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
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;

/**
 * Class that encapsulates an information needed to load an entity instance.
 * <p/> This information has the following string representation:
 * <code>metaclassName-id[-viewName]</code>, e.g.:
 * <pre>
 * sec$User-60885987-1b61-4247-94c7-dff348347f93
 * sec$Role-0c018061-b26f-4de2-a5be-dff348347f93-role.browse
 * ref$Seller-101
 * ref$Currency-{usd}
 * </pre>
 * <p/> viewName part is optional.
 * <p/> id part should be:
 * <ul>
 *     <li>For UUID keys: canonical UUID representation with 5 groups of hex digits delimited by dashes</li>
 *     <li>For numeric keys: decimal representation of the number</li>
 *     <li>For string keys: the key surrounded by curly brackets, e.g {mykey}</li>
 * </ul>
 * Use {@link EntityLoadInfoBuilder#parse(String)} and {@link #toString()} methods to convert from/to a string.
 *
 */
public class EntityLoadInfo {

    public static final String NEW_PREFIX = "NEW-";

    private MetaClass metaClass;
    private Object id;
    private String viewName;
    private boolean newEntity;
    private boolean stringKey;

    protected EntityLoadInfo(Object id, MetaClass metaClass, String viewName, boolean stringKey) {
        this(id, metaClass, viewName, stringKey, false);
    }

    protected EntityLoadInfo(Object id, MetaClass metaClass, String viewName, boolean stringKey, boolean newEntity) {
        this.id = id;
        this.metaClass = metaClass;
        this.viewName = viewName;
        this.newEntity = newEntity;
        this.stringKey = stringKey;
    }

    public Object getId() {
        return id;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Nullable
    public String getViewName() {
        return viewName;
    }

    public boolean isNewEntity() {
        return newEntity;
    }

    /**
     * Create a new info instance.
     * <p>Consider using {@link EntityLoadInfoBuilder} for better performance.</p>
     * @param entity    entity instance
     * @param viewName  view name, can be null
     * @return          info instance
     */
    public static EntityLoadInfo create(Entity entity, @Nullable String viewName) {
        EntityLoadInfoBuilder builder = AppBeans.get(EntityLoadInfoBuilder.NAME);
        return builder.create(entity, viewName);
    }

    /**
     * Create a new info instance with empty view name.
     * <p>Consider using {@link EntityLoadInfoBuilder} for better performance.</p>
     * @param entity    entity instance
     * @return          info instance
     */
    public static EntityLoadInfo create(Entity entity) {
        return create(entity, null);
    }

    /**
     * Parse an info from the string.
     * <p>Consider using {@link EntityLoadInfoBuilder} for better performance.</p>
     * @param str   string representation of the info
     * @return      info instance or null if the string can not be parsed. Any exception is silently swallowed.
     */
    public static @Nullable EntityLoadInfo parse(String str) {
        EntityLoadInfoBuilder builder = AppBeans.get(EntityLoadInfoBuilder.NAME);
        return builder.parse(str);
    }

    @Override
    public String toString() {
        String key = stringKey ? "{" + id + "}" : id.toString();
        return metaClass.getName() + "-" + key + (viewName == null ? "" : "-" + viewName);
    }
}