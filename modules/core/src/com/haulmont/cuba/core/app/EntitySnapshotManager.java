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

package com.haulmont.cuba.core.app;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.ViewSerializationAPI;
import com.haulmont.cuba.core.app.serialization.ViewSerializationOption;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.diff.EntityDiff;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.dom4j.*;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Component(EntitySnapshotAPI.NAME)
public class EntitySnapshotManager implements EntitySnapshotAPI {

    @Inject
    protected Persistence persistence;

    @Inject
    protected Metadata metadata;

    @Inject
    protected EntityDiffManager diffManager;

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    @Inject
    protected ViewSerializationAPI viewSerializationAPI;

    @Override
    public List<EntitySnapshot> getSnapshots(MetaClass metaClass, UUID id) {
        metaClass = getOriginalOrCurrentMetaClass(metaClass);

        List<EntitySnapshot> resultList = null;
        View view = metadata.getViewRepository().getView(EntitySnapshot.class, "entitySnapshot.browse");
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<EntitySnapshot> query = em.createQuery(
                    "select s from sys$EntitySnapshot s where s.entityId = :entityId and s.entityMetaClass = :metaClass " +
                            "order by s.snapshotDate desc", EntitySnapshot.class);
            query.setParameter("entityId", id);
            query.setParameter("metaClass", metaClass.getName());
            query.setView(view);
            resultList = query.getResultList();

            tx.commit();
        } finally {
            tx.end();
        }

