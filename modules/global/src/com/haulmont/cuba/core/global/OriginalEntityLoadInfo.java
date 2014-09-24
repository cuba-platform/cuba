/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
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
public class OriginalEntityLoadInfo extends EntityLoadInfo {

    private OriginalEntityLoadInfo(UUID id, MetaClass metaClass) {
        super(id, metaClass, null);
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
        return new OriginalEntityLoadInfo((UUID) entity.getId(), metaClass);
    }
}