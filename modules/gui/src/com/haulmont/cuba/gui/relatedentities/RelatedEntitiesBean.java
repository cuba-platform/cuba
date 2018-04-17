/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.relatedentities;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.RelatedEntitiesService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.gui.*;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.sys.ValuePathHelper;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.filter.ConditionParamBuilder;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FilterParser;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.CustomCondition;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import com.haulmont.cuba.gui.components.filter.descriptor.PropertyConditionDescriptor;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component(RelatedEntitiesAPI.NAME)
public class RelatedEntitiesBean implements RelatedEntitiesAPI {

    @Inject
    protected ExtendedEntities extendedEntities;

    @Inject
    protected RelatedEntitiesService relatedEntitiesService;

    @Inject
    protected Messages messages;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected WindowConfig windowConfig;

    @Inject
    protected WindowManagerProvider windowManagerProvider;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected FilterParser filterParser;

    @Inject
    protected ConditionParamBuilder paramBuilder;

    @Override
    public void openRelatedScreen(Collection<? extends Entity> selectedEntities, MetaClass metaClass, MetaProperty metaProperty) {
        openRelatedScreen(selectedEntities, metaClass, metaProperty, new RelatedScreenDescriptor());
    }

    @Override
    public void openRelatedScreen(Collection<? extends Entity> selectedEntities, MetaClass metaClass, MetaProperty metaProperty,
                                  RelatedScreenDescriptor descriptor) {
        Preconditions.checkNotNullArgument(metaClass, "MetaClass can't be null");
        Preconditions.checkNotNullArgument(metaProperty, "MetaProperty can't be null");

        WindowManager windowManager = windowManagerProvider.get();
        if (!selectedEntities.isEmpty()) {
            Map<String, Object> params = new HashMap<>();

            WindowParams.DISABLE_AUTO_REFRESH.set(params, true);
            WindowParams.DISABLE_RESUME_SUSPENDED.set(params, true);

            if (descriptor != null && descriptor.getScreenParams() != null) {
                params.putAll(descriptor.getScreenParams());
            }

            String screen;
            if (descriptor != null && StringUtils.isNotEmpty(descriptor.getScreenId())) {
                screen = descriptor.getScreenId();
            } else {
                screen = windowConfig.getBrowseScreenId(metaProperty.getRange().asClass());
            }

            if (StringUtils.isEmpty(screen)) {
                String message = String.format("Can't show related entities: passed screenId is null and " +
                        "there is no default browse screen for %s", metaClass.getName());
                throw new IllegalStateException(message);
            }


            WindowManager.OpenType openType = WindowManager.OpenType.THIS_TAB;
            if (descriptor != null) {
                openType = descriptor.getOpenType();
            }

            Window window = windowManager.openWindow(windowConfig.getWindowInfo(screen), openType, params);

            boolean found = ComponentsHelper.walkComponents(window, screenComponent -> {
                if (!(screenComponent instanceof Filter)) {
                    return false;
                } else {
                    MetaClass actualMetaClass = ((Filter) screenComponent).getDatasource().getMetaClass();
                    MetaClass relatedMetaClass = metaProperty.getRange().asClass();
                    MetaClass effectiveMetaClass = extendedEntities.getEffectiveMetaClass(relatedMetaClass);
                    if (Objects.equals(actualMetaClass, effectiveMetaClass)) {
                        MetaDataDescriptor metaDataDescriptor = new MetaDataDescriptor(metaClass, metaProperty);

                        applyFilter(((Filter) screenComponent), selectedEntities, descriptor, metaDataDescriptor);
                        return true;
                    }
                    return false;
                }
            });
            if (!found) {
                windowManager.showNotification(messages.getMainMessage("actions.Related.FilterNotFound"), Frame.NotificationType.WARNING);
            }
            ((DsContextImplementation) window.getDsContext()).resumeSuspended();
        } else {
            windowManager.showNotification(messages.getMainMessage("actions.Related.NotSelected"), Frame.NotificationType.HUMANIZED);
        }
    }

