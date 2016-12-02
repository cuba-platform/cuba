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
 */

package com.haulmont.cuba.core.app;

import com.google.common.collect.Sets;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.core.global.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * INTERNAL.
 * Populates references to entities from different data stores.
 */
@Component(AlienAttributesLoader.NAME)
@Scope("prototype")
public class AlienAttributesLoader {

    public static final String NAME = "cuba_AlienAttributesLoader";

    private Logger log = LoggerFactory.getLogger(AlienAttributesLoader.class);

    @Inject
    private Metadata metadata;

    @Inject
    private MetadataTools metadataTools;

    @Inject
    private DataManager dataManager;

    @Inject
    private ServerConfig serverConfig;

    private MetaClass metaClass;

    private View view;

    public AlienAttributesLoader(MetaClass metaClass, View view) {
        Preconditions.checkNotNullArgument(metaClass, "metaClass is null");
        Preconditions.checkNotNullArgument(view, "view is null");
        this.metaClass = metaClass;
        this.view = view;
    }

    public Map<Class<? extends Entity>, List<AlienProperty>> getAlienPropertiesMap() {
        Map<Class<? extends Entity>, List<AlienProperty>> alienPropertiesMap = new HashMap<>();
        traverseView(view, alienPropertiesMap, Sets.newIdentityHashSet());
        return alienPropertiesMap;
    }

    private void traverseView(View view, Map<Class<? extends Entity>, List<AlienProperty>> alienPropertiesMap, Set<View> visited) {
        if (visited.contains(view))
            return;
        visited.add(view);

        String storeName = metadataTools.getStoreName(metaClass);

        Class<? extends Entity> entityClass = view.getEntityClass();
        for (ViewProperty viewProperty : view.getProperties()) {
            MetaProperty metaProperty = metadata.getClassNN(entityClass).getPropertyNN(viewProperty.getName());
            if (metaProperty.getRange().isClass()) {
                MetaClass propertyMetaClass = metaProperty.getRange().asClass();
                if (!Objects.equals(metadataTools.getStoreName(propertyMetaClass), storeName)) {
                    List<String> relatedProperties = metadataTools.getRelatedProperties(metaProperty);
                    if (relatedProperties.size() == 0) {
                        continue;
                    }
                    if (relatedProperties.size() > 1) {
                        log.warn("More than 1 related property is defined for attribute {}, skip handling different data store", metaProperty);
                        continue;
                    }
                    List<AlienProperty> alienProperties = alienPropertiesMap.computeIfAbsent(entityClass, k -> new ArrayList<>());
                    if (alienProperties.stream().noneMatch(aProp -> aProp.property == metaProperty))
                        alienProperties.add(new AlienProperty(metaProperty, viewProperty));
                }
                View propertyView = viewProperty.getView();
                if (propertyView != null) {
                    traverseView(propertyView, alienPropertiesMap, visited);
                }
            }
        }
    }

    public void processEntities(Collection<? extends Entity> entities) {
        Map<Class<? extends Entity>, List<AlienProperty>> alienPropertiesMap = getAlienPropertiesMap();
        if (alienPropertiesMap.isEmpty())
            return;

        Set<Entity> affectedEntities = getAffectedEntities(entities, alienPropertiesMap);
        if (affectedEntities.isEmpty())
            return;

        List<EntityAlienProperty> entityAlienPropertyList = new ArrayList<>();
        for (Entity affectedEntity : affectedEntities) {
            for (AlienProperty alienProperty : alienPropertiesMap.get(affectedEntity.getClass())) {
                entityAlienPropertyList.add(new EntityAlienProperty(affectedEntity, alienProperty));
            }
        }
        if (entityAlienPropertyList.size() == 1) {
            loadOne(entityAlienPropertyList.get(0));
        } else {
            entityAlienPropertyList.stream()
                    .collect(Collectors.groupingBy(EntityAlienProperty::getAlienProp))
                    .forEach((ap, eapList) ->
                            loadMany(ap, eapList.stream().map(eap -> eap.entity).collect(Collectors.toList()))
                    );
        }
    }

