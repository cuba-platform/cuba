/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.CategorizedEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.filter.*;
import com.haulmont.cuba.gui.components.filter.addcondition.SelectionHandler;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebFilter;
import com.haulmont.cuba.web.toolkit.ui.TreeTable;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.ui.*;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

import java.util.List;

import static org.apache.commons.lang.BooleanUtils.isTrue;

/**
 * @author krivopustov
 * @version $Id$
 */
public class FilterEditor extends AbstractFilterEditor {

    protected static final String EDITOR_WIDTH = "640px";
    protected static final String TABLE_WIDTH = "600px";

    protected AbstractOrderedLayout layout;
    protected TextField nameField;
    protected TreeTable table;
    protected Select addSelect;
    protected CheckBox defaultCb;
    protected CheckBox applyDefaultCb;

    protected CheckBox globalCb;
    protected Button saveBtn;

    protected ConditionsContainer container;

    public FilterEditor(final WebFilter webFilter, FilterEntity filterEntity,
                        Element filterDescriptor, List<String> existingNames) {
        super(webFilter, filterEntity, filterDescriptor, existingNames);
    }

    @Override
    public void init() {
        container = new ConditionsContainer(conditions);
        container.addListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                updateControls();
            }
        });

        layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true, false, false, false);
        layout.setWidth(EDITOR_WIDTH);

        GridLayout topGrid = new GridLayout(2, 1);
        topGrid.setWidth(TABLE_WIDTH);
        topGrid.setSpacing(true);

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setWidth(TABLE_WIDTH);
        bottomLayout.setSpacing(true);

        HorizontalLayout flagsLayout = new HorizontalLayout();
        flagsLayout.setSpacing(true);

        HorizontalLayout controlLayout = new HorizontalLayout();
        controlLayout.setSpacing(true);

        // Move up button
        Button upBtn = WebComponentsHelper.createButton("icons/up.png");
        upBtn.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object item = table.getValue();
                if (item != table.getNullSelectionItemId()) {
                    container.moveUp((Node<AbstractCondition>) item);
                }
            }
        });
        upBtn.setEnabled(true);

        // Move down button
        Button downBtn = WebComponentsHelper.createButton("icons/down.png");
        downBtn.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object item = table.getValue();
                if (item != table.getNullSelectionItemId()) {
                    container.moveDown((Node<AbstractCondition>) item);
                }
            }
        });
        downBtn.setEnabled(true);

        // Save button
        saveBtn = WebComponentsHelper.createButton("icons/ok.png");
        saveBtn.setCaption(messages.getMainMessage("actions.Ok"));
        saveBtn.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (commit())
                    ((WebFilter) filter).editorCommitted();
            }
        });
        if (filterEntity.getFolder() == null && filterEntity.getCode() != null)
            saveBtn.setEnabled(false);
        controlLayout.addComponent(saveBtn);

        // Cancel button
        Button cancelBtn = WebComponentsHelper.createButton("icons/cancel.png");
        cancelBtn.setCaption(messages.getMainMessage("actions.Cancel"));
        cancelBtn.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ((WebFilter) filter).editorCancelled();
            }
        });
        controlLayout.addComponent(cancelBtn);

        bottomLayout.addComponent(flagsLayout);

        bottomLayout.setExpandRatio(flagsLayout, 1f);
        bottomLayout.addComponent(controlLayout);
        bottomLayout.setComponentAlignment(controlLayout, Alignment.MIDDLE_RIGHT);

        globalCb = new CheckBox();
        globalCb.setCaption(getMessage("FilterEditor.global"));
        globalCb.setValue(filterEntity.getUser() == null);
        globalCb.setEnabled(AppBeans.get(UserSessionSource.class).getUserSession()
                .isSpecificPermitted("cuba.gui.filter.global"));
        controlLayout.addComponent(globalCb);

        flagsLayout.addComponent(globalCb);

        defaultCb = new CheckBox();
        defaultCb.setCaption(getMessage("FilterEditor.isDefault"));
        defaultCb.setImmediate(true);

        defaultCb.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if ((Boolean) defaultCb.getValue()) {
                    applyDefaultCb.setEnabled(true);
                } else {
                    applyDefaultCb.setEnabled(false);
                    applyDefaultCb.setValue(false);
                }
                if (filterEntity != null) {
                    filterEntity.setIsDefault(isTrue((Boolean) defaultCb.getValue()));

                }
            }
        });

        flagsLayout.addComponent(defaultCb);

        applyDefaultCb = new CheckBox();
        applyDefaultCb.setCaption(getMessage("FilterEditor.applyDefault"));
        applyDefaultCb.setImmediate(true);
        applyDefaultCb.setEnabled(false);
        applyDefaultCb.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (filterEntity != null) {
                    filterEntity.setApplyDefault(isTrue((Boolean) applyDefaultCb.getValue()));
                }
            }
        });

        flagsLayout.addComponent(applyDefaultCb);

        HorizontalLayout nameLayout = new HorizontalLayout();
        nameLayout.setSpacing(true);

        Label label = new Label(getMessage("FilterEditor.nameLab"));
        nameLayout.addComponent(label);

        nameField = new TextField();
        nameField.setValue(filterEntity.getName());
        nameField.setWidth("200px");
        nameLayout.addComponent(nameField);

        topGrid.addComponent(nameLayout, 0, 0);
        topGrid.setWidth(TABLE_WIDTH);

        HorizontalLayout addLayout = new HorizontalLayout();
        addLayout.setSpacing(true);

        if (AppBeans.get(Configuration.class).getConfig(ClientConfig.class).getGenericFilterTreeConditionSelect()) {
            initAddDialog(addLayout);
        } else {
            initAddSelect(addLayout);
        }
        topGrid.addComponent(addLayout, 1, 0);
        topGrid.setComponentAlignment(addLayout, Alignment.MIDDLE_RIGHT);

        HorizontalLayout hlayLayout = new HorizontalLayout();
        hlayLayout.setSpacing(true);

        VerticalLayout controlsAndtable = new VerticalLayout();
        controlsAndtable.addComponent(topGrid);
        controlsAndtable.setSpacing(true);
        initTable(controlsAndtable);

        controlsAndtable.addComponent(bottomLayout);

        hlayLayout.addComponent(controlsAndtable);

        VerticalLayout upDownLayout = new VerticalLayout();
        upDownLayout.setSpacing(true);
        upDownLayout.addComponent(upBtn);
        upDownLayout.addComponent(downBtn);
        hlayLayout.addComponent(upDownLayout);
        hlayLayout.setComponentAlignment(upDownLayout, Alignment.MIDDLE_CENTER);
        layout.addComponent(hlayLayout);

        if (App.getInstance().isTestMode()) {
            nameField.setCubaId("filterNameField");
            defaultCb.setCubaId("defaultCb");
            applyDefaultCb.setCubaId("applyDefaultCb");
            globalCb.setCubaId("globalCb");
            saveBtn.setCubaId("saveBtn");
            cancelBtn.setCubaId("cancelBtn");
            table.setCubaId("conditionsTable");

            upBtn.setCubaId("upBtn");
            downBtn.setCubaId("downBtn");
        }

        updateControls();
    }

    public Button getSaveButton() {
        return saveBtn;
    }

    protected void initAddDialog(HorizontalLayout addLayout) {
        Button addBtn = new Button(getMessage("FilterEditor.addCondition"));
        addBtn.addListener(new AddConditionClickListener());
        addLayout.addComponent(addBtn);

        if (App.getInstance().isTestMode()) {
            addBtn.setCubaId("addConditionBtn");
        }
    }

    protected void initAddSelect(AbstractOrderedLayout layout) {
        Label label = new Label(getMessage("FilterEditor.addCondition"));
        layout.addComponent(label);
        layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

        addSelect = new Select();
        addSelect.setImmediate(true);
        addSelect.setNullSelectionAllowed(true);
        addSelect.setFilteringMode(Select.FILTERINGMODE_CONTAINS);
        addSelect.setWidth("100px");
        for (AbstractConditionDescriptor descriptor : descriptors) {
            addSelect.addItem(descriptor);
            addSelect.setItemCaption(descriptor, descriptor.getLocCaption());
        }

        if (CategorizedEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
            RuntimePropConditionCreator runtimePropCreator = new RuntimePropConditionCreator(filterComponentName, datasource);
            addSelect.addItem(runtimePropCreator);
            addSelect.setItemCaption(runtimePropCreator, runtimePropCreator.getLocCaption());
        }

        GroupCreator andGroupCreator = new GroupCreator(GroupType.AND, filterComponentName, datasource);
        addSelect.addItem(andGroupCreator);
        addSelect.setItemCaption(andGroupCreator, andGroupCreator.getLocCaption());

        GroupCreator orGroupCreator = new GroupCreator(GroupType.OR, filterComponentName, datasource);
        addSelect.addItem(orGroupCreator);
        addSelect.setItemCaption(orGroupCreator, orGroupCreator.getLocCaption());

        if (UserSessionProvider.getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions")) {
            ConditionCreator conditionCreator = new ConditionCreator(filterComponentName, datasource);
            addSelect.addItem(conditionCreator);
            addSelect.setItemCaption(conditionCreator, conditionCreator.getLocCaption());
        }

        addSelect.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (addSelect.getValue() != null) {
                    addCondition((AbstractConditionDescriptor) addSelect.getValue());
                    addSelect.select(null);
                }
            }
        });
        layout.addComponent(addSelect);

        Button addBtn = new Button(getMessage("FilterEditor.addMoreConditions"));
        addBtn.addListener(new AddConditionClickListener());
        layout.addComponent(addBtn);

        if (App.getInstance().isTestMode()) {
            addBtn.setCubaId("addConditionBtn");
            addSelect.setCubaId("addConditionSelect");
        }
    }

    protected void initTable(AbstractLayout layout) {
        table = new TreeTable() {
            @Override
            public void containerItemSetChange(Container.ItemSetChangeEvent event) {
                super.containerItemSetChange(event);
                if (table.getValue() != null) {
                    if (!event.getContainer().containsId(table.getValue())) {
                        table.setValue(null); // If selected item is removed it should not be used as a parent item.
                    }
                }
            }
        };

        table.setImmediate(true);
        table.setSelectable(true);
        table.setPageLength(0);
        table.setWidth(TABLE_WIDTH);
        table.setHeight("200px");
        table.setStyleName("table filter-conditions");
        table.setColumnReorderingAllowed(false);
        table.setSortDisabled(true);

        table.setContainerDataSource(container);

        String nameCol = getMessage("FilterEditor.column.name");
        String opCol = getMessage("FilterEditor.column.op");
        String paramCol = getMessage("FilterEditor.column.param");
        String hiddenCol = getMessage("FilterEditor.column.hidden");
        String requiredCol = getMessage("FilterEditor.column.required");
        String cntrCol = getMessage("FilterEditor.column.control");

        table.setColumnWidth(ConditionsContainer.NAME_PROP_ID, 220);
        table.setColumnHeader(ConditionsContainer.NAME_PROP_ID, nameCol);

        table.setColumnWidth(ConditionsContainer.OP_PROP_ID, 100);
        table.setColumnHeader(ConditionsContainer.OP_PROP_ID, opCol);

        table.setColumnWidth(ConditionsContainer.PARAM_PROP_ID, 160);
        table.setColumnHeader(ConditionsContainer.PARAM_PROP_ID, paramCol);

        table.setColumnWidth(ConditionsContainer.HIDDEN_PROP_ID, 30);
        table.setColumnHeader(ConditionsContainer.HIDDEN_PROP_ID, hiddenCol);

        table.setColumnWidth(ConditionsContainer.REQUIRED_PROP_ID, 30);
        table.setColumnHeader(ConditionsContainer.REQUIRED_PROP_ID, requiredCol);

        table.setColumnWidth(ConditionsContainer.CONTROL_PROP_ID, 30);
        table.setColumnHeader(ConditionsContainer.CONTROL_PROP_ID, cntrCol);

        table.expandAll();

        final Action showNameAction = new Action(messages.getMessage(MESSAGES_PACK, "FilterEditor.showNameAction"));
        table.addActionHandler(
                new Action.Handler() {
                    @Override
                    public Action[] getActions(Object target, Object sender) {
                        return new Action[]{showNameAction};
                    }

                    @Override
                    public void handleAction(Action action, Object sender, Object target) {
                        if (action.equals(showNameAction)) {
                            Node<AbstractCondition> node = (Node<AbstractCondition>) target;
                            App.getInstance().getWindowManager().showMessageDialog(
                                    messages.getMessage(MESSAGES_PACK, "FilterEditor.showNameTitle"),
                                    node != null && node.getData().getParam() != null ?
                                            node.getData().getParam().getName() :
                                            messages.getMessage(MESSAGES_PACK, "FilterEditor.conditionIsNotSelected"),
                                    IFrame.MessageType.CONFIRMATION
                            );
                        }
                    }
                }
        );

        layout.addComponent(table);
    }

    protected void addCondition(AbstractConditionDescriptor descriptor) {
        AbstractCondition condition = descriptor.createCondition();
        Node<AbstractCondition> node = new Node<>(condition);

        Node<AbstractCondition> parentNode = null;
        Object selected = table.getValue();
        if (selected != table.getNullSelectionItemId()) {
            parentNode = (Node<AbstractCondition>) selected;
            if (parentNode.getData().isGroup()) {
                parentNode.addChild(node);
            }
        }
        container.addItem(node);
        table.setExpanded(node);

        if (node.getData().isGroup()) {
            // Select the added node if it is a group
            table.setValue(node);
        } else if (parentNode != null) {
            // Otherwise select a parent node if it is a group
            table.setValue(parentNode);
        }

        AbstractOperationEditor operationEditor = condition.getOperationEditor();
        if (operationEditor instanceof HasAction && descriptor.isShowImmediately()) {
            ((HasAction) operationEditor).doAction(layout);
        }
    }

    protected void updateControls() {
        if (filterEntity.getFolder() != null || filterEntity.getCode() == null)
            saveBtn.setEnabled(!conditions.getRootNodes().isEmpty());
        else
            saveBtn.setEnabled(false);
        defaultCb.setVisible(filterEntity.getFolder() == null);
        defaultCb.setValue(isTrue(filterEntity.getIsDefault()));
        applyDefaultCb.setVisible(defaultCb.isVisible() && manualApplyRequired);
        applyDefaultCb.setValue(BooleanUtils.isTrue(filterEntity.getApplyDefault()));
    }

    @Override
    protected AbstractFilterParser createFilterParser(String xml, String messagesPack, String filterComponentName,
                                                      CollectionDatasource datasource) {
        return new FilterParser(xml, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractPropertyConditionDescriptor createPropertyConditionDescriptor(
            Element element, String messagesPack, String filterComponentName, CollectionDatasource datasource) {
        return new PropertyConditionDescriptor(element, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractPropertyConditionDescriptor createPropertyConditionDescriptor(
            String name, String caption, String messagesPack, String filterComponentName, CollectionDatasource datasource) {
        return new PropertyConditionDescriptor(name, caption, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractCustomConditionDescriptor createCustomConditionDescriptor(
            Element element, String messagesPack, String filterComponentName, CollectionDatasource datasource) {
        return new CustomConditionDescriptor(element, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected AbstractFilterParser createFilterParser(ConditionsTree conditions, String messagesPack,
                                                      String filterComponentName, Datasource datasource) {
        return new FilterParser(conditions, messagesPack, filterComponentName, datasource);
    }

    @Override
    protected String getName() {
        return (String) nameField.getValue();
    }

    @Override
    protected boolean isGlobal() {
        return (Boolean) globalCb.getValue();
    }

    @Override
    protected void showNotification(String caption, String description) {
        App.getInstance().getWindowManager().showNotification(caption, description, IFrame.NotificationType.HUMANIZED);
    }

    public AbstractOrderedLayout getLayout() {
        return layout;
    }

    @Override
    public FilterEntity getFilterEntity() {
        return filterEntity;
    }

    protected class AddConditionClickListener implements Button.ClickListener {
        @Override
        public void buttonClick(Button.ClickEvent event) {
            AddConditionDlg dlg = new AddConditionDlg(
                    metaClass,
                    descriptors,
                    new AddConditionDlg.DescriptorBuilder(messagesPack, filterComponentName, datasource),
                    new SelectionHandler() {
                        @Override
                        public void select(AbstractConditionDescriptor descriptor) {
                            addCondition(descriptor);
                        }
                    });
            dlg.center();
            App.getInstance().getAppWindow().addWindow(dlg);
        }
    }
}