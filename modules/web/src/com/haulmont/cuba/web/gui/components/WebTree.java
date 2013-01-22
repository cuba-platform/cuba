/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.01.2009 13:01:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ShowInfoAction;
import com.haulmont.cuba.gui.components.Tree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsActionsNotifier;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;

import java.util.Set;

public class WebTree
    extends
        WebAbstractList<com.haulmont.cuba.web.toolkit.ui.Tree>
    implements
        Tree, Component.Wrapper
{
    private String hierarchyProperty;
    private CaptionMode captionMode = CaptionMode.ITEM;
    private String captionProperty;
    
    public WebTree() {
        component = new com.haulmont.cuba.web.toolkit.ui.Tree();
        component.setMultiSelect(false);
        component.setImmediate(true);

        component.addActionHandler(new ActionsAdapter());
        component.addListener(
                new Property.ValueChangeListener()
                {
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

    public void setMultiSelect(boolean multiselect) {
        component.setMultiSelect(multiselect);
    }

    public CaptionMode getCaptionMode() {
        return captionMode;
    }

    public void setCaptionMode(CaptionMode captionMode) {
        this.captionMode = captionMode;
        switch (captionMode) {
            case ITEM: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_ITEM);
                break;
            }
            case PROPERTY: {
                component.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
                break;
            }
            default :{
                throw new UnsupportedOperationException();
            }
        }
    }

    public String getCaptionProperty() {
        return captionProperty;
    }

    public void setCaptionProperty(String captionProperty) {
        this.captionProperty = captionProperty;
        if (datasource != null) {
            component.setItemCaptionPropertyId(datasource.getMetaClass().getProperty(captionProperty));
        }
    }

    public void expandTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.expandItemsRecursively(id);
            }
        }
    }

    public void collapse(Object itemId) {
        component.collapseItem(itemId);
    }

    public void expand(Object itemId) {
        component.expandItem(itemId);
    }

    public void collapseTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.collapseItemsRecursively(id);
            }
        }
    }

    public boolean isExpanded(Object itemId) {
        return component.isExpanded(itemId);
    }

    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    public void setDatasource(HierarchicalDatasource datasource)
    {
        this.datasource = datasource;
        this.hierarchyProperty = datasource.getHierarchyPropertyName();

        // if showProperty is null, the Tree will use itemId.toString
        MetaProperty metaProperty = hierarchyProperty == null ? null : datasource.getMetaClass().getProperty(hierarchyProperty);
        component.setItemCaptionPropertyId(metaProperty);

        HierarchicalDsWrapper wrapper = new HierarchicalDsWrapper(datasource);
        component.setContainerDataSource(wrapper);

        if (UserSessionProvider.getUserSession().isSpecificPermitted(ShowInfoAction.ACTION_PERMISSION)) {
            ShowInfoAction action = (ShowInfoAction) getAction(ShowInfoAction.ACTION_ID);
            if (action == null) {
                action = new ShowInfoAction();
                addAction(action);
            }
            action.setDatasource(datasource);
        }

        datasource.addListener(new CollectionDsActionsNotifier(this));
    }
}
