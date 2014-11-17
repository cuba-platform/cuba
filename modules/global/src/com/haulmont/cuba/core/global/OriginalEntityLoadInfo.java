/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;

import java.util.Objects;
import java.util.UUID;

/**
 * Class that encapsulates an information needed to load an entity instance.
 * <p>
 * The same as {@link EntityLoadInfo} but always creates an entity of original class if an extended MetaClass is
 * provided.
 *
 * @author krivopustov
 * @version $Id$
 */
public class OriginalEntityLoadInfo extends EntityLoadInfo {

    private OriginalEntityLoadInfo(UUID id, MetaClass metaClass, boolean isStringKey) {
        super(id, metaClass, null, isStringKey);
    }

    /**
     * Create a new info instance.
     * @param entity    entity instance
     * @return          info instance
     */
    public static OriginalEntityLoadInfo create(Entity entity) {
        Objects.requireNonNull(entity, "entity is null");

        Metadata metadata = AppBeans.get(Metadata.NAME);
        MetaClass metaClass = metadata.getSession().getClassNN(entity.getClass());

        MetaClass originalMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
        if (originalMetaClass != null) {
            metaClass = originalMetaClass;
        }

        MetaProperty primaryKeyProperty = metadata.getTools().getPrimaryKeyProperty(metaClass);
        boolean stringKey = primaryKeyProperty != null && primaryKeyProperty.getJavaType().equals(String.class);

        return new OriginalEntityLoadInfo((UUID) entity.getId(), metaClass, stringKey);
    }
}