    @Override
    public <T extends Entity> void openRelatedScreen(Collection<T> selectedEntities, Class<T> clazz, String property) {
        openRelatedScreen(selectedEntities, clazz, property, new RelatedScreenDescriptor());
    }

    @Override
    public <T extends Entity> void openRelatedScreen(Collection<T> selectedEntities, Class<T> clazz, String property, RelatedScreenDescriptor descriptor) {
        Preconditions.checkNotNullArgument(clazz, "Class can't be null");

        if (StringUtils.isEmpty(property)) {
            throw new IllegalArgumentException("Property can't be null");
        }

        MetaClass metaClass = metadata.getClassNN(clazz);
        MetaProperty metaProperty = metaClass.getPropertyNN(property);

        openRelatedScreen(selectedEntities, metaClass, metaProperty, descriptor);
    }

    protected void applyFilter(Filter component, Collection<? extends Entity> selectedParents, RelatedScreenDescriptor descriptor, MetaDataDescriptor metaDataDescriptor) {
        FilterEntity filterEntity = metadata.create(FilterEntity.class);
        filterEntity.setComponentId(ComponentsHelper.getFilterComponentPath(component));

        if (StringUtils.isNotEmpty(descriptor.getFilterCaption())) {
            filterEntity.setName(descriptor.getFilterCaption());
        } else {
            MetaProperty metaProperty = metaDataDescriptor.getMetaProperty();
            filterEntity.setName(messages.getMainMessage("actions.Related.Filter") +
                    " " + messageTools.getPropertyCaption(metaProperty.getDomain(), metaProperty.getName()));
        }

        MetaClass effectiveMetaClass = extendedEntities.getEffectiveMetaClass(metaDataDescriptor.getRelatedMetaClass());

        filterEntity.setXml(getRelatedEntitiesFilterXml(effectiveMetaClass, selectedParents, component, metaDataDescriptor));
        filterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());

