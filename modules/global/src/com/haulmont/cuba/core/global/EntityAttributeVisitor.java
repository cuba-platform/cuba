/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Visitor to be submitted to {@link MetadataTools#traverseAttributes(Entity, EntityAttributeVisitor)}.
 *
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public interface EntityAttributeVisitor {
    /**
     * Visits an entity attribute.
     *
     * @param entity    entity instance
     * @param property  meta-property pointing to the visited attribute
     */
    void visit(Entity entity, MetaProperty property);
}
