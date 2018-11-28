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

package com.haulmont.cuba.gui.components.filter.edit;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
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
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Window for generic filter edit
 */
public class FilterEditor extends AbstractWindow {

    private static final Logger log = LoggerFactory.getLogger(FilterEditor.class);

    protected static final String GLOBAL_FILTER_PERMISSION = "cuba.gui.filter.global";

    protected FilterEntity filterEntity;

    protected Filter filter;

    @Inject
    protected ConditionsDs conditionsDs;

    @Inject
    protected GridLayout filterPropertiesGrid;

    @Inject
    protected TextField<String> filterName;

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
    private CheckBox globalDefaultCb;

    @Inject
    private Label globalDefaultLabel;

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
    protected FtsConditionFrame ftsConditionFrame;

    @Inject
    protected Tree conditionsTree;

    @Inject
    protected ThemeConstants theme;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected Companion companion;

    @Inject
    private Metadata metadata;

    protected ConditionsTree conditions;

    protected AddConditionHelper addConditionHelper;

    protected ConditionFrame activeConditionFrame;

    protected boolean treeItemChangeListenerEnabled = true;

    @WindowParam(name = "useShortConditionForm")
    protected Boolean useShortConditionForm;

    @WindowParam(name = "hideDynamicAttributes")
    protected Boolean hideDynamicAttributes;

    @WindowParam(name = "hideCustomConditions")
    protected Boolean hideCustomConditions;

    @WindowParam(name = "showConditionHiddenOption")
    protected Boolean showConditionHiddenOption = Boolean.FALSE;

    protected final List<String> componentsToHideInShortForm = Arrays.asList("hiddenLabel", "hidden",
            "requiredLabel", "required", "widthLabel", "width", "captionLabel", "caption");

    protected final List<String> componentsForHiddenOption = Arrays.asList("hiddenLabel", "hidden");

    @Inject
    private DataManager dataManager;

    protected Set<Entity> modifiedGlobalDefaultFilters = new HashSet<>();

    public interface Companion {
        void showComponentName(WindowManager windowManager, String title, String message);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        if (Boolean.TRUE.equals(useShortConditionForm)) {
            setCaption(messages.getMainMessage("filter.editor.captionShortForm"));
        }

        getDialogOptions()
                .setWidth(theme.get("cuba.gui.filterEditor.dialog.width"))
                .setHeight(theme.get("cuba.gui.filterEditor.dialog.height"))
                .setResizable(true);

        filterEntity = (FilterEntity) params.get("filterEntity");
        if (filterEntity == null) {
            throw new RuntimeException("Filter entity was not passed to filter editor");
        }
        filter = (Filter) params.get("filter");

        ConditionsTree paramConditions = (ConditionsTree) params.get("conditions");
        conditions = paramConditions.createCopy();
        refreshConditionsDs();
        conditionsTree.expandTree();

        MetaProperty property = metadata.getClassNN(FilterEntity.class).getPropertyNN("name");
        Map<String, Object> annotations = property.getAnnotations();
        Integer maxLength = (Integer) annotations.get("length");
        if (maxLength != null) {
            filterName.setMaxLength(maxLength);
        }

        if (!messages.getMainMessage("filter.adHocFilter").equals(filterEntity.getName())) {
            filterName.setValue(filterEntity.getName());
        }
        availableForAllCb.setValue(filterEntity.getUser() == null);
        defaultCb.setValue(filterEntity.getIsDefault());
        applyDefaultCb.setValue(filterEntity.getApplyDefault());
        globalDefaultCb.setValue(filterEntity.getGlobalDefault());

        if (filterEntity.getUser() != null) {
            globalDefaultCb.setEnabled(false);
        }

        if (!userSessionSource.getUserSession().isSpecificPermitted(GLOBAL_FILTER_PERMISSION)) {
            availableForAllCb.setVisible(false);
            availableForAllLabel.setVisible(false);
            globalDefaultCb.setVisible(false);
            globalDefaultLabel.setVisible(false);
        }

        availableForAllCb.addValueChangeListener(e -> {
            Boolean isFilterAvailableForAll = (Boolean) e.getValue();
            globalDefaultCb.setEnabled(isFilterAvailableForAll);
            if (!isFilterAvailableForAll) {
                globalDefaultCb.setValue(null);
            }
        });

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

        conditionsDs.addItemChangeListener(e -> {
            if (!treeItemChangeListenerEnabled) {
                return;
            }

            //commit previously selected condition
            if (activeConditionFrame != null) {
                List<Validatable> validatables = new ArrayList<>();
                Collection<Component> frameComponents = ComponentsHelper.getComponents(activeConditionFrame);
                for (Component frameComponent : frameComponents) {
                    if (frameComponent instanceof Validatable) {
                        validatables.add((Validatable) frameComponent);
                    }
                }
                if (validate(validatables)) {
                    activeConditionFrame.commit();
                } else {
                    treeItemChangeListenerEnabled = false;
                    conditionsTree.setSelected(e.getPrevItem());
                    treeItemChangeListenerEnabled = true;
                    return;
                }
            }

            if (e.getItem() == null) {
                activeConditionFrame = null;
            } else {
                if (e.getItem() instanceof PropertyCondition) {
                    activeConditionFrame = propertyConditionFrame;
                } else if (e.getItem() instanceof DynamicAttributesCondition) {
                    activeConditionFrame = dynamicAttributesConditionFrame;
                } else if (e.getItem() instanceof CustomCondition) {
                    activeConditionFrame = customConditionFrame;
                } else if (e.getItem() instanceof GroupCondition) {
                    activeConditionFrame = groupConditionFrame;
                } else if (e.getItem() instanceof FtsCondition) {
                    activeConditionFrame = ftsConditionFrame;
                } else {
                    log.warn("Conditions frame for condition with type " + e.getItem().getClass().getSimpleName() + " not found");
                }
            }

            propertyConditionFrame.setVisible(false);
            customConditionFrame.setVisible(false);
            dynamicAttributesConditionFrame.setVisible(false);
            groupConditionFrame.setVisible(false);
            ftsConditionFrame.setVisible(false);

            if (activeConditionFrame != null) {
                activeConditionFrame.setVisible(true);
                activeConditionFrame.setCondition(e.getItem());

                if (Boolean.TRUE.equals(useShortConditionForm)) {
                    for (String componentName : componentsToHideInShortForm) {
                        Component component = activeConditionFrame.getComponent(componentName);
                        if (component != null) {
                            if (BooleanUtils.isTrue(showConditionHiddenOption) && componentsForHiddenOption.contains(componentName)) {
                                continue;
                            }
                            component.setVisible(false);
                        }
                    }
                }
            }
        });

