/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.CategorizedEntity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractCustomConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;
import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.components.filter.addcondition.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

import java.util.*;

/**
 * Window to select a new generic filter condition.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AddConditionDlg extends Window {

    protected static List<?> MODEL_PROPERTY_IDS = Collections.singletonList("caption");

    protected com.haulmont.cuba.web.toolkit.ui.Tree tree;
    protected Button okBtn;

    protected SelectionHandler selectionHandler;
    private Button cancelBtn;

    public AddConditionDlg(MetaClass metaClass,
                           List<AbstractConditionDescriptor> propertyDescriptors,
                           Map<AbstractConditionDescriptor, String> descriptoMessages, DescriptorBuilder descriptorBuilder,
                           SelectionHandler selectionHandler) {
        Messages messages = AppBeans.get(Messages.NAME);
        setCaption(messages.getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.addCondition"));

        this.selectionHandler = selectionHandler;

        setModal(true);
        setWidth("400px");
        setHeight("300px");

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();

        setContent(layout);

        VerticalLayout topLayout = new VerticalLayout();

        tree = new com.haulmont.cuba.web.toolkit.ui.Tree();
        tree.setWidth("100%");
        tree.setHeight("100%");
        tree.setImmediate(true);
        tree.setMultiSelect(false);
        Model model = new Model(metaClass, propertyDescriptors, descriptoMessages, descriptorBuilder);
        tree.setContainerDataSource(model);
        tree.setItemCaptionPropertyId(MODEL_PROPERTY_IDS.get(0));
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);

        topLayout.addComponent(tree);
        topLayout.setSizeFull();

        layout.addComponent(topLayout);
        layout.setExpandRatio(topLayout, 1);

        VerticalLayout bottomLayout = new VerticalLayout();

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        initButtonsLayout(buttonsLayout);

        bottomLayout.addComponent(buttonsLayout);
        bottomLayout.setComponentAlignment(buttonsLayout, Alignment.MIDDLE_RIGHT);

        layout.addComponent(bottomLayout);

        initShortcuts();

        tree.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                ModelItem modelItem = (ModelItem) tree.getValue();
                okBtn.setEnabled(modelItem != null && modelItem.getDescriptor() != null);
            }
        });

        tree.setDoubleClickMode(true);
        tree.addListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    tree.setValue(event.getItemId());

                    if (okBtn.isEnabled()) {
                        commit(AddConditionDlg.this.selectionHandler);
                    }
                }
            }
        });

        tree.setValue(model.rootItemIds().iterator().next());
        tree.focus();

        if (App.getInstance().isTestMode()) {
            setCubaId("addFilterConditionsDialog");

            okBtn.setCubaId("okBtn");
            cancelBtn.setCubaId("cancelBtn");
            tree.setCubaId("conditionsTree");
        }
    }

    protected void initButtonsLayout(HorizontalLayout buttonsLayout) {
        buttonsLayout.setSpacing(true);

        Messages messages = AppBeans.get(Messages.NAME);

        okBtn = new Button(messages.getMessage(AppConfig.getMessagesPack(), "actions.Select"));
        okBtn.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                commit(AddConditionDlg.this.selectionHandler);
            }
        });
        buttonsLayout.addComponent(okBtn);

        cancelBtn = new Button(messages.getMessage(AppConfig.getMessagesPack(), "actions.Cancel"));
        cancelBtn.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttonsLayout.addComponent(cancelBtn);
    }

    protected void initShortcuts() {
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        KeyCombination close = KeyCombination.create(clientConfig.getCloseShortcut());
        KeyCombination commit = KeyCombination.create(clientConfig.getCommitShortcut());

        ShortcutAction closeAction = new ShortcutAction("Close", close.getKey().getCode(),
                KeyCombination.Modifier.codes(close.getModifiers()));
        ShortcutAction commitAction = new ShortcutAction("Commit", commit.getKey().getCode(),
                KeyCombination.Modifier.codes(commit.getModifiers()));

        Map<Action, Runnable> actionsMap = new HashMap<>();
        actionsMap.put(closeAction, new Runnable() {
            @Override
            public void run() {
                close();
            }
        });
        actionsMap.put(commitAction, new Runnable() {
            @Override
            public void run() {
                commit(selectionHandler);
            }
        });

        WebComponentsHelper.setActions(this, actionsMap);
    }

    protected void commit(SelectionHandler selectionHandler) {
        if (selectionHandler != null && tree.getValue() != null
                && ((ModelItem) tree.getValue()).getDescriptor() != null) {
            selectionHandler.select(((ModelItem) tree.getValue()).getDescriptor());
            close();
        }
    }

    public static class DescriptorBuilder extends AbstractDescriptorBuilder {

        public DescriptorBuilder(String messagesPack, String filterComponentName, CollectionDatasource datasource) {
            super(messagesPack, filterComponentName, datasource);
        }

        @Override
        public PropertyConditionDescriptor buildPropertyConditionDescriptor(String name, String caption) {
            return new PropertyConditionDescriptor(name, caption, messagesPack, filterComponentName, datasource);
        }

        @Override
        public GroupCreator buildGroupConditionDescriptor(GroupType groupType) {
            return new GroupCreator(groupType, filterComponentName, datasource);
        }

        @Override
        public ConditionCreator buildCustomConditionDescriptor() {
            return new ConditionCreator(filterComponentName, datasource);
        }

        @Override
        public RuntimePropConditionCreator buildRuntimePropConditionDescriptor() {
            return new RuntimePropConditionCreator(filterComponentName, datasource);
        }
    }

    protected static class ItemWrapper implements Item {

        private ModelItem modelItem;

        private ItemWrapper(ModelItem modelItem) {
            this.modelItem = modelItem;
        }

        @Override
        public Property getItemProperty(Object id) {
            return new Property() {

                @Override
                public Object getValue() {
                    return modelItem.getCaption();
                }

                @Override
                public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                }

                @Override
                public Class<?> getType() {
                    return String.class;
                }

                @Override
                public boolean isReadOnly() {
                    return true;
                }

                @Override
                public void setReadOnly(boolean newStatus) {
                }

                @Override
                public String toString() {
                    return modelItem.getCaption();
                }
            };
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            return MODEL_PROPERTY_IDS;
        }

        @Override
        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemWrapper that = (ItemWrapper) o;

            return modelItem.equals(that.modelItem);
        }

        @Override
        public int hashCode() {
            return modelItem.hashCode();
        }
    }

    protected static class Model implements Container.Hierarchical {

        private MetaClass metaClass;
        private List<AbstractConditionDescriptor> propertyDescriptors;
        private Map<AbstractConditionDescriptor, String> descriptoMessages;
        private DescriptorBuilder descriptorBuilder;
        private List<ModelItem> rootModelItems;

        private Model(MetaClass metaClass, List<AbstractConditionDescriptor> propertyDescriptors,
                      Map<AbstractConditionDescriptor, String> descriptoMessages, DescriptorBuilder descriptorBuilder) {
            this.metaClass = metaClass;
            this.propertyDescriptors = propertyDescriptors;
            this.descriptoMessages = descriptoMessages;
            this.descriptorBuilder = descriptorBuilder;
            initRootModelItems();
        }

        private void initRootModelItems() {
            rootModelItems = new ArrayList<>();

            rootModelItems.add(new RootPropertyModelItem(metaClass, propertyDescriptors, descriptoMessages, descriptorBuilder));

            for (AbstractConditionDescriptor descriptor : propertyDescriptors) {
                if (descriptor instanceof AbstractCustomConditionDescriptor) {
                    rootModelItems.add(new RootCustomConditionModelItem(propertyDescriptors));
                    break;
                }
            }

            rootModelItems.add(new RootGroupingModelItem(descriptorBuilder));

            if (CategorizedEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
                rootModelItems.add(new RootRuntimePropertiesModelItem(descriptorBuilder));
            }

            Security security = AppBeans.get(Security.NAME);
            if (security.isSpecificPermitted("cuba.gui.filter.customConditions")) {
                rootModelItems.add(new NewCustomConditionModelItem(descriptorBuilder));
            }
        }

        @Override
        public Collection<?> getChildren(Object itemId) {
            return ((ModelItem) itemId).getChildren();
        }

        @Override
        public Object getParent(Object itemId) {
            return ((ModelItem) itemId).getParent();
        }

        @Override
        public Collection<?> rootItemIds() {
            return rootModelItems;
        }

        @Override
        public boolean setParent(Object itemId, Object newParentId) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean areChildrenAllowed(Object itemId) {
            return true; // always return true to get correct styles in Tree web implementation
        }

        @Override
        public boolean setChildrenAllowed(Object itemId, boolean areChildrenAllowed) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean isRoot(Object itemId) {
            return ((ModelItem) itemId).getParent() == null;
        }

        @Override
        public boolean hasChildren(Object itemId) {
            return !((ModelItem) itemId).getChildren().isEmpty();
        }

        @Override
        public Item getItem(Object itemId) {
            return new ItemWrapper((ModelItem) itemId);
        }

        @Override
        public Collection<?> getContainerPropertyIds() {
            return MODEL_PROPERTY_IDS;
        }

        @Override
        public Collection<?> getItemIds() {
            return Collections.emptyList();
        }

        @Override
        public Property getContainerProperty(Object itemId, Object propertyId) {
            return getItem(itemId).getItemProperty(propertyId);
        }

        @Override
        public Class<?> getType(Object propertyId) {
            return String.class;
        }

        @Override
        public int size() {
            return rootItemIds().size();
        }

        @Override
        public boolean containsId(Object itemId) {
            return true;
        }

        @Override
        public Item addItem(Object itemId) throws UnsupportedOperationException {
            return null;
        }

        @Override
        public Object addItem() throws UnsupportedOperationException {
            return null;
        }

        @Override
        public boolean removeItem(Object itemId) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
            return false;
        }

        @Override
        public boolean removeAllItems() throws UnsupportedOperationException {
            return false;
        }
    }
}