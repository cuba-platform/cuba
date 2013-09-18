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
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractSelect;

import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebTree
        extends
            WebAbstractList<com.vaadin.ui.Tree>
        implements
            Tree, Component.Wrapper {

    private String hierarchyProperty;
    private CaptionMode captionMode = CaptionMode.ITEM;
    private String captionProperty;
    
    public WebTree() {
        component = new com.vaadin.ui.Tree();
        component.setMultiSelect(false);
        component.setImmediate(true);

        component.addActionHandler(new ActionsAdapter());
        component.addValueChangeListener(
                new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        Set items = getSelected();
                        if (items.isEmpty()) {
                            //noinspection unchecked
                            datasource.setItem(null);
                        } else if (items.size() == 1) {
                            //noinspection unchecked
                            datasource.setItem((Entity) items.iterator().next());
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                }
        );
    }

    @Override
    public void addAction(Action action) {
        super.addAction(action);
        if (action.getShortcut() != null) {
            addShortcutActionBridge(action.getId(), action.getShortcut());
        }
    }

    /**
     * Connect shortcut action to default list action
     *
     * @param actionId Shortcut action id
     * @param keyCombination   KeyCombination object
     */
    protected void addShortcutActionBridge(final String actionId, KeyCombination keyCombination) {
        component.addShortcutListener(new ShortcutListener(actionId, keyCombination.getKey().getCode(),
                KeyCombination.Modifier.codes(keyCombination.getModifiers())) {
            @Override
            public void handleAction(Object sender, Object target) {
                if (target == component) {
                    Action action = getAction(actionId);
                    if (action != null && action.isEnabled())
                        action.actionPerform(WebTree.this);
                }
            }
        });
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

        datasource.addListener(new CollectionDsActionsNotifier(this));

        for (Action action : getActions()) {
            action.refreshState();
        }
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
    }
}