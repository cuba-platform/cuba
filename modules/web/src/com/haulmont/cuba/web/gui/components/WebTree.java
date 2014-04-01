/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractSelect;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebTree extends WebAbstractList<CubaTree> implements Tree {

    protected String hierarchyProperty;
    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    protected ShortcutsDelegate<ShortcutListener> shortcutsDelegate;
    
    public WebTree() {
        component = new CubaTree();
        component.setMultiSelect(false);
        component.setImmediate(true);

        component.addActionHandler(new ActionsAdapter());
        component.addValueChangeListener(
                new Property.ValueChangeListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        if (datasource != null) {
                            Set selected = getSelected();
                            if (selected.isEmpty()) {
                                datasource.setItem(null);
                            } else {
                                // reset selection and select new item
                                if (isMultiSelect()) {
                                    datasource.setItem(null);
                                }
                                datasource.setItem((Entity) selected.iterator().next());
                            }
                        }
                    }
                }
        );

        shortcutsDelegate = new ShortcutsDelegate<ShortcutListener>() {
            @Override
            protected ShortcutListener attachShortcut(final String actionId, KeyCombination keyCombination) {
                ShortcutListener shortcut = new ShortcutListener(actionId, keyCombination.getKey().getCode(),
                        KeyCombination.Modifier.codes(keyCombination.getModifiers())) {

                    @Override
                    public void handleAction(Object sender, Object target) {
                        if (target == component) {
                            Action action = getAction(actionId);
                            if (action != null && action.isEnabled() && action.isVisible()) {
                                action.actionPerform(WebTree.this);
                            }
                        }
                    }
                };
                component.addShortcutListener(shortcut);
                return shortcut;
            }

            @Override
            protected void detachShortcut(Action action, ShortcutListener shortcutDescriptor) {
                component.removeShortcutListener(shortcutDescriptor);
            }

            @Override
            protected Collection<Action> getActions() {
                return WebTree.this.getActions();
            }
        };
    }

    @Override
    public void addAction(Action action) {
        checkNotNullArgument(action, "action must be non null");

        Action oldAction = getAction(action.getId());

        super.addAction(action);

        shortcutsDelegate.addAction(oldAction, action);
    }

    @Override
    public void removeAction(Action action) {
        super.removeAction(action);

        shortcutsDelegate.removeAction(action);
    }

    @Override
    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    @Override
    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
        switch (captionMode) {
            case ITEM: {
                component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
                break;
            }
            case PROPERTY: {
                component.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
                break;
            }
            default :{
                throw new UnsupportedOperationException();
            }
        }
    }

    @Override
    public String getCaptionProperty() {
        return captionProperty;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
        if (datasource != null) {
            component.setItemCaptionPropertyId(datasource.getMetaClass().getProperty(captionProperty));
        }
    }

    @Override
    public void expandTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.expandItemsRecursively(id);
            }
        }
    }

    @Override
    public void collapse(Object itemId) {
        component.collapseItem(itemId);
    }

    @Override
    public void expand(Object itemId) {
        component.expandItem(itemId);
    }

    @Override
    public void collapseTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.collapseItemsRecursively(id);
            }
        }
    }

    @Override
    public boolean isExpanded(Object itemId) {
        return component.isExpanded(itemId);
    }

    @Override
    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    @Override
    public void setDatasource(HierarchicalDatasource datasource) {
        this.datasource = datasource;
        this.hierarchyProperty = datasource.getHierarchyPropertyName();

        // if showProperty is null, the Tree will use itemId.toString
        MetaProperty metaProperty = hierarchyProperty == null ? null : datasource.getMetaClass().getProperty(hierarchyProperty);
        component.setItemCaptionPropertyId(metaProperty);

        HierarchicalDsWrapper wrapper = new HierarchicalDsWrapper(datasource);
        component.setContainerDataSource(wrapper);

        if (AppBeans.get(UserSessionSource.class).getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        datasource.addListener(new CollectionDsActionsNotifier(this) {
            @Override
            public void collectionChanged(CollectionDatasource ds, Operation operation, List<Entity> items) {
                // #PL-2035, reload selection from ds
                Set<Object> selectedItemIds = getSelectedItemIds();
                if (selectedItemIds == null) {
                    selectedItemIds = Collections.emptySet();
                }

                Set<Object> newSelection = new HashSet<>();
                for (Object entityId : selectedItemIds) {
                    if (ds.containsItem(entityId)) {
                        newSelection.add(entityId);
                    }
                }

                if (ds.getState() == Datasource.State.VALID && ds.getItem() != null) {
                    newSelection.add(ds.getItem().getId());
                }

                if (newSelection.isEmpty()) {
                    setSelected((Entity) null);
                } else {
                    setSelectedIds(newSelection);
                }
            }
        });

        for (Action action : getActions()) {
            action.refreshState();
        }

        assignAutoDebugId();
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
    }

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId())) {
            return getClass().getSimpleName()  + "_" + datasource.getId();
        }

        return getClass().getSimpleName();
    }
}