        return resultList;
    }

    private void replaceInXmlTree(Element element, Map<Class, Class> classMapping) {
        for (int i = 0; i < element.nodeCount(); i++) {
            Node node = element.node(i);
            if (node instanceof Element) {
                Element childElement = (Element) node;
                replaceClasses(childElement, classMapping);
                replaceInXmlTree(childElement, classMapping);
            }
        }
    }

    private void replaceClasses(Element element, Map<Class, Class> classMapping) {
        // translate XML
        for (Map.Entry<Class, Class> classEntry : classMapping.entrySet()) {
            Class beforeClass = classEntry.getKey();
            Class afterClass = classEntry.getValue();

            checkNotNull(beforeClass);
            checkNotNull(afterClass);

            // If BeforeClass != AfterClass
            if (!beforeClass.equals(afterClass)) {
                String beforeClassName = beforeClass.getCanonicalName();
                String afterClassName = afterClass.getCanonicalName();

                if (beforeClassName.equals(element.getName())) {
                    element.setName(afterClassName);
                }

                Attribute classAttribute = element.attribute("class");
                if ((classAttribute != null) && beforeClassName.equals(classAttribute.getValue())) {
                    classAttribute.setValue(afterClassName);
                }
            }
        }
    }

    private String processViewXml(String viewXml, Map<Class, Class> classMapping) {
        if (!isXml(viewXml)) {
            return viewXml;
        }
        for (Map.Entry<Class, Class> classEntry : classMapping.entrySet()) {
            Class beforeClass = classEntry.getKey();
            Class afterClass = classEntry.getValue();

            checkNotNull(beforeClass);
            checkNotNull(afterClass);

            String beforeClassName = beforeClass.getCanonicalName();
            String afterClassName = afterClass.getCanonicalName();

            viewXml = viewXml.replaceAll(beforeClassName, afterClassName);
        }
        return viewXml;
    }

    private String processSnapshotXml(String snapshotXml, Map<Class, Class> classMapping) {
        if (!isXml(snapshotXml)) {
            return snapshotXml;
        }
        Document document;
        try {
            document = DocumentHelper.parseText(snapshotXml);
        } catch (DocumentException e) {
            throw new RuntimeException("Couldn't parse snapshot xml content", e);
        }
        replaceClasses(document.getRootElement(), classMapping);
        replaceInXmlTree(document.getRootElement(), classMapping);
        return document.asXML();
    }

    @Override
    public void migrateSnapshots(MetaClass metaClass, UUID id, Map<Class, Class> classMapping) {
        metaClass = getOriginalOrCurrentMetaClass(metaClass);

        // load snapshots
        List<EntitySnapshot> snapshotList = getSnapshots(metaClass, id);
        Class javaClass = metaClass.getJavaClass();

        MetaClass mappedMetaClass = null;
        if (classMapping.containsKey(javaClass)) {
            Class mappedClass = classMapping.get(javaClass);
            mappedMetaClass = getOriginalOrCurrentMetaClass(mappedClass);
        }

        for (EntitySnapshot snapshot : snapshotList) {
            if (mappedMetaClass != null) {
                snapshot.setEntityMetaClass(mappedMetaClass.getName());
            }

            String snapshotXml = snapshot.getSnapshotXml();
            String viewXml = snapshot.getViewXml();

            snapshot.setSnapshotXml(processSnapshotXml(snapshotXml, classMapping));
            snapshot.setViewXml(processViewXml(viewXml, classMapping));
        }

        // Save snapshots to db
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            for (EntitySnapshot snapshot : snapshotList) {
                em.merge(snapshot);
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    public EntitySnapshot createSnapshot(Entity entity, View view) {
        return createSnapshot(entity, view, timeSource.currentTimestamp());
    }

    @Override
    public EntitySnapshot createSnapshot(Entity entity, View view, Date snapshotDate) {
        User user = userSessionSource.getUserSession().getUser();
        return createSnapshot(entity, view, snapshotDate, user);
    }

    @Override
    public EntitySnapshot createSnapshot(Entity entity, View view, Date snapshotDate, User author) {
        if (!(entity instanceof HasUuid))
            throw new UnsupportedOperationException("Entity " + entity + " has no persistent UUID attribute");

        Preconditions.checkNotNullArgument(entity);
        Preconditions.checkNotNullArgument(view);
        Preconditions.checkNotNullArgument(snapshotDate);

        Class viewEntityClass = view.getEntityClass();
        Class entityClass = entity.getClass();

        if (!viewEntityClass.isAssignableFrom(entityClass)) {
            throw new IllegalStateException("View could not be used with this propertyValue");
        }

        EntitySnapshot snapshot = metadata.create(EntitySnapshot.class);
        snapshot.setEntityId(((HasUuid) entity).getUuid());

        MetaClass metaClass = getOriginalOrCurrentMetaClass(entity.getClass());

        snapshot.setEntityMetaClass(metaClass.getName());
        snapshot.setViewXml(viewSerializationAPI.toJson(view, ViewSerializationOption.COMPACT_FORMAT));
        snapshot.setSnapshotXml(entitySerializationAPI.toJson(entity));
        snapshot.setSnapshotDate(snapshotDate);
        snapshot.setAuthor(author);

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            em.persist(snapshot);

            tx.commit();
        } finally {
            tx.end();
        }

        return snapshot;
    }

    @Override
    public Entity extractEntity(EntitySnapshot snapshot) {
        String rawResult = snapshot.getSnapshotXml();
        BaseUuidEntity entity;
        if (isXml(rawResult)) {
            entity = (BaseUuidEntity) fromXML(snapshot.getSnapshotXml());
        } else {
            entity = entitySerializationAPI.entityFromJson(rawResult, metadata.getClass(snapshot.getEntityMetaClass()));
        }
        return entity;
    }

    @Override
    public View extractView(EntitySnapshot snapshot) {
        String rawResult = snapshot.getViewXml();
        View view;
        if (isXml(rawResult)) {
            view = (View) fromXML(rawResult);
        } else {
            view = viewSerializationAPI.fromJson(rawResult);
        }
        return view;
    }

    @Override
    public EntityDiff getDifference(@Nullable EntitySnapshot first, EntitySnapshot second) {
        return diffManager.getDifference(first, second);
    }

    private MetaClass getOriginalOrCurrentMetaClass(Class javaClass) {
        MetaClass metaClass = metadata.getSession().getClass(javaClass);
        return getOriginalOrCurrentMetaClass(metaClass);
    }

    private MetaClass getOriginalOrCurrentMetaClass(MetaClass mappedMetaClass) {
        MetaClass originalMappedMetaClass = extendedEntities.getOriginalMetaClass(mappedMetaClass);
        if (originalMappedMetaClass != null) {
            mappedMetaClass = originalMappedMetaClass;
        }
        return mappedMetaClass;
    }

    private Object fromXML(String xml) {
        final List exclUpdateFields = Arrays.asList("updateTs", "updatedBy");
        XStream xStream = new XStream() {
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        boolean result = super.shouldSerializeMember(definedIn, fieldName);
                        if (!result) {
                            return false;
                        }
                        if (fieldName != null) {
                            if (exclUpdateFields.contains(fieldName)
                                    && Updatable.class.isAssignableFrom(definedIn)) {
                                return false;
                            }
                            if ("uuid".equals(fieldName)) {
                                if (!HasUuid.class.isAssignableFrom(definedIn)
                                        && BaseGenericIdEntity.class.isAssignableFrom(definedIn)) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }
                };
            }
        };
        xStream.getConverterRegistry().removeConverter(ExternalizableConverter.class);
        xStream.omitField(BaseGenericIdEntity.class, "createTs");
        xStream.omitField(BaseGenericIdEntity.class, "createdBy");

        return xStream.fromXML(xml);
    }

    protected boolean isXml(String value) {
        return value != null && value.trim().startsWith("<");
    }
}