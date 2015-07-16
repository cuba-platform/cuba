/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.filter.AddConditionHelper;
import com.haulmont.cuba.gui.components.filter.ConditionsTree;
import com.haulmont.cuba.gui.components.filter.FilterHelper;
import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.components.filter.condition.*;
import com.haulmont.cuba.gui.components.filter.descriptor.GroupConditionDescriptor;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 * Window for generic filter edit
 *
 * @author gorbunkov
 * @version $Id$
 */
public class FilterEditor extends AbstractWindow {

    protected static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";

    protected FilterEntity filterEntity;

    protected Filter filter;

    @Inject
    protected ConditionsDs conditionsDs;

    @Inject
    protected TextField filterName;

    @Inject
    protected Label filterNameLabel;

    @Inject
    protected CheckBox availableForAllCb;

    @Inject
    protected Label availableForAllLabel;

    @Inject
    protected CheckBox defaultCb;

    @Inject
    protected Label defaultLabel;

    @Inject
    protected CheckBox applyDefaultCb;

    @Inject
    protected Label applyDefaultLabel;

    @Inject
    protected DynamicAttributesConditionFrame dynamicAttributesConditionFrame;

    @Inject
    protected CustomConditionFrame customConditionFrame;

    @Inject
    protected PropertyConditionFrame propertyConditionFrame;

    @Inject
    protected GroupConditionFrame groupConditionFrame;

    @Inject
    protected Tree conditionsTree;

    @Inject
    protected ThemeConstants theme;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Companion companion;

    protected ConditionsTree conditions;

    protected AddConditionHelper addConditionHelper;

    protected ConditionFrame activeConditionFrame;

    protected boolean treeItemChangeListenerEnabled = true;

    protected static Log log = LogFactory.getLog(FilterEditor.class);

    @WindowParam(name = "useShortConditionForm")
    protected Boolean useShortConditionForm;

    protected final List<String> componentsToHideInShortForm = Arrays.asList("hiddenLabel", "hidden",
            "requiredLabel", "required", "widthLabel", "width", "defaultValueLayoutLabel", "defaultValueLayout",
            "captionLabel", "caption");

    public interface Companion {
        void showComponentName(WindowManager windowManager, String title, String message);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        if (Boolean.TRUE.equals(useShortConditionForm)) {
            setCaption(getMessage("captionShortForm"));
        }

        getDialogParams()
                .setWidth(theme.getInt("cuba.gui.filterEditor.dialog.width"))
                .setHeight(theme.getInt("cuba.gui.filterEditor.dialog.height"))
                .setResizable(true);

        filterEntity = (FilterEntity) params.get("filterEntity");
        if (filterEntity == null)
            throw new RuntimeException("Filter entity was not passed to filter editor");
        filter = (Filter) params.get("filter");
        ConditionsTree paramConditions = (ConditionsTree) params.get("conditions");
        conditions = paramConditions.createCopy();
        refreshConditionsDs();
        conditionsTree.expandTree();

        filterName.setValue(filterEntity.getName());
        availableForAllCb.setValue(filterEntity.getUser() == null);
        defaultCb.setValue(filterEntity.getIsDefault());
        applyDefaultCb.setValue(filterEntity.getApplyDefault());

        if (!userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION)) {
            availableForAllCb.setVisible(false);
            availableForAllLabel.setVisible(false);
        }

        Configuration configuration = AppBeans.get(Configuration.NAME);
        boolean manualApplyRequired = filter.getManualApplyRequired() != null ?
                filter.getManualApplyRequired() :
                configuration.getConfig(ClientConfig.class).getGenericFilterManualApplyRequired();

        if (!manualApplyRequired) {
            applyDefaultCb.setVisible(manualApplyRequired);
            applyDefaultLabel.setVisible(manualApplyRequired);
        }

        if (filterEntity.getFolder() != null) {
            defaultCb.setVisible(false);
            defaultLabel.setVisible(false);
        }

        conditionsDs.addListener(new CollectionDsListenerAdapter<AbstractCondition>() {
            @Override
            public void itemChanged(Datasource<AbstractCondition> ds, @Nullable AbstractCondition prevItem, @Nullable AbstractCondition item) {
                if (!treeItemChangeListenerEnabled)
                    return;

                //commit previously selected condition
                if (activeConditionFrame != null) {
                    List<Validatable> validatables = new ArrayList<>();
                    Collection<Component> frameComponents = ComponentsHelper.getComponents(activeConditionFrame);
                    for (Component frameComponent : frameComponents) {
                        if (frameComponent instanceof Validatable)
                            validatables.add((Validatable) frameComponent);
                    }
                    if (validate(validatables)) {
                        activeConditionFrame.commit();
                    } else {
                        treeItemChangeListenerEnabled = false;
                        conditionsTree.setSelected(prevItem);
                        treeItemChangeListenerEnabled = true;
                        return;
                    }
                }

                if (item == null) {
                    activeConditionFrame = null;
                } else {
                    if (item instanceof PropertyCondition) {
                        activeConditionFrame = propertyConditionFrame;
                    } else if (item instanceof DynamicAttributesCondition) {
                        activeConditionFrame = dynamicAttributesConditionFrame;
                    } else if (item instanceof CustomCondition) {
                        activeConditionFrame = customConditionFrame;
                    } else if (item instanceof GroupCondition) {
                        activeConditionFrame = groupConditionFrame;
                    } else {
                        log.warn("Conditions frame for condition with type " + item.getClass().getSimpleName() + " not found");
                    }
                }

                propertyConditionFrame.setVisible(false);
                customConditionFrame.setVisible(false);
                dynamicAttributesConditionFrame.setVisible(false);
                groupConditionFrame.setVisible(false);

                if (activeConditionFrame != null) {
                    activeConditionFrame.setVisible(true);
                    activeConditionFrame.setCondition(item);

                    if (Boolean.TRUE.equals(useShortConditionForm)) {
                        for (String componentName : componentsToHideInShortForm) {
                            Component component = activeConditionFrame.getComponent(componentName);
                            if (component != null) {
                                component.setVisible(false);
                            }
                        }
                    }
                }
            }
        });

