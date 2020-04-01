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

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaEnhanced;
import com.haulmont.cuba.core.sys.CubaEnhancingDisabled;
import com.haulmont.cuba.core.sys.UuidConverter;
import com.haulmont.cuba.core.sys.persistence.mapping.processors.JoinExpressionProvider;
import com.haulmont.cuba.core.sys.persistence.mapping.processors.MappingProcessor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.config.CacheIsolationType;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEventListener;
import org.eclipse.persistence.descriptors.InheritancePolicy;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.internal.descriptors.PersistenceObject;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.weaving.PersistenceWeaved;
import org.eclipse.persistence.internal.weaving.PersistenceWeavedFetchGroups;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.platform.database.HSQLPlatform;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.eclipse.persistence.platform.database.OraclePlatform;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.platform.database.SQLServerPlatform;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

        Map<String, ClassDescriptor> mappedSuperclassDescriptorMap = session.getProject().getMappedSuperclassDescriptors();
        for (Map.Entry<String, ClassDescriptor> entry : mappedSuperclassDescriptorMap.entrySet()) {
            try {
                Class javaClass = getClass().getClassLoader().loadClass(entry.getKey());
                enhancementCheck(javaClass, missingEnhancements);
            } catch (ClassNotFoundException e) {
                log.warn("MappedSuperclass {} was not found by ClassLoader", entry.getKey());
            }
        }

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

            setAdditionalCriteria(desc);

            if (SoftDelete.class.isAssignableFrom(desc.getJavaClass())) {
                desc.setDeletePredicate(entity -> entity instanceof SoftDelete &&
                        PersistenceHelper.isLoaded(entity, "deleteTs") &&
                        ((SoftDelete) entity).isDeleted());
            }

            for (DatabaseMapping mapping : desc.getMappings()) {

                //Fetch type check
                fetchTypeCheck(mapping, entry.getKey(), wrongFetchTypes);

                //TODO support UUID in DatabasePlatform definition, see @setDatabaseFieldParameters method.
                addUuidSupport(session, metaClass, mapping);

                //Applying additional join criteria, e.g. for soft delete or multitenancy
                if (mapping.isOneToManyMapping() || mapping.isOneToOneMapping()) {

                    //Collect all join expressions from all providers
                    Expression expression = AppBeans.getAll(JoinExpressionProvider.class)
                            .values().stream()
                            .map(provider -> provider.getJoinCriteriaExpression(mapping))
                            .filter(Objects::nonNull)
                            .reduce(Expression::and).orElse(null);

                    //Apply expression to mappings
                    //TODO Generalize this - extract @setAdditionalJoinCriteria method to a common interface, joinCriteria should be collection
                    if (mapping.isOneToManyMapping()) {
                        ((OneToManyMapping) mapping).setAdditionalJoinCriteria(expression);
                    } else if (mapping.isOneToOneMapping()) {
                        ((OneToOneMapping) mapping).setAdditionalJoinCriteria(expression);
                    }
                }

                //Adding all nessesary updates for mappings. Used for SoftDelete, add-ons can append their modifications.
                Map<String, MappingProcessor> mappingProcessors = AppBeans.getAll(MappingProcessor.class);
                for (MappingProcessor processor : mappingProcessors.values()) {
                    processor.process(mapping);
                }
            }
        }
        logCheckResult(wrongFetchTypes, missingEnhancements);
    }

    private void addUuidSupport(Session session, MetaClass metaClass, DatabaseMapping mapping) {
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
    }

    private void setAdditionalCriteria(ClassDescriptor desc) {
        Map<String, AdditionalCriteriaProvider> additionalCriteriaProviderMap = AppBeans.getAll(AdditionalCriteriaProvider.class);

        StringBuilder criteriaBuilder = new StringBuilder();
        additionalCriteriaProviderMap.values().stream()
                .filter(item -> item.requiresAdditionalCriteria(desc.getJavaClass()))
                .forEach(additionalCriteriaProvider ->
                        criteriaBuilder.append(additionalCriteriaProvider.getAdditionalCriteria(desc.getJavaClass())).append(" AND")
                );

        if (criteriaBuilder.length() != 0) {
            String additionalCriteriaResult = criteriaBuilder.substring(0, criteriaBuilder.length() - 4);
            desc.getQueryManager().setAdditionalCriteria(additionalCriteriaResult);
        }
    }

    protected void enhancementCheck(Class entityClass, List<Pair<Class, String>> missingEnhancements) {
        boolean cubaEnhanced = ArrayUtils.contains(entityClass.getInterfaces(), CubaEnhanced.class)
                || !(AbstractInstance.class.isAssignableFrom(entityClass))
                || ArrayUtils.contains(entityClass.getInterfaces(), CubaEnhancingDisabled.class);
        boolean persistenceObject = ArrayUtils.contains(entityClass.getInterfaces(), PersistenceObject.class);
        boolean persistenceWeaved = ArrayUtils.contains(entityClass.getInterfaces(), PersistenceWeaved.class);
        boolean persistenceWeavedFetchGroups = ArrayUtils.contains(entityClass.getInterfaces(), PersistenceWeavedFetchGroups.class);
        if (!cubaEnhanced || !persistenceObject || !persistenceWeaved || !persistenceWeavedFetchGroups) {
            String message = String.format("Entity class %s is missing some of enhancing interfaces:%s%s%s%s",
                    entityClass.getSimpleName(),
                    cubaEnhanced ? "" : " CubaEnhanced;",
                    persistenceObject ? "" : " PersistenceObject;",
                    persistenceWeaved ? "" : " PersistenceWeaved;",
                    persistenceWeavedFetchGroups ? "" : " PersistenceWeavedFetchGroups;");
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
            if (!Boolean.parseBoolean(AppContext.getProperty("cuba.disableEntityEnhancementCheck"))) {
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
            desc.setHasMultipleTableConstraintDependecy(true);
        }
    }

    private boolean hasMultipleTableConstraintDependency() {
        String value = AppContext.getProperty("cuba.hasMultipleTableConstraintDependency");
        return value == null || BooleanUtils.toBoolean(value);
    }

    //TODO create ticket for this - move to DatabasePlatform definition
    private void setDatabaseFieldParameters(Session session, DatabaseField field) {
        if (session.getPlatform() instanceof PostgreSQLPlatform) {
            field.setSqlType(Types.OTHER);
            field.setType(UUID.class);
            field.setColumnDefinition("UUID");
        } else if (session.getPlatform() instanceof MySQLPlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("varchar(32)");
        } else if (session.getPlatform() instanceof HSQLPlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("varchar(36)");
        } else if (session.getPlatform() instanceof SQLServerPlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("uniqueidentifier");
        } else if (session.getPlatform() instanceof OraclePlatform) {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
            field.setColumnDefinition("varchar2(32)");
        } else {
            field.setSqlType(Types.VARCHAR);
            field.setType(String.class);
        }
    }

    private void setPrintInnerJoinOnClause(Session session) {
        boolean useInnerJoinOnClause = BooleanUtils.toBoolean(
                AppContext.getProperty("cuba.useInnerJoinOnClause"));
        session.getPlatform().setPrintInnerJoinInWhereClause(!useInnerJoinOnClause);
    }
}