        component.setFilterEntity(filterEntity);
        component.apply(true);
    }

    protected String getRelatedEntitiesFilterXml(MetaClass relatedMetaCLass, Collection<? extends Entity> selectedEntities, Filter component, MetaDataDescriptor descriptor) {
        ConditionsTree tree = new ConditionsTree();

        String filterComponentPath = ComponentsHelper.getFilterComponentPath(component);
        String[] strings = ValuePathHelper.parse(filterComponentPath);
        String filterComponentName = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));

        String relatedPrimaryKey = metadataTools.getPrimaryKeyName(relatedMetaCLass);
        AbstractCondition condition = getOptimizedCondition(getParentIds(selectedEntities), component.getDatasource(),
                filterComponentName, relatedPrimaryKey, descriptor);

        if (condition == null) {
            condition = getNonOptimizedCondition(relatedMetaCLass, getRelatedIds(selectedEntities, descriptor), component,
                    filterComponentName, relatedPrimaryKey);
        }

        tree.setRootNodes(Collections.singletonList(new Node<>(condition)));

        return filterParser.getXml(tree, Param.ValueProperty.VALUE);
    }

    @Nullable
    protected AbstractCondition getOptimizedCondition(List<Object> parentIds, CollectionDatasource datasource,
                                                      String filterComponentName, String relatedPrimaryKey, MetaDataDescriptor descriptor) {
        Range.Cardinality cardinality = descriptor.getMetaProperty().getRange().getCardinality();

        if (cardinality == Range.Cardinality.MANY_TO_ONE) {
            return getManyToOneCondition(parentIds, datasource, filterComponentName, relatedPrimaryKey, descriptor);
        } else if (cardinality == Range.Cardinality.ONE_TO_MANY || cardinality == Range.Cardinality.ONE_TO_ONE) {
            return getOneToManyCondition(parentIds, datasource, filterComponentName, descriptor);
        } else if (cardinality == Range.Cardinality.MANY_TO_MANY) {
            return getManyToManyCondition(parentIds, datasource, filterComponentName, relatedPrimaryKey, descriptor);
        }

        return null;
    }

    @Nullable
    protected AbstractCondition getOneToManyCondition(List<Object> parentIds, CollectionDatasource datasource,
                                                      String filterComponentName, MetaDataDescriptor descriptor) {
        MetaProperty inverseField = descriptor.getMetaProperty().getInverse();
        if (inverseField == null) {
            return null;
        }

        MetaClass metaClass = descriptor.getMetaClass();
        String parentPrimaryKey = metadataTools.getPrimaryKeyName(metaClass);
        CustomCondition customCondition = getParentEntitiesCondition(parentIds, parentPrimaryKey, datasource,
                filterComponentName, metaClass);

        String whereString = String.format("{E}.%s.%s in :%s",
                inverseField.getName(), parentPrimaryKey, customCondition.getParam().getName());
        customCondition.setWhere(whereString);

        return customCondition;
    }

    @Nullable
    protected AbstractCondition getManyToManyCondition(List<Object> parentIds, CollectionDatasource datasource,
                                                       String filterComponentName, String relatedPrimaryKey, MetaDataDescriptor descriptor) {
        MetaClass metaClass = descriptor.getMetaClass();
        String parentPrimaryKey = metadataTools.getPrimaryKeyName(metaClass);
        CustomCondition customCondition = getParentEntitiesCondition(parentIds, parentPrimaryKey, datasource,
                filterComponentName, metaClass);

        String parentEntityAlias = RandomStringUtils.randomAlphabetic(6);
        String entityAlias = RandomStringUtils.randomAlphabetic(6);
        String select = String.format("select %s.%s from %s %s ", entityAlias, relatedPrimaryKey, metaClass, parentEntityAlias);

        String joinWhere = String.format("join %s.%s %s where %s.%s in :%s", parentEntityAlias, descriptor.getMetaProperty().getName(),
                entityAlias, parentEntityAlias, parentPrimaryKey, customCondition.getParam().getName());

        String whereString = String.format("{E}.%s in (%s)", relatedPrimaryKey, select + joinWhere);
        customCondition.setWhere(whereString);

        return customCondition;
    }

    @Nullable
    protected AbstractCondition getManyToOneCondition(List<Object> parentIds, CollectionDatasource datasource,
                                                      String filterComponentName, String relatedPrimaryKey, MetaDataDescriptor descriptor) {
        MetaClass metaClass = descriptor.getMetaClass();
        String parentPrimaryKey = metadataTools.getPrimaryKeyName(metaClass);
        CustomCondition customCondition = getParentEntitiesCondition(parentIds, parentPrimaryKey, datasource, filterComponentName, metaClass);

        String entityAlias = RandomStringUtils.randomAlphabetic(6);
        String subQuery = String.format("select %s.%s.%s from %s %s where %s.%s in :%s", entityAlias,
                descriptor.getMetaProperty().getName(), relatedPrimaryKey, metaClass.getName(), entityAlias,
                entityAlias, parentPrimaryKey, customCondition.getParam().getName());

        String whereString = String.format("{E}.%s in (%s)", relatedPrimaryKey, subQuery);
        customCondition.setWhere(whereString);

        return customCondition;
    }

    protected Param getParentEntitiesParam(List<Object> parentIds, String parentPrimaryKey, CollectionDatasource datasource,
                                           Class parentPrimaryKeyClass, String paramName, MetaClass parentMetaClass) {
        Param param = Param.Builder.getInstance().setName(paramName)
                .setJavaClass(parentPrimaryKeyClass)
                .setEntityWhere(StringUtils.EMPTY)
                .setEntityView(StringUtils.EMPTY)
                .setDataSource(datasource)
                .setProperty(parentMetaClass.getPropertyNN(parentPrimaryKey))
                .setInExpr(true)
                .setRequired(true)
                .build();
        param.setValue(parentIds);
        return param;
    }

    protected CustomCondition getParentEntitiesCondition(List<Object> parentIds, String parentPrimaryKey, CollectionDatasource datasource,
                                                         String filterComponentName, MetaClass parentMetaClass) {
        String conditionName = String.format("related_%s", RandomStringUtils.randomAlphabetic(6));
        CustomCondition condition = new CustomCondition(getConditionXmlElement(conditionName, parentMetaClass), AppConfig.getMessagesPack(),
                filterComponentName, datasource);


        Class<?> parentPrimaryKeyClass = parentMetaClass.getPropertyNN(parentPrimaryKey).getJavaType();
        condition.setJavaClass(parentPrimaryKeyClass);
        condition.setHidden(true);
        condition.setInExpr(true);

        int randInt = new Random().nextInt((99999 - 11111) + 1) + 11111;
        String paramName = String.format("component$%s.%s%s", filterComponentName, conditionName, randInt);

        condition.setParam(getParentEntitiesParam(parentIds, parentPrimaryKey, datasource, parentPrimaryKeyClass, paramName, parentMetaClass));

        return condition;
    }

    protected Element getConditionXmlElement(String conditionName, MetaClass metaClass) {
        Element conditionElement = DocumentHelper.createDocument().addElement("c");
        conditionElement.addAttribute("name", conditionName);
        conditionElement.addAttribute("width", "1");
        conditionElement.addAttribute("type", "CUSTOM");
        String conditionCaption = String.format("%s ids", metaClass.getName().split("\\$")[1]);
        // condition will be hidden so we don't have to load localized condition caption
        conditionElement.addAttribute("locCaption", conditionCaption);
        return conditionElement;
    }

    protected PropertyCondition getNonOptimizedCondition(MetaClass metaClass, List<Object> ids, Filter component,
                                                         String filterComponentName, String primaryKey) {
        PropertyConditionDescriptor conditionDescriptor = new PropertyConditionDescriptor(primaryKey, primaryKey,
                AppConfig.getMessagesPack(), filterComponentName, component.getDatasource());

        PropertyCondition condition = (PropertyCondition) conditionDescriptor.createCondition();
        condition.setInExpr(true);
        condition.setHidden(true);
        condition.setOperator(Op.IN);

        @SuppressWarnings("ConstantConditions")
        Class idType = metaClass.getProperty(primaryKey).getJavaType();

        Param param = Param.Builder.getInstance().setName(paramBuilder.createParamName(condition))
                .setJavaClass(idType)
                .setEntityWhere("")
                .setEntityView("")
                .setDataSource(component.getDatasource())
                .setProperty(metaClass.getProperty(primaryKey))
                .setInExpr(true)
                .setRequired(true)
                .build();
        param.setValue(ids);

        condition.setParam(param);
        return condition;
    }

    protected List<Object> getRelatedIds(Collection<? extends Entity> selectedParents, MetaDataDescriptor descriptor) {
        if (selectedParents.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Object> parentIds = new ArrayList<>();
            for (Entity e : selectedParents) {
                parentIds.add(e.getId());
            }

            //noinspection UnnecessaryLocalVariable
            List<Object> relatedIds = relatedEntitiesService.getRelatedIds(parentIds, descriptor.getMetaClass().getName(),
                    descriptor.getMetaProperty().getName());
            return relatedIds;
        }
    }

    protected List<Object> getParentIds(Collection<? extends Entity> selectedParents) {
        if (selectedParents.isEmpty()) {
            return Collections.emptyList();
        } else {
            return selectedParents.stream().map(Entity::getId).collect(Collectors.toList());
        }
    }

    protected class MetaDataDescriptor {

        protected final MetaClass metaClass;
        protected final MetaProperty metaProperty;
        protected final MetaClass relatedMetaClass;

        public MetaDataDescriptor(MetaClass metaClass, MetaProperty metaProperty) {
            this.metaClass = metaClass;
            this.metaProperty = metaProperty;

            this.relatedMetaClass = metaProperty.getRange().asClass();
        }

        protected MetaProperty getMetaProperty() {
            return metaProperty;
        }

        protected MetaClass getMetaClass() {
            return metaClass;
        }

        protected MetaClass getRelatedMetaClass() {
            return relatedMetaClass;
        }
    }
}