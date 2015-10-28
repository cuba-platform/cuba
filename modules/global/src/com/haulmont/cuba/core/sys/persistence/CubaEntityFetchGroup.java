/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.global.IllegalEntityStateException;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.internal.queries.EntityFetchGroup;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;

import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaEntityFetchGroup extends EntityFetchGroup {

    public CubaEntityFetchGroup(FetchGroup fetchGroup) {
        super(fetchGroup);
    }

    public CubaEntityFetchGroup(Collection<String> attributeNames) {
        super(attributeNames);
    }

    @Override
    public String onUnfetchedAttribute(FetchGroupTracker entity, String attributeName) {
        String[] inaccessible = ((BaseGenericIdEntity) entity).__inaccessibleAttributes();
        if (inaccessible != null) {
            for (String inaccessibleAttribute : inaccessible) {
                if (attributeName.equals(inaccessibleAttribute))
                    return null;
            }
        }

        if (attributeName == null && entity._persistence_getSession() != null) { // occurs on merge
            return super.onUnfetchedAttribute(entity, null);
        }

        String entityDescriptor = entity.getClass().getName() + "-" + ((BaseGenericIdEntity) entity).getId();
        throw new IllegalEntityStateException(ExceptionLocalization.buildMessage("cannot_get_unfetched_attribute", new Object[]{entityDescriptor, attributeName}));
    }
}