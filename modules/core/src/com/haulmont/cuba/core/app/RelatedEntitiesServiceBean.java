/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author artamonov
 * @version $Id$
 */
@Service(RelatedEntitiesService.NAME)
public class RelatedEntitiesServiceBean implements RelatedEntitiesService {

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected ExtendedEntities extendedEntities;

    @SuppressWarnings("unchecked")
    @Override
    public List<UUID> getRelatedIds(List<UUID> parents, String parentMetaClass, String relationProperty) {
        checkNotNullArgument(parents, "Please specify parents set");
        checkNotNullArgument(parentMetaClass, "Please specify parent meta class");
        checkNotNullArgument(relationProperty, "Please specify relation property");

        MetaClass metaClass = extendedEntities.getEffectiveMetaClass(metadata.getClass(parentMetaClass));
        checkNotNullArgument(metaClass, "Could not find meta class '%s'", parentMetaClass);

        Class parentClass = metaClass.getJavaClass();

        MetaProperty metaProperty = metaClass.getProperty(relationProperty);
        checkNotNullArgument(metaClass,
                "Could not find meta property '%s' for meta class '%s'", relationProperty, parentMetaClass);

        // return empty list only after all argument checks
        if (parents.isEmpty()) {
            return Collections.emptyList();
        }

        MetaClass propertyMetaClass = extendedEntities.getEffectiveMetaClass(metaProperty.getRange().asClass());
        Class propertyClass = propertyMetaClass.getJavaClass();

        List<UUID> relatedIds = new ArrayList<>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            String queryString = "select x from " + parentMetaClass + " x where x.id in :ids";
            TypedQuery query = em.createQuery(queryString, parentClass);

            View view = new View(parentClass);
            view.addProperty(relationProperty, new View(propertyClass).addProperty("id"));

            query.setView(view);
            query.setParameter("ids", parents);

            List<Entity> resultList = query.getResultList();
            for (Entity e : resultList) {
                Object value = e.getValue(relationProperty);
                if (value instanceof BaseUuidEntity) {
                    relatedIds.add(((BaseUuidEntity) value).getId());
                } else if (value instanceof Collection) {
                    for (Object collectionItem : (Collection)value) {
                        relatedIds.add(((BaseUuidEntity) collectionItem).getId());
                    }
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }

        return relatedIds;
    }
}