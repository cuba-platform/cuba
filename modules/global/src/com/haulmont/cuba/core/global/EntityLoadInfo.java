/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

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
 * Use {@link #parse(String)} and {@link #toString()} methods to convert from/to a string.
 *
 * @author krivopustov
 * @version $Id$
 */
public class EntityLoadInfo {

    private static final String NEW_PREFIX = "NEW-";

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
     * @param entity    entity instance
     * @param viewName  view name, can be null
     * @return          info instance
     */
    public static EntityLoadInfo create(Entity entity, @Nullable String viewName) {
        Objects.requireNonNull(entity, "entity is null");

        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass metaClass = metadata.getSession().getClassNN(entity.getClass());

        MetaProperty primaryKeyProperty = metadata.getTools().getPrimaryKeyProperty(metaClass);
        boolean stringKey = primaryKeyProperty != null && primaryKeyProperty.getJavaType().equals(String.class);

        return new EntityLoadInfo(entity.getId(), metaClass, viewName, stringKey);
    }

    /**
     * Create a new info instance with empty view name.
     * @param entity    entity instance
     * @return          info instance
     */
    public static EntityLoadInfo create(Entity entity) {
        return create(entity, null);
    }

    /**
     * Parse an info from the string.
     * @param str   string representation of the info
     * @return      info instance or null if the string can not be parsed. Any exception is silently swallowed.
     */
    public static @Nullable EntityLoadInfo parse(String str) {
        boolean isNew = false;
        if (str.startsWith(NEW_PREFIX)) {
            str = str.substring("NEW-".length());
            isNew = true;
        }

        int idDashPos = str.indexOf('-');
        if (idDashPos == -1) {
            return null;
        }

        String entityName = str.substring(0, idDashPos);
        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass metaClass = metadata.getSession().getClass(entityName);
        if (metaClass == null) {
            return null;
        }

        Object id;
        String viewName;
        boolean stringKey = false;

        MetaProperty primaryKeyProp = metadata.getTools().getPrimaryKeyProperty(metaClass);
        if (primaryKeyProp == null)
            return null;

        if (primaryKeyProp.getJavaType().equals(UUID.class)) {
            int viewDashPos = -1;
            int dashCount = StringUtils.countMatches(str, "-");
            if (dashCount < 5) {
                return null;
            }
            if (dashCount >= 6) {
                int i = 0;
                while (i < 6) {
                    viewDashPos = str.indexOf('-', viewDashPos + 1);
                    i++;
                }

                viewName = str.substring(viewDashPos + 1);
            } else {
                viewDashPos = str.length();
                viewName = null;
            }
            String entityIdStr = str.substring(idDashPos + 1, viewDashPos);
            try {
                id = UuidProvider.fromString(entityIdStr);
            } catch (Exception e) {
                return null;
            }
        } else {
            String entityIdStr;
            if (primaryKeyProp.getJavaType().equals(String.class)) {
                stringKey = true;
                int viewDashPos = str.indexOf("}-", idDashPos + 2);
                if (viewDashPos > -1) {
                    viewName = str.substring(viewDashPos + 2);
                } else {
                    viewDashPos = str.length() - 1;
                    viewName = null;
                }
                entityIdStr = str.substring(idDashPos + 2, viewDashPos);
            } else {
                int viewDashPos = str.indexOf('-', idDashPos + 1);
                if (viewDashPos > -1) {
                    viewName = str.substring(viewDashPos + 1);
                } else {
                    viewDashPos = str.length();
                    viewName = null;
                }
                entityIdStr = str.substring(idDashPos + 1, viewDashPos);
            }
            try {
                if (primaryKeyProp.getJavaType().equals(Long.class)) {
                    id = Long.valueOf(entityIdStr);
                } else if (primaryKeyProp.getJavaType().equals(Integer.class)) {
                    id = Integer.valueOf(entityIdStr);
                } else {
                    id = entityIdStr;
                }
            } catch (Exception e) {
                return null;
            }
        }

        return new EntityLoadInfo(id, metaClass, viewName, stringKey, isNew);
    }

    @Override
    public String toString() {
        String key = stringKey ? "{" + id + "}" : id.toString();
        return metaClass.getName() + "-" + key + (viewName == null ? "" : "-" + viewName);
    }
}