        addConditionHelper = new AddConditionHelper(filter, BooleanUtils.isTrue(hideDynamicAttributes), BooleanUtils.isTrue(hideCustomConditions), condition -> {
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
        });

        FilterHelper filterHelper = AppBeans.get(FilterHelper.class);
        filterHelper.initConditionsDragAndDrop(conditionsTree, conditions);

        if (Boolean.TRUE.equals(useShortConditionForm)) {
            filterPropertiesGrid.setVisible(false);
        }
    }

    public ConditionsTree getConditions() {
        return conditions;
    }

    public void commitAndClose() {
        if (!validateAll()) {
            return;
        }
        if (hasEmptyGroupConditions()) {
            showNotification(getMessage("filter.editor.groupConditionCannotBeEmpty"), NotificationType.WARNING);
            return;
        }
        if (activeConditionFrame != null) {
            activeConditionFrame.commit();
        }
        filterEntity.setName(filterName.getValue());
        if (availableForAllCb.getValue()) {
            filterEntity.setUser(null);
        } else {
            filterEntity.setUser(userSessionSource.getUserSession().getCurrentOrSubstitutedUser());
        }
        filterEntity.setIsDefault(defaultCb.getValue());
        filterEntity.setApplyDefault(applyDefaultCb.getValue());

        boolean globalDefaultShouldBeChecked = !Boolean.TRUE.equals(filterEntity.getGlobalDefault()) && globalDefaultCb.getValue();
        filterEntity.setGlobalDefault(globalDefaultCb.getValue());

        if (globalDefaultShouldBeChecked) {
            checkGlobalDefaultAndCloseEditor();
        } else {
            close(COMMIT_ACTION_ID, true);
        }
    }

    protected void checkGlobalDefaultAndCloseEditor() {
        List<FilterEntity> otherDefaultFilters = dataManager.loadList(LoadContext.create(FilterEntity.class)
                .setQuery(LoadContext.createQuery("select f from sec$Filter f where f.globalDefault = true and " +
                        "f.componentId = :componentId and " +
                        "f.id <> :currentId ")
                        .setParameter("componentId", filterEntity.getComponentId())
                        .setParameter("currentId", filterEntity.getId())));

        if (!otherDefaultFilters.isEmpty()) {
            String otherFilterNamesStr = otherDefaultFilters.stream()
                    .map(FilterEntity::getName)
                    .collect(Collectors.joining(", "));
            showOptionDialog(getMessage("filter.editor.anotherGlobalDefaultFilterFound.dialogTitle"),
                    formatMessage("filter.editor.anotherGlobalDefaultFilterFound.dialogMessage", otherFilterNamesStr),
                    MessageType.WARNING,
                    new Action[]{
                            new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                                otherDefaultFilters.forEach(otherDefaultFilter -> otherDefaultFilter.setGlobalDefault(false));
                                modifiedGlobalDefaultFilters = dataManager.commit(new CommitContext(otherDefaultFilters));
                                close(COMMIT_ACTION_ID, true);
                            }),
                            new DialogAction(DialogAction.Type.NO, Action.Status.NORMAL).withHandler(e -> {
                                filterEntity.setGlobalDefault(false);
                                close(COMMIT_ACTION_ID, true);
                            }),
                    });
        } else {
            close(COMMIT_ACTION_ID, true);
        }
    }

    protected boolean hasEmptyGroupConditions() {
        for (Node<AbstractCondition> rootNode : conditions.getRootNodes()) {
            if (rootNode.getData() instanceof GroupCondition && rootNode.getChildren().isEmpty()) return true;
        }
        return false;
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
        conditionsDs.refresh(Collections.singletonMap("conditions", conditions));
    }

    public void addAndGroup() {
        addGroup(GroupType.AND);
    }

    public void addOrGroup() {
        addGroup(GroupType.OR);
    }

    protected void addGroup(GroupType groupType) {
        GroupConditionDescriptor conditionDescriptor = new GroupConditionDescriptor(groupType, filter.getId(),
                ((FilterImplementation) filter).getEntityMetaClass(), ((FilterImplementation) filter).getEntityAlias());
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
        String message = (item != null && item.getParam() != null) ? item.getParam().getName() : messages.getMainMessage("filter.editor.showComponentName.conditionIsNotSelected");
        companion.showComponentName(getWindowManager(), messages.getMainMessage("filter.editor.showComponentName.title"), message);
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

    public Set<FilterEntity> getModifiedGlobalDefaultFilters() {
        return modifiedGlobalDefaultFilters.stream()
                .map(entity -> (FilterEntity) entity)
                .collect(Collectors.toSet());
    }
}