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

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.RelatedEntitiesService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.filter.Op;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.ConditionParamBuilder;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FilterParser;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import com.haulmont.cuba.gui.components.filter.descriptor.PropertyConditionDescriptor;
import com.haulmont.cuba.gui.data.impl.DsContextImplementation;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class RelatedAction extends AbstractAction implements Action.HasBeforeActionPerformedHandler {

    public static final String ACTION_ID = "related";

    protected final ListComponent target;
    protected final MetaProperty metaProperty;
    protected final MetaClass metaClass;

    protected String screen;
    protected String filterCaption;

    protected OpenType openType = OpenType.THIS_TAB;

    protected ExtendedEntities extendedEntities = AppBeans.get(ExtendedEntities.NAME);
    protected RelatedEntitiesService relatedEntitiesService = AppBeans.get(RelatedEntitiesService.NAME);

    protected BeforeActionPerformedHandler beforeActionPerformedHandler;

    public RelatedAction(String id, ListComponent target, MetaClass metaClass, MetaProperty metaProperty) {
        super(id);

        this.target = target;
        this.metaClass = metaClass;
        this.metaProperty = metaProperty;

        MessageTools tools = AppBeans.get(MessageTools.NAME);
        setCaption(StringUtils.capitalize(tools.getPropertyCaption(target.getDatasource().getMetaClass(), metaProperty.getName())));
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getFilterCaption() {
        return filterCaption;
    }

    public void setFilterCaption(String filterCaption) {
        this.filterCaption = filterCaption;
    }

    public OpenType getOpenType() {
        return openType;
    }

    public void setOpenType(OpenType openType) {
        this.openType = openType;
    }

    @Override
    public void actionPerform(Component component) {
        if (beforeActionPerformedHandler != null) {
            if (!beforeActionPerformedHandler.beforeActionPerformed())
                return;
        }

        final Set<Entity> selected = target.getSelected();

        if (!selected.isEmpty()) {
            Map<String, Object> params = new HashMap<>();

            WindowParams.DISABLE_AUTO_REFRESH.set(params, true);
            WindowParams.DISABLE_RESUME_SUSPENDED.set(params, true);

            Window window = target.getFrame().openWindow(getScreen(), getOpenType(), params);

            boolean found = ComponentsHelper.walkComponents(window, screenComponent -> {
                if (!(screenComponent instanceof Filter)) {
                    return false;
                } else {
                    MetaClass actualMetaClass = ((Filter) screenComponent).getDatasource().getMetaClass();
                    MetaClass propertyMetaClass = extendedEntities.getEffectiveMetaClass(metaProperty.getRange().asClass());
                    if (ObjectUtils.equals(actualMetaClass, propertyMetaClass)) {
                        applyFilter(((Filter) screenComponent), selected);
                        return true;
                    }
                    return false;
                }
            });
            if (!found) {
                target.getFrame().showNotification(messages.getMainMessage("actions.Related.FilterNotFound"), Frame.NotificationType.WARNING);
            }

            ((DsContextImplementation) window.getDsContext()).resumeSuspended();
        } else {
            target.getFrame().showNotification(messages.getMainMessage("actions.Related.NotSelected"), Frame.NotificationType.HUMANIZED);
        }
    }

    protected void applyFilter(Filter component, Set<Entity> selectedParents) {
        MessageTools messageTools = AppBeans.get(MessageTools.NAME);
        Metadata metadata = AppBeans.get(Metadata.NAME);

        List<Object> relatedIds = getRelatedIds(selectedParents);

        FilterEntity filterEntity = metadata.create(FilterEntity.class);
        filterEntity.setComponentId(ComponentsHelper.getFilterComponentPath(component));

        if (StringUtils.isNotEmpty(filterCaption)) {
            filterEntity.setName(filterCaption);
        } else {
            filterEntity.setName(messages.getMainMessage("actions.Related.Filter") +
                    " " + messageTools.getPropertyCaption(metaProperty.getDomain(), metaProperty.getName()));
        }

        MetaClass effectiveMetaClass = extendedEntities.getEffectiveMetaClass(metaProperty.getRange().asClass());

        filterEntity.setXml(getRelatedEntitiesFilterXml(effectiveMetaClass, relatedIds, component));

        component.setFilterEntity(filterEntity);
        component.apply(true);
    }

    protected String getRelatedEntitiesFilterXml(MetaClass metaClass, List<Object> ids, Filter component) {
        ConditionsTree tree = new ConditionsTree();

        String filterComponentPath = ComponentsHelper.getFilterComponentPath(component);
        String[] strings = ValuePathHelper.parse(filterComponentPath);
        String filterComponentName = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));

        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        String primaryKey = metadataTools.getPrimaryKeyName(metaClass);

        PropertyConditionDescriptor conditionDescriptor = new PropertyConditionDescriptor(primaryKey, primaryKey,
                AppConfig.getMessagesPack(), filterComponentName, component.getDatasource());

        PropertyCondition condition = (PropertyCondition) conditionDescriptor.createCondition();
        condition.setInExpr(true);
        condition.setHidden(true);
        condition.setOperator(Op.IN);

        ConditionParamBuilder paramBuilder = AppBeans.get(ConditionParamBuilder.class);

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

        tree.setRootNodes(Collections.singletonList(new Node<>(condition)));

        FilterParser filterParser = AppBeans.get(FilterParser.class);
        return filterParser.getXml(tree, Param.ValueProperty.VALUE);
    }

    protected List<Object> getRelatedIds(Set<Entity> selectedParents) {
        if (selectedParents.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<Object> parentIds = new ArrayList<>();
            for (Entity e : selectedParents) {
                parentIds.add(e.getId());
            }

            String parentMetaClass = metaClass.getName();

            //noinspection UnnecessaryLocalVariable
            List<Object> relatedIds = relatedEntitiesService.getRelatedIds(parentIds, parentMetaClass, metaProperty.getName());
            return relatedIds;
        }
    }

    @Override
    public BeforeActionPerformedHandler getBeforeActionPerformedHandler() {
        return beforeActionPerformedHandler;
    }

    @Override
    public void setBeforeActionPerformedHandler(BeforeActionPerformedHandler handler) {
        beforeActionPerformedHandler = handler;
    }
}