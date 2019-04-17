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

import com.google.common.base.Strings;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaEnhanced;
import com.haulmont.cuba.core.sys.UuidConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.config.CacheIsolationType;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEventListener;
import org.eclipse.persistence.descriptors.InheritancePolicy;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.internal.descriptors.PersistenceObject;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.weaving.PersistenceWeaved;
import org.eclipse.persistence.internal.weaving.PersistenceWeavedChangeTracking;
import org.eclipse.persistence.internal.weaving.PersistenceWeavedFetchGroups;
import org.eclipse.persistence.mappings.*;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.OneToOne;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EclipseLinkSessionEventListener extends SessionEventAdapter {

    private static final Logger log = LoggerFactory.getLogger(EclipseLinkSessionEventListener.class);

    private Metadata metadata = AppBeans.get(Metadata.NAME);

    private DescriptorEventListener descriptorEventListener = AppBeans.get(EclipseLinkDescriptorEventListener.NAME);

    @Override
    public void preLogin(SessionEvent event) {

        Session session = event.getSession();
        setPrintInnerJoinOnClause(session);

        List<String> wrongFetchTypes = new ArrayList<>();
        List<Pair<Class, String>> missingEnhancements = new ArrayList<>();

        Map<Class, ClassDescriptor> descriptorMap = session.getDescriptors();
        boolean hasMultipleTableConstraintDependency = hasMultipleTableConstraintDependency();
        for (Map.Entry<Class, ClassDescriptor> entry : descriptorMap.entrySet()) {
            MetaClass metaClass = metadata.getSession().getClassNN(entry.getKey());
            ClassDescriptor desc = entry.getValue();

            enhancementCheck(entry.getKey(), missingEnhancements);

            setCacheable(metaClass, desc, session);

            if (hasMultipleTableConstraintDependency) {
                setMultipleTableConstraintDependency(metaClass, desc);
            }

            if (Entity.class.isAssignableFrom(desc.getJavaClass())) {
                // set DescriptorEventManager that doesn't invoke listeners for base classes
                desc.setEventManager(new DescriptorEventManagerWrapper(desc.getDescriptorEventManager()));
                desc.getEventManager().addListener(descriptorEventListener);
            }

            if (SoftDelete.class.isAssignableFrom(desc.getJavaClass())) {
                desc.getQueryManager().setAdditionalCriteria("this.deleteTs is null");
                desc.setDeletePredicate(entity -> entity instanceof SoftDelete &&
                        PersistenceHelper.isLoaded(entity, "deleteTs") &&
                        ((SoftDelete) entity).isDeleted());
            }

            List<DatabaseMapping> mappings = desc.getMappings();
            for (DatabaseMapping mapping : mappings) {

                //Fetch type check
                fetchTypeCheck(mapping, entry.getKey(), wrongFetchTypes);

                // support UUID
                String attributeName = mapping.getAttributeName();
                MetaProperty metaProperty = metaClass.getPropertyNN(attributeName);
                if (metaProperty.getRange().isDatatype()) {
                    if (metaProperty.getJavaType().equals(UUID.class)) {
                        ((DirectToFieldMapping) mapping).setConverter(UuidConverter.getInstance());
                        setDatabaseFieldParameters(session, mapping.getField());
                    }
                } else if (metaProperty.getRange().isClass() && !metaProperty.getRange().getCardinality().isMany()) {
                    MetaClass refMetaClass = metaProperty.getRange().asClass();
                    MetaProperty refPkProperty = metadata.getTools().getPrimaryKeyProperty(refMetaClass);
                    if (refPkProperty != null && refPkProperty.getJavaType().equals(UUID.class)) {
                        for (DatabaseField field : ((OneToOneMapping) mapping).getForeignKeyFields()) {
                            setDatabaseFieldParameters(session, field);
                        }
                    }
                }
                // embedded attributes
                if (mapping instanceof AggregateObjectMapping) {
                    EmbeddedParameters embeddedParameters =
                            metaProperty.getAnnotatedElement().getAnnotation(EmbeddedParameters.class);
                    if (embeddedParameters != null && !embeddedParameters.nullAllowed())
                        ((AggregateObjectMapping) mapping).setIsNullAllowed(false);
                }

                if (mapping.isOneToManyMapping()) {
                    OneToManyMapping oneToManyMapping = (OneToManyMapping) mapping;
                    if (SoftDelete.class.isAssignableFrom(oneToManyMapping.getReferenceClass())) {
                        oneToManyMapping.setAdditionalJoinCriteria(new ExpressionBuilder().get("deleteTs").isNull());
                    }
                }

                if (mapping.isOneToOneMapping()) {
                    OneToOneMapping oneToOneMapping = (OneToOneMapping) mapping;
                    if (SoftDelete.class.isAssignableFrom(oneToOneMapping.getReferenceClass())) {
                        if (mapping.isManyToOneMapping()) {
                            oneToOneMapping.setSoftDeletionForBatch(false);
                            oneToOneMapping.setSoftDeletionForValueHolder(false);
                        } else {
                            OneToOne oneToOne = metaProperty.getAnnotatedElement().getAnnotation(OneToOne.class);
                            if (oneToOne != null) {
                                if (Strings.isNullOrEmpty(oneToOne.mappedBy())) {
                                    oneToOneMapping.setSoftDeletionForBatch(false);
                                    oneToOneMapping.setSoftDeletionForValueHolder(false);
                                } else {
                                    oneToOneMapping.setAdditionalJoinCriteria(
                                            new ExpressionBuilder().get("deleteTs").isNull());
                                }
                            }
                        }
                    }
                }
            }
        }
        logCheckResult(wrongFetchTypes, missingEnhancements);
    }

    protected void enhancementCheck(Class entityClass, List<Pair<Class, String>> missingEnhancements) {
        boolean cubaEnhanced = ArrayUtils.contains(entityClass.getInterfaces(), CubaEnhanced.class);
        boolean persistenceObject = ArrayUtils.contains(entityClass.getInterfaces(), PersistenceObject.class);
        boolean persistenceWeaved = ArrayUtils.contains(entityClass.getInterfaces(), PersistenceWeaved.class);
        boolean persistenceWeavedFetchGroups = ArrayUtils.contains(entityClass.getInterfaces(), PersistenceWeavedFetchGroups.class);
        boolean persistenceWeavedChangeTracking = ArrayUtils.contains(entityClass.getInterfaces(), PersistenceWeavedChangeTracking.class);
        if (!cubaEnhanced || !persistenceObject || !persistenceWeaved || !persistenceWeavedFetchGroups
                || !persistenceWeavedChangeTracking) {
            String message = String.format("Entity class %s is missing some of enhancing interfaces:%s%s%s%s%s",
                    entityClass.getSimpleName(),
                    cubaEnhanced ? "" : " CubaEnhanced;",
                    persistenceObject ? "" : " PersistenceObject;",
                    persistenceWeaved ? "" : " PersistenceWeaved;",
                    persistenceWeavedFetchGroups ? "" : " PersistenceWeavedFetchGroups;",
                    persistenceWeavedChangeTracking ? "" : " PersistenceWeavedChangeTracking;");
            missingEnhancements.add(new Pair<>(entityClass, message));
        }
    }

    protected void fetchTypeCheck(DatabaseMapping mapping, Class entityClass, List<String> wrongFetchTypes) {
        if ((mapping.isOneToOneMapping() || mapping.isOneToManyMapping()
                || mapping.isManyToOneMapping() || mapping.isManyToManyMapping())) {
            if (!mapping.isLazy()) {
                mapping.setIsLazy(true);
                wrongFetchTypes.add(String.format("EAGER fetch type detected for reference field %s of entity %s; Set to LAZY",
                        mapping.getAttributeName(), entityClass.getSimpleName()));
            }
        } else {
            if (mapping.isLazy()) {
                mapping.setIsLazy(false);
                wrongFetchTypes.add(String.format("LAZY fetch type detected for basic field %s of entity %s; Set to EAGER",
                        mapping.getAttributeName(), entityClass.getSimpleName()));
            }
        }
    }

    protected void logCheckResult(List<String> wrongFetchTypes, List<Pair<Class, String>> missingEnhancements) {
        if (!wrongFetchTypes.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("\n=================================================================");
            message.append("\nIncorrectly defined fetch types detected:");
            for (String wft : wrongFetchTypes) {
                message.append("\n");
                message.append(wft);
            }
            message.append("\n=================================================================");
            log.warn(message.toString());
        }
        if (!missingEnhancements.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("\n=================================================================");
            message.append("\nProblems with entity enhancement detected:");
            for (Pair me : missingEnhancements) {
                message.append("\n");
                message.append(me.getSecond());
            }
            message.append("\n=================================================================");
            log.error(message.toString());
            if (!Boolean.parseBoolean(AppContext.getProperty("cuba.disableEnhancementChecks"))) {
                StringBuilder exceptionMessage = new StringBuilder();
                for (Pair me : missingEnhancements) {
                    exceptionMessage.append(me.getFirst());
                    exceptionMessage.append("; ");
                }
                throw new EntityNotEnhancedException(exceptionMessage.toString());
            }
        }
    }

    private void setCacheable(MetaClass metaClass, ClassDescriptor desc, Session session) {
        String property = (String) session.getProperty("eclipselink.cache.shared.default");
        boolean defaultCache = property == null || Boolean.valueOf(property);

        if ((defaultCache && !desc.isIsolated())
                || desc.getCacheIsolation() == CacheIsolationType.SHARED
                || desc.getCacheIsolation() == CacheIsolationType.PROTECTED) {
            metaClass.getAnnotations().put("cacheable", true);
            desc.getCachePolicy().setCacheCoordinationType(CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS);
        }
    }

    private void setMultipleTableConstraintDependency(MetaClass metaClass, ClassDescriptor desc) {
        InheritancePolicy policy = desc.getInheritancePolicyOrNull();
        if (policy != null && policy.isJoinedStrategy() && policy.getParentClass() != null) {
            boolean hasOneToMany = metaClass.getOwnProperties().stream().anyMatch(metaProperty ->
                    metadata.getTools().isPersistent(metaProperty)
                            && metaProperty.getRange().isClass()
                            && metaProperty.getRange().getCardinality() == Range.Cardinality.ONE_TO_MANY);
            if (hasOneToMany) {
                desc.setHasMultipleTableConstraintDependecy(true);
            }
        }
    }

    private boolean hasMultipleTableConstraintDependency() {
        return BooleanUtils.toBoolean(
                AppContext.getProperty("cuba.hasMultipleTableConstraintDependency"));
    }

    private void setDatabaseFieldParameters(Session session, DatabaseField field) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            field.setSqlType(Types.OTHER);
            field.setType(UUID.class);
        } else {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
        }
        field.setColumnDefinition("UUID");
    }

    private void setPrintInnerJoinOnClause(Session session) {
        boolean useInnerJoinOnClause = BooleanUtils.toBoolean(
                AppContext.getProperty("cuba.useInnerJoinOnClause"));
        session.getPlatform().setPrintInnerJoinInWhereClause(!useInnerJoinOnClause);
    }
}