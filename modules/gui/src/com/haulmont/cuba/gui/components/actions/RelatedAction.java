/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.actions;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.RelatedEntitiesService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.ComponentFinder;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FilterParser;
import com.haulmont.cuba.gui.components.filter.Op;
import com.haulmont.cuba.gui.components.filter.Param;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;
import com.haulmont.cuba.gui.components.filter.condition.PropertyCondition;
import com.haulmont.cuba.gui.components.filter.descriptor.PropertyConditionDescriptor;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class RelatedAction extends AbstractAction {

    public static final String ACTION_ID = "related";

    protected final ListComponent owner;
    protected final MetaProperty metaProperty;
    protected final MetaClass metaClass;

    protected String screen;
    protected String filterCaption;

    protected WindowManager.OpenType openType;

    protected ExtendedEntities extendedEntities = AppBeans.get(ExtendedEntities.NAME);
    protected RelatedEntitiesService relatedEntitiesService = AppBeans.get(RelatedEntitiesService.NAME);

    public RelatedAction(String id, ListComponent owner, MetaClass metaClass, MetaProperty metaProperty) {
        super(id);

        this.owner = owner;
        this.metaClass = metaClass;
        this.metaProperty = metaProperty;

        MessageTools tools = AppBeans.get(MessageTools.NAME);
        setCaption(StringUtils.capitalize(tools.getPropertyCaption(metaProperty)));
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

    public WindowManager.OpenType getOpenType() {
        return openType;
    }

    public void setOpenType(WindowManager.OpenType openType) {
        this.openType = openType;
    }

    @Override
    public void actionPerform(Component component) {
        final Set<Entity> selected = owner.getSelected();
        if (!selected.isEmpty()) {
            Window window = owner.getFrame().openWindow(getScreen(), openType);

            boolean found = ComponentsHelper.walkComponents(window, new ComponentFinder() {
                @Override
                public boolean visit(Component component) {
                    if (!(component instanceof Filter)) {
                        return false;
                    } else {
                        MetaClass actualMetaClass = ((Filter) component).getDatasource().getMetaClass();
                        MetaClass propertyMetaClass = extendedEntities.getEffectiveMetaClass(metaProperty.getRange().asClass());
                        if (ObjectUtils.equals(actualMetaClass, propertyMetaClass)) {
                            applyFilter(((Filter) component), selected);
                            return true;
                        }
                        return false;
                    }
                }
            });
            if (!found) {
                owner.getFrame().showNotification(messages.getMainMessage("actions.Related.FilterNotFound"), IFrame.NotificationType.WARNING);
            }
        } else {
            owner.getFrame().showNotification(messages.getMainMessage("actions.Related.NotSelected"), IFrame.NotificationType.HUMANIZED);
        }
    }

    protected void applyFilter(Filter component, Set<Entity> selectedParents) {
        MessageTools messageTools = AppBeans.get(MessageTools.NAME);

        List<UUID> relatedIds = getRelatedIds(selectedParents);

        FilterEntity filterEntity = new FilterEntity();
        filterEntity.setComponentId(ComponentsHelper.getFilterComponentPath(component));

        if (StringUtils.isNotEmpty(filterCaption)) {
            filterEntity.setName(filterCaption);
        } else {
            filterEntity.setName(messages.getMainMessage("actions.Related.Filter") +
                    " " + messageTools.getPropertyCaption(metaProperty));
        }

        MetaClass effectiveMetaClass = extendedEntities.getEffectiveMetaClass(metaProperty.getRange().asClass());

        filterEntity.setXml(getRelatedEntitiesFilterXml(effectiveMetaClass, relatedIds, component));

        component.setFilterEntity(filterEntity);

        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);

        if (Boolean.TRUE.equals(component.getManualApplyRequired())
                || (component.getManualApplyRequired() == null && clientConfig.getGenericFilterManualApplyRequired())) {
            component.apply(true);
        }
    }

    protected String getRelatedEntitiesFilterXml(MetaClass metaClass, List<UUID> ids, Filter component) {
        ConditionsTree tree = new ConditionsTree();

        String filterComponentPath = ComponentsHelper.getFilterComponentPath(component);
        String[] strings = ValuePathHelper.parse(filterComponentPath);
        String filterComponentName = ValuePathHelper.format(Arrays.copyOfRange(strings, 1, strings.length));

        PropertyConditionDescriptor conditionDescriptor = new PropertyConditionDescriptor("id", "id", AppConfig.getMessagesPack(), filterComponentName, component.getDatasource());

        PropertyCondition condition = (PropertyCondition) conditionDescriptor.createCondition();
        condition.setInExpr(true);
        condition.setHidden(true);
        condition.setOperator(Op.IN);

        Param param = new Param(condition.createParamName(), UUID.class, "", "", component.getDatasource(), metaClass.getProperty("id"), true, true);
        param.setValue(ids);

        condition.setParam(param);

        tree.setRootNodes(Collections.singletonList(new Node<AbstractCondition>(condition)));

        return FilterParser.getXml(tree, Param.ValueProperty.VALUE);
    }

    protected List<UUID> getRelatedIds(Set<Entity> selectedParents) {
        if (selectedParents.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<UUID> parentIds = new ArrayList<>();
            for (Entity e : selectedParents) {
                parentIds.add((UUID) e.getId());
            }

            String parentMetaClass = metaClass.getName();

            return relatedEntitiesService.getRelatedIds(parentIds, parentMetaClass, metaProperty.getName());
        }
    }
}