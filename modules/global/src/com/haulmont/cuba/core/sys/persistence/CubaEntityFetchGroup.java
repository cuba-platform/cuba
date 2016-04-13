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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.global.IllegalEntityStateException;
import org.eclipse.persistence.internal.localization.ExceptionLocalization;
import org.eclipse.persistence.internal.queries.EntityFetchGroup;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;

import java.util.Collection;

public class CubaEntityFetchGroup extends EntityFetchGroup {

    public CubaEntityFetchGroup(FetchGroup fetchGroup) {
        super(fetchGroup);
    }

    public CubaEntityFetchGroup(Collection<String> attributeNames) {
        super(attributeNames);
    }

    @Override
    public String onUnfetchedAttribute(FetchGroupTracker entity, String attributeName) {
        String[] inaccessible = BaseEntityInternalAccess.getInaccessibleAttributes((BaseGenericIdEntity) entity);
        if (inaccessible != null) {
            for (String inaccessibleAttribute : inaccessible) {
                if (attributeName.equals(inaccessibleAttribute))
                    return null;
            }
        }

        if ((attributeName == null && entity._persistence_getSession() != null) // occurs on merge
                || BaseEntityInternalAccess.isRemoved((BaseGenericIdEntity) entity) /* EclipseLink can access reference fields to reorder deletes */) {
            return super.onUnfetchedAttribute(entity, null);
        }

        String entityDescriptor = entity.getClass().getName() + "-" + ((BaseGenericIdEntity) entity).getId();
        throw new IllegalEntityStateException(ExceptionLocalization.buildMessage("cannot_get_unfetched_attribute", new Object[]{entityDescriptor, attributeName}));
    }
}