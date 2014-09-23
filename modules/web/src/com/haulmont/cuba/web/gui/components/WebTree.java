/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.ShowInfoAction;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaTree;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebTree extends WebAbstractList<CubaTree> implements Tree {

    protected String hierarchyProperty;
    protected CaptionMode captionMode = CaptionMode.ITEM;
    protected String captionProperty;

    public WebTree() {
        component = new CubaTree();
        component.setMultiSelect(false);
        component.setImmediate(true);

        contextMenuPopup.setParent(component);
        component.setContextMenuPopup(contextMenuPopup);

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
    }

    @Override
    protected ContextMenuButton createContextMenuButton() {
        return new ContextMenuButton() {
            @Override
            protected void beforeActionPerformed() {
                WebTree.this.component.hideContextMenuPopup();
            }

            @Override
            protected void performAction(Action action) {
                // do action for table component
                action.actionPerform(WebTree.this);
            }
        };
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

        Security security = AppBeans.get(Security.NAME);
        if (security.isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
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