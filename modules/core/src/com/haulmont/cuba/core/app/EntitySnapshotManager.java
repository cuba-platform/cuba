/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.EntityDiff;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@ManagedBean(EntitySnapshotAPI.NAME)
@SuppressWarnings({"unchecked"})
public class EntitySnapshotManager implements EntitySnapshotAPI {

    @Inject
    private Persistence persistence;

    private EntityDiffManager diffManager;

    public EntitySnapshotManager() {
        diffManager = new EntityDiffManager(this);
    }

    public List<EntitySnapshot> getSnapshots(MetaClass metaClass, UUID id) {
        List<EntitySnapshot> resultList = null;

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            Query query = em.createQuery(
                    "select s from core$EntitySnapshot s where s.entityId = :entityId and s.entityMetaClass = :metaClass");
            query.setParameter("entityId", id);
            query.setParameter("metaClass", metaClass.getName());
            resultList = query.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }

        return resultList;
    }

    @Override
    public void migrateSnapshots(MetaClass metaClass, UUID id, Map<Class, Class> classMapping) {
        // load snapshots
        List<EntitySnapshot> snapshotList = getSnapshots(metaClass, id);
        // translate XML
        for (EntitySnapshot snapshot : snapshotList) {

        }
        // Save snapshots to db
    }

    public EntitySnapshot createSnapshot(BaseEntity entity, View view) {

        if (entity == null)
            throw new NullPointerException("Could not be create snapshot for null entity");

        if (view == null)
            throw new NullPointerException("Could not be create snapshot for entity with null view");

        Class viewEntityClass = view.getEntityClass();
        Class entityClass = entity.getClass();

        if (!viewEntityClass.isAssignableFrom(entityClass))
            throw new IllegalStateException("View could not be used with this propertyValue");

        EntitySnapshot snapshot = new EntitySnapshot();
        snapshot.setEntityId(entity.getUuid());

        MetaClass metaClass = MetadataProvider.getSession().getClass(entity.getClass());
        snapshot.setEntityMetaClass(metaClass.getName());

        snapshot.setViewXml(toXML(view));
        snapshot.setSnapshotXml(toXML(entity));

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
            em.persist(snapshot);

            tx.commit();
        } finally {
            tx.end();
        }

        return snapshot;
    }

    public BaseEntity extractEntity(EntitySnapshot snapshot) {
        String xml = snapshot.getSnapshotXml();
        return (BaseUuidEntity) fromXML(xml);
    }

    public View extractView(EntitySnapshot snapshot) {
        String xml = snapshot.getViewXml();
        return (View) fromXML(xml);
    }

    public EntityDiff getDifference(EntitySnapshot first, EntitySnapshot second) {
        return diffManager.getDifference(first, second);
    }

    private Object fromXML(String xml) {
        XStream xStream = new XStream();
        xStream.getConverterRegistry().removeConverter(ExternalizableConverter.class);
        return xStream.fromXML(xml);
    }

    private String toXML(Object obj) {
        XStream xStream = new XStream();
        xStream.getConverterRegistry().removeConverter(ExternalizableConverter.class);
        return xStream.toXML(obj);
    }
}