        addConditionHelper = new AddConditionHelper(filter, new AddConditionHelper.Handler() {
            @Override
            public void handle(AbstractCondition condition) {
                AbstractCondition item = conditionsDs.getItem();
                if (item != null && item instanceof GroupCondition) {
                    Node<AbstractCondition> newNode = new Node<>(condition);
                    Node<AbstractCondition> selectedNode = conditions.getNode(item);
                    selectedNode.addChild(newNode);
                    refreshConditionsDs();
                    conditionsTree.expand(item.getId());
                } else {
                    conditions.getRootNodes().add(new Node<>(condition));
                    refreshConditionsDs();
                }
                conditionsTree.setSelected(condition);
            }
        });

        FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
        filterHelper.initConditionsDragAndDrop(conditionsTree, conditions);

        if (Boolean.TRUE.equals(useShortConditionForm)) {
            filterName.setVisible(false);
            filterNameLabel.setVisible(false);
            availableForAllCb.setVisible(false);
            availableForAllLabel.setVisible(false);
            defaultCb.setVisible(false);
            defaultLabel.setVisible(false);
        }
    }

    public ConditionsTree getConditions() {
        return conditions;
    }

    public void commitAndClose() {
        if (!validateAll()) return;
        if (activeConditionFrame != null)
            activeConditionFrame.commit();
        filterEntity.setName((String) filterName.getValue());
        if (availableForAllCb.getValue())
            filterEntity.setUser(null);
        else
            filterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        filterEntity.setIsDefault((Boolean) defaultCb.getValue());
        filterEntity.setApplyDefault((Boolean) applyDefaultCb.getValue());
        close(COMMIT_ACTION_ID, true);
    }

    public void cancel() {
        close(CLOSE_ACTION_ID);
    }

    public void removeCondition() {
        AbstractCondition item = conditionsDs.getItem();
        if (item == null) {
            return;
        }
        conditions.removeCondition(item);
        refreshConditionsDs();
    }

    public void moveConditionUp() {
        AbstractCondition condition = conditionsDs.getItem();
        Node<AbstractCondition> node = conditions.getNode(condition);

        List<Node<AbstractCondition>> siblings = node.getParent() == null ?
                conditions.getRootNodes() : node.getParent().getChildren();

        int idx = siblings.indexOf(node);
        if (idx > 0) {
            Node<AbstractCondition> prev = siblings.get(idx - 1);
            siblings.set(idx - 1, node);
            siblings.set(idx, prev);
            refreshConditionsDs();
            conditionsTree.setSelected(condition);
        }
    }

    public void moveConditionDown() {
        AbstractCondition condition = conditionsDs.getItem();
        Node<AbstractCondition> node = conditions.getNode(condition);

        List<Node<AbstractCondition>> siblings = node.getParent() == null ?
                conditions.getRootNodes() : node.getParent().getChildren();

        int idx = siblings.indexOf(node);
        if (idx < siblings.size() - 1) {
            Node<AbstractCondition> next = siblings.get(idx + 1);
            siblings.set(idx + 1, node);
            siblings.set(idx, next);

            refreshConditionsDs();
            conditionsTree.setSelected(condition);
        }

    }

    protected void refreshConditionsDs() {
        conditionsDs.refresh(Collections.<String, Object>singletonMap("conditions", conditions));
    }

    public void addAndGroup() {
        addGroup(GroupType.AND);
    }

    public void addOrGroup() {
        addGroup(GroupType.OR);
    }

    protected void addGroup(GroupType groupType) {
        GroupConditionDescriptor conditionDescriptor = new GroupConditionDescriptor(groupType, filter.getId(), filter.getDatasource());
        AbstractCondition condition = conditionDescriptor.createCondition();
        AbstractCondition selectedCondition = conditionsDs.getItem();
        Node<AbstractCondition> newNode = new Node<>(condition);
        if (selectedCondition != null && selectedCondition instanceof GroupCondition) {
            Node<AbstractCondition> node = conditions.getNode(selectedCondition);
            if (node != null) {
                node.addChild(newNode);
                conditionsTree.expand(selectedCondition.getId());
            }
        } else {
            conditions.getRootNodes().add(newNode);
        }
        refreshConditionsDs();
        conditionsTree.setSelected(condition);
    }

    public void addCondition() {
        addConditionHelper.addCondition(conditions);
    }

    public void showComponentName() {
        AbstractCondition item = conditionsDs.getItem();
        String message = (item != null && item.getParam() != null) ? item.getParam().getName() : getMessage("conditionIsNotSelected");
        companion.showComponentName(getWindowManager(), getMessage("showComponentName.title"), message);
    }

    public FilterEntity getFilterEntity() {
        return filterEntity;
    }

    public ConditionsTree getConditionsTree() {
        return conditions;
    }

    public Filter getFilter() {
        return filter;
    }
}
