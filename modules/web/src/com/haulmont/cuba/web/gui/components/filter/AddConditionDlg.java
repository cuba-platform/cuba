/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.filter;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.entity.CategorizedEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.filter.AbstractConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractCustomConditionDescriptor;
import com.haulmont.cuba.gui.components.filter.AbstractFilterEditor;
import com.haulmont.cuba.gui.components.filter.GroupType;
import com.haulmont.cuba.gui.components.filter.addcondition.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
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

    protected Tree tree;
    protected Button okBtn;

    protected SelectionHandler selectionHandler;
    protected ModelItem selectedItem;

    public AddConditionDlg(MetaClass metaClass,
                           List<AbstractConditionDescriptor> propertyDescriptors,
                           DescriptorBuilder descriptorBuilder,
                           SelectionHandler selectionHandler) {

        super(AppBeans.get(Messages.class).getMessage(AbstractFilterEditor.MESSAGES_PACK, "FilterEditor.addCondition"));

        this.selectionHandler = selectionHandler;

        setModal(true);
        setWidth("400px");
        setHeight("300px");

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(new MarginInfo(true, false, false, false));
        layout.setSpacing(true);
        layout.setSizeFull();

        Panel treePanel = new Panel();

        tree = new Tree();
        tree.setWidth("100%");
        tree.setHeight("100%");
        tree.setImmediate(true);
        tree.setSelectable(true);
        tree.setMultiSelect(false);

        tree.setPropertyDataSource(new Property<ModelItem>() {
            @Override
            public ModelItem getValue() {
                return selectedItem;
            }

            @Override
            public void setValue(ModelItem newValue) throws ReadOnlyException {
                selectedItem = newValue;
            }

            @Override
            public Class<ModelItem> getType() {
                return ModelItem.class;
            }

            @Override
            public boolean isReadOnly() {
                return false;
            }

            @Override
            public void setReadOnly(boolean newStatus) {
            }
        });

        Model model = new Model(metaClass, propertyDescriptors, descriptorBuilder);
        tree.setContainerDataSource(model);
        tree.setItemCaptionPropertyId(MODEL_PROPERTY_IDS.get(0));
        tree.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);

        treePanel.setContent(tree);
        treePanel.setSizeFull();

        layout.addComponent(treePanel);
        layout.setExpandRatio(treePanel, 1);

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        initButtonsLayout(buttonsLayout);

        layout.addComponent(buttonsLayout);

        setContent(layout);

        initShortcuts();

        // TODO styles generator doesn't work correctly - style applies to all nested items
//        tree.setItemStyleGenerator(new Tree.ItemStyleGenerator() {
//            @Override
//            public String getStyle(Object itemId) {
//                if (itemId instanceof ModelItem && ((ModelItem) itemId).getDescriptor() == null)
//                    return "filter-new-cond-dlg";
//                else
//                    return null;
//            }
//        });

        tree.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                ModelItem modelItem = (ModelItem) tree.getValue();
                okBtn.setEnabled(modelItem != null && modelItem.getDescriptor() != null);
            }
        });

        tree.setValue(model.rootItemIds().iterator().next());
        tree.focus();
    }

    protected void initButtonsLayout(HorizontalLayout buttonsLayout) {
        buttonsLayout.setWidth("100%");
        buttonsLayout.setSpacing(true);

        Label spacer = new Label();
        buttonsLayout.addComponent(spacer);
        buttonsLayout.setExpandRatio(spacer, 1);

        Messages messages = AppBeans.get(Messages.class);
        okBtn = new Button(messages.getMessage(AppConfig.getMessagesPack(), "actions.Select"));
        okBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                commit(AddConditionDlg.this.selectionHandler);
            }
        });
        buttonsLayout.addComponent(okBtn);

        Button cancelBtn = new Button(messages.getMessage(AppConfig.getMessagesPack(), "actions.Cancel"));
        cancelBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        buttonsLayout.addComponent(cancelBtn);
    }

    protected void initShortcuts() {
        ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        KeyCombination close = KeyCombination.create(clientConfig.getCloseShortcut());
        KeyCombination commit = KeyCombination.create(clientConfig.getCommitShortcut());

        ShortcutAction closeAction = new ShortcutAction("Close", close.getKey().getCode(),
                KeyCombination.Modifier.codes(close.getModifiers()));
        ShortcutAction commitAction = new ShortcutAction("Commit", commit.getKey().getCode(),
                KeyCombination.Modifier.codes(commit.getModifiers()));

        Map<Action, Runnable> actionsMap = new HashMap<Action, Runnable>();
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
                public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
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
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemWrapper that = (ItemWrapper) o;

            if (!modelItem.equals(that.modelItem)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return modelItem.hashCode();
        }
    }

    protected static class Model implements Container.Hierarchical {

        private MetaClass metaClass;
        private List<AbstractConditionDescriptor> propertyDescriptors;
        private DescriptorBuilder descriptorBuilder;
        private List<ModelItem> rootModelItems;

        private Model(MetaClass metaClass, List<AbstractConditionDescriptor> propertyDescriptors,
                      DescriptorBuilder descriptorBuilder) {
            this.metaClass = metaClass;
            this.propertyDescriptors = propertyDescriptors;
            this.descriptorBuilder = descriptorBuilder;
            initRootModelItems();
        }

        private void initRootModelItems() {
            rootModelItems = new ArrayList<>();

            rootModelItems.add(new RootPropertyModelItem(metaClass, propertyDescriptors, descriptorBuilder));

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

            if (AppBeans.get(UserSessionSource.class).getUserSession().isSpecificPermitted("cuba.gui.filter.customConditions")) {
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
            return !((ModelItem) itemId).getChildren().isEmpty();
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