    private Set<Entity> getAffectedEntities(Collection<? extends Entity> entities,
                                            Map<Class<? extends Entity>, List<AlienProperty>> alienPropertiesMap) {
        Set<Entity> resultSet = new HashSet<>();
        for (Entity entity : entities) {
            metadataTools.traverseAttributesByView(view, entity, new EntityAttributeVisitor() {
                @Override
                public void visit(Entity entity, MetaProperty property) {
                    List<AlienProperty> alienProperties = alienPropertiesMap.get(entity.getClass());
                    if (alienProperties != null) {
                        alienProperties.stream()
                                .filter(ap -> ap.property == property)
                                .forEach(ap -> {
                                    if (entity.getValue(ap.relatedPropertyName) != null) {
                                        resultSet.add(entity);
                                    }
                                });
                    }
                }

                @Override
                public boolean skip(MetaProperty property) {
                    return !property.getRange().isClass();
                }
            });
        }
        return resultSet;
    }

    private void loadOne(EntityAlienProperty entityAlienProperty) {
        Entity entity = entityAlienProperty.entity;
        AlienProperty aProp = entityAlienProperty.aProp;
        Object id = entity.getValue(aProp.relatedPropertyName);

        LoadContext<Entity> loadContext = new LoadContext<>(aProp.property.getRange().asClass());
        loadContext.setId(id);
        if (aProp.viewProperty.getView() != null)
            loadContext.setView(aProp.viewProperty.getView());
        Entity relatedEntity = dataManager.load(loadContext);
        entity.setValue(aProp.property.getName(), relatedEntity);
    }

    private void loadMany(AlienProperty alienProperty, List<Entity> entities) {
        int offset = 0, limit = serverConfig.getAlienEntityLoadingBatchSize();
        while (true) {
            int end = offset + limit;
            List<Entity> batch = entities.subList(offset, Math.min(end, entities.size()));
            loadBatch(alienProperty, batch);
            if (end >= entities.size())
                break;
            else
                offset += limit;
        }
    }

    private void loadBatch(AlienProperty alienProperty, List<Entity> entities) {
        List<Object> idList = entities.stream()
                .map(e -> e.getValue(alienProperty.relatedPropertyName))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (idList.isEmpty())
            return;

        MetaClass alienMetaClass = alienProperty.property.getRange().asClass();

        String queryString = String.format(
                "select e from %s e where e.%s in :idList", alienMetaClass, alienProperty.primaryKeyName);

        LoadContext<Entity> loadContext = new LoadContext<>(alienMetaClass);
        loadContext.setQuery(LoadContext.createQuery(queryString).setParameter("idList", idList));
        loadContext.setView(alienProperty.viewProperty.getView());

        List<Entity> alienEntities = dataManager.loadList(loadContext);

        for (Entity entity : entities) {
            Object relatedPropertyValue = entity.getValue(alienProperty.relatedPropertyName);
            alienEntities.stream()
                    .filter(ae -> {
                        Object aeId = ae.getId() instanceof IdProxy ? ((IdProxy) ae.getId()).getNN() : ae.getId();
                        return aeId.equals(relatedPropertyValue);
                    })
                    .findAny()
                    .ifPresent(ae ->
                            entity.setValue(alienProperty.property.getName(), ae)
                    );
        }
    }

    private static class EntityAlienProperty {

        private final Entity entity;
        public final AlienProperty aProp;

        public EntityAlienProperty(Entity entity, AlienProperty alienProperty) {
            this.entity = entity;
            this.aProp = alienProperty;
        }

        public AlienProperty getAlienProp() {
            return aProp;
        }

        @Override
        public String toString() {
            return entity + " -> " + aProp;
        }
    }

    public class AlienProperty {

        public final MetaProperty property;
        public final ViewProperty viewProperty;
        public final String relatedPropertyName;
        public final String primaryKeyName;

        public AlienProperty(MetaProperty metaProperty, ViewProperty viewProperty) {
            this.property = metaProperty;
            this.viewProperty = viewProperty;

            List<String> relatedProperties = metadataTools.getRelatedProperties(property);
            relatedPropertyName = relatedProperties.get(0);

            String pkName = metadataTools.getPrimaryKeyName(property.getRange().asClass());
            primaryKeyName = pkName != null
                    ? pkName
                    : "id"; // sensible default for non-persistent entities
        }

        @Override
        public String toString() {
            return "AlienProperty: {" + property + "}";
        }
    }
}
