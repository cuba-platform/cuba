/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Component(PersistentAttributesLoadChecker.NAME)
public class GlobalPersistentAttributesLoadChecker implements PersistentAttributesLoadChecker {
    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected Metadata metadata;

    @Override
    public boolean isLoaded(Object entity, String property) {
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        MetaProperty metaProperty = metaClass.getPropertyNN(property);

        if (!metadataTools.isPersistent(metaProperty)) {
            List<String> relatedProperties = metadataTools.getRelatedProperties(metaProperty);
            if (relatedProperties.isEmpty())
                return true;
            else {
                for (String relatedProperty : relatedProperties) {
                    if (!isLoaded(entity, relatedProperty))
                        return false;
                }
                return true;
            }
        }

        Boolean isLoaded = isLoadedCommonCheck(entity, property);
        if (isLoaded != null) {
            return isLoaded;
        }

        return isLoadedSpecificCheck(entity, property, metaClass, metaProperty);
    }

    protected Boolean isLoadedCommonCheck(Object entity, String property) {
        if (entity instanceof BaseGenericIdEntity) {
            BaseGenericIdEntity baseGenericIdEntity = (BaseGenericIdEntity) entity;

            if (baseGenericIdEntity.__inaccessibleAttributes() != null) {
                for (String inaccessibleAttr : baseGenericIdEntity.__inaccessibleAttributes()) {
                    if (inaccessibleAttr.equals(property))
                        return false;
                }
            }
        }

        if (entity instanceof FetchGroupTracker) {
            FetchGroup fetchGroup = ((FetchGroupTracker) entity)._persistence_getFetchGroup();
            if (fetchGroup != null)
                return fetchGroup.getAttributeNames().contains(property);
        }

        return null;
    }

    protected boolean isLoadedSpecificCheck(Object entity, String property, MetaClass metaClass, MetaProperty metaProperty) {
        return checkIsLoadedWithGetter(entity, property);
    }

    protected boolean checkIsLoadedWithGetter(Object entity, String property) {
        if (entity instanceof Instance) {
            try {
                Object value = ((Instance) entity).getValue(property);
                if (value instanceof Collection) {//check for IndirectCollection behaviour, should fail if property is not loaded
                    ((Collection) value).size();
                }
                return true;
            } catch (Exception ignored) {
                return false;
            }
        } else {
            throw new IllegalArgumentException("Unable to check if the attribute is loaded: the entity is of unknown type");
        }
    }
}
