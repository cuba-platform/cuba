/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

/**
 * Class that encapsulates an information needed to load an entity instance.
 * <p/> This information has the following string representation:
 * <code>metaclassName-id{-viewName}</code>, e.g.:
 * <pre>
 * sec$User-60885987-1b61-4247-94c7-dff348347f93
 * sec$Role-0c018061-b26f-4de2-a5be-dff348347f93-role.browse
 * </pre>
 * Use {@link #parse(String)} and {@link #toString()} methods to convert from/to a string.
 *
 * @author krivopustov
 * @version $Id$
 */
public class EntityLoadInfo {

    private MetaClass metaClass;
    private UUID id;
    private String viewName;

    private EntityLoadInfo(UUID id, MetaClass metaClass, String viewName) {
        this.id = id;
        this.metaClass = metaClass;
        this.viewName = viewName;
    }

    public UUID getId() {
        return id;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    @Nullable
    public String getViewName() {
        return viewName;
    }

    /**
     * Create a new info instance.
     * @param entity    entity instance
     * @param viewName  view name, can be null
     * @return          info instance
     */
    public static EntityLoadInfo create(Entity entity, @Nullable String viewName) {
        Objects.requireNonNull(entity, "entity is null");

        MetaClass metaClass = AppBeans.get(Metadata.class).getSession().getClassNN(entity.getClass());
        return new EntityLoadInfo((UUID) entity.getId(), metaClass, viewName);
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
        int dashCount = StringUtils.countMatches(str, "-");
        if (dashCount < 5) {
            return null;
        }

        int idDashPos = str.indexOf('-');
        String entityName = str.substring(0, idDashPos);
        MetaClass metaClass = AppBeans.get(Metadata.class).getSession().getClass(entityName);
        if (metaClass == null) {
            return null;
        }

        int viewDashPos = -1;
        String viewName;
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
        UUID id;
        try {
            id = UUID.fromString(entityIdStr);
        } catch (Exception e) {
            return null;
        }

        return new EntityLoadInfo(id, metaClass, viewName);
    }

    @Override
    public String toString() {
        return metaClass.getName() + "-" + id + (viewName == null ? "" : "-" + viewName);
    }
}
