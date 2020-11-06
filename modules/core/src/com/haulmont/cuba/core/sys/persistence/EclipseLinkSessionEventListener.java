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
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.CubaEnhanced;
import com.haulmont.cuba.core.sys.CubaEnhancingDisabled;
import com.haulmont.cuba.core.sys.persistence.mapping.MappingProcessor;
import com.haulmont.cuba.core.sys.persistence.mapping.MappingProcessorContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.config.CacheIsolationType;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorEventListener;
import org.eclipse.persistence.descriptors.InheritancePolicy;
import org.eclipse.persistence.internal.descriptors.PersistenceObject;
import org.eclipse.persistence.internal.weaving.PersistenceWeaved;
import org.eclipse.persistence.internal.weaving.PersistenceWeavedFetchGroups;
import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EclipseLinkSessionEventListener extends SessionEventAdapter {

    private static final Logger log = LoggerFactory.getLogger(EclipseLinkSessionEventListener.class);

    private Metadata metadata = AppBeans.get(Metadata.NAME);

    private DescriptorEventListener descriptorEventListener = AppBeans.get(EclipseLinkDescriptorEventListener.NAME);

    @Override
    public void preLogin(SessionEvent event) {

        Session session = event.getSession();

        boolean useJoinSubclasses = useJoinSubclasses();
        boolean hasMultipleTableConstraintDependency = hasMultipleTableConstraintDependency();

        setPrintInnerJoinOnClause(session);
        if (useJoinSubclasses) {
            setJoinSubclasses(session);
        }

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
        for (Map.Entry<Class, ClassDescriptor> entry : descriptorMap.entrySet()) {
            MetaClass metaClass = metadata.getSession().getClassNN(entry.getKey());
            ClassDescriptor desc = entry.getValue();

            enhancementCheck(entry.getKey(), missingEnhancements);

            setCacheable(metaClass, desc, session);

            if (hasMultipleTableConstraintDependency) {
                setMultipleTableConstraintDependency(metaClass, desc);
            }

            if (useJoinSubclasses) {
                setJoinSubclasses(metaClass, desc);
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

            Map<String, MappingProcessor> mappingProcessors = AppBeans.getAll(MappingProcessor.class);

            for (DatabaseMapping mapping : desc.getMappings()) {
                MappingProcessorContext mappingProcessorCtx = new MappingProcessorContext(mapping, session);
                for (MappingProcessor mp : mappingProcessors.values()) {
                    log.debug("{} mapping processor is started", mp.getClass());
                    mp.process(mappingProcessorCtx);
                    log.debug("{} mapping processor is finished", mp.getClass());
                }
            }
        }
        logCheckResult(missingEnhancements);
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

    protected void logCheckResult(List<Pair<Class, String>> missingEnhancements) {
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

    private boolean useJoinSubclasses() {
        String value = AppContext.getProperty("cuba.useJoinSubclasses");
        return value != null && BooleanUtils.toBoolean(value);
    }

    private void setJoinSubclasses(MetaClass metaClass, ClassDescriptor desc) {
        if (desc.hasInheritance()) {
            desc.getInheritancePolicy().setShouldOuterJoinSubclasses(true);
        }
    }

    private void setJoinSubclasses(Session session) {
        session.getPlatform().setPrintInheritanceTableJoinsInFromClause(true);
    }

    private void setPrintInnerJoinOnClause(Session session) {
        boolean useInnerJoinOnClause = BooleanUtils.toBoolean(
                AppContext.getProperty("cuba.useInnerJoinOnClause"));
        session.getPlatform().setPrintInnerJoinInWhereClause(!useInnerJoinOnClause);
    }
}
